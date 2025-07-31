package interface_adapter.Sophia.edit_todays_goal;

import use_case.goalManage.edit_todays_goal.EditTodaysGoalInputBoundary;
import use_case.goalManage.edit_todays_goal.EditTodaysGoalInputData;
import java.time.LocalDate;

public class EditTodaysGoalController {
    private final EditTodaysGoalInputBoundary interactor;

    public EditTodaysGoalController(EditTodaysGoalInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String goalName, LocalDate newDueDate, boolean confirmed) {
        EditTodaysGoalInputData inputData = new EditTodaysGoalInputData(
                goalName, newDueDate, confirmed
        );
        interactor.execute(inputData);
    }
}