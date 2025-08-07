package data_access;

import entity.Sophia.Goal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileGoalRepository implements GoalRepository {
    private List<Goal> goals = new ArrayList<>();
    private List<Goal> currentGoals = new ArrayList<>();
    private Goal todayGoal = null;  // Single today's goal

    @Override
    public void save(Goal goal) {
        // Remove existing goal with same name if present
        deleteByName(goal.getGoalInfo().getInfo().getName());
        goals.add(goal);
    }

    @Override
    public Optional<Goal> findByName(String name) {
        return goals.stream()
                .filter(g -> g.getGoalInfo().getInfo().getName().equals(name))
                .findFirst();
    }

    @Override
    public void deleteByName(String name) {
        goals.removeIf(g -> g.getGoalInfo().getInfo().getName().equals(name));
        currentGoals.removeIf(g -> g.getGoalInfo().getInfo().getName().equals(name));
        if (todayGoal != null && todayGoal.getGoalInfo().getInfo().getName().equals(name)) {
            todayGoal = null;
        }
    }

    @Override
    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals);
    }

    @Override
    public List<Goal> getCurrentGoals() {
        return new ArrayList<>(currentGoals);
    }

    @Override
    public void addToCurrentGoals(Goal goal) {
        if (!currentGoals.contains(goal)) {
            currentGoals.add(goal);
        }
    }

    @Override
    public void removeFromCurrentGoals(Goal goal) {
        currentGoals.remove(goal);
    }

    @Override
    public boolean isInCurrentGoals(Goal goal) {
        return currentGoals.contains(goal);
    }

    @Override
    public boolean existsByName(String name) {
        return goals.stream()
                .anyMatch(g -> g.getGoalInfo().getInfo().getName().equals(name));
    }

    @Override
    public List<Goal> findByTargetAmountRange(double minAmount, double maxAmount) {
        return goals.stream()
                .filter(g -> {
                    double amount = Double.parseDouble(g.getGoalInfo().getTargetTaskInfo().getDescription());
                    return amount >= minAmount && amount <= maxAmount;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Goal> findAvailableGoals() {
        return goals.stream()
                .filter(Goal::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public Goal getTodayGoal() {
        return todayGoal;
    }

    @Override
    public void saveTodayGoal(Goal goal) {
        this.todayGoal = goal;
    }

    @Override
    public void removeGoal(String goalName) {
        deleteByName(goalName);  // Reuse existing implementation
    }

    @Override
    public List<Goal> getTodayGoals() {
        // Implement logic to get today's goals from your data source
        return Collections.emptyList(); // Replace with actual implementation
    }

    @Override
    public void addGoalToToday(String goalName) {
        // Implement logic to add goal to today's list
    }

    @Override
    public void removeGoalFromToday(String goalName) {
        // Implement logic to remove goal from today's list
    }


}