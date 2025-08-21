package app;

import data_access.InMemoryTaskDataAccessObject;
import data_access.InMemoryCategoryDataAccessObject;
import data_access.alex.TodaysEventDataAccessObject;
import data_access.alex.TodaysWellnessLogDataAccessObject;
import data_access.InMemoryTodaySoFarDataAccess;
import data_access.files.FileGoalRepository;
import entity.alex.DailyEventLog.DailyEventLogFactory;
import entity.alex.DailyWellnessLog.DailyWellnessLogFactory;
import entity.Sophia.GoalFactory;
import interface_adapter.Angela.category.CategoryManagementViewModel;
import java.io.File;

/**
 * Singleton factory for creating data access objects and shared view models.
 * This ensures all pages use the same data access instances.
 */
public class AppDataAccessFactory {
    private static AppDataAccessFactory instance;
    
    private final InMemoryTaskDataAccessObject taskGateway;
    private final InMemoryCategoryDataAccessObject categoryDataAccess;
    private final TodaysEventDataAccessObject eventDataAccess;
    private final TodaysWellnessLogDataAccessObject wellnessDataAccess;
    private final FileGoalRepository goalRepository;
    
    // Shared ViewModels that need to be consistent across pages
    private final CategoryManagementViewModel categoryManagementViewModel;
    
    /**
     * Private constructor - use getInstance() instead.
     */
    private AppDataAccessFactory() {
        // Initialize data access objects
        this.taskGateway = new InMemoryTaskDataAccessObject();
        this.categoryDataAccess = new InMemoryCategoryDataAccessObject();
        
        // Initialize shared ViewModels
        this.categoryManagementViewModel = new CategoryManagementViewModel();
        
        // Initialize event data access
        this.eventDataAccess = new TodaysEventDataAccessObject(
            new DailyEventLogFactory()
        );
        
        // Initialize wellness data access
        this.wellnessDataAccess = new TodaysWellnessLogDataAccessObject(
            new DailyWellnessLogFactory()
        );
        
        // Initialize goal repository
        this.goalRepository = new FileGoalRepository(
            new File("goals.txt"),
            new File("current_goals.txt"),
            new File("today_goal.txt"),
            new GoalFactory()
        );
        
        // Inject goal repository into task data access object for goal-task relationship checking
        this.taskGateway.setGoalRepository(this.goalRepository);
    }
    
    /**
     * Gets the singleton instance.
     * @return The shared instance
     */
    public static synchronized AppDataAccessFactory getInstance() {
        if (instance == null) {
            instance = new AppDataAccessFactory();
        }
        return instance;
    }
    
    /**
     * Creates a configured TodaySoFar data access object.
     * @return A new InMemoryTodaySoFarDataAccess instance
     */
    public InMemoryTodaySoFarDataAccess createTodaySoFarDataAccess() {
        return new InMemoryTodaySoFarDataAccess(
            taskGateway,
            eventDataAccess,
            wellnessDataAccess,
            goalRepository
        );
    }
    
    /**
     * Gets the task gateway.
     * @return The task gateway
     */
    public InMemoryTaskDataAccessObject getTaskGateway() {
        return taskGateway;
    }
    
    /**
     * Gets the category data access object.
     * @return The category data access object
     */
    public InMemoryCategoryDataAccessObject getCategoryDataAccess() {
        return categoryDataAccess;
    }
    
    /**
     * Gets the event data access object.
     * @return The event data access object
     */
    public TodaysEventDataAccessObject getEventDataAccess() {
        return eventDataAccess;
    }
    
    /**
     * Gets the wellness data access object.
     * @return The wellness data access object
     */
    public TodaysWellnessLogDataAccessObject getWellnessDataAccess() {
        return wellnessDataAccess;
    }
    
    /**
     * Gets the goal repository.
     * @return The goal repository
     */
    public FileGoalRepository getGoalRepository() {
        return goalRepository;
    }
    
    /**
     * Gets the shared category management view model.
     * @return The category management view model
     */
    public CategoryManagementViewModel getCategoryManagementViewModel() {
        return categoryManagementViewModel;
    }
}