package use_case.Angela.category.delete;

import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import entity.Category;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import use_case.Angela.category.delete.DeleteCategoryCategoryDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryTaskDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryEventDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryInteractor;
import use_case.Angela.category.delete.DeleteCategoryInputData;
import use_case.Angela.category.delete.DeleteCategoryOutputBoundary;
import use_case.Angela.category.delete.DeleteCategoryOutputData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DeleteCategoryInteractor with segregated interfaces following SOLID principles.
 * Tests the refactored implementation with separate interfaces for category, task, and event operations.
 */
class DeleteCategoryWithSegregatedInterfacesTest {

    private TestCategoryDataAccess categoryDataAccess;
    private TestTaskDataAccess taskDataAccess;
    private TestEventDataAccess eventDataAccess;
    private TestDeleteCategoryPresenter testPresenter;
    private DeleteCategoryInteractor interactor;

    @BeforeEach
    void setUp() {
        categoryDataAccess = new TestCategoryDataAccess();
        taskDataAccess = new TestTaskDataAccess();
        eventDataAccess = new TestEventDataAccess();
        testPresenter = new TestDeleteCategoryPresenter();
        
        interactor = new DeleteCategoryInteractor(
            categoryDataAccess,
            taskDataAccess,
            eventDataAccess,
            testPresenter
        );
    }

    @Test
    void testSuccessfulDeleteWithTasksAndEvents() {
        // Setup: Create categories
        String categoryId = "cat-1";
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.addCategory(category);
        categoryDataAccess.addCategory(new Category("cat-2", "Personal", "#00FF00"));
        categoryDataAccess.addCategory(new Category("cat-3", "Urgent", "#FF0000"));
        categoryDataAccess.addCategory(new Category("cat-4", "Extra", "#FFFF00"));

        // Add tasks with this category
        Info taskInfo1 = new Info.Builder("Task 1").category(categoryId).build();
        TaskAvailable task1 = new TaskAvailable(taskInfo1);
        taskDataAccess.addAvailableTask(task1);

        Info taskInfo2 = new Info.Builder("Task 2").category(categoryId).build();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        Task todayTask = new Task("task-template-1", taskInfo2, dates, false);
        taskDataAccess.addTodayTask(todayTask);

        // Add events with this category
        Info eventInfo = new Info.Builder("Event 1").category(categoryId).build();
        eventDataAccess.addAvailableEvent(eventInfo);

        // Execute delete
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        // The actual format is: "Updated X available tasks, Y today's tasks, Z available events, and W today's events"
        // Note: The interactor now successfully updates the today's task
        assertTrue(testPresenter.lastOutputData.getMessage().contains("1 available tasks"));
        assertTrue(testPresenter.lastOutputData.getMessage().contains("1 today's tasks"));
        assertTrue(testPresenter.lastOutputData.getMessage().contains("1 available events"));
        assertTrue(testPresenter.lastOutputData.getMessage().contains("0 today's events"));

        // Verify category was deleted
        assertFalse(categoryDataAccess.categories.containsKey(categoryId));

        // Verify tasks were updated
        assertTrue(taskDataAccess.updatedAvailableTasks.contains(task1.getId()));
        assertTrue(taskDataAccess.updatedTodayTasks.contains(todayTask.getId()));

        // Verify events were cleared
        assertTrue(eventDataAccess.clearedAvailableEvents.contains(eventInfo.getId()));
    }

    @Test
    void testDeleteWithOnlyOneCategory() {
        // Setup: Create only 1 category (no minimum requirement)
        categoryDataAccess.addCategory(new Category("cat-1", "Work", "#0000FF"));

        // Delete the only category (should succeed with new business rule)
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("cat-1");
        interactor.execute(inputData);

        // Verify success - users can have 0 categories
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        assertTrue(testPresenter.lastOutputData.getMessage().contains("Category deleted successfully"));
        
        // Verify category was deleted
        assertEquals(0, categoryDataAccess.categories.size());
    }

    @Test
    void testFailureCategoryNotFound() {
        // Setup: Create some categories
        categoryDataAccess.addCategory(new Category("cat-1", "Work", "#0000FF"));
        categoryDataAccess.addCategory(new Category("cat-2", "Personal", "#00FF00"));
        categoryDataAccess.addCategory(new Category("cat-3", "Urgent", "#FF0000"));

        // Try to delete non-existent category
        DeleteCategoryInputData inputData = new DeleteCategoryInputData("non-existent");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category not found", testPresenter.lastError);
    }

