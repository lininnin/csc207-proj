package use_case.Alex.add_event;

import entity.info.Info;

public interface ReadAvailableEventDataAccessInterf {
    Info findInfoByName(String name);
}
