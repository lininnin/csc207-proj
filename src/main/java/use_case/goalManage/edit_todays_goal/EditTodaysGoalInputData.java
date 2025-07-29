package use_case.goalManage.edit_todays_goal;

import java.time.LocalDate;

public class EditTodaysGoalInputData {
    private final String goalName;
    private final LocalDate newDueDate;
    private final boolean isConfirmed;

    public EditTodaysGoalInputData(String goalName, LocalDate newDueDate, boolean isConfirmed) {
        this.goalName = goalName;
        this.newDueDate = newDueDate;
        this.isConfirmed = isConfirmed;
    }

    // Getters
    public String getGoalName() { return goalName; }
    public LocalDate getNewDueDate() { return newDueDate; }
    public boolean isConfirmed() { return isConfirmed; }
}