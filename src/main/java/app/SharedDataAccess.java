package app;

import data_access.InMemoryTaskDataAccessObject;
import data_access.InMemoryCategoryDataAccessObject;
import data_access.alex.TodaysEventDataAccessObject;
import data_access.alex.TodaysWellnessLogDataAccessObject;
import data_access.files.FileGoalRepository;
import entity.alex.DailyEventLog.DailyEventLogFactory;
import entity.alex.DailyWellnessLog.DailyWellnessLogFactory;
import entity.Sophia.GoalFactory;
import java.io.File;

/**
 * Singleton class to share data access objects across all page builders.
 * This ensures that all pages work with the same data and updates are reflected everywhere.
 */
public class SharedDataAccess {
    private static SharedDataAccess instance;
    
    private final InMemoryTaskDataAccessObject taskGateway;
    private final InMemoryCategoryDataAccessObject categoryDataAccess;
    private final TodaysEventDataAccessObject eventDataAccess;
    private final TodaysWellnessLogDataAccessObject wellnessDataAccess;
    private final FileGoalRepository goalRepository;
    
    private SharedDataAccess() {
        // Initialize shared data access objects
        this.taskGateway = new InMemoryTaskDataAccessObject();
        this.categoryDataAccess = new InMemoryCategoryDataAccessObject();
        
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
        
        // Inject goal repository into task gateway for goal-task relationship checking
        this.taskGateway.setGoalRepository(this.goalRepository);
    }
    
    /**
     * Gets the singleton instance of SharedDataAccess.
     * @return The shared instance
     */
    public static synchronized SharedDataAccess getInstance() {
        if (instance == null) {
            instance = new SharedDataAccess();
        }
        return instance;
    }
    
    /**
     * Gets the shared task gateway.
     * @return The task gateway
     */
    public InMemoryTaskDataAccessObject getTaskGateway() {
        return taskGateway;
    }
    
    /**
     * Gets the shared category data access.
     * @return The category data access
     */
    public InMemoryCategoryDataAccessObject getCategoryDataAccess() {
        return categoryDataAccess;
    }
    
    /**
     * Gets the shared event data access.
     * @return The event data access
     */
    public TodaysEventDataAccessObject getEventDataAccess() {
        return eventDataAccess;
    }
    
    /**
     * Gets the shared wellness data access.
     * @return The wellness data access
     */
    public TodaysWellnessLogDataAccessObject getWellnessDataAccess() {
        return wellnessDataAccess;
    }
    
    /**
     * Gets the shared goal repository.
     * @return The goal repository
     */
    public FileGoalRepository getGoalRepository() {
        return goalRepository;
    }
    
    /**
     * Resets the singleton instance for testing purposes.
     * This clears all data and creates fresh data access objects.
     * WARNING: Only use this in tests!
     */
    public static synchronized void resetForTesting() {
        instance = null;
    }
    
    /**
     * Clears all data from the shared data access objects.
     * This is useful for cleaning up between tests.
     */
    public void clearAllData() {
        // Clear task data
        if (taskGateway != null) {
            taskGateway.clearAllData();
        }
        
        // Clear category data  
        if (categoryDataAccess != null) {
            categoryDataAccess.clearAllData();
        }
        
        // Clear event data
        if (eventDataAccess != null) {
            eventDataAccess.clearAllData();
        }
        
        // Clear wellness data
        if (wellnessDataAccess != null) {
            // wellnessDataAccess.clearAllData(); // TODO: Add clear method to TodaysWellnessLogDataAccessObject
        }
        
        // Note: We don't clear file-based goal repository as it persists to disk
    }
}