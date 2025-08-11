package data_access.in_memory_repo;

import entity.Alex.Event.Event;
import use_case.repository.EventRepository;
import java.util.ArrayList;
import java.util.List;

public class InMemoryEventRepository implements EventRepository {
    private final List<Event> allEvents = new ArrayList<>();
    private final List<Event> todayEvents = new ArrayList<>();

    @Override
    public List<Event> getAllEvents() {
        return new ArrayList<>(allEvents);
    }

    @Override
    public List<Event> getTodayEvents() {
        return new ArrayList<>(todayEvents);
    }

    @Override
    public void save(Event event) {
        allEvents.add(event);
    }

    // Additional methods for testing
    public void addToToday(Event event) {
        todayEvents.add(event);
    }
}