package app;

import data_access.*;
import data_access.alex.TodaysEventDataAccessObject;
import data_access.alex.TodaysWellnessLogDataAccessObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SharedDataAccess singleton.
 * Tests the singleton pattern and shared data access components.
 */
class SharedDataAccessTest {
    
    @BeforeEach
    @AfterEach
    void resetSingleton() throws Exception {
        // Reset the singleton instance between tests using reflection
        Field instanceField = SharedDataAccess.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }
    
    @Test
    void testSingletonInstance() {
        // Get instance twice
        SharedDataAccess instance1 = SharedDataAccess.getInstance();
        SharedDataAccess instance2 = SharedDataAccess.getInstance();
        
        // Verify same instance
        assertSame(instance1, instance2, "Should return the same singleton instance");
    }
    
    @Test
    void testSharedDataAccessObjects() {
        SharedDataAccess sharedData = SharedDataAccess.getInstance();
        
        // Verify all DAOs are initialized
        assertNotNull(sharedData.getTaskGateway(), "Task gateway should be initialized");
        assertNotNull(sharedData.getCategoryGateway(), "Category gateway should be initialized");
        assertNotNull(sharedData.getEventDataAccess(), "Event data access should be initialized");
        assertNotNull(sharedData.getWellnessDataAccess(), "Wellness data access should be initialized");
        assertNotNull(sharedData.getGoalRepository(), "Goal repository should be initialized");
    }
    
    @Test
    void testSameInstancesAcrossMultipleGets() {
        SharedDataAccess sharedData = SharedDataAccess.getInstance();
        
        // Get DAOs multiple times
        InMemoryTaskGateway taskGateway1 = sharedData.getTaskGateway();
        InMemoryTaskGateway taskGateway2 = sharedData.getTaskGateway();
        
        InMemoryCategoryGateway categoryGateway1 = sharedData.getCategoryGateway();
        InMemoryCategoryGateway categoryGateway2 = sharedData.getCategoryGateway();
        
        TodaysEventDataAccessObject eventDAO1 = sharedData.getEventDataAccess();
        TodaysEventDataAccessObject eventDAO2 = sharedData.getEventDataAccess();
        
        TodaysWellnessLogDataAccessObject wellnessDAO1 = sharedData.getWellnessDataAccess();
        TodaysWellnessLogDataAccessObject wellnessDAO2 = sharedData.getWellnessDataAccess();
        
        FileGoalRepository goalRepo1 = sharedData.getGoalRepository();
        FileGoalRepository goalRepo2 = sharedData.getGoalRepository();
        
        // Verify same instances are returned
        assertSame(taskGateway1, taskGateway2, "Should return same task gateway instance");
        assertSame(categoryGateway1, categoryGateway2, "Should return same category gateway instance");
        assertSame(eventDAO1, eventDAO2, "Should return same event DAO instance");
        assertSame(wellnessDAO1, wellnessDAO2, "Should return same wellness DAO instance");
        assertSame(goalRepo1, goalRepo2, "Should return same goal repository instance");
    }
    
    @Test
    void testThreadSafety() throws InterruptedException {
        // Test that singleton is thread-safe
        final SharedDataAccess[] instances = new SharedDataAccess[2];
        
        Thread thread1 = new Thread(() -> {
            instances[0] = SharedDataAccess.getInstance();
        });
        
        Thread thread2 = new Thread(() -> {
            instances[1] = SharedDataAccess.getInstance();
        });
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        assertNotNull(instances[0], "First thread should get an instance");
        assertNotNull(instances[1], "Second thread should get an instance");
        assertSame(instances[0], instances[1], "Both threads should get the same instance");
    }
}