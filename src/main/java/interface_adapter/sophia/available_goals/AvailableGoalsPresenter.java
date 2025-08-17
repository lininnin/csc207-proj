package interface_adapter.sophia.available_goals;

import use_case.goalManage.available_goals.AvailableGoalsOutputBoundary;
import use_case.goalManage.available_goals.AvailableGoalsOutputData;

/**
 * Presenter class for handling the presentation logic of available goals.
 * Implements the AvailableGoalsOutputBoundary to interact with the view model.
 */
public class AvailableGoalsPresenter implements AvailableGoalsOutputBoundary {
    /**
     * The view model associated with this presenter.
     */
    private final AvailableGoalsViewModel viewModel;

    /**
     * Constructs an AvailableGoalsPresenter with the specified view model.
     *
     * @param viewModel The view model to associate with this presenter
     */
    public AvailableGoalsPresenter(AvailableGoalsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Presents the available goals by updating the view model state
     * and notifying the view of changes.
     *
     * @param outputData Contains the available goals data to present
     */
    @Override
    public void presentAvailableGoals(AvailableGoalsOutputData outputData) {
        final AvailableGoalsState state = viewModel.getState();
        state.setAvailableGoals(outputData.getAvailableGoals());
        viewModel.firePropertyChanged();
    }
}
