package interface_adapter.Alex.Event_related.available_event_module.edit_event;

public class EditedEventState {

    private String eventId = "";
    private String name = "";
    private String nameError;

    private String category = "";
    private String categoryError;

    private String description = "";
    private String descriptionError;

    private boolean oneTime = false;

    private String editError; // ✅ 新增：用于整体编辑失败的提示

    // ----------- Getters -----------

    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getNameError() {
        return nameError;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryError() {
        return categoryError;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionError() {
        return descriptionError;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public String getEditError() {
        return editError;
    }

    // ----------- Setters -----------

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCategoryError(String categoryError) {
        this.categoryError = categoryError;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionError(String descriptionError) {
        this.descriptionError = descriptionError;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public void setEditError(String editError) {
        this.editError = editError;
    }
}


