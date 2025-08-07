package interface_adapter.Sophia.today_goal;

import use_case.goalManage.today_goal.TodayGoalOutputBoundary;
import use_case.goalManage.today_goal.TodayGoalOutputData;
import interface_adapter.Sophia.today_goal.TodayGoalsViewModel;

public class TodayGoalPresenter implements TodayGoalOutputBoundary {
    private final TodayGoalsViewModel todayGoalsViewModel;

    public TodayGoalPresenter(TodayGoalsViewModel todayGoalsViewModel) {
        this.todayGoalsViewModel = todayGoalsViewModel;
    }

    @Override
    public void prepareSuccessView(TodayGoalOutputData outputData) {
        TodaysGoalsState currentState = todayGoalsViewModel.getState();
        currentState.setTodayGoals(outputData.getTodayGoals());
        todayGoalsViewModel.setState(currentState);
        todayGoalsViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
       System.err.println("Operation failed: " + error);

         }
}