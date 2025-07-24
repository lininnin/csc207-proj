package use_case.Alex.add_event;

import entity.Info.Info;

public interface ReadAvailableEventDataAccessInterf {
    Info findInfoByName(String name);
}
