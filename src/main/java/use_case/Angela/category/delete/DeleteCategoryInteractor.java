package use_case.Angela.category.delete;

import entity.Category;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import entity.info.Info;
import java.util.List;

/**
 * Interactor for the delete category use case.
 * Implements the business logic for deleting a category and updating all affected tasks.
 * Now follows ISP by using separate interfaces for category and task operations.
 */
public class DeleteCategoryInteractor implements DeleteCategoryInputBoundary {
    private final DeleteCategoryCategoryDataAccessInterface categoryDataAccess;
    private final DeleteCategoryTaskDataAccessInterface taskDataAccess;
    private final DeleteCategoryEventDataAccessInterface eventDataAccess;
    private final DeleteCategoryOutputBoundary outputBoundary;

    /**
     * Constructor with segregated interfaces following ISP.
     * @param categoryDataAccess Interface for category operations
     * @param taskDataAccess Interface for task update operations
     * @param eventDataAccess Interface for event update operations (can be null)
     * @param outputBoundary Output boundary for the use case
     */
    public DeleteCategoryInteractor(DeleteCategoryCategoryDataAccessInterface categoryDataAccess,
                                    DeleteCategoryTaskDataAccessInterface taskDataAccess,
                                    DeleteCategoryEventDataAccessInterface eventDataAccess,
                                    DeleteCategoryOutputBoundary outputBoundary) {
        this.categoryDataAccess = categoryDataAccess;
        this.taskDataAccess = taskDataAccess;
        this.eventDataAccess = eventDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteCategoryInputData inputData) {
        String categoryId = inputData.getCategoryId();
        System.out.println("DEBUG: DeleteCategoryInteractor.execute() called with categoryId: " + categoryId);

        // Check if category exists
        Category category = categoryDataAccess.getCategoryById(categoryId);
        System.out.println("DEBUG: Found category: " + (category != null ? category.getName() : "null"));
        
        if (category == null) {
            outputBoundary.prepareFailView("Category not found");
            return;
        }
        
        // Removed minimum category requirement - users can delete all categories if they want

        // CRITICAL: Find and update all tasks that have this category BEFORE deleting the category
        // Update available tasks (templates)
        System.out.println("DEBUG: Finding available tasks with category: " + category.getName());
        List<TaskAvailable> availableTasks = taskDataAccess.findAvailableTasksByCategory(categoryId);
        System.out.println("DEBUG: Found " + availableTasks.size() + " available tasks to update");
        
        int updatedAvailableCount = 0;
        for (TaskAvailable task : availableTasks) {
            System.out.println("DEBUG: Updating task '" + task.getInfo().getName() + "' to remove category");
            if (taskDataAccess.updateAvailableTaskCategory(task.getId(), "")) {
                updatedAvailableCount++;
                System.out.println("DEBUG: Successfully updated task: " + task.getId());
            } else {
                System.out.println("DEBUG: Failed to update task: " + task.getId());
            }
        }

        // Update today's tasks (instances)
        System.out.println("DEBUG: Finding today's tasks with category: " + category.getName());
        List<Task> todaysTasks = taskDataAccess.findTodaysTasksByCategory(categoryId);
        System.out.println("DEBUG: Found " + todaysTasks.size() + " today's tasks to update");
        
        int updatedTodaysCount = 0;
        for (Task task : todaysTasks) {
            System.out.println("DEBUG: Updating today's task '" + task.getInfo().getName() + "' to remove category");
            if (taskDataAccess.updateTodaysTaskCategory(task.getId(), "")) {
                updatedTodaysCount++;
                System.out.println("DEBUG: Successfully updated today's task: " + task.getId());
            } else {
                System.out.println("DEBUG: Failed to update today's task: " + task.getId());
            }
        }
        
        // CRITICAL: Also handle events if we have event data access
        int updatedAvailableEventsCount = 0;
        int updatedTodaysEventsCount = 0;
        
        // Handle events if event data access is provided (follows OCP - no instanceof check)
        if (eventDataAccess != null) {
            // Update available events
            System.out.println("DEBUG: Finding available events with category: " + category.getName());
            List<Info> availableEvents = eventDataAccess.findAvailableEventsByCategory(categoryId);
            System.out.println("DEBUG: Found " + availableEvents.size() + " available events to update");
            
            for (Info event : availableEvents) {
                System.out.println("DEBUG: Clearing category for available event: " + event.getName());
                if (eventDataAccess.clearAvailableEventCategory(event.getId())) {
                    updatedAvailableEventsCount++;
                }
            }
            
            // Update today's events
            System.out.println("DEBUG: Finding today's events with category: " + category.getName());
            List<Info> todaysEvents = eventDataAccess.findTodaysEventsByCategory(categoryId);
            System.out.println("DEBUG: Found " + todaysEvents.size() + " today's events to update");
            
            for (Info event : todaysEvents) {
                System.out.println("DEBUG: Clearing category for today's event: " + event.getName());
                if (eventDataAccess.clearTodaysEventCategory(event.getId())) {
                    updatedTodaysEventsCount++;
                }
            }
        }

        // Now delete the category
        System.out.println("DEBUG: Deleting category: " + category.getName());
        boolean deleted = categoryDataAccess.deleteCategory(category);
        System.out.println("DEBUG: Category deletion result: " + deleted);

        if (deleted) {
            String message;
            if (updatedAvailableEventsCount > 0 || updatedTodaysEventsCount > 0) {
                message = String.format(
                    "Category deleted successfully. Updated %d available tasks, %d today's tasks, %d available events, and %d today's events to have empty category.",
                    updatedAvailableCount, updatedTodaysCount, updatedAvailableEventsCount, updatedTodaysEventsCount
                );
            } else {
                message = String.format(
                    "Category deleted successfully. Updated %d available tasks and %d today's tasks to have empty category.",
                    updatedAvailableCount, updatedTodaysCount
                );
            }
            System.out.println("DEBUG: " + message);
            DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(categoryId, message);
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to delete category");
        }
    }
}