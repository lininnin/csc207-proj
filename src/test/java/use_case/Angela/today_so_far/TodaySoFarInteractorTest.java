package use_case.Angela.today_so_far;

import entity.Angela.Task.Task;
import entity.Alex.Event.Event;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Sophia.Goal;
import entity.Category;
import use_case.Angela.category.CategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TodaySoFarInteractor use case.
 */
class TodaySoFarInteractorTest {
    
    private TestTodaySoFarDataAccess dataAccess;
    private TestTodaySoFarPresenter presenter;
    private TestCategoryGateway categoryGateway;
    private TodaySoFarInteractor interactor;
    
    @BeforeEach
    void setUp() {
        dataAccess = new TestTodaySoFarDataAccess();
        presenter = new TestTodaySoFarPresenter();
        categoryGateway = new TestCategoryGateway();
        interactor = new TodaySoFarInteractor(dataAccess, presenter, categoryGateway);
    }
    
    @Test
    void testRefreshWithCompleteData() {
        // Set up test data
        dataAccess.setCompletedTasksCount(3);
        dataAccess.setTotalTasks(5);
        dataAccess.setHasGoals(true);
        dataAccess.setHasCompletedItems(true);
        dataAccess.setHasWellnessEntries(true);
        
        // Execute
        interactor.refreshTodaySoFar();
        
        // Verify presenter was called with output data
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
        
        // Verify completion rate calculation
        assertEquals(60, presenter.lastOutputData.getCompletionRate());
        
        // Verify data was collected
        assertFalse(presenter.lastOutputData.getGoals().isEmpty());
        assertFalse(presenter.lastOutputData.getCompletedItems().isEmpty());
        assertFalse(presenter.lastOutputData.getWellnessEntries().isEmpty());
    }
    
    @Test
    void testRefreshWithEmptyData() {
        // All data sources return empty
        dataAccess.setCompletedTasksCount(0);
        dataAccess.setTotalTasks(0);
        dataAccess.setHasGoals(false);
        dataAccess.setHasCompletedItems(false);
        dataAccess.setHasWellnessEntries(false);
        
        // Execute
        interactor.refreshTodaySoFar();
        
        // Verify presenter was called
        assertNotNull(presenter.lastOutputData);
        assertNull(presenter.lastError);
        
        // Verify empty results
        assertTrue(presenter.lastOutputData.getGoals().isEmpty());
        assertTrue(presenter.lastOutputData.getCompletedItems().isEmpty());
        assertTrue(presenter.lastOutputData.getWellnessEntries().isEmpty());
        assertEquals(0, presenter.lastOutputData.getCompletionRate());
    }
    
    @Test
    void testRefreshWithPartialData() {
        // Only tasks completed, no goals or wellness
        dataAccess.setCompletedTasksCount(2);
        dataAccess.setTotalTasks(4);
        dataAccess.setHasGoals(false);
        dataAccess.setHasCompletedItems(true);
        dataAccess.setHasWellnessEntries(false);
        
        // Execute
        interactor.refreshTodaySoFar();
        
        // Verify mixed results
        assertNotNull(presenter.lastOutputData);
        assertEquals(50, presenter.lastOutputData.getCompletionRate());
        assertTrue(presenter.lastOutputData.getGoals().isEmpty());
        assertFalse(presenter.lastOutputData.getCompletedItems().isEmpty());
        assertTrue(presenter.lastOutputData.getWellnessEntries().isEmpty());
    }
    
    @Test
    void testCompletionRateCalculation() {
        // Test various completion scenarios
        testCompletionRate(0, 0, 0);    // No tasks
        testCompletionRate(0, 5, 0);    // No completed
        testCompletionRate(5, 5, 100);  // All completed
        testCompletionRate(1, 3, 33);   // Partial
        testCompletionRate(2, 3, 67);   // Partial
    }
    
    private void testCompletionRate(int completed, int total, int expectedRate) {
        dataAccess.setCompletedTasksCount(completed);
        dataAccess.setTotalTasks(total);
        interactor.refreshTodaySoFar();
        assertEquals(expectedRate, presenter.lastOutputData.getCompletionRate());
    }
    
    @Test
    void testErrorHandling() {
        // Make data access throw exception
        dataAccess.setShouldThrowException(true);
        
        // Execute
        interactor.refreshTodaySoFar();
        
        // Verify error was presented
        assertNull(presenter.lastOutputData);
        assertNotNull(presenter.lastError);
        assertTrue(presenter.lastError.contains("Failed to load"));
    }
    
    /**
     * Test implementation of data access.
     */
    private static class TestTodaySoFarDataAccess implements TodaySoFarDataAccessInterface {
        private int completedTasksCount = 0;
        private int totalTasks = 0;
        private boolean hasGoals = false;
        private boolean hasCompletedItems = false;
        private boolean hasWellnessEntries = false;
        private boolean shouldThrowException = false;
        
        void setCompletedTasksCount(int count) { this.completedTasksCount = count; }
        void setTotalTasks(int count) { this.totalTasks = count; }
        void setHasGoals(boolean has) { this.hasGoals = has; }
        void setHasCompletedItems(boolean has) { this.hasCompletedItems = has; }
        void setHasWellnessEntries(boolean has) { this.hasWellnessEntries = has; }
        void setShouldThrowException(boolean should) { this.shouldThrowException = should; }
        
        @Override
        public List<Task> getCompletedTasksForToday() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            return hasCompletedItems ? Arrays.asList((Task) null) : new ArrayList<>();
        }
        
        @Override
        public List<Event> getCompletedEventsForToday() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            return new ArrayList<>();
        }
        
        @Override
        public List<WellnessLogEntry> getWellnessEntriesForToday() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            return hasWellnessEntries ? Arrays.asList((WellnessLogEntry) null) : new ArrayList<>();
        }
        
        @Override
        public List<Goal> getActiveGoals() {
            if (shouldThrowException) throw new RuntimeException("Test exception");
            return hasGoals ? Arrays.asList((Goal) null) : new ArrayList<>();
        }
        
        @Override
        public int getTodayTaskCompletionRate() {
            if (totalTasks == 0) return 0;
            return (int) Math.round((completedTasksCount * 100.0) / totalTasks);
        }
        
        @Override
        public int getTotalTasksForToday() {
            return totalTasks;
        }
        
        @Override
        public int getCompletedTasksCountForToday() {
            return completedTasksCount;
        }
    }
    
    /**
     * Test implementation of presenter.
     */
    private static class TestTodaySoFarPresenter implements TodaySoFarOutputBoundary {
        TodaySoFarOutputData lastOutputData;
        String lastError;
        
        @Override
        public void presentTodaySoFar(TodaySoFarOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }
        
        @Override
        public void presentError(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }
    }
    
    /**
     * Test implementation of category gateway.
     */
    private static class TestCategoryGateway implements CategoryGateway {
        @Override
        public String saveCategory(Category category) { return "test-id"; }
        
        @Override
        public Category getCategoryById(String id) {
            if (id == null || id.isEmpty()) return null;
            return new Category("Test Category");
        }
        
        @Override
        public List<Category> getAllCategories() { return new ArrayList<>(); }
        
        @Override
        public void deleteCategory(String id) {}
        
        @Override
        public void updateCategory(String id, Category category) {}
        
        @Override
        public boolean categoryNameExists(String name) { return false; }
        
        @Override
        public void setTaskGateway(Object gateway) {}
    }
}