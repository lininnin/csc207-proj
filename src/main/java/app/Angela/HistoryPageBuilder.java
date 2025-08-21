package app.Angela;

import data_access.InMemoryHistoryDataAccess;
import interface_adapter.Angela.view_history.ViewHistoryController;
import interface_adapter.Angela.view_history.ViewHistoryPresenter;
import interface_adapter.Angela.view_history.ViewHistoryViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarViewModel;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.sophia.today_goal.TodayGoalsViewModel;
import use_case.Angela.view_history.ViewHistoryDataAccessInterface;
import use_case.Angela.view_history.ViewHistoryInteractor;
import use_case.Angela.view_history.ViewHistoryInputBoundary;
import use_case.Angela.view_history.ViewHistoryOutputBoundary;
import view.Angela.HistoryView;

import javax.swing.*;

/**
 * Builder class for creating the History page with all its dependencies.
 * Follows Clean Architecture by wiring all layers together.
 * Creates separate view models for the reusable components.
 */
public class HistoryPageBuilder {
    private static ViewHistoryDataAccessInterface historyDataAccess;
    private static ViewHistoryViewModel viewModel;
    
    // View models for the reusable components
    private static TodaySoFarViewModel todaySoFarViewModel;
    private static TodayTasksViewModel todayTasksViewModel;
    private static TodaysEventsViewModel todaysEventsViewModel;
    private static TodayGoalsViewModel todayGoalsViewModel;
    
    /**
     * Creates and returns a fully wired History view panel.
     * @return The History view panel
     */
    public static JPanel createHistoryView() {
        // Initialize data access (singleton pattern for shared data)
        if (historyDataAccess == null) {
            historyDataAccess = new InMemoryHistoryDataAccess();
        }
        
        // Create view models
        if (viewModel == null) {
            viewModel = new ViewHistoryViewModel();
        }
        
        // Create view models for reusable components (separate instances for history)
        if (todaySoFarViewModel == null) {
            todaySoFarViewModel = new TodaySoFarViewModel();
        }
        if (todayTasksViewModel == null) {
            todayTasksViewModel = new TodayTasksViewModel();
        }
        if (todaysEventsViewModel == null) {
            todaysEventsViewModel = new TodaysEventsViewModel();
        }
        if (todayGoalsViewModel == null) {
            todayGoalsViewModel = new TodayGoalsViewModel();
        }
        
        // Create presenter
        ViewHistoryOutputBoundary presenter = new ViewHistoryPresenter(viewModel);
        
        // Create interactor
        ViewHistoryInputBoundary interactor = new ViewHistoryInteractor(historyDataAccess, presenter);
        
        // Create controller
        ViewHistoryController controller = new ViewHistoryController(interactor);
        
        // Create and return view with all view models
        return new HistoryView(
            controller, 
            viewModel,
            todaySoFarViewModel,
            todayTasksViewModel,
            todaysEventsViewModel,
            todayGoalsViewModel
        );
    }
    
    /**
     * Gets the shared history data access instance.
     * Used by other components to save snapshots.
     * @return The history data access instance
     */
    public static ViewHistoryDataAccessInterface getHistoryDataAccess() {
        if (historyDataAccess == null) {
            historyDataAccess = new InMemoryHistoryDataAccess();
        }
        return historyDataAccess;
    }
    
    /**
     * Resets the history data (useful for testing).
     */
    public static void resetHistory() {
        if (historyDataAccess instanceof InMemoryHistoryDataAccess) {
            ((InMemoryHistoryDataAccess) historyDataAccess).clear();
        }
        // Reset view models
        todaySoFarViewModel = null;
        todayTasksViewModel = null;
        todaysEventsViewModel = null;
        todayGoalsViewModel = null;
    }
}