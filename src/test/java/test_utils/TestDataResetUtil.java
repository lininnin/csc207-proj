package test_utils;

/**
 * Utility class for resetting shared data access objects between tests.
 * This ensures test isolation by clearing singleton state.
 */
public class TestDataResetUtil {
    
    /**
     * Resets all shared data access objects for test isolation.
     * This should be called before or after each test to ensure clean state.
     */
    public static void resetAllSharedData() {
        // Reset the SharedDataAccess singleton
        app.SharedDataAccess.resetForTesting();
        
        // Reset other singletons
        app.SharedTodaySoFarComponents.resetForTesting();
        
        // Reset CategoryManager singleton
        entity.Angela.CategoryManager.resetInstance();
        
        // Reset DailySnapshotService singleton
        app.Angela.DailySnapshotService.resetForTesting();
    }
    
    /**
     * Clears data from existing shared instances without resetting singletons.
     * Use this if you want to preserve instance configuration but clear data.
     */
    public static void clearSharedData() {
        try {
            app.SharedDataAccess sharedData = app.SharedDataAccess.getInstance();
            sharedData.clearAllData();
        } catch (Exception e) {
            // Ignore if singleton not initialized
        }
    }
}