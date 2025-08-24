package use_case.Angela.category.edit;

import entity.Category;
import entity.CategoryFactory;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import java.util.List;

/**
 * Interactor for the edit category use case.
 * Implements the business logic for editing a category and updating all affected tasks.
 */
public class EditCategoryInteractor implements EditCategoryInputBoundary {
    private final EditCategoryDataAccessInterface categoryDataAccess;
    private final EditCategoryTaskDataAccessInterface taskDataAccess;
    private final EditCategoryOutputBoundary outputBoundary;
    private final CategoryFactory categoryFactory;

    public EditCategoryInteractor(EditCategoryDataAccessInterface categoryDataAccess,
                                  EditCategoryTaskDataAccessInterface taskDataAccess,
                                  EditCategoryOutputBoundary outputBoundary,
                                  CategoryFactory categoryFactory) {
        this.categoryDataAccess = categoryDataAccess;
        this.taskDataAccess = taskDataAccess;
        this.outputBoundary = outputBoundary;
        this.categoryFactory = categoryFactory;
    }

    @Override
    public void execute(EditCategoryInputData inputData) {
        String categoryId = inputData.getCategoryId();
        String newName = inputData.getNewCategoryName();
        
        // Validate category ID first
        if (categoryId == null || categoryId.trim().isEmpty()) {
            outputBoundary.prepareFailView("Category ID is required");
            return;
        }

        // Validate input
        if (newName == null || newName.trim().isEmpty()) {
            outputBoundary.prepareFailView("Category name cannot be empty");
            return;
        }

        if (newName.length() > 20) {
            outputBoundary.prepareFailView("The name of category cannot exceed 20 letters");
            return;
        }

        // Check if category exists
        Category category = categoryDataAccess.getCategoryById(categoryId);
        if (category == null) {
            outputBoundary.prepareFailView("Category not found");
            return;
        }

        String oldName = category.getName();

        // Check if new name already exists (unless it's the same as current name)
        if (!oldName.equalsIgnoreCase(newName) && 
            categoryDataAccess.existsByNameExcluding(newName, categoryId)) {
            outputBoundary.prepareFailView("The category name already exists");
            return;
        }

        // Create new immutable category with updated name - DON'T trim the name
        Category updatedCategory = categoryFactory.create(categoryId, newName, category.getColor());
        boolean updated = categoryDataAccess.updateCategory(updatedCategory);

        if (updated) {
            // CRITICAL: Update all tasks that use this category
            // Update available tasks
            List<TaskAvailable> availableTasks = taskDataAccess.findAvailableTasksByCategory(categoryId);
            
            int updatedAvailableCount = 0;
            for (TaskAvailable task : availableTasks) {
                // Note: We're keeping the same category ID, just the name changed in the Category object
                // So we don't need to update the task's category field
                updatedAvailableCount++;
            }
            
            // Update today's tasks
            List<Task> todaysTasks = taskDataAccess.findTodaysTasksByCategory(categoryId);
            
            int updatedTodaysCount = 0;
            for (Task task : todaysTasks) {
                // Note: We're keeping the same category ID, just the name changed in the Category object
                // So we don't need to update the task's category field
                updatedTodaysCount++;
            }
            
            EditCategoryOutputData outputData = new EditCategoryOutputData(
                    categoryId, oldName, newName
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to update category");
        }
    }
}