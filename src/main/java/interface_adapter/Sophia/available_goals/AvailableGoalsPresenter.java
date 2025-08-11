package interface_adapter.Sophia.available_goals;

import use_case.goalManage.available_goals.AvailableGoalsOutputBoundary;
import use_case.goalManage.available_goals.AvailableGoalsOutputData;

public class AvailableGoalsPresenter implements AvailableGoalsOutputBoundary {
    private final AvailableGoalsViewModel viewModel;

    public AvailableGoalsPresenter(AvailableGoalsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentAvailableGoals(AvailableGoalsOutputData outputData) {
        AvailableGoalsState state = viewModel.getState();
        state.setAvailableGoals(outputData.getAvailableGoals());
        viewModel.firePropertyChanged();
    }
}