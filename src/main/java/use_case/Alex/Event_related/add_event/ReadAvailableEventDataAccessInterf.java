package use_case.Alex.Event_related.add_event;

import entity.info.Info;

public interface ReadAvailableEventDataAccessInterf {
    Info findInfoByName(String name);
}
