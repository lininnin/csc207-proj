package interface_adapter.Angela.view_history;

import use_case.Angela.view_history.ViewHistoryOutputBoundary;
import use_case.Angela.view_history.ViewHistoryOutputData;

import java.time.LocalDate;
import java.util.List;

/**
 * Presenter for the view history use case.
 * Converts use case output to view model state.
 */
public class ViewHistoryPresenter implements ViewHistoryOutputBoundary {
    private final ViewHistoryViewModel viewModel;
    
    public ViewHistoryPresenter(ViewHistoryViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void prepareSuccessView(ViewHistoryOutputData outputData) {
        ViewHistoryState newState = new ViewHistoryState.Builder(viewModel.getState())
            .selectedDate(outputData.getDate())
            .hasData(true)
            .errorMessage(null)
            .todaysTasks(outputData.getTodaysTasks())
            .completedTasks(outputData.getCompletedTasks())
            .taskCompletionRate(outputData.getTaskCompletionRate())
            .overdueTasks(outputData.getOverdueTasks())
            .todaysEvents(outputData.getTodaysEvents())
            .goalProgress(outputData.getGoalProgress())
            .wellnessEntries(outputData.getWellnessEntries())
            .build();
        
        viewModel.setState(newState);
    }
    
    @Override
    public void prepareFailView(String error) {
        ViewHistoryState newState = new ViewHistoryState.Builder(viewModel.getState())
            .hasData(false)
            .errorMessage(error)
            .build();
        
        viewModel.setState(newState);
    }
    
    @Override
    public void presentAvailableDates(List<LocalDate> availableDates) {
        ViewHistoryState newState = new ViewHistoryState.Builder(viewModel.getState())
            .availableDates(availableDates)
            .build();
        
        viewModel.setState(newState);
    }
    
    @Override
    public void presentExportSuccess(String filePath) {
        ViewHistoryState newState = new ViewHistoryState.Builder(viewModel.getState())
            .exportMessage("History exported successfully to: " + filePath)
            .build();
        
        viewModel.setState(newState);
    }
    
    @Override
    public void presentExportFailure(String error) {
        ViewHistoryState newState = new ViewHistoryState.Builder(viewModel.getState())
            .exportMessage("Export failed: " + error)
            .build();
        
        viewModel.setState(newState);
    }
}