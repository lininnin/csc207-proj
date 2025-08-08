package use_case.alex.event_related.add_event;

import entity.info.Info;

public interface ReadAvailableEventDataAccessInterf {
    Info findInfoByName(String name);
}
