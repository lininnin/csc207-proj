package entity.Alex.EventAvailable;

import entity.Info.Info;
import java.util.List;

public interface EventAvailableInterf {
    void addEvent(Info info);
    boolean removeEvent(Info info);
    List<Info> getEventAvailable();
    List<Info> getEventsByCategory(String category);
    List<Info> getEventsByName(String name);
    int getEventCount();
    boolean contains(Info info);
    void clearAll();
}

