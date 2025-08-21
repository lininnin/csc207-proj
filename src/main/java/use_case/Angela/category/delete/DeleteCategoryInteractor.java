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
        
        // Check if deleting this category will create duplicate tasks with the same name and empty category
        // First, get all tasks that currently have this category
        List<TaskAvailable> tasksWithThisCategory = taskDataAccess.findAvailableTasksByCategory(categoryId);
        List<Task> todaysTasksWithThisCategory = taskDataAccess.findTodaysTasksByCategory(categoryId);
        
        // Then check if there are already tasks with empty categories that have the same names
        List<TaskAvailable> existingEmptyAvailableTasks = taskDataAccess.findAvailableTasksWithEmptyCategory();
        List<Task> existingEmptyTodaysTasks = taskDataAccess.findTodaysTasksWithEmptyCategory();
        
        // Check for name conflicts in available tasks
        int conflictingAvailableTasks = 0;
        for (TaskAvailable taskWithCategory : tasksWithThisCategory) {
            String taskName = taskWithCategory.getInfo().getName();
            for (TaskAvailable emptyTask : existingEmptyAvailableTasks) {
                if (taskName != null && taskName.equalsIgnoreCase(emptyTask.getInfo().getName())) {
                    conflictingAvailableTasks++;
                    break; // Found one conflict for this task name, no need to check more
                }
            }
        }
        
        // Check for name conflicts in today's tasks
        int conflictingTodaysTasks = 0;
        for (Task taskWithCategory : todaysTasksWithThisCategory) {
            String taskName = taskWithCategory.getInfo().getName();
            for (Task emptyTask : existingEmptyTodaysTasks) {
                if (taskName != null && taskName.equalsIgnoreCase(emptyTask.getInfo().getName())) {
                    conflictingTodaysTasks++;
                    break; // Found one conflict for this task name, no need to check more
                }
            }
        }
        
        // Check events if available
        int conflictingAvailableEvents = 0;
        int conflictingTodaysEvents = 0;
        
        if (eventDataAccess != null) {
            List<Info> eventsWithThisCategory = eventDataAccess.findAvailableEventsByCategory(categoryId);
            List<Info> todaysEventsWithThisCategory = eventDataAccess.findTodaysEventsByCategory(categoryId);
            List<Info> existingEmptyAvailableEvents = eventDataAccess.findAvailableEventsWithEmptyCategory();
            List<Info> existingEmptyTodaysEvents = eventDataAccess.findTodaysEventsWithEmptyCategory();
            
            // Check for name conflicts in available events
            for (Info eventWithCategory : eventsWithThisCategory) {
                String eventName = eventWithCategory.getName();
                for (Info emptyEvent : existingEmptyAvailableEvents) {
                    if (eventName != null && eventName.equalsIgnoreCase(emptyEvent.getName())) {
                        conflictingAvailableEvents++;
                        break;
                    }
                }
            }
            
            // Check for name conflicts in today's events
            for (Info eventWithCategory : todaysEventsWithThisCategory) {
                String eventName = eventWithCategory.getName();
                for (Info emptyEvent : existingEmptyTodaysEvents) {
                    if (eventName != null && eventName.equalsIgnoreCase(emptyEvent.getName())) {
                        conflictingTodaysEvents++;
                        break;
                    }
                }
            }
        }
        
        int totalConflictingTasks = conflictingAvailableTasks + conflictingTodaysTasks;
        int totalConflictingEvents = conflictingAvailableEvents + conflictingTodaysEvents;
        int totalConflicts = totalConflictingTasks + totalConflictingEvents;
        
        if (totalConflicts > 0) {
            String warningMessage;
            if (totalConflictingEvents > 0) {
                warningMessage = String.format(
                    "Warning: Deleting this category will create %d duplicate task(s) and %d duplicate event(s) " +
                    "with the same names that already exist with empty categories. Continue?",
                    totalConflictingTasks, totalConflictingEvents
                );
            } else {
                warningMessage = String.format(
                    "Warning: Deleting this category will create %d duplicate task(s) " +
                    "with the same names that already exist with empty categories. Continue?",
                    totalConflictingTasks
                );
            }
            outputBoundary.prepareFailView(warningMessage);
            return;
        }
        

        // Count how many tasks and events will be updated BEFORE updating them
        List<TaskAvailable> availableTasksToUpdate = taskDataAccess.findAvailableTasksByCategory(categoryId);
        List<Task> todaysTasksToUpdate = taskDataAccess.findTodaysTasksByCategory(categoryId);
        
        int availableEventsToUpdate = 0;
        int todaysEventsToUpdate = 0;
        if (eventDataAccess != null) {
            List<entity.info.Info> availableEvents = eventDataAccess.findAvailableEventsByCategory(categoryId);
            List<entity.info.Info> todaysEvents = eventDataAccess.findTodaysEventsByCategory(categoryId);
            availableEventsToUpdate = availableEvents.size();
            todaysEventsToUpdate = todaysEvents.size();
        }
        
        // CRITICAL: Update all tasks that have this category BEFORE deleting the category
        System.out.println("DEBUG: Updating tasks with category: " + category.getName());
        boolean taskUpdateSuccess = taskDataAccess.updateTasksCategoryToNull(categoryId);
        System.out.println("DEBUG: Task update result: " + taskUpdateSuccess);
        
        if (!taskUpdateSuccess) {
            outputBoundary.prepareFailView("Failed to delete category");
            return;
        }
        
        // CRITICAL: Update all events that have this category BEFORE deleting the category
        boolean eventUpdateSuccess = true;
        if (eventDataAccess != null) {
            System.out.println("DEBUG: Updating events with category: " + category.getName());
            eventUpdateSuccess = eventDataAccess.updateEventsCategoryToNull(categoryId);
            System.out.println("DEBUG: Event update result: " + eventUpdateSuccess);
            
            if (!eventUpdateSuccess) {
                outputBoundary.prepareFailView("Failed to delete category");
                return;
            }
        }

        // Now delete the category
        System.out.println("DEBUG: Deleting category: " + category.getName());
        boolean deleted = categoryDataAccess.deleteCategory(category);
        System.out.println("DEBUG: Category deletion result: " + deleted);

        if (deleted) {
            String message;
            if (eventDataAccess != null && (availableEventsToUpdate > 0 || todaysEventsToUpdate > 0)) {
                message = String.format(
                    "Category deleted successfully. Updated %d available tasks, %d today's tasks, %d available events, and %d today's events", 
                    availableTasksToUpdate.size(), todaysTasksToUpdate.size(), 
                    availableEventsToUpdate, todaysEventsToUpdate
                );
            } else {
                message = String.format(
                    "Category deleted successfully. Updated %d available tasks and %d today's tasks to have empty category.", 
                    availableTasksToUpdate.size(), todaysTasksToUpdate.size()
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