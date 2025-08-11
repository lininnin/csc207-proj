package entity.Alex.EventAvailable;

import entity.info.InfoInterf;
import java.util.List;

public interface EventAvailableInterf {
    void addEvent(InfoInterf info);
    boolean removeEvent(InfoInterf info);
    List<InfoInterf> getEventAvailable();
    List<InfoInterf> getEventsByCategory(String category);
    List<InfoInterf> getEventsByName(String name);
    int getEventCount();
    boolean contains(InfoInterf info);
    void clearAll();
}


