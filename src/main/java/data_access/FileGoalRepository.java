package data_access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import entity.sophia.Goal;
import entity.sophia.GoalFactory;

/**
 * An implementation of the {@link GoalRepository} interface that persists goal data to files.
 * This class handles saving and loading goals, current goals, and today's goals using
 * different file-based storage mechanisms. The main goal data is stored using object
 * serialization, while the lists of current and today's goal names are stored in simple
 * text files.
 */
public class FileGoalRepository implements GoalRepository {
    /**
     * The file where the main Map of all goals is stored.
     */
    private final File goalsFile;
    /**
     * The file where the list of names for current goals is stored.
     */
    private final File currentGoalsFile;
    /**
     * The file where the list of names for today's goals is stored.
     */
    private final File todayGoalFile;
    /**
     * A map to hold all goals in memory, keyed by their name.
     */
    private final Map<String, Goal> goals = new HashMap<>();
    /**
     * A list of goal names that are currently active.
     */
    private final List<String> currentGoalNames = new ArrayList<>();
    /**
     * A list of goal names for today's goals.
     */
    private final List<String> todayGoalNames = new ArrayList<>();
    /**
     * A factory for creating new Goal objects.
     */
    private final GoalFactory factory;

    /**
     * Constructs a {@code FileGoalRepository} with the specified file paths and goal factory.
     * It reads and loads all existing goals and lists from the files upon initialization.
     *
     * @param goalsFile The file for storing all goals.
     * @param currentGoalsFile The file for storing current goal names.
     * @param todayGoalFile The file for storing today's goal names.
     * @param factory The goal factory to be used.
     */
    public FileGoalRepository(File goalsFile, File currentGoalsFile, File todayGoalFile, GoalFactory factory) {
        this.goalsFile = goalsFile;
        this.currentGoalsFile = currentGoalsFile;
        this.todayGoalFile = todayGoalFile;
        this.factory = factory;
        readGoalsFromFile();
        loadCurrentGoals();
        loadTodayGoals();
    }

