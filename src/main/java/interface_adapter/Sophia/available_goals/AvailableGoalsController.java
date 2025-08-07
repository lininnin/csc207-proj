package interface_adapter.Sophia.available_goals;

import use_case.goalManage.available_goals.AvailableGoalsInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputBoundary;
import use_case.goalManage.delete_goal.DeleteGoalInputData;
import use_case.goalManage.today_goal.TodayGoalInputBoundary;
import use_case.goalManage.today_goal.TodayGoalInputData;

public class AvailableGoalsController {
    private final AvailableGoalsInputBoundary availableGoalsInteractor;
    private final TodayGoalInputBoundary todayGoalInteractor;
    private final DeleteGoalInputBoundary deleteGoalInteractor;

    public AvailableGoalsController(AvailableGoalsInputBoundary availableGoalsInteractor,
                                    TodayGoalInputBoundary todayGoalInteractor,
                                    DeleteGoalInputBoundary deleteGoalInteractor) {
        this.availableGoalsInteractor = availableGoalsInteractor;
        this.todayGoalInteractor = todayGoalInteractor;
        this.deleteGoalInteractor = deleteGoalInteractor;
    }

    public void execute(String command) {
        if (command.startsWith("add_to_today:")) {
            String goalName = command.substring("add_to_today:".length());
            TodayGoalInputData inputData = new TodayGoalInputData(goalName, false);
            this.todayGoalInteractor.addToToday(inputData);
        }
        else if (command.startsWith("delete:")) {
            String goalName = command.substring("delete:".length());
            DeleteGoalInputData inputData = new DeleteGoalInputData(goalName, false);
            this.deleteGoalInteractor.execute(inputData);
        }
        else {
            // Default refresh operation
            this.availableGoalsInteractor.execute();
        }
    }
}