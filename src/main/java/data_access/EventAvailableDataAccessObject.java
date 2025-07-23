package data_access;

import entity.Alex.EventAvailable.EventAvailable;
import entity.Info.Info;
import use_case.Alex.create_event.CreateEventDataAccessInterface;

import java.util.List;

public class EventAvailableDataAccessObject implements CreateEventDataAccessInterface {

    private final EventAvailable eventAvailable = new EventAvailable();

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
}

