package interface_adapter.alex.event_related.create_event;

/**
 * State object for the Create Event View.
 * Stores user input and error messages related to the event creation form.
 */
public class CreatedEventState {

    private String name = "";
    private String nameError = "";

    private String description = "";
    private String descriptionError = "";

    private String category = "";
    private String categoryError = "";

    //private boolean oneTime = false;

    // ---------------- Getters ----------------

    public String getName() {
        return name;
    }

    public String getNameError() {
        return nameError;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionError() {
        return descriptionError;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryError() {
        return categoryError;
    }

//    public boolean isOneTime() {
//        return oneTime;
//    }

    // ---------------- Setters ----------------

    public void setName(String name) {
        this.name = name != null ? name.trim() : "";
    }

    public void setNameError(String nameError) {
        this.nameError = nameError != null ? nameError.trim() : "";
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
    }

    public void setDescriptionError(String descriptionError) {
        this.descriptionError = descriptionError != null ? descriptionError.trim() : "";
    }

    public void setCategory(String category) {
        this.category = category != null ? category.trim() : "";
    }

    public void setCategoryError(String categoryError) {
        this.categoryError = categoryError != null ? categoryError.trim() : "";
    }

//    public void setOneTime(boolean oneTime) {
//        this.oneTime = oneTime;
//    }

    // ---------------- Debug Output ----------------

    @Override
    public String toString() {
        return "CreatedEventState{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'';
                //", oneTime=" + oneTime +
                //'}';
    }
}
