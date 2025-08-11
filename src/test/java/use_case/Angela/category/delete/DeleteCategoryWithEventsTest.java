package use_case.Angela.category.delete;

import entity.Category;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for DeleteCategoryInteractor with event support.
 * Tests that both tasks and events have their categories cleared when a category is deleted.
 */
class DeleteCategoryWithEventsTest {
    
    private DeleteCategoryInteractor interactor;
    private TestDataAccess dataAccess;
    private TestOutputBoundary outputBoundary;
    
    @BeforeEach
    void setUp() {
        dataAccess = new TestDataAccess();
        outputBoundary = new TestOutputBoundary();
        interactor = new DeleteCategoryInteractor(dataAccess, outputBoundary);
        
        // Set up initial categories (Category constructor requires id, name, color)
        dataAccess.addCategory(new Category("1", "Work", "#FF0000"));
        dataAccess.addCategory(new Category("2", "Personal", "#00FF00"));
        dataAccess.addCategory(new Category("3", "Health", "#0000FF"));
        dataAccess.addCategory(new Category("4", "Shopping", "#FFFF00"));
    }
    
    @Test
    void testDeleteCategoryWithEventsAndTasks() {
        // Add tasks and events with category "1"
        // Info uses builder pattern
        Info taskInfo1 = new Info.Builder("Task 1")
                .category("1")
                .build();
        dataAccess.addAvailableTask(new TaskAvailable(taskInfo1));
        
        Info taskInfo2 = new Info.Builder("Task 2")
                .category("1")
                .build();
        // Task constructor needs templateTaskId, info, dates, isOneTime
        Task todayTask = new Task("template2", taskInfo2, 
                new entity.BeginAndDueDates.BeginAndDueDates(java.time.LocalDate.now(), java.time.LocalDate.now()),
                false);
        dataAccess.addTodayTask(todayTask);
        
        // Add events with category "1"
        Info eventInfo1 = new Info.Builder("Event 1")
                .category("1")
                .build();
        dataAccess.addAvailableEvent(eventInfo1);
        
        Info eventInfo2 = new Info.Builder("Event 2")
                .category("1")
                .build();
        dataAccess.addTodayEvent(eventInfo2);
        
        // Execute delete
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("1");
        interactor.execute(inputData);
        
        // Verify success
        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failureCalled);
        
        // Verify category is deleted
        assertNull(dataAccess.getCategoryById("1"));
        
        // Verify all tasks and events have empty categories
        assertTrue(dataAccess.taskCategoriesCleared);
        assertTrue(dataAccess.eventCategoriesCleared);
        // We added 1 available task and 1 today task with category "1"
        assertTrue(dataAccess.clearedAvailableTasks.size() >= 1);
        assertTrue(dataAccess.clearedTodayTasks.size() >= 1);
        // We added 2 events but they're tracked differently
        assertTrue(dataAccess.clearedAvailableEvents.size() >= 1);
        assertTrue(dataAccess.clearedTodayEvents.size() >= 1);
    }
    
    @Test
    void testDeleteCategoryMinimumCheck() {
        // Remove one category to have exactly 3
        dataAccess.removeCategory("4");
        
        // Try to delete another category
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("1");
        interactor.execute(inputData);
        
        // Verify failure due to minimum requirement
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failureCalled);
        assertEquals("Cannot delete category: minimum 3 categories required", outputBoundary.errorMessage);
        
        // Verify category still exists
        assertNotNull(dataAccess.getCategoryById("1"));
    }
    
    @Test
    void testDeleteNonExistentCategory() {
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("999");
        interactor.execute(inputData);
        
        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failureCalled);
        assertEquals("Category not found", outputBoundary.errorMessage);
    }
    
    /**
     * Test data access implementation that supports both tasks and events.
     */
    private static class TestDataAccess implements DeleteCategoryDataAccessInterface, DeleteCategoryEventDataAccessInterface {
        private final Map<String, Category> categories = new HashMap<>();
        private final List<TaskAvailable> availableTasks = new ArrayList<>();
        private final List<Task> todayTasks = new ArrayList<>();
        private final List<Info> availableEvents = new ArrayList<>();
        private final List<Info> todayEvents = new ArrayList<>();
        
        // Track what was cleared
        boolean taskCategoriesCleared = false;
        boolean eventCategoriesCleared = false;
        List<String> clearedAvailableTasks = new ArrayList<>();
        List<String> clearedTodayTasks = new ArrayList<>();
        List<String> clearedAvailableEvents = new ArrayList<>();
        List<String> clearedTodayEvents = new ArrayList<>();
        
        void addCategory(Category category) {
            categories.put(category.getId(), category);
        }
        
        void removeCategory(String id) {
            categories.remove(id);
        }
        
        void addAvailableTask(TaskAvailable task) {
            availableTasks.add(task);
        }
        
        void addTodayTask(Task task) {
            todayTasks.add(task);
        }
        
        void addAvailableEvent(Info event) {
            availableEvents.add(event);
        }
        
        void addTodayEvent(Info event) {
            todayEvents.add(event);
        }
        
        @Override
        public Category getCategoryById(String categoryId) {
            return categories.get(categoryId);
        }
        
        @Override
        public int getCategoryCount() {
            return categories.size();
        }
        
        @Override
        public boolean exists(Category category) {
            return categories.containsKey(category.getId());
        }
        
        @Override
        public boolean deleteCategory(Category category) {
            return categories.remove(category.getId()) != null;
        }
        
        @Override
        public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
            taskCategoriesCleared = true;
            return availableTasks.stream()
                    .filter(t -> categoryId.equals(t.getInfo().getCategory()))
                    .toList();
        }
        
        @Override
        public List<Task> findTodaysTasksByCategory(String categoryId) {
            return todayTasks.stream()
                    .filter(t -> categoryId.equals(t.getInfo().getCategory()))
                    .toList();
        }
        
        @Override
        public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
            // Track that we attempted to clear a task
            if (taskId != null) {
                clearedAvailableTasks.add(taskId);
            }
            return true;
        }
        
        @Override
        public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
            // Track that we attempted to clear a task
            if (taskId != null) {
                clearedTodayTasks.add(taskId);
            }
            return true;
        }
        
        @Override
        public List<Info> findAvailableEventsByCategory(String categoryId) {
            eventCategoriesCleared = true;
            return availableEvents.stream()
                    .filter(e -> categoryId.equals(e.getCategory()))
                    .toList();
        }
        
        @Override
        public List<Info> findTodaysEventsByCategory(String categoryId) {
            return todayEvents.stream()
                    .filter(e -> categoryId.equals(e.getCategory()))
                    .toList();
        }
        
        @Override
        public boolean clearAvailableEventCategory(String eventId) {
            if (eventId != null) {
                clearedAvailableEvents.add(eventId);
            }
            return true;
        }
        
        @Override
        public boolean clearTodaysEventCategory(String eventId) {
            if (eventId != null) {
                clearedTodayEvents.add(eventId);
            }
            return true;
        }
    }
    
    /**
     * Test output boundary to capture results.
     */
    private static class TestOutputBoundary implements DeleteCategoryOutputBoundary {
        boolean successCalled = false;
        boolean failureCalled = false;
        String errorMessage = null;
        DeleteCategoryOutputData outputData = null;
        
        @Override
        public void prepareSuccessView(DeleteCategoryOutputData outputData) {
            this.successCalled = true;
            this.outputData = outputData;
        }
        
        @Override
        public void prepareFailView(String error) {
            this.failureCalled = true;
            this.errorMessage = error;
        }
    }
}