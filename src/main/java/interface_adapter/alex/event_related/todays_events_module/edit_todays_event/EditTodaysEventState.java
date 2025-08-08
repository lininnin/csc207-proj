package interface_adapter.alex.event_related.todays_events_module.edit_todays_event;

public class EditTodaysEventState {

    private String eventId = "";
    private String dueDate = "";
    private String dueDateError;
    private String editError; // 用于整体编辑失败的提示

    // ----------- Getters -----------

    public String getEventId() {
        return eventId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueDateError() {
        return dueDateError;
    }

    public String getEditError() {
        return editError;
    }

    // ----------- Setters -----------

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setDueDateError(String dueDateError) {
        this.dueDateError = dueDateError;
    }

    public void setEditError(String editError) {
        this.editError = editError;
    }
}

