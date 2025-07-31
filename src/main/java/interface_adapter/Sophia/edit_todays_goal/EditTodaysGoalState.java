package interface_adapter.Sophia.edit_todays_goal;

import java.time.LocalDate;

public class EditTodaysGoalState {
    private String goalName = "";
    private LocalDate newDueDate;
    private String error = null;

    // Getter and setter for goalName
    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    // Getter and setter for newDueDate
    public LocalDate getNewDueDate() {
        return newDueDate;
    }

    public void setNewDueDate(LocalDate newDueDate) {
        this.newDueDate = newDueDate;
    }

    // Getter and setter for error
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
