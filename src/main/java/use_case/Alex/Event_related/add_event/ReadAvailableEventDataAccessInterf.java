package use_case.Alex.Event_related.add_event;

import entity.Info.Info;

public interface ReadAvailableEventDataAccessInterf {
    Info findInfoByName(String name);
}
