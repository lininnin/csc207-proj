package use_case.alex.event_related.add_event;


import entity.info.InfoInterf;

public interface ReadAvailableEventDataAccessInterf {
    InfoInterf findInfoByName(String name);
}
