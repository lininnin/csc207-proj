package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import entity.info.Info;
import entity.info.InfoInterf;
import entity.Sophia.Goal;
import data_access.GoalRepository;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import use_case.Angela.task.delete.DeleteTaskDataAccessInterface;
import use_case.Angela.task.edit_available.EditAvailableTaskDataAccessInterface;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import use_case.Angela.task.mark_complete.MarkTaskCompleteDataAccessInterface;
import use_case.Angela.task.edit_today.EditTodayTaskDataAccessInterface;
import use_case.Angela.category.edit.EditCategoryTaskDataAccessInterface;
import use_case.Angela.task.remove_from_today.RemoveFromTodayDataAccessInterface;
import use_case.Angela.task.overdue.OverdueTasksDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryTaskDataAccessInterface;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Objects;

/**
 * In-memory implementation of TaskGateway and DataAccessInterfaces for quick demos.
 * Replace with TaskRepository (JSON-based) for production.
 * Following Alex's pattern: one concrete implementation implements multiple interfaces.
 */
public class InMemoryTaskGateway implements 
        CreateTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        EditAvailableTaskDataAccessInterface,
        AddToTodayDataAccessInterface,
        MarkTaskCompleteDataAccessInterface,
        EditTodayTaskDataAccessInterface,
        EditCategoryTaskDataAccessInterface,
        RemoveFromTodayDataAccessInterface,
        OverdueTasksDataAccessInterface,
        DeleteCategoryTaskDataAccessInterface {
    private final Map<String, Info> availableTasks = Collections.synchronizedMap(new HashMap<>()); // Legacy storage for backward compatibility
    private final Map<String, TaskAvailable> availableTaskTemplates = Collections.synchronizedMap(new HashMap<>()); // New storage for TaskAvailable
    private final Map<String, Task> todaysTasks = Collections.synchronizedMap(new HashMap<>());
    private GoalRepository goalRepository; // Optional dependency for goal-task relationship checking

    /**
     * Sets the goal repository for checking goal-task relationships.
     * This is optional and can be null if goal checking is not needed.
     */
    public void setGoalRepository(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public String saveAvailableTask(Info info) {
        String taskId = info.getId(); // Info already has an ID
        System.out.println("DEBUG: saveAvailableTask (legacy) called - ID: " + taskId + ", Name: " + info.getName());
        availableTasks.put(taskId, info);
        System.out.println("DEBUG: After legacy save - availableTasks size: " + availableTasks.size());
        return taskId;
    }

    public List<Info> getAllAvailableTasks() {
        return new ArrayList<>(availableTasks.values());
    }

    public boolean availableTaskNameExists(String name) {
        return availableTasks.values().stream()
                .anyMatch(info -> info.getName().equalsIgnoreCase(name));
    }

    public boolean updateAvailableTask(Info info) {
        if (availableTasks.containsKey(info.getId())) {
            availableTasks.put(info.getId(), info);
            return true;
        }
        return false;
    }

    public boolean deleteFromAvailable(String taskId) {
        return availableTasks.remove(taskId) != null;
    }

    public Task addToToday(String taskId, Task.Priority priority, LocalDate dueDate) {
        TaskAvailableInterf taskAvailable = getTaskAvailableById(taskId);
        if (taskAvailable == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        return (Task) addTaskToToday(taskAvailable, priority, dueDate);
    }

    public List<Task> getTodaysTasks() {
        List<Task> tasks = new ArrayList<>(todaysTasks.values());
        
        // Sort tasks alphabetically by name (case-insensitive)
        tasks.sort((a, b) -> {
            String nameA = a.getInfo().getName();
            String nameB = b.getInfo().getName();
            if (nameA == null && nameB == null) return 0;
            if (nameA == null) return 1;  // null names go to end
            if (nameB == null) return -1;
            return nameA.compareToIgnoreCase(nameB);
        });
        
        return tasks;
    }

    public boolean updateTodaysTask(Task task) {
        if (task == null || task.getId() == null) {
            return false;
        }
        
        // Check if the task exists in today's tasks
        if (!todaysTasks.containsKey(task.getId())) {
            return false;
        }
        
        // Update the task in the map
        todaysTasks.put(task.getId(), task);
        
        System.out.println("DEBUG: Updated today's task - ID: " + task.getId() + 
                          ", Name: " + task.getInfo().getName() +
                          ", Category: " + task.getInfo().getCategory());
        
        return true;
    }

    public boolean removeFromToday(String taskId) {
        return todaysTasks.remove(taskId) != null;
    }

    public boolean markTaskComplete(String taskId) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean unmarkTaskComplete(String taskId) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean existsInAvailable(String taskId) {
        return availableTasks.containsKey(taskId);
    }

    public boolean existsInToday(String taskId) {
        return todaysTasks.containsKey(taskId);
    }

    public String getTaskName(String taskId) {
        InfoInterf info = availableTasks.get(taskId);
        if (info != null) {
            return info.getName();
        }
        Task task = todaysTasks.get(taskId);
        if (task != null) {
            return task.getInfo().getName();
        }
        return null;
    }

    public List<Task> getTasksWithDueDates() {
        // For demo purposes, return empty list
        return new ArrayList<>();
    }

    public List<Task> getOverdueTasks() {
        // For demo purposes, return empty list
        return new ArrayList<>();
    }

    public List<Task> getCompletedTasksToday() {
        return todaysTasks.values().stream()
                .filter(Task::isCompleted)
                .collect(java.util.stream.Collectors.toList());
    }

    public double getTodaysCompletionRate() {
        // Filter out overdue tasks - they shouldn't count in today's completion rate
        List<Task> actualTodayTasks = todaysTasks.values().stream()
                .filter(task -> {
                    // Include task if it's NOT overdue OR if it's completed
                    // (completed overdue tasks still count as today's completions)
                    return !task.isOverdue() || task.isCompleted();
                })
                .collect(java.util.stream.Collectors.toList());
        
        if (actualTodayTasks.isEmpty()) {
            return 0.0;
        }
        
        long completed = actualTodayTasks.stream()
                .filter(Task::isCompleted)
                .count();
        
        return (double) completed / actualTodayTasks.size() * 100;
    }

    // ===== CreateTaskDataAccessInterface methods =====

    @Override
    public String saveTaskAvailable(TaskAvailableInterf taskAvailable) {
        String taskId = taskAvailable.getId();
        System.out.println("DEBUG: saveTaskAvailable called - ID: " + taskId + ", Name: " + taskAvailable.getInfo().getName());
        
        availableTaskTemplates.put(taskId, (TaskAvailable) taskAvailable);
        // Also save to legacy storage for backward compatibility
        availableTasks.put(taskId, (Info) taskAvailable.getInfo());
        
        System.out.println("DEBUG: After save - availableTaskTemplates size: " + availableTaskTemplates.size());
        System.out.println("DEBUG: After save - availableTasks size: " + availableTasks.size());
        
        return taskId;
    }

    @Override
    public boolean taskExistsWithNameAndCategory(String name, String categoryId) {
        if (name == null) {
            return false;
        }
        
        // Normalize category for comparison (null/empty both treated as "no category")
        String normalizedCategory = (categoryId == null || categoryId.trim().isEmpty()) ? "" : categoryId.trim();
        
        // Check in TaskAvailable storage
        for (TaskAvailable taskAvailable : availableTaskTemplates.values()) {
            if (taskAvailable != null && taskAvailable.getInfo() != null) {
                InfoInterf info = taskAvailable.getInfo();
                if (info.getName() != null && info.getName().equalsIgnoreCase(name)) {
                    String taskCategory = (info.getCategory() == null || info.getCategory().trim().isEmpty()) 
                        ? "" : info.getCategory().trim();
                    if (taskCategory.equalsIgnoreCase(normalizedCategory)) {
                        return true;
                    }
                }
            }
        }
        
        // Also check legacy storage for backward compatibility
        for (InfoInterf info : availableTasks.values()) {
            if (info != null && info.getName() != null && info.getName().equalsIgnoreCase(name)) {
                String taskCategory = (info.getCategory() == null || info.getCategory().trim().isEmpty()) 
                    ? "" : info.getCategory().trim();
                if (taskCategory.equalsIgnoreCase(normalizedCategory)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public List<TaskAvailableInterf> getAllAvailableTaskTemplates() {
        List<TaskAvailableInterf> result = new ArrayList<>();
        for (TaskAvailable task : availableTaskTemplates.values()) {
            result.add(task);
        }
        return result;
    }


    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        // First check the new storage
        TaskAvailable taskAvailable = availableTaskTemplates.get(taskId);
        if (taskAvailable != null) {
            return taskAvailable;
        }
        
        // Fallback: Check legacy storage and convert Info to TaskAvailable
        Info info = availableTasks.get(taskId);
        if (info != null) {
            // Create a temporary TaskAvailable from the Info for backward compatibility
            TaskAvailable temp = new TaskAvailable(info);
            return temp;
        }
        
        return null;
    }

    @Override
    public int getAvailableTaskCount() {
        return availableTaskTemplates.size();
    }

    @Override
    public boolean exists(TaskAvailableInterf taskAvailable) {
        return availableTaskTemplates.containsKey(taskAvailable.getId());
    }

    // ===== DeleteTaskDataAccessInterface methods =====

    @Override
    public TaskInterf getTodaysTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }

    @Override
    public boolean existsInAvailable(TaskAvailableInterf taskAvailable) {
        return availableTaskTemplates.containsKey(taskAvailable.getId());
    }

    @Override
    public boolean existsInToday(TaskInterf task) {
        return todaysTasks.containsKey(task.getInfo().getId());
    }

    @Override
    public boolean templateExistsInToday(String templateTaskId) {
        return todaysTasks.values().stream()
                .anyMatch(task -> templateTaskId.equals(task.getTemplateTaskId()));
    }

    @Override
    public boolean deleteFromAvailable(TaskAvailableInterf taskAvailable) {
        boolean removedFromNew = availableTaskTemplates.remove(taskAvailable.getId()) != null;
        boolean removedFromLegacy = availableTasks.remove(taskAvailable.getId()) != null;
        return removedFromNew || removedFromLegacy;
    }

    @Override
    public boolean deleteAllTodaysTasksWithTemplate(String templateTaskId) {
        List<String> tasksToRemove = todaysTasks.entrySet().stream()
                .filter(entry -> templateTaskId.equals(entry.getValue().getTemplateTaskId()))
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
        
        for (String taskId : tasksToRemove) {
            todaysTasks.remove(taskId);
        }
        
        return !tasksToRemove.isEmpty();
    }

    @Override
    public List<TaskInterf> getTodaysTasksByTemplate(String templateTaskId) {
        List<TaskInterf> result = new ArrayList<>();
        for (Task task : todaysTasks.values()) {
            if (templateTaskId.equals(task.getTemplateTaskId())) {
                result.add(task);
            }
        }
        return result;
    }

    @Override
    public List<TaskInterf> getAllTodaysTasks() {
        List<TaskInterf> tasks = new ArrayList<>();
        for (Task task : todaysTasks.values()) {
            tasks.add(task);
        }
        
        // Sort tasks alphabetically by name (case-insensitive)
        tasks.sort((a, b) -> {
            String nameA = a.getInfo().getName();
            String nameB = b.getInfo().getName();
            if (nameA == null && nameB == null) return 0;
            if (nameA == null) return 1;  // null names go to end
            if (nameB == null) return -1;
            return nameA.compareToIgnoreCase(nameB);
        });
        
        return tasks;
    }

    @Override
    public List<String> getGoalNamesTargetingTask(String taskId) {
        List<String> goalNames = new ArrayList<>();
        
        if (goalRepository == null) {
            return goalNames; // Return empty list if no goal repository is available
        }
        
        try {
            // Get the full task info for the given taskId
            TaskAvailable taskTemplate = availableTaskTemplates.get(taskId);
            Info taskInfo = null;
            
            if (taskTemplate != null) {
                taskInfo = (Info) taskTemplate.getInfo();
            } else {
                // Fall back to legacy storage
                taskInfo = availableTasks.get(taskId);
            }
            
            if (taskInfo == null) {
                return goalNames; // Task not found, return empty list
            }
            
            // Check all goals to see if any target this EXACT task
            List<Goal> allGoals = goalRepository.getAllGoals();
            for (Goal goal : allGoals) {
                Info targetTaskInfo = goal.getTargetTaskInfo();
                if (targetTaskInfo != null && isExactSameTask(taskInfo, targetTaskInfo, taskTemplate)) {
                    goalNames.add(goal.getInfo().getName());
                }
            }
        } catch (Exception e) {
            System.err.println("Warning: Error checking goal-task relationships: " + e.getMessage());
            // Return empty list on error to allow operation to continue
        }
        
        return goalNames;
    }
    
    /**
     * Checks if two tasks are exactly the same (name, category, description, one-time info).
     */
    private boolean isExactSameTask(Info taskInfo1, Info targetTaskInfo, TaskAvailable taskTemplate) {
        // Compare name (case-insensitive)
        if (!Objects.equals(taskInfo1.getName(), targetTaskInfo.getName())) {
            return false;
        }
        
        // Compare category (normalize null/empty to empty string)
        String category1 = (taskInfo1.getCategory() == null || taskInfo1.getCategory().trim().isEmpty()) 
            ? "" : taskInfo1.getCategory().trim();
        String category2 = (targetTaskInfo.getCategory() == null || targetTaskInfo.getCategory().trim().isEmpty()) 
            ? "" : targetTaskInfo.getCategory().trim();
        if (!category1.equalsIgnoreCase(category2)) {
            return false;
        }
        
        // Compare description (normalize null to empty string)
        String desc1 = taskInfo1.getDescription() != null ? taskInfo1.getDescription() : "";
        String desc2 = targetTaskInfo.getDescription() != null ? targetTaskInfo.getDescription() : "";
        if (!desc1.equals(desc2)) {
            return false;
        }
        
        // Compare one-time info (only available if we have TaskAvailable)
        // Note: We can't compare one-time info from goal's target task since it's stored as Info
        // For now, we'll skip one-time comparison as it's not available in the goal's target task Info
        
        return true;
    }

    @Override
    public boolean deleteTaskCompletely(String templateTaskId) {
        boolean deletedFromAvailable = false;
        boolean deletedFromToday = false;
        
        // Remove from available task templates
        TaskAvailable template = availableTaskTemplates.remove(templateTaskId);
        if (template != null) {
            deletedFromAvailable = true;
        }
        
        // ALSO check and remove from legacy storage
        InfoInterf legacyInfo = availableTasks.remove(templateTaskId);
        if (legacyInfo != null) {
            deletedFromAvailable = true;
        }
        
        // Remove all today's tasks that reference this template
        List<String> todaysTaskIdsToRemove = new ArrayList<>();
        for (Map.Entry<String, Task> entry : todaysTasks.entrySet()) {
            Task task = entry.getValue();
            if (task != null && templateTaskId.equals(task.getTemplateTaskId())) {
                todaysTaskIdsToRemove.add(entry.getKey());
            }
        }
        
        // Remove the found today's tasks
        for (String taskId : todaysTaskIdsToRemove) {
            todaysTasks.remove(taskId);
            deletedFromToday = true;
        }
        
        return deletedFromAvailable || deletedFromToday;
    }

    // ===== EditAvailableTaskDataAccessInterface methods =====

    @Override
    public boolean updateAvailableTask(String taskId, String newName, String newDescription, 
                                       String newCategoryId, boolean isOneTime) {
        // Get the existing task
        TaskAvailable existingTask = availableTaskTemplates.get(taskId);
        if (existingTask == null) {
            // Try legacy storage
            Info existingInfo = availableTasks.get(taskId);
            if (existingInfo == null) {
                return false;
            }
            // Convert legacy to TaskAvailable
            existingTask = new TaskAvailable(existingInfo);
            availableTaskTemplates.put(taskId, existingTask);
        }

        // Update the existing Info object using setters
        Info info = (Info) existingTask.getInfo();
        info.setName(newName);
        info.setDescription(newDescription != null ? newDescription : "");
        info.setCategory(newCategoryId != null ? newCategoryId : "");

        // Update the isOneTime flag
        existingTask.setOneTime(isOneTime);

        // Update legacy storage for backward compatibility
        availableTasks.put(taskId, info);

        // Propagate ALL changes to Today's tasks
        for (Task todayTask : todaysTasks.values()) {
            if (taskId.equals(todayTask.getTemplateTaskId())) {
                // Update ALL properties in today's task
                InfoInterf todayInfo = todayTask.getInfo();
                todayInfo.setName(newName);
                todayInfo.setDescription(newDescription != null ? newDescription : "");
                todayInfo.setCategory(newCategoryId != null ? newCategoryId : "");
                
                // Also update the one-time flag if it changed
                todayTask.setOneTime(isOneTime);
                
                System.out.println("DEBUG: Updated all properties in Today's task for template: " + taskId);
            }
        }

        return true;
    }

    @Override
    public boolean taskExistsWithNameAndCategoryExcluding(String name, String categoryId, String excludeTaskId) {
        if (name == null) {
            return false;
        }
        
        // Normalize category for comparison
        String normalizedCategory = (categoryId == null || categoryId.trim().isEmpty()) ? "" : categoryId.trim();
        
        // Check in TaskAvailable storage
        for (Map.Entry<String, TaskAvailable> entry : availableTaskTemplates.entrySet()) {
            // Skip the task being edited
            if (entry.getKey().equals(excludeTaskId)) {
                continue;
            }
            
            TaskAvailable taskAvailable = entry.getValue();
            if (taskAvailable != null && taskAvailable.getInfo() != null) {
                InfoInterf info = taskAvailable.getInfo();
                if (info.getName() != null && info.getName().equalsIgnoreCase(name)) {
                    String taskCategory = (info.getCategory() == null || info.getCategory().trim().isEmpty()) 
                        ? "" : info.getCategory().trim();
                    if (taskCategory.equalsIgnoreCase(normalizedCategory)) {
                        return true;
                    }
                }
            }
        }
        
        // Also check legacy storage
        for (Map.Entry<String, Info> entry : availableTasks.entrySet()) {
            // Skip the task being edited
            if (entry.getKey().equals(excludeTaskId)) {
                continue;
            }
            
            InfoInterf info = entry.getValue();
            if (info != null && info.getName() != null && info.getName().equalsIgnoreCase(name)) {
                String taskCategory = (info.getCategory() == null || info.getCategory().trim().isEmpty()) 
                    ? "" : info.getCategory().trim();
                if (taskCategory.equalsIgnoreCase(normalizedCategory)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    public List<TaskAvailableInterf> getAllAvailableTasksWithDetails() {
        List<TaskAvailableInterf> allTasks = new ArrayList<>();
        
        System.out.println("DEBUG: getAllAvailableTasksWithDetails called");
        System.out.println("DEBUG: availableTaskTemplates size: " + availableTaskTemplates.size());
        System.out.println("DEBUG: availableTasks size: " + availableTasks.size());
        
        // Add all tasks from the new storage
        for (TaskAvailable task : availableTaskTemplates.values()) {
            allTasks.add(task);
        }
        
        // Add any tasks that only exist in legacy storage
        for (Map.Entry<String, Info> entry : availableTasks.entrySet()) {
            String taskId = entry.getKey();
            if (!availableTaskTemplates.containsKey(taskId)) {
                // Create TaskAvailable from legacy Info
                TaskAvailable legacyTask = new TaskAvailable(entry.getValue());
                allTasks.add(legacyTask);
                System.out.println("DEBUG: Added legacy task: " + entry.getValue().getName());
            }
        }
        
        // Sort tasks alphabetically by name (case-insensitive)
        allTasks.sort((a, b) -> {
            String nameA = a.getInfo().getName();
            String nameB = b.getInfo().getName();
            if (nameA == null && nameB == null) return 0;
            if (nameA == null) return 1;  // null names go to end
            if (nameB == null) return -1;
            return nameA.compareToIgnoreCase(nameB);
        });
        
        System.out.println("DEBUG: Total tasks returned (sorted alphabetically): " + allTasks.size());
        for (TaskAvailableInterf task : allTasks) {
            System.out.println("DEBUG: Task - ID: " + task.getId() + ", Name: " + task.getInfo().getName());
        }
        
        return allTasks;
    }
    
    /**
     * Public method to get all available task templates for external use (like GoalPageBuilder).
     * @return List of all available task templates
     */
    public List<TaskAvailable> getAvailableTaskTemplates() {
        // For backward compatibility, convert interface list to concrete list
        return getAllAvailableTasksWithDetails().stream()
                .map(task -> (TaskAvailable) task)
                .collect(java.util.stream.Collectors.toList());
    }

    // ===== AddToTodayDataAccessInterface methods =====

    @Override
    public TaskAvailableInterf getAvailableTaskById(String taskId) {
        return getTaskAvailableById(taskId); // Reuse existing method
    }

    @Override
    public TaskInterf addTaskToToday(TaskAvailableInterf taskAvailable, Task.Priority priority, java.time.LocalDate dueDate) {
        // Create a new Task instance from the TaskAvailable template
        Info templateInfo = (Info) taskAvailable.getInfo();
        
        // Create a new Info instance for today's task (clone the template)
        Info todayInfo = new Info.Builder(templateInfo.getName())
                .description(templateInfo.getDescription())
                .category(templateInfo.getCategory())
                .build();
        
        // Create BeginAndDueDates
        // TEMPORARY: For testing overdue functionality, if due date is in the past,
        // set begin date to one day before due date to avoid validation error
        // and ensure the task doesn't appear in today's "to-do" calculation
        LocalDate beginDate = LocalDate.now();
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            beginDate = dueDate.minusDays(1); // Set begin date to one day before due date for testing
        }
        
        entity.BeginAndDueDates.BeginAndDueDates dates = new entity.BeginAndDueDates.BeginAndDueDates(
                beginDate,
                dueDate // Can be null
        );
        
        // Create today's task with reference to template
        Task todayTask = new Task(
                taskAvailable.getId(), // templateTaskId - link back to template
                todayInfo,
                dates,
                taskAvailable.isOneTime()
        );
        
        // Set priority if provided
        if (priority != null) {
            todayTask.setPriority(priority);
        }
        
        // Store in today's tasks map using the Task's ID
        String taskId = todayTask.getId();
        todaysTasks.put(taskId, todayTask);
        
        
        return todayTask;
    }

    @Override
    public boolean isTaskInTodaysList(String templateTaskId) {
        return templateExistsInToday(templateTaskId); // Reuse existing method
    }
    
    @Override
    public boolean isTaskInTodaysListAndNotOverdue(String templateTaskId) {
        // Check if task exists in today's list
        if (!templateExistsInToday(templateTaskId)) {
            return false; // Not in today's list at all
        }
        
        // Find the task and check if it's overdue
        for (Task task : todaysTasks.values()) {
            // Use Objects.equals for null-safe comparison
            if (templateTaskId != null && templateTaskId.equals(task.getTemplateTaskId())) {
                // Return true only if task is NOT overdue
                // This allows re-adding overdue tasks with new due dates
                return !task.isOverdue();
            }
        }
        
        return false; // Should not reach here if templateExistsInToday worked correctly
    }
    
    @Override
    public boolean isExactDuplicateInTodaysList(String templateTaskId, Task.Priority priority, LocalDate dueDate) {
        // Check all today's tasks for exact matches
        for (Task task : todaysTasks.values()) {
            if (templateTaskId != null && templateTaskId.equals(task.getTemplateTaskId())) {
                // Found a task with the same template ID, now check priority and due date
                
                // Compare priority (null-safe)
                boolean priorityMatches = Objects.equals(task.getPriority(), priority);
                
                // Compare due date (null-safe)
                LocalDate taskDueDate = (task.getDates() != null) ? task.getDates().getDueDate() : null;
                boolean dueDateMatches = Objects.equals(taskDueDate, dueDate);
                
                if (priorityMatches && dueDateMatches) {
                    return true; // Exact duplicate found
                }
            }
        }
        return false; // No exact duplicate found
    }

    // ===== MarkTaskCompleteDataAccessInterface methods =====

    @Override
    public TaskInterf getTodayTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }

    @Override
    public boolean updateTaskCompletionStatus(String taskId, boolean isCompleted) {
        Task task = todaysTasks.get(taskId);
        if (task == null) {
            return false;
        }
        
        if (isCompleted) {
            task.markComplete();
            // In a full implementation, we would move this task to the end of the display order
            System.out.println("DEBUG: Task marked complete - would move to bottom in full implementation");
        } else {
            task.unmarkComplete();
            // In a full implementation, we would restore original position
            System.out.println("DEBUG: Task unmarked - would restore position in full implementation");
        }
        
        return true;
    }

    // ===== EditTodayTaskDataAccessInterface methods =====

    @Override
    public boolean updateTodayTaskPriorityAndDueDate(String taskId, Task.Priority priority, LocalDate dueDate) {
        // Validate due date if provided
        if (dueDate != null && !isValidDueDate(dueDate)) {
            throw new IllegalArgumentException("Due date cannot be before today");
        }
        
        Task task = todaysTasks.get(taskId);
        if (task == null) {
            return false;
        }
        
        // Update priority (can be null to clear)
        task.setPriority(priority);
        
        // Update due date directly on the existing BeginAndDueDates object
        if (task.getDates() != null) {
            task.getDates().setDueDate(dueDate);  // Can be null to clear due date
        }
        
        System.out.println("DEBUG: Updated today's task - ID: " + taskId + 
                          ", Priority: " + (priority != null ? priority : "none") + 
                          ", Due Date: " + (dueDate != null ? dueDate : "none"));
        
        return true;
    }

    @Override
    public boolean isValidDueDate(LocalDate dueDate) {
        if (dueDate == null) {
            return true;  // null due date is valid (means no due date)
        }
        return !dueDate.isBefore(LocalDate.now());
    }

    // ===== EditCategoryTaskDataAccessInterface methods =====

    @Override
    public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
        List<TaskAvailable> result = new ArrayList<>();
        
        // Check in availableTaskTemplates
        for (TaskAvailable task : availableTaskTemplates.values()) {
            if (task.getInfo().getCategory() != null && 
                task.getInfo().getCategory().equals(categoryId)) {
                result.add(task);
            }
        }
        
        // Also check legacy storage for completeness
        for (Map.Entry<String, Info> entry : availableTasks.entrySet()) {
            String taskId = entry.getKey();
            Info info = entry.getValue();
            // Only add if not already in result from availableTaskTemplates
            if (!availableTaskTemplates.containsKey(taskId) &&
                info.getCategory() != null && 
                info.getCategory().equals(categoryId)) {
                // Create TaskAvailable wrapper for legacy Info
                result.add(new TaskAvailable(info));
            }
        }
        
        return result;
    }

    @Override
    public List<Task> findTodaysTasksByCategory(String categoryId) {
        List<Task> result = new ArrayList<>();
        
        for (Task task : todaysTasks.values()) {
            if (task.getInfo().getCategory() != null && 
                task.getInfo().getCategory().equals(categoryId)) {
                result.add(task);
            }
        }
        
        return result;
    }

    @Override
    public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
        // Update in availableTaskTemplates
        TaskAvailable taskAvailable = availableTaskTemplates.get(taskId);
        if (taskAvailable != null) {
            taskAvailable.getInfo().setCategory(newCategoryId);
        }
        
        // Also update in legacy storage
        InfoInterf info = availableTasks.get(taskId);
        if (info != null) {
            info.setCategory(newCategoryId);
            return true;
        }
        
        return taskAvailable != null;
    }

    @Override
    public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
        Task task = todaysTasks.get(taskId);
        if (task != null) {
            task.getInfo().setCategory(newCategoryId);
            return true;
        }
        return false;
    }

    @Override
    public List<TaskAvailable> findAvailableTasksWithEmptyCategory() {
        List<TaskAvailable> result = new ArrayList<>();
        
        // Check in availableTaskTemplates
        for (TaskAvailable task : availableTaskTemplates.values()) {
            String category = task.getInfo().getCategory();
            if (category == null || category.trim().isEmpty()) {
                result.add(task);
            }
        }
        
        return result;
    }

    @Override
    public List<Task> findTodaysTasksWithEmptyCategory() {
        List<Task> result = new ArrayList<>();
        
        for (Task task : todaysTasks.values()) {
            String category = task.getInfo().getCategory();
            if (category == null || category.trim().isEmpty()) {
                result.add(task);
            }
        }
        
        return result;
    }

    // ===== RemoveFromTodayDataAccessInterface methods =====

    @Override
    public boolean removeFromTodaysList(String taskId) {
        // Only remove from today's tasks, NOT from available tasks
        Task removed = todaysTasks.remove(taskId);
        if (removed != null) {
            System.out.println("DEBUG: Removed task from today's list - ID: " + taskId + 
                              ", Name: " + removed.getInfo().getName());
            return true;
        }
        return false;
    }

    // Note: getTodayTaskById is already implemented for MarkTaskCompleteDataAccessInterface

    // ===== OverdueTasksDataAccessInterface methods =====

    @Override
    public List<TaskInterf> getOverdueTasks(int daysBack) {
        LocalDate cutoffDate = LocalDate.now().minusDays(daysBack);
        LocalDate today = LocalDate.now();
        
        List<TaskInterf> overdueTasks = new ArrayList<>();
        for (Task task : todaysTasks.values()) {
            if (task.isCompleted()) {
                continue;
            }
            LocalDate dueDate = task.getBeginAndDueDates().getDueDate();
            if (dueDate == null) {
                continue;
            }
            // Task is overdue if due date is before today
            // and due date is not before our cutoff (within range)
            if (dueDate.isBefore(today) && !dueDate.isBefore(cutoffDate)) {
                overdueTasks.add(task);
            }
        }
        
        // Sort by due date - most overdue first (earliest date first)
        overdueTasks.sort((t1, t2) -> {
            LocalDate date1 = t1.getBeginAndDueDates().getDueDate();
            LocalDate date2 = t2.getBeginAndDueDates().getDueDate();
            int dateComparison = date1.compareTo(date2);
            
            // If same due date, sort alphabetically by task name
            if (dateComparison == 0) {
                String name1 = t1.getInfo().getName();
                String name2 = t2.getInfo().getName();
                if (name1 == null && name2 == null) return 0;
                if (name1 == null) return 1;
                if (name2 == null) return -1;
                return name1.compareToIgnoreCase(name2);
            }
            
            return dateComparison;
        });
            
        return overdueTasks;
    }

    @Override  
    public List<TaskInterf> getAllOverdueTasks() {
        List<TaskInterf> overdueTasks = new ArrayList<>();
        for (Task task : todaysTasks.values()) {
            if (task.isOverdue()) {
                overdueTasks.add(task);
            }
        }
        
        // Sort by due date - most overdue first (earliest date first)
        overdueTasks.sort((t1, t2) -> {
            LocalDate date1 = t1.getBeginAndDueDates().getDueDate();
            LocalDate date2 = t2.getBeginAndDueDates().getDueDate();
            if (date1 == null || date2 == null) {
                return 0;
            }
            int dateComparison = date1.compareTo(date2);
            
            // If same due date, sort alphabetically by task name
            if (dateComparison == 0) {
                String name1 = t1.getInfo().getName();
                String name2 = t2.getInfo().getName();
                if (name1 == null && name2 == null) return 0;
                if (name1 == null) return 1;
                if (name2 == null) return -1;
                return name1.compareToIgnoreCase(name2);
            }
            
            return dateComparison;
        });
            
        return overdueTasks;
    }
    
    // Additional methods to satisfy interface requirements after refactoring
    
    /**
     * Clears all data from this gateway for testing purposes.
     * WARNING: This will delete all tasks and should only be used in tests!
     */
    public void clearAllData() {
        availableTasks.clear();
        availableTaskTemplates.clear();
        todaysTasks.clear();
    }
}