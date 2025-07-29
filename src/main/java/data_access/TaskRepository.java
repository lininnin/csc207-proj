package data_access;

import entity.Angela.Task.Task;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import use_case.Angela.task.TaskGateway;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JSON-based implementation of TaskGateway.
 * Persists tasks to tasks.json file, managing both Available and Today's tasks.
 */
public class TaskRepository implements TaskGateway {
    private static final String TASKS_FILE = "tasks.json";
    private final Map<String, Info> availableTasks;
    private final Map<String, Task> todaysTasks;
    private final Map<String, Integer> taskOriginalPositions; // Track original positions for unmark

    public TaskRepository() {
        this.availableTasks = new HashMap<>();
        this.todaysTasks = new LinkedHashMap<>(); // Preserve order
        this.taskOriginalPositions = new HashMap<>();
        loadTasks();
    }

    @Override
    public String saveAvailableTask(Info info) {
        if (availableTaskNameExists(info.getName())) {
            throw new IllegalArgumentException("Task name already exists: " + info.getName());
        }
        availableTasks.put(info.getId(), info);
        saveToFile();
        return info.getId();
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
            // Also update in today's tasks if exists
            if (todaysTasks.containsKey(info.getId())) {
                Task todayTask = todaysTasks.get(info.getId());
                Task updatedTask = new Task(info, todayTask.getBeginAndDueDates(),
                        todayTask.getTaskPriority());
                if (todayTask.isComplete()) {
                    updatedTask.completeTask(todayTask.getCompletedDateTime());
                }
                todaysTasks.put(info.getId(), updatedTask);
            }
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFromAvailable(String taskId) {
        boolean removed = availableTasks.remove(taskId) != null;
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    @Override
    public Task addToToday(String taskId, Task.Priority priority, LocalDate dueDate) {
        Info taskInfo = availableTasks.get(taskId);
        if (taskInfo == null) {
            throw new IllegalArgumentException("Task not found in Available Tasks");
        }

        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);
        Task task = new Task(taskInfo, dates, priority);

        todaysTasks.put(taskId, task);
        taskOriginalPositions.put(taskId, todaysTasks.size() - 1);
        saveToFile();
        return task;
    }

    @Override
    public List<Task> getTodaysTasks() {
        return new ArrayList<>(todaysTasks.values());
    }

    @Override
    public boolean updateTodaysTask(Task task) {
        String taskId = task.getInfo().getId();
        if (todaysTasks.containsKey(taskId)) {
            todaysTasks.put(taskId, task);
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFromToday(String taskId) {
        boolean removed = todaysTasks.remove(taskId) != null;
        if (removed) {
            taskOriginalPositions.remove(taskId);
            saveToFile();
        }
        return removed;
    }

    @Override
    public boolean markTaskComplete(String taskId) {
        Task task = todaysTasks.get(taskId);
        if (task != null && !task.isComplete()) {
            task.completeTask(LocalDateTime.now());
            // Move to bottom of list
            todaysTasks.remove(taskId);
            todaysTasks.put(taskId, task);
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean unmarkTaskComplete(String taskId) {
        Task task = todaysTasks.get(taskId);
        if (task != null && task.isComplete()) {
            // Create new incomplete task
            Task newTask = new Task(task.getInfo(), task.getBeginAndDueDates(),
                    task.getTaskPriority());

            // Try to restore original position
            Integer originalPos = taskOriginalPositions.get(taskId);
            if (originalPos != null) {
                // Complex reordering logic would go here
                // For now, just put it back in the map
                // TODO: Implement position restoration logic
            }

            todaysTasks.put(taskId, newTask);
            saveToFile();
            return true;
        }
        return false;
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
        return task != null ? task.getInfo().getName() : null;
    }

    @Override
    public boolean deleteTaskCompletely(String taskId) {
        boolean removedFromAvailable = availableTasks.remove(taskId) != null;
        boolean removedFromToday = todaysTasks.remove(taskId) != null;
        if (removedFromAvailable || removedFromToday) {
            taskOriginalPositions.remove(taskId);
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public List<Task> getTasksWithDueDates() {
        return todaysTasks.values().stream()
                .filter(task -> task.getBeginAndDueDates().getDueDate() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getOverdueTasks() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return todaysTasks.values().stream()
                .filter(Task::isOverdue)
                .filter(task -> {
                    LocalDate dueDate = task.getBeginAndDueDates().getDueDate();
                    return dueDate != null && !dueDate.isBefore(sevenDaysAgo);
                })
                .sorted((t1, t2) -> {
                    LocalDate d1 = t1.getBeginAndDueDates().getDueDate();
                    LocalDate d2 = t2.getBeginAndDueDates().getDueDate();
                    return d1.compareTo(d2); // Most overdue first
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getCompletedTasksToday() {
        LocalDate today = LocalDate.now();
        return todaysTasks.values().stream()
                .filter(Task::isComplete)
                .filter(task -> {
                    LocalDateTime completedTime = task.getCompletedDateTime();
                    return completedTime != null && completedTime.toLocalDate().equals(today);
                })
                .collect(Collectors.toList());
    }

    @Override
    public double getTodaysCompletionRate() {
        if (todaysTasks.isEmpty()) {
            return 0.0;
        }
        long completedCount = todaysTasks.values().stream()
                .filter(Task::isComplete)
                .count();
        return (double) completedCount / todaysTasks.size();
    }

    /**
     * Loads tasks from the JSON file.
     */
    private void loadTasks() {
        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(TASKS_FILE)));
            JSONObject json = new JSONObject(content);

            // Load available tasks
            JSONArray availableArray = json.optJSONArray("availableTasks");
            if (availableArray != null) {
                for (int i = 0; i < availableArray.length(); i++) {
                    JSONObject taskJson = availableArray.getJSONObject(i);
                    Info info = jsonToInfo(taskJson);
                    availableTasks.put(info.getId(), info);
                }
            }

            // Load today's tasks
            JSONArray todayArray = json.optJSONArray("todaysTasks");
            if (todayArray != null) {
                for (int i = 0; i < todayArray.length(); i++) {
                    JSONObject taskJson = todayArray.getJSONObject(i);
                    Task task = jsonToTask(taskJson);
                    todaysTasks.put(task.getInfo().getId(), task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    /**
     * Saves tasks to the JSON file.
     */
    private void saveToFile() {
        JSONObject root = new JSONObject();

        // Save available tasks
        JSONArray availableArray = new JSONArray();
        for (Info info : availableTasks.values()) {
            availableArray.put(infoToJson(info));
        }
        root.put("availableTasks", availableArray);

        // Save today's tasks
        JSONArray todayArray = new JSONArray();
        for (Task task : todaysTasks.values()) {
            todayArray.put(taskToJson(task));
        }
        root.put("todaysTasks", todayArray);

        try (FileWriter writer = new FileWriter(TASKS_FILE)) {
            writer.write(root.toString(2));
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private JSONObject infoToJson(Info info) {
        JSONObject json = new JSONObject();
        json.put("id", info.getId());
        json.put("name", info.getName());
        json.put("description", info.getDescription() != null ? info.getDescription() : "");
        json.put("category", info.getCategory() != null ? info.getCategory() : "");
        json.put("createdDate", info.getCreatedDate().toString());
        return json;
    }

    private Info jsonToInfo(JSONObject json) {
        return new Info.Builder(json.getString("name"))
                .description(json.optString("description", ""))
                .category(json.optString("category", ""))
                .build();
    }

    private JSONObject taskToJson(Task task) {
        JSONObject json = new JSONObject();
        json.put("info", infoToJson(task.getInfo()));
        json.put("priority", task.getTaskPriority().name());
        json.put("isComplete", task.isComplete());

        BeginAndDueDates dates = task.getBeginAndDueDates();
        json.put("beginDate", dates.getBeginDate().toString());
        if (dates.getDueDate() != null) {
            json.put("dueDate", dates.getDueDate().toString());
        }

        if (task.getCompletedDateTime() != null) {
            json.put("completedDateTime", task.getCompletedDateTime().toString());
        }

        return json;
    }

    private Task jsonToTask(JSONObject json) {
        Info info = jsonToInfo(json.getJSONObject("info"));
        Task.Priority priority = Task.Priority.valueOf(json.getString("priority"));

        LocalDate beginDate = LocalDate.parse(json.getString("beginDate"));
        LocalDate dueDate = json.has("dueDate") ?
                LocalDate.parse(json.getString("dueDate")) : null;

        BeginAndDueDates dates = new BeginAndDueDates(beginDate, dueDate);
        Task task = new Task(info, dates, priority);

        if (json.getBoolean("isComplete") && json.has("completedDateTime")) {
            LocalDateTime completedTime = LocalDateTime.parse(json.getString("completedDateTime"));
            task.completeTask(completedTime);
        }

        return task;
    }
}