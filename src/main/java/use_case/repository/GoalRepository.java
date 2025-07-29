package use_case.repository;

import entity.Sophia.Goal;
import java.util.List;
import java.util.Optional;

/**
 * Data access interface for Goal entities
 */
public interface GoalRepository {
    // Basic CRUD operations
    void save(Goal goal);
    Optional<Goal> findByName(String name);
    void deleteByName(String name);
    List<Goal> findAll();

    // Special queries for goals
    boolean existsByName(String name);
    List<Goal> findCurrentGoals();
    void addToCurrentGoals(Goal goal);
    void removeFromCurrentGoals(Goal goal);
    boolean isInCurrentGoals(Goal goal);
}