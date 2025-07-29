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
 * File-based implementation of TaskGateway using org.json.
 */
public class FileTaskRepository implements TaskGateway {
    private static final String TASKS_FILE = "data/tasks.json";
    private static final String DATA_DIR = "data";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Map<String, Task> allTasks;
    private final Set<String> todaysTaskIds;

    public FileTaskRepository() {
        this.allTasks = new HashMap<>();
        this.todaysTaskIds = new HashSet<>();
        ensureDataDirectoryExists();
        loadTasksFromFile();
    }

    private void ensureDataDirectoryExists() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    @Override
    public String saveAvailableTask(Info info) {
        String taskId = info.getId();
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);
        Task task = new Task(info, dates);
        allTasks.put(taskId, task);
        saveTasksToFile();
        return taskId;
    }

    @Override
    public List<Info> getAllAvailableTasks() {
        return allTasks.values().stream()
                .filter(task -> !todaysTaskIds.contains(task.getInfo().getId()))
                .map(Task::getInfo)
                .collect(Collectors.toList());
    }

    @Override
    public boolean availableTaskNameExists(String name) {
        return allTasks.values().stream()
                .anyMatch(task -> task.getInfo().getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean updateAvailableTask(Info info) {
        String taskId = info.getId();
        Task existingTask = allTasks.get(taskId);
        if (existingTask != null) {
            Task updatedTask = new Task(info, existingTask.getBeginAndDueDates(),
                    existingTask.getTaskPriority());
            if (existingTask.isComplete()) {
                updatedTask.completeTask(existingTask.getCompletedDateTime());
            }
            updatedTask.setOneTime(existingTask.isOneTime());
            allTasks.put(taskId, updatedTask);
            saveTasksToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFromAvailable(String taskId) {
        if (allTasks.remove(taskId) != null) {
            saveTasksToFile();
            return true;
        }
        return false;
    }

    @Override
    public Task addToToday(String taskId, Task.Priority priority, LocalDate dueDate) {
        Task task = allTasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }

        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);
        Task todayTask = new Task(task.getInfo(), dates, priority);
        todayTask.setOneTime(task.isOneTime());

        allTasks.put(taskId, todayTask);
        todaysTaskIds.add(taskId);
        saveTasksToFile();
        return todayTask;
    }

    @Override
    public List<Task> getTodaysTasks() {
        return todaysTaskIds.stream()
                .map(allTasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateTodaysTask(Task task) {
        String taskId = task.getInfo().getId();
        if (todaysTaskIds.contains(taskId)) {
            allTasks.put(taskId, task);
            saveTasksToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFromToday(String taskId) {
        if (todaysTaskIds.remove(taskId)) {
            saveTasksToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean markTaskComplete(String taskId) {
        Task task = allTasks.get(taskId);
        if (task != null && todaysTaskIds.contains(taskId) && !task.isComplete()) {
            task.completeTask(LocalDateTime.now());
            saveTasksToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean unmarkTaskComplete(String taskId) {
        Task task = allTasks.get(taskId);
        if (task != null && task.isComplete()) {
            Task newTask = task.unmarkComplete();
            allTasks.put(taskId, newTask);
            saveTasksToFile();
            return true;
        }
        return false;
    }

    @Override
    public boolean existsInAvailable(String taskId) {
        return allTasks.containsKey(taskId) && !todaysTaskIds.contains(taskId);
    }

    @Override
    public boolean existsInToday(String taskId) {
        return todaysTaskIds.contains(taskId);
    }

    @Override
    public String getTaskName(String taskId) {
        Task task = allTasks.get(taskId);
        return task != null ? task.getInfo().getName() : null;
    }

    @Override
    public boolean deleteTaskCompletely(String taskId) {
        boolean removed = allTasks.remove(taskId) != null;
        todaysTaskIds.remove(taskId);
        if (removed) {
            saveTasksToFile();
        }
        return removed;
    }

    @Override
    public List<Task> getTasksWithDueDates() {
        return allTasks.values().stream()
                .filter(task -> task.getBeginAndDueDates().getDueDate() != null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getOverdueTasks() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return allTasks.values().stream()
                .filter(Task::isOverdue)
                .filter(task -> {
                    LocalDate dueDate = task.getBeginAndDueDates().getDueDate();
                    return dueDate != null && !dueDate.isBefore(sevenDaysAgo);
                })
                .sorted((t1, t2) -> {
                    LocalDate d1 = t1.getBeginAndDueDates().getDueDate();
                    LocalDate d2 = t2.getBeginAndDueDates().getDueDate();
                    return d1.compareTo(d2);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getCompletedTasksToday() {
        LocalDate today = LocalDate.now();
        return todaysTaskIds.stream()
                .map(allTasks::get)
                .filter(Objects::nonNull)
                .filter(Task::isComplete)
                .filter(task -> {
                    LocalDateTime completedTime = task.getCompletedDateTime();
                    return completedTime != null && completedTime.toLocalDate().equals(today);
                })
                .collect(Collectors.toList());
    }

    @Override
    public double getTodaysCompletionRate() {
        List<Task> todaysTasks = getTodaysTasks();
        if (todaysTasks.isEmpty()) {
            return 0.0;
        }
        long completedCount = todaysTasks.stream()
                .filter(Task::isComplete)
                .count();
        return (double) completedCount / todaysTasks.size();
    }

    private void loadTasksFromFile() {
        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(TASKS_FILE)));
            JSONObject root = new JSONObject(content);

            // Load all tasks
            JSONArray tasksArray = root.optJSONArray("tasks");
            if (tasksArray != null) {
                for (int i = 0; i < tasksArray.length(); i++) {
                    JSONObject taskJson = tasksArray.getJSONObject(i);
                    Task task = taskFromJson(taskJson);
                    allTasks.put(task.getInfo().getId(), task);
                }
            }

            // Load today's task IDs
            JSONArray todayArray = root.optJSONArray("todaysTaskIds");
            if (todayArray != null) {
                for (int i = 0; i < todayArray.length(); i++) {
                    todaysTaskIds.add(todayArray.getString(i));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    private void saveTasksToFile() {
        JSONObject root = new JSONObject();

        // Save all tasks
        JSONArray tasksArray = new JSONArray();
        for (Task task : allTasks.values()) {
            tasksArray.put(taskToJson(task));
        }
        root.put("tasks", tasksArray);

        // Save today's task IDs
        JSONArray todayArray = new JSONArray(todaysTaskIds);
        root.put("todaysTaskIds", todayArray);

        try {
            File file = new File(TASKS_FILE);
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(root.toString(2));
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private JSONObject taskToJson(Task task) {
        JSONObject json = new JSONObject();

        // Save info
        JSONObject infoJson = new JSONObject();
        Info info = task.getInfo();
        infoJson.put("id", info.getId());
        infoJson.put("name", info.getName());
        infoJson.put("description", info.getDescription() != null ? info.getDescription() : "");
        infoJson.put("category", info.getCategory() != null ? info.getCategory() : "");
        infoJson.put("createdDate", info.getCreatedDate().format(DATE_FORMATTER));
        json.put("info", infoJson);

        // Save dates
        BeginAndDueDates dates = task.getBeginAndDueDates();
        json.put("beginDate", dates.getBeginDate().format(DATE_FORMATTER));
        if (dates.getDueDate() != null) {
            json.put("dueDate", dates.getDueDate().format(DATE_FORMATTER));
        }

        // Save task properties
        if (task.getTaskPriority() != null) {
            json.put("priority", task.getTaskPriority().name());
        }
        json.put("isComplete", task.isComplete());
        if (task.getCompletedDateTime() != null) {
            json.put("completedDateTime", task.getCompletedDateTime().format(DATETIME_FORMATTER));
        }
        json.put("oneTime", task.isOneTime());

        return json;
    }

    private Task taskFromJson(JSONObject json) {
        // Load info
        JSONObject infoJson = json.getJSONObject("info");
        Info.Builder infoBuilder = new Info.Builder(infoJson.getString("name"));

        String description = infoJson.optString("description", "");
        if (!description.isEmpty()) {
            infoBuilder.description(description);
        }

        String category = infoJson.optString("category", "");
        if (!category.isEmpty()) {
            infoBuilder.category(category);
        }

        Info info = infoBuilder.build();

        // Load dates
        LocalDate beginDate = LocalDate.parse(json.getString("beginDate"), DATE_FORMATTER);
        LocalDate dueDate = null;
        if (json.has("dueDate")) {
            dueDate = LocalDate.parse(json.getString("dueDate"), DATE_FORMATTER);
        }
        BeginAndDueDates dates = new BeginAndDueDates(beginDate, dueDate);

        // Create task
        Task task;
        if (json.has("priority")) {
            Task.Priority priority = Task.Priority.valueOf(json.getString("priority"));
            task = new Task(info, dates, priority);
        } else {
            task = new Task(info, dates);
        }

        // Set completion status
        if (json.getBoolean("isComplete") && json.has("completedDateTime")) {
            LocalDateTime completedTime = LocalDateTime.parse(
                    json.getString("completedDateTime"), DATETIME_FORMATTER
            );
            task.completeTask(completedTime);
        }

        // Set one-time flag
        task.setOneTime(json.optBoolean("oneTime", false));

        return task;
    }
}