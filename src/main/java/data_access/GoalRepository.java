package data_access;

import java.util.List;
import java.util.Optional;

import entity.sophia.Goal;

/**
 * An interface for data access operations related to goals.
 * This repository provides methods for CRUD (Create, Read, Update, Delete) operations,
 * as well as specific queries for managing current and today's goals.
 */
public interface GoalRepository {
    /**
     * Saves a goal to the repository.
     * If the goal already exists, it is updated; otherwise, it is created.
     *
     * @param goal The Goal object to be saved.
     */
    void save(Goal goal);

    /**
     * Finds a goal by its unique name.
     *
     * @param name The name of the goal to find.
     * @return An Optional containing the Goal if found, or an empty Optional otherwise.
     */
    Optional<Goal> findByName(String name);

    /**
     * Deletes a goal from the repository by its name.
     *
     * @param name The name of the goal to be deleted.
     */
    void deleteByName(String name);

    /**
     * Retrieves all goals stored in the repository.
     *
     * @return A List of all Goal objects.
     */
    List<Goal> getAllGoals();

    /**
     * Retrieves a list of goals marked as "current goals".
     *
     * @return A List of Goal objects representing the current goals.
     */
    List<Goal> getCurrentGoals();

    /**
     * Adds a goal to the list of current goals.
     *
     * @param goal The Goal object to be added.
     */
    void addToCurrentGoals(Goal goal);

    /**
     * Removes a goal from the list of current goals.
     *
     * @param goal The Goal object to be removed.
     */
    void removeFromCurrentGoals(Goal goal);

    /**
     * Checks if a specific goal is in the list of current goals.
     *
     * @param goal The Goal object to check.
     * @return {@code true} if the goal is in the current goals list, {@code false} otherwise.
     */
    boolean isInCurrentGoals(Goal goal);

    /**
     * Checks if a goal with the given name exists in the repository.
     *
     * @param name The name of the goal to check for.
     * @return {@code true} if a goal with the name exists, {@code false} otherwise.
     */
    boolean existsByName(String name);

    /**
     * Finds goals with a target amount within a specified range.
     *
     * @param minAmount The minimum target amount.
     * @param maxAmount The maximum target amount.
     * @return A List of Goal objects that fall within the specified amount range.
     */
    List<Goal> findByTargetAmountRange(double minAmount, double maxAmount);

    /**
     * Retrieves a list of goals that are available to be selected,
     * typically meaning they are not currently active or completed.
     *
     * @return A List of available Goal objects.
     */
    List<Goal> findAvailableGoals();

    /**
     * Retrieves the single goal for today.
     *
     * @return The Goal object for today.
     */
    Goal getTodayGoal();

    /**
     * Saves the single goal for today.
     *
     * @param goal The Goal object to be set as today's goal.
     */
    void saveTodayGoal(Goal goal);

    /**
     * Removes a goal from the repository by its name.
     *
     * @param goalName The name of the goal to remove.
     */
    void removeGoal(String goalName);

    /**
     * Adds a goal to today's list of goals.
     *
     * @param goalName The name of the goal to add to today's list.
     */
    void addGoalToToday(String goalName);

    /**
     * Removes a goal from today's list of goals.
     *
     * @param goalName The name of the goal to remove from today's list.
     */
    void removeGoalFromToday(String goalName);

    /**
     * Updates the progress of a specific goal in today's list.
     *
     * @param goalName The name of the goal to update.
     * @param newProgress The new progress value.
     */
    void updateTodayGoalProgress(String goalName, int newProgress);

    /**
     * Retrieves the list of goals for today.
     *
     * @return A List of Goal objects for today.
     */
    List<Goal> getTodayGoals();

    /**
     * Loads all goals from a persistent storage.
     *
     * @return A List of all loaded Goal objects.
     */
    List<Goal> loadGoals();

    /**
     * Loads the current goals from a persistent storage.
     *
     * @return A List of loaded Goal objects that are marked as current.
     */
    List<Goal> loadCurrentGoals();

    /**
     * Persists all goals to a storage medium.
     */
    void saveGoals();

    /**
     * Persists the current goals to a storage medium.
     */
    void saveCurrentGoals();
}
