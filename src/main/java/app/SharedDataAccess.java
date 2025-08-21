package app;

import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryGateway;
import data_access.TodaysEventDataAccessObject;
import data_access.TodaysWellnessLogDataAccessObject;
import data_access.FileGoalRepository;
import entity.Alex.DailyEventLog.DailyEventLogFactory;
import entity.Alex.DailyWellnessLog.DailyWellnessLogFactory;
import entity.Sophia.GoalFactory;
import java.io.File;

/**
 * Singleton class to share data access objects across all page builders.
 * This ensures that all pages work with the same data and updates are reflected everywhere.
 */
public class SharedDataAccess {
    private static SharedDataAccess instance;
    
    private final InMemoryTaskGateway taskGateway;
    private final InMemoryCategoryGateway categoryGateway;
    private final TodaysEventDataAccessObject eventDataAccess;
    private final TodaysWellnessLogDataAccessObject wellnessDataAccess;
    private final FileGoalRepository goalRepository;
    
    private SharedDataAccess() {
        // Initialize shared data access objects
        this.taskGateway = new InMemoryTaskGateway();
        this.categoryGateway = new InMemoryCategoryGateway();
        
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
    public InMemoryTaskGateway getTaskGateway() {
        return taskGateway;
    }
    
    /**
     * Gets the shared category gateway.
     * @return The category gateway
     */
    public InMemoryCategoryGateway getCategoryGateway() {
        return categoryGateway;
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
}