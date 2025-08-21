package app;

import interface_adapter.Angela.task.overdue.OverdueTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.task.overdue.OverdueTasksPresenter;
import interface_adapter.Angela.today_so_far.TodaySoFarViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.Angela.today_so_far.TodaySoFarPresenter;
import use_case.Angela.task.overdue.OverdueTasksInputBoundary;
import use_case.Angela.task.overdue.OverdueTasksInteractor;
import use_case.Angela.task.overdue.OverdueTasksOutputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInteractor;
import data_access.InMemoryTodaySoFarDataAccess;
import view.Angela.TodaySoFarView;

/**
 * Singleton class to share Today So Far components across all pages.
 * This ensures that all pages show the same Today So Far state.
 */
public class SharedTodaySoFarComponents {
    private static SharedTodaySoFarComponents instance;
    
    // Shared ViewModels
    private final OverdueTasksViewModel overdueTasksViewModel;
    private final TodaySoFarViewModel todaySoFarViewModel;
    
    // Shared Controllers
    private final OverdueTasksController overdueTasksController;
    private final TodaySoFarController todaySoFarController;
    
    private SharedTodaySoFarComponents() {
        // Get shared data access factory
        AppDataAccessFactory dataAccessFactory = AppDataAccessFactory.getInstance();
        
        // Create shared ViewModels
        this.overdueTasksViewModel = new OverdueTasksViewModel();
        this.todaySoFarViewModel = new TodaySoFarViewModel();
        
        // Wire up Overdue Tasks Use Case
        OverdueTasksOutputBoundary overdueTasksPresenter = new OverdueTasksPresenter(overdueTasksViewModel);
        OverdueTasksInputBoundary overdueTasksInteractor = new OverdueTasksInteractor(
                dataAccessFactory.getTaskGateway(), 
                dataAccessFactory.getCategoryDataAccess(), 
                overdueTasksPresenter);
        this.overdueTasksController = new OverdueTasksController(overdueTasksInteractor);
        
        // Wire up Today So Far Use Case using the same data access instances as the pages
        InMemoryTodaySoFarDataAccess todaySoFarDataAccess = dataAccessFactory.createTodaySoFarDataAccess();
        
        TodaySoFarPresenter todaySoFarPresenter = new TodaySoFarPresenter(todaySoFarViewModel);
        TodaySoFarInputBoundary todaySoFarInteractor = new TodaySoFarInteractor(
                todaySoFarDataAccess, 
                todaySoFarPresenter, 
                dataAccessFactory.getCategoryDataAccess());
        this.todaySoFarController = new TodaySoFarController(todaySoFarInteractor);
    }
    
    /**
     * Gets the singleton instance.
     * @return The shared instance
     */
    public static synchronized SharedTodaySoFarComponents getInstance() {
        if (instance == null) {
            instance = new SharedTodaySoFarComponents();
        }
        return instance;
    }
    
    /**
     * Creates a new Today So Far view using the shared ViewModels and Controllers.
     * Each page gets its own View but shares the underlying ViewModels and Controllers.
     * @return A new TodaySoFarView connected to shared components
     */
    public TodaySoFarView createTodaySoFarView() {
        TodaySoFarView view = new TodaySoFarView(overdueTasksViewModel, todaySoFarViewModel);
        view.setOverdueTasksController(overdueTasksController);
        view.setTodaySoFarController(todaySoFarController);
        return view;
    }
    
    /**
     * Gets the shared overdue tasks controller.
     * @return The overdue tasks controller
     */
    public OverdueTasksController getOverdueTasksController() {
        return overdueTasksController;
    }
    
    /**
     * Gets the shared today so far controller.
     * @return The today so far controller  
     */
    public TodaySoFarController getTodaySoFarController() {
        return todaySoFarController;
    }
    
    /**
     * Refreshes the Today So Far panel data.
     */
    public void refresh() {
        todaySoFarController.refresh();
        overdueTasksController.execute(7);
    }
    
    /**
     * Resets the singleton instance for testing purposes.
     * WARNING: Only use this in tests!
     */
    public static synchronized void resetForTesting() {
        instance = null;
    }
}