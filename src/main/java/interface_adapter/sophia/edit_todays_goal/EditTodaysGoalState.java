package interface_adapter.sophia.edit_todays_goal;

import java.time.LocalDate;

/**
 * Represents the state of today's goal being edited.
 * Tracks the goal name, new due date, and any errors.
 */
public class EditTodaysGoalState {
    private String goalName;
    private LocalDate newDueDate;
    private String error;

    /**
     * Constructs a new EditTodaysGoalState with default values.
     */
    public EditTodaysGoalState() {
        this.goalName = "";
        this.newDueDate = null;
        this.error = null;
    }

    /**
     * Gets the name of the goal being edited.
     * @return The goal name
     */
    public String getGoalName() {
        return goalName;
    }

    /**
     * Sets the name of the goal being edited.
     * @param goalName The goal name to set
     */
    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    /**
     * Gets the new due date for the goal.
     * @return The new due date
     */
    public LocalDate getNewDueDate() {
        return newDueDate;
    }

    /**
     * Sets the new due date for the goal.
     * @param newDueDate The new due date to set
     */
    public void setNewDueDate(LocalDate newDueDate) {
        this.newDueDate = newDueDate;
    }

    /**
     * Gets the error message if editing failed.
     * @return The error message
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for failed editing.
     * @param error The error message to set
     */
    public void setError(String error) {
        this.error = error;
    }
}
