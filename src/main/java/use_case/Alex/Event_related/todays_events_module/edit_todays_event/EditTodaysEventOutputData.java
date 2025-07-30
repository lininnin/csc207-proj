package use_case.Alex.Event_related.todays_events_module.edit_todays_event;

public class EditTodaysEventOutputData {
    private final String id;
    private final String dueDate;
    private final boolean useCaseFailed;

    public EditTodaysEventOutputData(String id, String dueDate, boolean useCaseFailed) {
        this.id = id;
        this.dueDate = dueDate;
        this.useCaseFailed = useCaseFailed;
    }

    public String getId() {
        return id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}

