package data_access;

import entity.Sophia.Goal;
import java.util.List;
import java.util.Optional;

public interface GoalRepository {
    // Basic CRUD operations
    void save(Goal goal);
    Optional<Goal> findByName(String name);
    void deleteByName(String name);
    List<Goal> getAllGoals();

    // Current goals management
    List<Goal> getCurrentGoals();
    void addToCurrentGoals(Goal goal);
    void removeFromCurrentGoals(Goal goal);
    boolean isInCurrentGoals(Goal goal);

    // Utility methods
    boolean existsByName(String name);

    // Additional query methods
    List<Goal> findByTargetAmountRange(double minAmount, double maxAmount);
    List<Goal> findAvailableGoals();

    Goal getTodayGoal();
    void saveTodayGoal(Goal goal);
    void removeGoal(String goalName);

    List<Goal> getTodayGoals();
    void addGoalToToday(String goalName);
    void removeGoalFromToday(String goalName);
}
