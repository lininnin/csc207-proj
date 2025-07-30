package data_access;

import entity.Alex.EventAvailable.EventAvailable;
import entity.Info.Info;
import use_case.Alex.Event_related.add_event.ReadAvailableEventDataAccessInterf;
import use_case.Alex.Event_related.create_event.CreateEventDataAccessInterface;
import use_case.Alex.Event_related.avaliable_events_module.delete_event.DeleteEventDataAccessInterf;
import use_case.Alex.Event_related.avaliable_events_module.edit_event.EditEventDataAccessInterf;

import java.util.List;

public class EventAvailableDataAccessObject implements
        CreateEventDataAccessInterface,
        DeleteEventDataAccessInterf,
        EditEventDataAccessInterf,
        ReadAvailableEventDataAccessInterf {

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

    // ✅ 补充实现
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
                // ✅ 就地修改原 Info 的字段
                info.setName(updatedInfo.getName());
                info.setCategory(updatedInfo.getCategory());
                info.setDescription(updatedInfo.getDescription());
                return true;
            }
        }
        return false; // 如果没找到
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


