package data_access;

import entity.Alex.EventAvailable.EventAvailableFactoryInterf;
import entity.Alex.EventAvailable.EventAvailableInterf;
import entity.info.Info;
import use_case.alex.event_related.add_event.ReadAvailableEventDataAccessInterf;
import use_case.alex.event_related.create_event.CreateEventDataAccessInterface;
import use_case.alex.event_related.avaliable_events_module.delete_event.DeleteEventDataAccessInterf;
import use_case.alex.event_related.avaliable_events_module.edit_event.EditEventDataAccessInterf;

import java.util.List;

/**
 * DAO for managing available events using DIP.
 * Depends on EventAvailableInterf and uses a factory to instantiate it.
 */
public class EventAvailableDataAccessObject implements
        CreateEventDataAccessInterface,
        DeleteEventDataAccessInterf,
        EditEventDataAccessInterf,
        ReadAvailableEventDataAccessInterf {

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
}
