// interface_adapter/GoalRepository.java
package interface_adapter;

import entity.Sophia.Goal;
import java.util.List;

public interface GoalRepository {
    List<Goal> getAllGoals();
    Goal getGoalByName(String name);
    void saveGoal(Goal goal);
    void deleteGoal(String name);
    List<Goal> getCurrentGoals();
    void addToCurrentGoals(Goal goal);
    void removeFromCurrentGoals(Goal goal);
}