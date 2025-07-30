package use_case.Alex.Event_related.todays_events_module.edit_todays_event;

public class EditTodaysEventInputData {

    private final String id;
    private final String dueDate;

    public EditTodaysEventInputData(String id, String dueDate) {
        this.id = id;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public String getDueDate() {
        return dueDate;
    }
}