    @Test
    void testDeleteWithNoTasksOrEvents() {
        // Setup: Create categories
        String categoryId = "cat-1";
        categoryDataAccess.addCategory(new Category(categoryId, "Work", "#0000FF"));
        categoryDataAccess.addCategory(new Category("cat-2", "Personal", "#00FF00"));
        categoryDataAccess.addCategory(new Category("cat-3", "Urgent", "#FF0000"));
        categoryDataAccess.addCategory(new Category("cat-4", "Extra", "#FFFF00"));

        // Execute delete (no tasks or events to update)
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertNull(testPresenter.lastError);
        // The actual format doesn't include category name
        assertEquals("Category deleted successfully. Updated 0 available tasks and 0 today's tasks to have empty category.", 
                    testPresenter.lastOutputData.getMessage());

        // Verify category was deleted
        assertFalse(categoryDataAccess.categories.containsKey(categoryId));
    }

    @Test
    void testInterfaceSegregation() {
        // This test verifies that each interface is called independently
        // and that the interactor properly coordinates between them
        
        String categoryId = "cat-1";
        categoryDataAccess.addCategory(new Category(categoryId, "Work", "#0000FF"));
        // Only add one category since there's no minimum requirement

        // Track method calls
        categoryDataAccess.resetCallTracking();
        taskDataAccess.resetCallTracking();
        eventDataAccess.resetCallTracking();

        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        interactor.execute(inputData);

        // Verify each interface was called appropriately
        assertTrue(categoryDataAccess.getCategoryByIdCalled, "Category interface should check if category exists");
        // getCategoryCountCalled is no longer needed since we removed the minimum check
        assertTrue(categoryDataAccess.deleteCategoryCalled, "Category interface should delete category");
        
        assertTrue(taskDataAccess.findAvailableTasksCalled, "Task interface should find available tasks");
        assertTrue(taskDataAccess.findTodaysTasksCalled, "Task interface should find today's tasks");
        
        assertTrue(eventDataAccess.findAvailableEventsCalled, "Event interface should find available events");
        assertTrue(eventDataAccess.findTodaysEventsCalled, "Event interface should find today's events");
    }

    /**
     * Test implementation of DeleteCategoryCategoryDataAccessInterface
     */
    private static class TestCategoryDataAccess implements DeleteCategoryCategoryDataAccessInterface {
        Map<String, Category> categories = new HashMap<>();
        boolean getCategoryByIdCalled = false;
        boolean getCategoryCountCalled = false;
        boolean existsCalled = false;
        boolean deleteCategoryCalled = false;

        void addCategory(Category category) {
            categories.put(category.getId(), category);
        }

        void resetCallTracking() {
            getCategoryByIdCalled = false;
            getCategoryCountCalled = false;
            existsCalled = false;
            deleteCategoryCalled = false;
        }

        @Override
        public Category getCategoryById(String categoryId) {
            getCategoryByIdCalled = true;
            return categories.get(categoryId);
        }

        @Override
        public boolean exists(Category category) {
            existsCalled = true;
            return categories.containsKey(category.getId());
        }

        @Override
        public boolean deleteCategory(Category category) {
            deleteCategoryCalled = true;
            return categories.remove(category.getId()) != null;
        }

        @Override
        public int getCategoryCount() {
            getCategoryCountCalled = true;
            return categories.size();
        }
    }

    /**
     * Test implementation of DeleteCategoryTaskDataAccessInterface
     */
    private static class TestTaskDataAccess implements DeleteCategoryTaskDataAccessInterface {
        List<TaskAvailable> availableTasks = new ArrayList<>();
        List<Task> todayTasks = new ArrayList<>();
        Set<String> updatedAvailableTasks = new HashSet<>();
        Set<String> updatedTodayTasks = new HashSet<>();
        boolean findAvailableTasksCalled = false;
        boolean findTodaysTasksCalled = false;

        void addAvailableTask(TaskAvailable task) {
            availableTasks.add(task);
        }

        void addTodayTask(Task task) {
            todayTasks.add(task);
        }

        void resetCallTracking() {
            findAvailableTasksCalled = false;
            findTodaysTasksCalled = false;
        }

        @Override
        public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
            findAvailableTasksCalled = true;
            return availableTasks.stream()
                .filter(t -> categoryId.equals(t.getInfo().getCategory()))
                .toList();
        }

        @Override
        public List<Task> findTodaysTasksByCategory(String categoryId) {
            findTodaysTasksCalled = true;
            return todayTasks.stream()
                .filter(t -> categoryId.equals(t.getInfo().getCategory()))
                .toList();
        }

