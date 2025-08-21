package use_case.Angela.category.delete;

import entity.info.Info;
import java.util.List;

/**
 * Interface for updating events when a category is deleted.
 * Events need their category field cleared when the category is deleted.
 */
public interface DeleteCategoryEventDataAccessInterface {
    
    /**
     * Find all available events that have the specified category.
     * @param categoryId The ID of the category to search for
     * @return List of events with the specified category
     */
    List<Info> findAvailableEventsByCategory(String categoryId);
    
    /**
     * Find all today's events that have the specified category.
     * @param categoryId The ID of the category to search for
     * @return List of today's events with the specified category
     */
    List<Info> findTodaysEventsByCategory(String categoryId);
    
    /**
     * Update an available event's category to empty/null.
     * @param eventId The ID of the event to update
     * @return true if update was successful
     */
    boolean clearAvailableEventCategory(String eventId);
    
    /**
     * Update a today's event's category to empty/null.
     * @param eventId The ID of the event to update
     * @return true if update was successful
     */
    boolean clearTodaysEventCategory(String eventId);

    /**
     * Finds all available events that have empty/null category.
     * Used to check if there are already events with empty categories before deletion.
     *
     * @return List of available events that have empty or null category
     */
    List<Info> findAvailableEventsWithEmptyCategory();

    /**
     * Finds all today's events that have empty/null category.
     * Used to check if there are already events with empty categories before deletion.
     *
     * @return List of today's events that have empty or null category
     */
    List<Info> findTodaysEventsWithEmptyCategory();

    /**
     * Updates all events that reference the given category to have null/empty category.
     * This is a bulk operation used when a category is deleted.
     *
     * @param categoryId The category ID whose references should be cleared
     * @return true if all updates succeeded, false otherwise
     */
    boolean updateEventsCategoryToNull(String categoryId);
}