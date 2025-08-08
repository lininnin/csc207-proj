package interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * State class for adding a new MoodLabel.
 * Stores user input, error messages, and available mood label names.
 */
public class AddMoodLabelState {

    private String moodName = "";
    private String moodNameError = "";

    private String type = ""; // "Positive" or "Negative"
    private String typeError = "";

    private boolean isCreating = false; // 是否处于“new”添加状态

    /** All available mood label names (for dropdown or duplication check). */
    private List<String> availableNames = new ArrayList<>();

    /** Error message to display at the view level (e.g., general error). */
    private String errorMessage = null;

    /** Optional success message for confirmation. */
    private String successMessage = null;

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

    public List<String> getAvailableNames() {
        return availableNames;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
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

    public void setAvailableNames(List<String> availableNames) {
        this.availableNames = availableNames != null ? availableNames : new ArrayList<>();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}


