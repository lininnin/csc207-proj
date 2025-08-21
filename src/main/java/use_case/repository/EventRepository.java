package use_case.repository;

import entity.alex.Event.Event;
import java.util.List;

public interface EventRepository {
    List<Event> getAllEvents();
    List<Event> getTodayEvents();
    void save(Event event);
}