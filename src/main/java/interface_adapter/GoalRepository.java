package interface_adapter;

import java.util.List;

import entity.Sophia.Goal;

/**
 * Repository interface for managing Goal entities.
 * Defines the contract for persistent storage operations related to goals.
 */
public interface GoalRepository {
    /**
     * Retrieves all goals from the repository.
     * @return List of all Goal entities
     */
    List<Goal> getAllGoals();

    /**
     * Retrieves a specific goal by its name.
     * @param name The name of the goal to retrieve
     * @return The Goal entity with the specified name, or null if not found
     */
    Goal getGoalByName(String name);

    /**
     * Saves or updates a goal in the repository.
     * @param goal The Goal entity to save
     */
    void saveGoal(Goal goal);

    /**
     * Deletes a goal from the repository.
     * @param name The name of the goal to delete
     */
    void deleteGoal(String name);

    /**
     * Retrieves the list of currently active goals.
     * @return List of current Goal entities
     */
    List<Goal> getCurrentGoals();

    /**
     * Adds a goal to the current goals list.
     * @param goal The Goal entity to add to current goals
     */
    void addToCurrentGoals(Goal goal);

    /**
     * Removes a goal from the current goals list.
     * @param goal The Goal entity to remove from current goals
     */
    void removeFromCurrentGoals(Goal goal);
}
