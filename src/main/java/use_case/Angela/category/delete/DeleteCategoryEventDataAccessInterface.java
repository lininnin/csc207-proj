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
}