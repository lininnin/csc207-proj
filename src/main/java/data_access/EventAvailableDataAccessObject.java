package data_access;

import entity.Alex.EventAvailable.EventAvailableFactoryInterf;
import entity.Alex.EventAvailable.EventAvailableInterf;
import entity.info.Info;
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

    private final EventAvailableInterf eventAvailable;

    /**
     * Constructs the DAO with a factory that creates EventAvailableInterf.
     *
     * @param eventFactory Factory for creating EventAvailableInterf
     */
    public EventAvailableDataAccessObject(EventAvailableFactoryInterf eventFactory) {
        this.eventAvailable = eventFactory.create();
    }

    @Override
    public void save(Info eventInfo) {
        eventAvailable.addEvent(eventInfo);
    }

    @Override
    public boolean remove(Info eventInfo) {
        return eventAvailable.removeEvent(eventInfo);
    }

    @Override
    public List<Info> getAllEvents() {
        return eventAvailable.getEventAvailable();
    }

    @Override
    public List<Info> getEventsByCategory(String category) {
        return eventAvailable.getEventsByCategory(category);
    }

    @Override
    public List<Info> getEventsByName(String name) {
        return eventAvailable.getEventsByName(name);
    }

    @Override
    public int getEventCount() {
        return eventAvailable.getEventCount();
    }

    @Override
    public boolean contains(Info eventInfo) {
        return eventAvailable.contains(eventInfo);
    }

    @Override
    public void clearAll() {
        eventAvailable.clearAll();
    }

    @Override
    public Info getEventById(String id) {
        for (Info info : eventAvailable.getEventAvailable()) {
            if (info.getId().equals(id)) {
                return info;
            }
        }
        return null;
    }

    @Override
    public boolean update(Info updatedInfo) {
        for (Info info : eventAvailable.getEventAvailable()) {
            if (info.getId().equals(updatedInfo.getId())) {
                info.setName(updatedInfo.getName());
                info.setCategory(updatedInfo.getCategory());
                info.setDescription(updatedInfo.getDescription());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsById(String id) {
        for (Info info : eventAvailable.getEventAvailable()) {
            if (info.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public Info findInfoByName(String name) {
        for (Info info : eventAvailable.getEventAvailable()) {
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
        for (Info event : eventAvailable.getEventAvailable()) {
            if (event.getCategory() != null && event.getCategory().equals(categoryId)) {
                result.add(event);
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
        for (Info event : eventAvailable.getEventAvailable()) {
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
