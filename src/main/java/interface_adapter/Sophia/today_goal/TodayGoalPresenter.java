package interface_adapter.Sophia.today_goal;

import use_case.goalManage.today_goal.TodayGoalOutputBoundary;
import use_case.goalManage.today_goal.TodayGoalOutputData;

public class TodayGoalPresenter implements TodayGoalOutputBoundary {
    private final TodayGoalsViewModel viewModel;

    public TodayGoalPresenter(TodayGoalsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(TodayGoalOutputData outputData) {
        TodaysGoalsState state = viewModel.getState();
        state.setTodaysGoals(outputData.getTodayGoals());
        state.setError(null);
        viewModel.setState(state); // This will trigger the property change
    }

    @Override
    public void prepareFailView(String error) {
        TodaysGoalsState state = viewModel.getState();
        state.setError(error);
        viewModel.setState(state); // This will trigger the property change
    }
}