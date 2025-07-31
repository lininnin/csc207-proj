package data_access;

import entity.Sophia.Goal;
import interface_adapter.GoalRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileGoalRepository implements GoalRepository {
    private final Map<String, Goal> goals = new HashMap<>();
    private final List<Goal> currentGoals = new ArrayList<>();

    @Override
    public List<Goal> getAllGoals() {
        return new ArrayList<>(goals.values());
    }

    @Override
    public Goal getGoalByName(String name) {
        return goals.get(name);
    }

    @Override
    public void saveGoal(Goal goal) {
        goals.put(goal.getInfo().getName(), goal);
    }

    @Override
    public void deleteGoal(String name) {
        Goal goal = goals.remove(name);
        currentGoals.remove(goal);
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
}