    /**
     * Internal method for loading all goals from the main goals file.
     * It uses object deserialization to read the entire goals map.
     */
    private void readGoalsFromFile() {
        if (goalsFile.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(goalsFile))) {
                final Object obj = in.readObject();
                if (obj instanceof Map<?, ?> map) {
                    for (Object key : map.keySet()) {
                        if (key instanceof String name && map.get(key) instanceof Goal goal) {
                            goals.put(name, goal);
                        }
                    }
                }
            }
            catch (IOException | ClassNotFoundException ex) {
                System.err.println("Error loading goals: " + ex.getMessage());
            }
        }
    }

    /**
     * Internal method for saving all goals to the main goals file.
     * It uses object serialization to write the entire goals map.
     */
    private void saveGoalsToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(goalsFile))) {
            out.writeObject(goals);
        }
        catch (IOException ex) {
            System.err.println("Error saving goals: " + ex.getMessage());
        }
    }

    /**
     * Loads the list of goal names for today from the today goals file.
     * Each line in the file is read as a goal name.
     */
    private void loadTodayGoals() {
        if (todayGoalFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(todayGoalFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    todayGoalNames.add(line.trim());
                }
            }
            catch (IOException ex) {
                System.err.println("Error loading today's goals: " + ex.getMessage());
            }
        }
    }

    /**
     * Persists the list of goal names for today to the today goals file.
     * Each goal name is written on a new line.
     */
    private void saveTodayGoals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(todayGoalFile))) {
            for (String name : todayGoalNames) {
                writer.write(name);
                writer.newLine();
            }
        }
        catch (IOException ex) {
            System.err.println("Error saving today's goals: " + ex.getMessage());
        }
    }

    /**
     * Loads all goals from a persistent storage.
     *
     * @return A List of all loaded Goal objects.
     */
    @Override
    public List<Goal> loadGoals() {
        return new ArrayList<>(goals.values());
    }

    /**
     * Loads the current goals from a persistent storage.
     *
     * @return A List of loaded Goal objects that are marked as current.
     */
    @Override
    public List<Goal> loadCurrentGoals() {
        final List<Goal> result = new ArrayList<>();
        for (String name : currentGoalNames) {
            final Goal goal = goals.get(name);
            if (goal != null) {
                result.add(goal);
            }
        }
        return result;
    }

    /**
     * Saves a goal to the repository.
     * If the goal already exists, it is updated; otherwise, it is created.
     * This method also persists the change to the file.
     *
     * @param goal The Goal object to be saved.
     */
    @Override
    public void save(Goal goal) {
        goals.put(goal.getGoalInfo().getInfo().getName(), goal);
        saveGoals();
    }

    /**
     * Finds a goal by its unique name.
     *
     * @param name The name of the goal to find.
     * @return An Optional containing the Goal if found, or an empty Optional otherwise.
     */
    @Override
    public Optional<Goal> findByName(String name) {
        return Optional.ofNullable(goals.get(name));
    }

    /**
     * Retrieves all goals stored in the repository.
     *
     * @return A List of all Goal objects.
     */
    @Override
    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals.values());
    }

    /**
     * Retrieves a list of goals marked as "current goals".
     *
     * @return A List of Goal objects representing the current goals.
     */
    @Override
    public List<Goal> getCurrentGoals() {
        final List<Goal> result = new ArrayList<>();
        for (String name : currentGoalNames) {
            final Goal goal = goals.get(name);
            if (goal != null) {
                result.add(goal);
            }
        }
        return result;
    }

    /**
     * Adds a goal to the list of current goals and persists the change to the file.
     *
     * @param goal The Goal object to be added.
     */
    @Override
    public void addToCurrentGoals(Goal goal) {
        final String name = goal.getGoalInfo().getInfo().getName();
        if (!currentGoalNames.contains(name)) {
            currentGoalNames.add(name);
            saveCurrentGoals();
        }
    }

    /**
     * Removes a goal from the list of current goals and persists the change to the file.
     *
     * @param goal The Goal object to be removed.
     */
    @Override
    public void removeFromCurrentGoals(Goal goal) {
        currentGoalNames.remove(goal.getGoalInfo().getInfo().getName());
        saveCurrentGoals();
    }

    /**
     * Checks if a specific goal is in the list of current goals.
     *
     * @param goal The Goal object to check.
     * @return {@code true} if the goal is in the current goals list, {@code false} otherwise.
     */
    @Override
    public boolean isInCurrentGoals(Goal goal) {
        return currentGoalNames.contains(goal.getGoalInfo().getInfo().getName());
    }

    /**
     * Checks if a goal with the given name exists in the repository.
     *
     * @param name The name of the goal to check for.
     * @return {@code true} if a goal with the name exists, {@code false} otherwise.
     */
    @Override
    public boolean existsByName(String name) {
        return goals.containsKey(name);
    }

    /**
     * Finds goals with a target amount within a specified range.
     *
     * @param minAmount The minimum target amount.
     * @param maxAmount The maximum target amount.
     * @return A List of Goal objects that fall within the specified amount range.
     */
    @Override
    public List<Goal> findByTargetAmountRange(double minAmount, double maxAmount) {
        final List<Goal> result = new ArrayList<>();
        for (Goal goal : goals.values()) {
            final double freq = goal.getFrequency();
            if (freq >= minAmount && freq <= maxAmount) {
                result.add(goal);
            }
        }
        return result;
    }

    /**
     * Retrieves a list of goals that are available to be selected,
     * which are defined as not being completed and having a begin and due date
     * that includes the current date.
     *
     * @return A List of available Goal objects.
     */
    @Override
    public List<Goal> findAvailableGoals() {
        final LocalDate today = LocalDate.now();
        final List<Goal> result = new ArrayList<>();
        for (Goal goal : goals.values()) {
            if (!goal.isCompleted()
                    && !today.isBefore(goal.getBeginAndDueDates().getBeginDate())
                    && !today.isAfter(goal.getBeginAndDueDates().getDueDate())) {
                result.add(goal);
            }
        }
        return result;
    }

    /**
     * Retrieves the single goal for today.
     *
     * @return The Goal object for today, or null if no goal is set for today.
     */
    @Override
    public Goal getTodayGoal() {
        Goal result = null;
        if (!todayGoalNames.isEmpty()) {
            result = goals.get(todayGoalNames.get(0));
        }
        return result;
    }

    /**
     * Saves the single goal for today by adding it to the today's goals list.
     *
     * @param goal The Goal object to be set as today's goal.
     */
    @Override
    public void saveTodayGoal(Goal goal) {
        if (goal != null) {
            addGoalToToday(goal.getGoalInfo().getInfo().getName());
        }
    }

    /**
     * Removes a goal from the repository by its name. This method delegates to {@link #deleteByName(String)}.
     *
     * @param goalName The name of the goal to remove.
     */
    @Override
    public void removeGoal(String goalName) {
        deleteByName(goalName);
    }

    /**
     * Adds a goal to today's list of goals and persists the change to the file.
     * The goal must exist in the main goals map.
     *
     * @param goalName The name of the goal to add to today's list.
     */
    @Override
    public void addGoalToToday(String goalName) {
        if (goals.containsKey(goalName) && !todayGoalNames.contains(goalName)) {
            todayGoalNames.add(goalName);
            saveTodayGoals();
        }
    }

    /**
     * Removes a goal from today's list of goals and persists the change to the file.
     *
     * @param goalName The name of the goal to remove from today's list.
     */
    @Override
    public void removeGoalFromToday(String goalName) {
        todayGoalNames.remove(goalName);
        saveTodayGoals();
    }

    /**
     * Updates the progress of a specific goal in today's list and saves the change.
     * If the new progress meets or exceeds the goal's frequency, the goal is marked as completed.
     *
     * @param goalName The name of the goal to update.
     * @param newProgress The new progress value.
     */
    @Override
    public void updateTodayGoalProgress(String goalName, int newProgress) {
        final Goal goal = goals.get(goalName);
        if (goal != null && todayGoalNames.contains(goalName)) {
            goal.setCurrentProgress(newProgress);
            if (newProgress >= goal.getFrequency()) {
                goal.setCompleted(true);
            }
            save(goal);
        }
    }

    /**
     * Retrieves the list of goals for today.
     *
     * @return A List of Goal objects for today.
     */
    @Override
    public List<Goal> getTodayGoals() {
        final List<Goal> result = new ArrayList<>();
        for (String name : todayGoalNames) {
            final Goal goal = goals.get(name);
            if (goal != null) {
                result.add(goal);
            }
        }
        return result;
    }

    /**
     * Persists all goals to a storage medium.
     */
    @Override
    public void saveGoals() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(goalsFile))) {
            out.writeObject(goals);
        }
        catch (IOException ex) {
            System.err.println("Error saving goals: " + ex.getMessage());
        }
    }

    /**
     * Persists the current goals to a storage medium.
     */
    @Override
    public void saveCurrentGoals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentGoalsFile))) {
            for (String name : currentGoalNames) {
                writer.write(name);
                writer.newLine();
            }
        }
        catch (IOException ex) {
            System.err.println("Error saving current goals: " + ex.getMessage());
        }
    }

    /**
     * Deletes a goal from the repository by its name. This method removes the goal
     * from all internal data structures (all goals, current goals, and today's goals)
     * and persists all changes to their respective files.
     *
     * @param name The name of the goal to be deleted.
     */
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
