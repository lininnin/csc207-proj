package data_access;


import entity.Sophia.*;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class FileGoalRepository implements GoalRepository {
    private final File goalsFile;
    private final File currentGoalsFile;
    private final File todayGoalFile;
    private final Map<String, Goal> goals = new HashMap<>();
    private final List<String> currentGoalNames = new ArrayList<>();
    private final List<String> todayGoalNames = new ArrayList<>();
    private final goalFactory factory;

    public FileGoalRepository(File goalsFile, File currentGoalsFile, File todayGoalFile, goalFactory factory) {
        this.goalsFile = goalsFile;
        this.currentGoalsFile = currentGoalsFile;
        this.todayGoalFile = todayGoalFile;
        this.factory = factory;
        readGoalsFromFile();
        loadCurrentGoals();
        loadTodayGoals();
    }

    // Internal method for loading goals from file - renamed to avoid clash
    private void readGoalsFromFile() {
        if (!goalsFile.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(goalsFile))) {
            Object obj = in.readObject();
            if (obj instanceof Map<?, ?> map) {
                for (Object key : map.keySet()) {
                    if (key instanceof String name && map.get(key) instanceof Goal goal) {
                        goals.put(name, goal);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading goals: " + e.getMessage());
        }
    }

    private void saveGoalsToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(goalsFile))) {
            out.writeObject(goals);
        } catch (IOException e) {
            System.err.println("Error saving goals: " + e.getMessage());
        }
    }


    public void saveCurrentGoals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentGoalsFile))) {
            for (String name : currentGoalNames) {
                writer.write(name);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving current goals: " + e.getMessage());
        }
    }

    private void loadTodayGoals() {
        if (!todayGoalFile.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(todayGoalFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                todayGoalNames.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error loading today's goals: " + e.getMessage());
        }
    }

    private void saveTodayGoals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(todayGoalFile))) {
            for (String name : todayGoalNames) {
                writer.write(name);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving today's goals: " + e.getMessage());
        }
    }

    // ... rest of your existing methods remain unchanged ...

    @Override
    public List<Goal> loadGoals() {
        return new ArrayList<>(goals.values());
    }

    @Override
    public List<Goal> loadCurrentGoals() {
        List<Goal> result = new ArrayList<>();
        for (String name : currentGoalNames) {
            Goal goal = goals.get(name);
            if (goal != null) {
                result.add(goal);
            }
        }
        return result;
    }

    @Override
    public void save(Goal goal) {
        goals.put(goal.getGoalInfo().getInfo().getName(), goal);
        saveGoals();
    }


    @Override
    public Optional<Goal> findByName(String name) {
        return Optional.ofNullable(goals.get(name));
    }

    @Override
    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals.values());
    }

    @Override
    public List<Goal> getCurrentGoals() {
        List<Goal> result = new ArrayList<>();
        for (String name : currentGoalNames) {
            Goal goal = goals.get(name);
            if (goal != null) {
                result.add(goal);
            }
        }
        return result;
    }

    @Override
    public void addToCurrentGoals(Goal goal) {
        String name = goal.getGoalInfo().getInfo().getName();
        if (!currentGoalNames.contains(name)) {
            currentGoalNames.add(name);
            saveCurrentGoals();
        }
    }

    @Override
    public void removeFromCurrentGoals(Goal goal) {
        currentGoalNames.remove(goal.getGoalInfo().getInfo().getName());
        saveCurrentGoals();
    }

    @Override
    public boolean isInCurrentGoals(Goal goal) {
        return currentGoalNames.contains(goal.getGoalInfo().getInfo().getName());
    }

    @Override
    public boolean existsByName(String name) {
        return goals.containsKey(name);
    }

    @Override
    public List<Goal> findByTargetAmountRange(double minAmount, double maxAmount) {
        List<Goal> result = new ArrayList<>();
        for (Goal goal : goals.values()) {
            double freq = goal.getFrequency();
            if (freq >= minAmount && freq <= maxAmount) {
                result.add(goal);
            }
        }
        return result;
    }

    @Override
    public List<Goal> findAvailableGoals() {
        LocalDate today = LocalDate.now();
        List<Goal> result = new ArrayList<>();
        for (Goal goal : goals.values()) {
            if (!goal.isCompleted()
                    && !today.isBefore(goal.getBeginAndDueDates().getBeginDate())
                    && !today.isAfter(goal.getBeginAndDueDates().getDueDate())) {
                result.add(goal);
            }
        }
        return result;
    }

    @Override
    public Goal getTodayGoal() {
        return todayGoalNames.isEmpty() ? null : goals.get(todayGoalNames.get(0));
    }

    @Override
    public void saveTodayGoal(Goal goal) {
        if (goal != null) {
            addGoalToToday(goal.getGoalInfo().getInfo().getName());
        }
    }

    @Override
    public void removeGoal(String goalName) {
        deleteByName(goalName);
    }

    @Override
    public void addGoalToToday(String goalName) {
        if (goals.containsKey(goalName) && !todayGoalNames.contains(goalName)) {
            todayGoalNames.add(goalName);
            saveTodayGoals();
        }
    }

    @Override
    public void removeGoalFromToday(String goalName) {
        todayGoalNames.remove(goalName);
        saveTodayGoals();
    }

    @Override
    public void updateTodayGoalProgress(String goalName, int newProgress) {
        Goal goal = goals.get(goalName);
        if (goal != null && todayGoalNames.contains(goalName)) {
            goal.setCurrentProgress(newProgress);
            if (newProgress >= goal.getFrequency()) {
                goal.setCompleted(true);
            }
            save(goal);
        }
    }

    @Override
    public List<Goal> getTodayGoals() {
        List<Goal> result = new ArrayList<>();
        for (String name : todayGoalNames) {
            Goal goal = goals.get(name);
            if (goal != null) {
                result.add(goal);
            }
        }
        return result;
    }

    @Override
    public void saveGoals() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(goalsFile))) {
            out.writeObject(goals);
        } catch (IOException e) {
            System.err.println("Error saving goals: " + e.getMessage());
        }
    }

    @Override
    public void deleteByName(String name) {
        goals.remove(name);
        currentGoalNames.remove(name);
        todayGoalNames.remove(name);
        saveGoals();
        saveCurrentGoals();
        saveTodayGoals();
    }




}