        @Override
        public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
            updatedAvailableTasks.add(taskId);
            // Find and update the task
            for (TaskAvailable task : availableTasks) {
                if (task.getId().equals(taskId)) {
                    task.getInfo().setCategory(newCategoryId);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
            updatedTodayTasks.add(taskId);
            // Find and update the task
            for (Task task : todayTasks) {
                if (task.getId().equals(taskId)) {
                    task.getInfo().setCategory(newCategoryId);
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public List<TaskAvailable> findAvailableTasksWithEmptyCategory() {
            return availableTasks.stream()
                .filter(t -> t.getInfo().getCategory() == null || t.getInfo().getCategory().isEmpty())
                .toList();
        }
        
        @Override
        public List<Task> findTodaysTasksWithEmptyCategory() {
            return todayTasks.stream()
                .filter(t -> t.getInfo().getCategory() == null || t.getInfo().getCategory().isEmpty())
                .toList();
        }

        @Override
        public boolean updateTasksCategoryToNull(String categoryId) {
            // Update all available tasks
            for (TaskAvailable task : availableTasks) {
                if (categoryId.equals(task.getInfo().getCategory())) {
                    task.getInfo().setCategory("");
                    updatedAvailableTasks.add(task.getId());
                }
            }
            
            // Update all today's tasks
            for (Task task : todayTasks) {
                if (categoryId.equals(task.getInfo().getCategory())) {
                    task.getInfo().setCategory("");
                    updatedTodayTasks.add(task.getId());
                }
            }
            
            return true;
        }
    }

    /**
     * Test implementation of DeleteCategoryEventDataAccessInterface
     */
    private static class TestEventDataAccess implements DeleteCategoryEventDataAccessInterface {
        List<Info> availableEvents = new ArrayList<>();
        List<Info> todayEvents = new ArrayList<>();
        Set<String> clearedAvailableEvents = new HashSet<>();
        Set<String> clearedTodayEvents = new HashSet<>();
        boolean findAvailableEventsCalled = false;
        boolean findTodaysEventsCalled = false;

        void addAvailableEvent(Info event) {
            availableEvents.add(event);
        }

        void addTodayEvent(Info event) {
            todayEvents.add(event);
        }

        void resetCallTracking() {
            findAvailableEventsCalled = false;
            findTodaysEventsCalled = false;
        }

        @Override
        public List<Info> findAvailableEventsByCategory(String categoryId) {
            findAvailableEventsCalled = true;
            return availableEvents.stream()
                .filter(e -> categoryId.equals(e.getCategory()))
                .toList();
        }

        @Override
        public List<Info> findTodaysEventsByCategory(String categoryId) {
            findTodaysEventsCalled = true;
            return todayEvents.stream()
                .filter(e -> categoryId.equals(e.getCategory()))
                .toList();
        }

        @Override
        public boolean clearAvailableEventCategory(String eventId) {
            clearedAvailableEvents.add(eventId);
            for (Info event : availableEvents) {
                if (event.getId().equals(eventId)) {
                    event.setCategory("");
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean clearTodaysEventCategory(String eventId) {
            clearedTodayEvents.add(eventId);
            for (Info event : todayEvents) {
                if (event.getId().equals(eventId)) {
                    event.setCategory("");
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public List<Info> findAvailableEventsWithEmptyCategory() {
            return availableEvents.stream()
                .filter(e -> e.getCategory() == null || e.getCategory().isEmpty())
                .toList();
        }
        
        @Override
        public List<Info> findTodaysEventsWithEmptyCategory() {
            return todayEvents.stream()
                .filter(e -> e.getCategory() == null || e.getCategory().isEmpty())
                .toList();
        }

        @Override
        public boolean updateEventsCategoryToNull(String categoryId) {
            // Update all available events
            for (Info event : availableEvents) {
                if (categoryId.equals(event.getCategory())) {
                    event.setCategory("");
                    clearedAvailableEvents.add(event.getName()); // Events don't have IDs, use name
                }
            }
            
            // Update all today's events
            for (Info event : todayEvents) {
                if (categoryId.equals(event.getCategory())) {
                    event.setCategory("");
                    clearedTodayEvents.add(event.getName()); // Events don't have IDs, use name
                }
            }
            
            return true;
        }
    }

    /**
     * Test presenter implementation
     */
    private static class TestDeleteCategoryPresenter implements DeleteCategoryOutputBoundary {
        DeleteCategoryOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(DeleteCategoryOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }
    }
}