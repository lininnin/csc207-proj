package interface_adapter.Alex.Event_related.add_event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * State for the Add Today's Event view.
 * Holds the user input and system feedback for the AddEvent use case.
 */
public class AddedEventState {

    /** The name selected from available events. */
    private String selectedName = "";

    /** The optional due date selected by the user. */
    private LocalDate dueDate = null;

    /** All available names (for dropdown display). */
    private List<String> availableNames = new ArrayList<>();

    /** Error message to be displayed in the view. */
    private String errorMessage = null;

    /** Optional success message to indicate event added successfully. */
    private String successMessage = null;

    // ---------------- Getters ----------------

    public String getSelectedName() {
        return selectedName;
    }

    public LocalDate getDueDate() {
        return dueDate;
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

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setAvailableNames(List<String> availableNames) {
        this.availableNames = availableNames;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}