package data_access;

import entity.Alex.EventAvailable.EventAvailableFactoryInterf;
import entity.Alex.EventAvailable.EventAvailableInterf;
import entity.info.Info;
import entity.info.InfoInterf;
import use_case.alex.event_related.add_event.ReadAvailableEventDataAccessInterf;
import use_case.alex.event_related.create_event.CreateEventDataAccessInterface;
import use_case.alex.event_related.avaliable_events_module.delete_event.DeleteEventDataAccessInterf;
import use_case.alex.event_related.avaliable_events_module.edit_event.EditEventDataAccessInterf;
import use_case.Angela.category.delete.DeleteCategoryEventDataAccessInterface;

import java.util.List;
import java.util.ArrayList;

/**
 * DAO for managing available events using DIP.
 * Depends on EventAvailableInterf and uses a factory to instantiate it.
 */
public class EventAvailableDataAccessObject implements
        CreateEventDataAccessInterface,
        DeleteEventDataAccessInterf,
        EditEventDataAccessInterf,
        ReadAvailableEventDataAccessInterf,
        DeleteCategoryEventDataAccessInterface {

    /** The underlying in-memory or persistent storage for available events. */
    private final EventAvailableInterf eventAvailable;


    /**
     * Constructs the DAO with a factory that creates EventAvailableInterf.
     *
     * @param eventFactory Factory for creating EventAvailableInterf
     */
    public EventAvailableDataAccessObject(final EventAvailableFactoryInterf eventFactory) {
        this.eventAvailable = eventFactory.create();
    }

    /**
     * Saves a new event into the available event pool.
     *
     * @param eventInfo The event information to save.
     */
    @Override
    public void save(final InfoInterf eventInfo) {
        eventAvailable.addEvent(eventInfo);
    }

    /**
     * Removes an event from the available pool.
     *
     * @param eventInfo The event to remove.
     * @return true if the event was found and removed.
     */
    @Override
    public boolean remove(final InfoInterf eventInfo) {
        return eventAvailable.removeEvent(eventInfo);
    }

    /**
     * Retrieves all available events.
     *
     * @return List of all available Info objects.
     */
    @Override
    public List<InfoInterf> getAllEvents() {
        return eventAvailable.getEventAvailable();
    }

    /**
     * Retrieves available events filtered by category.
     *
     * @param category The category to filter by.
     * @return List of events matching the category.
     */
    @Override
    public List<InfoInterf> getEventsByCategory(final String category) {
        return eventAvailable.getEventsByCategory(category);
    }

    /**
     * Retrieves available events filtered by name.
     *
     * @param name The name to filter by.
     * @return List of events matching the name.
     */
    @Override
    public List<InfoInterf> getEventsByName(final String name) {
        return eventAvailable.getEventsByName(name);
    }

    /**
     * Returns the total number of available events.
     *
     * @return Total count of events.
     */
    @Override
    public int getEventCount() {
        return eventAvailable.getEventCount();
    }

    /**
     * Checks if the given event exists in the pool.
     *
     * @param eventInfo The event to check.
     * @return true if the event exists, false otherwise.
     */
    @Override
    public boolean contains(final InfoInterf eventInfo) {
        return eventAvailable.contains(eventInfo);
    }

    /**
     * Clears all available events from the pool.
     */
    @Override
    public void clearAll() {
        eventAvailable.clearAll();
    }

    /**
     * Retrieves an event by its unique ID.
     *
     * @param id The ID of the event.
     * @return The matching Info object, or null if not found.
     */
    @Override
    public InfoInterf getEventById(final String id) {
        for (InfoInterf info : eventAvailable.getEventAvailable()) {
            if (info.getId().equals(id)) {
                return info;
            }
        }
        return null;
    }

    /**
     * Updates the details of an existing event.
     *
     * @param updatedInfo The new information for the event.
     * @return true if update succeeded, false otherwise.
     */
    @Override
    public boolean update(final InfoInterf updatedInfo) {
        for (InfoInterf info : eventAvailable.getEventAvailable()) {
            if (info.getId().equals(updatedInfo.getId())) {
                info.setName(updatedInfo.getName());
                info.setCategory(updatedInfo.getCategory());
                info.setDescription(updatedInfo.getDescription());
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an event with the given ID exists.
     *
     * @param id The ID to check.
     * @return true if exists, false otherwise.
     */
    @Override
    public boolean existsById(final String id) {
        for (InfoInterf info : eventAvailable.getEventAvailable()) {
            if (info.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds an event info object by its name.
     *
     * @param name The name to search for.
     * @return The matching Info object, or null if not found.
     */
    public InfoInterf findInfoByName(final String name) {
        for (InfoInterf info : eventAvailable.getEventAvailable()) {
            if (info.getName().equals(name)) {
                return info;
            }
        }
        return null;
    }
    
    // ===== DeleteCategoryEventDataAccessInterface methods =====
    
    @Override
    public List<Info> findAvailableEventsByCategory(String categoryId) {
        List<Info> result = new ArrayList<>();
        for (InfoInterf event : eventAvailable.getEventAvailable()) {
            if (event.getCategory() != null && event.getCategory().equals(categoryId)) {
                // Cast to Info since InfoInterf is implemented by Info
                if (event instanceof Info) {
                    result.add((Info) event);
                }
            }
        }
        return result;
    }
    
    @Override
    public List<Info> findTodaysEventsByCategory(String categoryId) {
        // Available events DAO doesn't manage today's events
        // This will be handled by TodaysEventDataAccessObject
        return new ArrayList<>();
    }
    
    @Override
    public boolean clearAvailableEventCategory(String eventId) {
        for (InfoInterf event : eventAvailable.getEventAvailable()) {
            if (event.getId().equals(eventId)) {
                event.setCategory(null);  // Clear the category
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean clearTodaysEventCategory(String eventId) {
        // Available events DAO doesn't manage today's events
        // This will be handled by TodaysEventDataAccessObject
        return false;
    }
}
