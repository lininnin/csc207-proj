package interface_adapter.Angela.today_so_far;

import use_case.Angela.today_so_far.TodaySoFarOutputBoundary;
import use_case.Angela.today_so_far.TodaySoFarOutputData;

/**
 * Presenter for the Today So Far panel.
 */
public class TodaySoFarPresenter implements TodaySoFarOutputBoundary {
    
    private final TodaySoFarViewModel viewModel;
    
    public TodaySoFarPresenter(TodaySoFarViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void presentTodaySoFar(TodaySoFarOutputData outputData) {
        System.out.println("DEBUG: TodaySoFarPresenter.presentTodaySoFar() called");
        TodaySoFarState state = new TodaySoFarState();
        
        // Convert output data to state
        state.setGoals(outputData.getGoals());
        state.setCompletedItems(outputData.getCompletedItems());
        state.setCompletionRate(outputData.getCompletionRate());
        state.setWellnessEntries(outputData.getWellnessEntries());
        state.setError(null);
        
        // Update view model
        viewModel.setState(state);
        System.out.println("DEBUG: Firing property change with " + state.getGoals().size() + " goals, " + 
                          state.getCompletedItems().size() + " completed items");
        viewModel.firePropertyChanged();
    }
    
    @Override
    public void presentError(String error) {
        TodaySoFarState state = viewModel.getState();
        if (state == null) {
            state = new TodaySoFarState();
        }
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}