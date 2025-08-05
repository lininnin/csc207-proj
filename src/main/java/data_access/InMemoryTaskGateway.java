package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.info.Info;
import use_case.Angela.task.TaskGateway;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import use_case.Angela.task.delete.DeleteTaskDataAccessInterface;
import use_case.Angela.task.edit_available.EditAvailableTaskDataAccessInterface;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import java.time.LocalDate;
import java.util.*;

/**
 * In-memory implementation of TaskGateway and DataAccessInterfaces for quick demos.
 * Replace with TaskRepository (JSON-based) for production.
 * Following Alex's pattern: one concrete implementation implements multiple interfaces.
 */
public class InMemoryTaskGateway implements 
        TaskGateway,
        CreateTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        EditAvailableTaskDataAccessInterface,
        AddToTodayDataAccessInterface {
    private final Map<String, Info> availableTasks = Collections.synchronizedMap(new HashMap<>()); // Legacy storage for backward compatibility
    private final Map<String, TaskAvailable> availableTaskTemplates = Collections.synchronizedMap(new HashMap<>()); // New storage for TaskAvailable
    private final Map<String, Task> todaysTasks = Collections.synchronizedMap(new HashMap<>());

    @Override
    public String saveAvailableTask(Info info) {
        String taskId = info.getId(); // Info already has an ID
        System.out.println("DEBUG: saveAvailableTask (legacy) called - ID: " + taskId + ", Name: " + info.getName());
        availableTasks.put(taskId, info);
        System.out.println("DEBUG: After legacy save - availableTasks size: " + availableTasks.size());
        return taskId;
    }

    @Override
    public List<Info> getAllAvailableTasks() {
        return new ArrayList<>(availableTasks.values());
    }

    @Override
    public boolean availableTaskNameExists(String name) {
        return availableTasks.values().stream()
                .anyMatch(info -> info.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean updateAvailableTask(Info info) {
        if (availableTasks.containsKey(info.getId())) {
            availableTasks.put(info.getId(), info);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFromAvailable(String taskId) {
        return availableTasks.remove(taskId) != null;
    }

    @Override
    public Task addToToday(String taskId, Task.Priority priority, LocalDate dueDate) {
        TaskAvailable taskAvailable = getTaskAvailableById(taskId);
        if (taskAvailable == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        return addTaskToToday(taskAvailable, priority, dueDate);
    }

    @Override
    public List<Task> getTodaysTasks() {
        return new ArrayList<>(todaysTasks.values());
    }

    @Override
    public boolean updateTodaysTask(Task task) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean removeFromToday(String taskId) {
        return todaysTasks.remove(taskId) != null;
    }

    @Override
    public boolean markTaskComplete(String taskId) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean unmarkTaskComplete(String taskId) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean existsInAvailable(String taskId) {
        return availableTasks.containsKey(taskId);
    }

    @Override
    public boolean existsInToday(String taskId) {
        return todaysTasks.containsKey(taskId);
    }

    @Override
    public String getTaskName(String taskId) {
        Info info = availableTasks.get(taskId);
        if (info != null) {
            return info.getName();
        }
        Task task = todaysTasks.get(taskId);
        if (task != null) {
            return task.getInfo().getName();
        }
        return null;
    }

    @Override
    public List<Task> getTasksWithDueDates() {
        // For demo purposes, return empty list
        return new ArrayList<>();
    }

    @Override
    public List<Task> getOverdueTasks() {
        // For demo purposes, return empty list
        return new ArrayList<>();
    }

    @Override
    public List<Task> getCompletedTasksToday() {
        return todaysTasks.values().stream()
                .filter(Task::isCompleted)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public double getTodaysCompletionRate() {
        if (todaysTasks.isEmpty()) {
            return 0.0;
        }
        long completed = todaysTasks.values().stream()
                .filter(Task::isCompleted)
                .count();
        return (double) completed / todaysTasks.size() * 100;
    }

    // ===== CreateTaskDataAccessInterface methods =====

    @Override
    public String saveTaskAvailable(TaskAvailable taskAvailable) {
        String taskId = taskAvailable.getId();
        System.out.println("DEBUG: saveTaskAvailable called - ID: " + taskId + ", Name: " + taskAvailable.getInfo().getName());
        
        availableTaskTemplates.put(taskId, taskAvailable);
        // Also save to legacy storage for backward compatibility
        availableTasks.put(taskId, taskAvailable.getInfo());
        
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
                Info info = taskAvailable.getInfo();
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
        for (Info info : availableTasks.values()) {
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
    public List<TaskAvailable> getAllAvailableTaskTemplates() {
        return new ArrayList<>(availableTaskTemplates.values());
    }

    @Override
    public TaskAvailable getTaskAvailableById(String taskId) {
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
    public boolean exists(TaskAvailable taskAvailable) {
        return availableTaskTemplates.containsKey(taskAvailable.getId());
    }

    // ===== DeleteTaskDataAccessInterface methods =====

    @Override
    public Task getTodaysTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }

    @Override
    public boolean existsInAvailable(TaskAvailable taskAvailable) {
        return availableTaskTemplates.containsKey(taskAvailable.getId());
    }

    @Override
    public boolean existsInToday(Task task) {
        return todaysTasks.containsKey(task.getInfo().getId());
    }

    @Override
    public boolean templateExistsInToday(String templateTaskId) {
        return todaysTasks.values().stream()
                .anyMatch(task -> templateTaskId.equals(task.getTemplateTaskId()));
    }

    @Override
    public boolean deleteFromAvailable(TaskAvailable taskAvailable) {
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
    public List<Task> getTodaysTasksByTemplate(String templateTaskId) {
        return todaysTasks.values().stream()
                .filter(task -> templateTaskId.equals(task.getTemplateTaskId()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Task> getAllTodaysTasks() {
        return new ArrayList<>(todaysTasks.values());
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
        Info legacyInfo = availableTasks.remove(templateTaskId);
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
        Info info = existingTask.getInfo();
        info.setName(newName);
        info.setDescription(newDescription != null ? newDescription : "");
        info.setCategory(newCategoryId != null ? newCategoryId : "");

        // Update the isOneTime flag
        existingTask.setOneTime(isOneTime);

        // Update legacy storage for backward compatibility
        availableTasks.put(taskId, info);

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
                Info info = taskAvailable.getInfo();
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
            
            Info info = entry.getValue();
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
    public List<TaskAvailable> getAllAvailableTasksWithDetails() {
        List<TaskAvailable> allTasks = new ArrayList<>();
        
        System.out.println("DEBUG: getAllAvailableTasksWithDetails called");
        System.out.println("DEBUG: availableTaskTemplates size: " + availableTaskTemplates.size());
        System.out.println("DEBUG: availableTasks size: " + availableTasks.size());
        
        // Add all tasks from the new storage
        allTasks.addAll(availableTaskTemplates.values());
        
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
        
        System.out.println("DEBUG: Total tasks returned: " + allTasks.size());
        for (TaskAvailable task : allTasks) {
            System.out.println("DEBUG: Task - ID: " + task.getId() + ", Name: " + task.getInfo().getName());
        }
        
        return allTasks;
    }

    // ===== AddToTodayDataAccessInterface methods =====

    @Override
    public TaskAvailable getAvailableTaskById(String taskId) {
        return getTaskAvailableById(taskId); // Reuse existing method
    }

    @Override
    public Task addTaskToToday(TaskAvailable taskAvailable, Task.Priority priority, java.time.LocalDate dueDate) {
        // Create a new Task instance from the TaskAvailable template
        Info templateInfo = taskAvailable.getInfo();
        
        // Create a new Info instance for today's task (clone the template)
        Info todayInfo = new Info.Builder(templateInfo.getName())
                .description(templateInfo.getDescription())
                .category(templateInfo.getCategory())
                .build();
        
        // Create BeginAndDueDates
        entity.BeginAndDueDates.BeginAndDueDates dates = new entity.BeginAndDueDates.BeginAndDueDates(
                LocalDate.now(), // Begin date is always today
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
        
        // Store in today's tasks map using the Info's generated ID
        todaysTasks.put(todayTask.getInfo().getId(), todayTask);
        
        return todayTask;
    }

    @Override
    public boolean isTaskInTodaysList(String templateTaskId) {
        return templateExistsInToday(templateTaskId); // Reuse existing method
    }
}