package interface_adapter.alex.Settings_related.edit_notificationTime;

public class EditNotificationTimeState {

    private String reminder1 = "08:00";
    private String reminder2 = "12:00";
    private String reminder3 = "20:00";

    private boolean isEditing = true; // 默认处于编辑状态（与 ViewModel 搭配逻辑一致）
    private String errorMessage = null;

    // ---------------- Getters ----------------

    public String getReminder1() {
        return reminder1;
    }

    public String getReminder2() {
        return reminder2;
    }

    public String getReminder3() {
        return reminder3;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // ---------------- Setters ----------------

    public void setReminder1(String reminder1) {
        this.reminder1 = reminder1;
    }

    public void setReminder2(String reminder2) {
        this.reminder2 = reminder2;
    }

    public void setReminder3(String reminder3) {
        this.reminder3 = reminder3;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

