package data_access;

import entity.Category;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.Task;
import entity.info.Info;
import use_case.Angela.category.CategoryGateway;
import use_case.Angela.category.create.CreateCategoryDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryDataAccessInterface;
import use_case.Angela.category.edit.EditCategoryDataAccessInterface;
import java.util.*;

/**
 * In-memory implementation of CategoryGateway and DataAccessInterfaces for quick demos.
 * Following Alex's pattern: one concrete implementation implements multiple interfaces.
 */
public class InMemoryCategoryGateway implements 
        CategoryGateway,
        CreateCategoryDataAccessInterface,
        DeleteCategoryDataAccessInterface,
        EditCategoryDataAccessInterface {
    private final Map<String, Category> categories = Collections.synchronizedMap(new HashMap<>());
    private int nextId = 4; // Start at 4 since we have 3 default categories
    
    // Task storage references for category deletion bug fix
    private InMemoryTaskGateway taskGateway;

    public InMemoryCategoryGateway() {
        // Add some default categories for demo
        saveCategory(new Category("1", "Work", null));
        saveCategory(new Category("2", "Personal", null));
        saveCategory(new Category("3", "Health", null));
    }

    @Override
    public void saveCategory(Category category) {
        categories.put(category.getId(), category);

        // Update nextId if we're saving a category with a numeric ID
        try {
            int id = Integer.parseInt(category.getId());
            if (id >= nextId) {
                nextId = id + 1;
            }
        } catch (NumberFormatException e) {
            // Ignore non-numeric IDs
        }
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categories.values());
    }

    @Override
    public Category getCategoryById(String id) {
        return categories.get(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categories.values().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateCategory(Category category) {
        if (categories.containsKey(category.getId())) {
            categories.put(category.getId(), category);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCategory(String categoryId) {
        return categories.remove(categoryId) != null;
    }

    @Override
    public boolean categoryNameExists(String name) {
        return categories.values().stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }

    @Override
    public String getNextCategoryId() {
        return String.valueOf(nextId++);
    }

    /**
     * Sets the task gateway for handling task updates during category deletion.
     * This is needed to implement the category deletion bug fix.
     */
    public void setTaskGateway(InMemoryTaskGateway taskGateway) {
        this.taskGateway = taskGateway;
    }

    // ===== CreateCategoryDataAccessInterface methods =====

    @Override
    public void save(Category category) {
        saveCategory(category); // Delegate to existing method
    }

    @Override
    public boolean existsByName(String name) {
        return categoryNameExists(name); // Delegate to existing method
    }

    @Override
    public int getCategoryCount() {
        return categories.size();
    }

    // ===== DeleteCategoryDataAccessInterface methods =====

    @Override
    public boolean exists(Category category) {
        return categories.containsKey(category.getId());
    }

    @Override
    public boolean deleteCategory(Category category) {
        return deleteCategory(category.getId()); // Delegate to existing method
    }

    @Override
    public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
        System.out.println("DEBUG: InMemoryCategoryGateway.findAvailableTasksByCategory() called with categoryId: " + categoryId);
        
        if (taskGateway == null) {
            System.out.println("DEBUG: taskGateway is null!");
            return new ArrayList<>(); // Return empty list if no task gateway
        }
        
        // Get TaskAvailable objects from task gateway and filter by category
        List<TaskAvailable> result = new ArrayList<>();
        List<TaskAvailable> allTemplates = taskGateway.getAllAvailableTaskTemplates();
        System.out.println("DEBUG: Found " + allTemplates.size() + " TaskAvailable templates in total");
        
        // CRITICAL: Also check legacy Info storage since tasks are being saved there
        List<Info> allInfos = taskGateway.getAllAvailableTasks();
        System.out.println("DEBUG: Found " + allInfos.size() + " Info objects in legacy storage");
        
        // First check TaskAvailable templates
        for (TaskAvailable taskAvailable : allTemplates) {
            if (taskAvailable != null && taskAvailable.getInfo() != null) {
                String taskCategory = taskAvailable.getInfo().getCategory();
                System.out.println("DEBUG: Checking TaskAvailable '" + taskAvailable.getInfo().getName() + 
                    "' with category: " + taskCategory);
                if (taskCategory != null && taskCategory.equals(categoryId)) {
                    result.add(taskAvailable);
                    System.out.println("DEBUG: Added TaskAvailable to result");
                }
            }
        }
        
        // Also check legacy Info storage and create temporary TaskAvailable objects
        for (Info info : allInfos) {
            if (info != null) {
                String taskCategory = info.getCategory();
                System.out.println("DEBUG: Checking Info '" + info.getName() + 
                    "' with category: " + taskCategory);
                if (taskCategory != null && taskCategory.equals(categoryId)) {
                    // Check if we already have this task in results
                    boolean alreadyInResult = result.stream()
                        .anyMatch(ta -> ta.getId().equals(info.getId()));
                    
                    if (!alreadyInResult) {
                        // Create temporary TaskAvailable for legacy Info
                        TaskAvailable tempTaskAvailable = new TaskAvailable(info);
                        result.add(tempTaskAvailable);
                        System.out.println("DEBUG: Added Info as TaskAvailable to result");
                    }
                }
            }
        }
        
        System.out.println("DEBUG: Returning " + result.size() + " tasks with category");
        return result;
    }

    @Override
    public List<Task> findTodaysTasksByCategory(String categoryId) {
        if (taskGateway == null) {
            return new ArrayList<>(); // Return empty list if no task gateway
        }
        
        return taskGateway.getTodaysTasks().stream()
                .filter(task -> {
                    String taskCategory = task.getInfo().getCategory();
                    return taskCategory != null && taskCategory.equals(categoryId);
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
        System.out.println("DEBUG: updateAvailableTaskCategory called with taskId: " + taskId + ", newCategoryId: '" + newCategoryId + "'");
        
        if (taskGateway == null || taskId == null) {
            System.out.println("DEBUG: taskGateway is null or taskId is null");
            return false;
        }
        
        // Since tasks are stored as Info objects in legacy storage, update the Info directly
        List<Info> allTasks = taskGateway.getAllAvailableTasks();
        for (Info info : allTasks) {
            if (info != null && info.getId().equals(taskId)) {
                System.out.println("DEBUG: Found Info to update: " + info.getName() + " (current category: " + info.getCategory() + ")");
                
                // Set category to null if newCategoryId is empty, otherwise use the new category ID
                String categoryToSet = (newCategoryId == null || newCategoryId.trim().isEmpty()) ? null : newCategoryId;
                info.setCategory(categoryToSet);
                
                System.out.println("DEBUG: Updated Info category to: " + categoryToSet);
                
                // Update the task in storage
                boolean updated = taskGateway.updateAvailableTask(info);
                System.out.println("DEBUG: Update result: " + updated);
                return updated;
            }
        }
        
        System.out.println("DEBUG: Task not found with ID: " + taskId);
        return false;
    }

    @Override
    public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
        if (taskGateway == null || taskId == null) {
            return false;
        }
        
        // Update category in today's tasks
        List<Task> todaysTasks = taskGateway.getTodaysTasks();
        for (Task task : todaysTasks) {
            if (task != null && task.getInfo() != null && task.getInfo().getId().equals(taskId)) {
                // Set category to null if newCategoryId is null or empty, otherwise use the new category
                String categoryToSet = (newCategoryId == null || newCategoryId.trim().isEmpty()) ? null : newCategoryId;
                task.getInfo().setCategory(categoryToSet);
                return taskGateway.updateTodaysTask(task);
            }
        }
        return false;
    }

    // ===== EditCategoryDataAccessInterface methods =====

    @Override
    public boolean existsByNameExcluding(String name, String excludeCategoryId) {
        return categories.values().stream()
                .anyMatch(c -> !c.getId().equals(excludeCategoryId) && 
                              c.getName().equalsIgnoreCase(name));
    }
}