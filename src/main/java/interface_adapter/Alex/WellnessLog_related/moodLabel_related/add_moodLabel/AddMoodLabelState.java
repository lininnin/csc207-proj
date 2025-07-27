package interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel;

/**
 * State class for adding a new MoodLabel.
 * Stores user input and error messages related to the creation form.
 */
public class AddMoodLabelState {

    private String moodName = "";
    private String moodNameError = "";

    private String type = ""; // "Positive" or "Negative"
    private String typeError = "";

    private boolean isCreating = false; // 是否处于“new”添加状态

    // ---------------- Getters ----------------

    public String getMoodName() {
        return moodName;
    }

    public String getMoodNameError() {
        return moodNameError;
    }

    public String getType() {
        return type;
    }

    public String getTypeError() {
        return typeError;
    }

    public boolean isCreating() {
        return isCreating;
    }

    // ---------------- Setters ----------------

    public void setMoodName(String moodName) {
        this.moodName = moodName != null ? moodName.trim() : "";
    }

    public void setMoodNameError(String moodNameError) {
        this.moodNameError = moodNameError;
    }

    public void setType(String type) {
        this.type = type != null ? type.trim() : "";
    }

    public void setTypeError(String typeError) {
        this.typeError = typeError;
    }

    public void setCreating(boolean creating) {
        isCreating = creating;
    }
}

