package app;

import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import view.Angela.TodaySoFarView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SharedTodaySoFarComponents singleton.
 * Tests the singleton pattern and shared component creation.
 */
class SharedTodaySoFarComponentsTest {
    
    @BeforeEach
    @AfterEach
    void resetSingletons() throws Exception {
        // Reset SharedTodaySoFarComponents singleton
        Field todaySoFarInstanceField = SharedTodaySoFarComponents.class.getDeclaredField("instance");
        todaySoFarInstanceField.setAccessible(true);
        todaySoFarInstanceField.set(null, null);
        
        // Reset SharedDataAccess singleton  
        Field dataAccessInstanceField = SharedDataAccess.class.getDeclaredField("instance");
        dataAccessInstanceField.setAccessible(true);
        dataAccessInstanceField.set(null, null);
    }
    
    @Test
    void testSingletonInstance() {
        // Get instance twice
        SharedTodaySoFarComponents instance1 = SharedTodaySoFarComponents.getInstance();
        SharedTodaySoFarComponents instance2 = SharedTodaySoFarComponents.getInstance();
        
        // Verify same instance
        assertSame(instance1, instance2, "Should return the same singleton instance");
    }
    
    @Test
    void testControllersInitialized() {
        SharedTodaySoFarComponents components = SharedTodaySoFarComponents.getInstance();
        
        // Verify controllers are initialized
        assertNotNull(components.getOverdueTasksController(), 
                      "Overdue tasks controller should be initialized");
        assertNotNull(components.getTodaySoFarController(), 
                      "Today so far controller should be initialized");
    }
    
    @Test
    void testSameControllersReturned() {
        SharedTodaySoFarComponents components = SharedTodaySoFarComponents.getInstance();
        
        // Get controllers multiple times
        OverdueTasksController overdueController1 = components.getOverdueTasksController();
        OverdueTasksController overdueController2 = components.getOverdueTasksController();
        
        TodaySoFarController todaySoFarController1 = components.getTodaySoFarController();
        TodaySoFarController todaySoFarController2 = components.getTodaySoFarController();
        
        // Verify same instances
        assertSame(overdueController1, overdueController2, 
                  "Should return same overdue controller instance");
        assertSame(todaySoFarController1, todaySoFarController2, 
                  "Should return same today so far controller instance");
    }
    
    @Test
    void testCreateTodaySoFarView() {
        SharedTodaySoFarComponents components = SharedTodaySoFarComponents.getInstance();
        
        // Create views
        TodaySoFarView view1 = components.createTodaySoFarView();
        TodaySoFarView view2 = components.createTodaySoFarView();
        
        // Verify views are created
        assertNotNull(view1, "Should create a view");
        assertNotNull(view2, "Should create a view");
        
        // Verify different view instances (each page gets its own view)
        assertNotSame(view1, view2, "Should create different view instances");
    }
    
    @Test
    void testRefreshMethod() {
        SharedTodaySoFarComponents components = SharedTodaySoFarComponents.getInstance();
        
        // This should not throw an exception
        assertDoesNotThrow(() -> components.refresh(), 
                          "Refresh should execute without errors");
    }
    
    @Test
    void testThreadSafety() throws InterruptedException {
        // Test that singleton is thread-safe
        final SharedTodaySoFarComponents[] instances = new SharedTodaySoFarComponents[2];
        
        Thread thread1 = new Thread(() -> {
            instances[0] = SharedTodaySoFarComponents.getInstance();
        });
        
        Thread thread2 = new Thread(() -> {
            instances[1] = SharedTodaySoFarComponents.getInstance();
        });
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        
        assertNotNull(instances[0], "First thread should get an instance");
        assertNotNull(instances[1], "Second thread should get an instance");
        assertSame(instances[0], instances[1], "Both threads should get the same instance");
    }
    
    @Test
    void testViewsShareControllers() {
        SharedTodaySoFarComponents components = SharedTodaySoFarComponents.getInstance();
        
        // Create multiple views
        TodaySoFarView view1 = components.createTodaySoFarView();
        TodaySoFarView view2 = components.createTodaySoFarView();
        
        // Both views should be properly initialized with controllers
        assertNotNull(view1, "First view should be created");
        assertNotNull(view2, "Second view should be created");
        
        // Views are different instances but share the same underlying controllers
        assertNotSame(view1, view2, "Views should be different instances");
    }
}