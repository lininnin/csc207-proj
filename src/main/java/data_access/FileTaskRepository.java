package data_access;

import entity.Angela.Task.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import use_case.repository.TaskRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File-based implementation of TaskRepository using org.json.
 */
public class FileTaskRepository implements TaskRepository {
    private static final String TASKS_FILE = "data/tasks.json";
    private static final Path DATA_DIR = Paths.get("data");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Map<String, Task> allTasks;
    private final Set<String> todaysTaskIds;

    public FileTaskRepository() {
        this.allTasks = new HashMap<>();
        this.todaysTaskIds = new HashSet<>();

        ensureDataDirectoryExists();
        loadTasks();
    }

    private void ensureDataDirectoryExists() {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory", e);
        }
    }

    private void loadTasks() {
        File file = new File(getTasksFilePath());
        if (file.exists()) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                JSONObject json = new JSONObject(content);

                // Load available tasks
                JSONArray tasksArray = json.optJSONArray("availableTasks");
                if (tasksArray != null) {
                    for (int i = 0; i < tasksArray.length(); i++) {
                        JSONObject taskJson = tasksArray.getJSONObject(i);
                        Task task = reconstructTask(taskJson);
                        if (task != null) {
                            allTasks.put(task.getInfo().getId(), task);
                        }
                    }
                }

                // Load today's task IDs
                JSONArray todaysArray = json.optJSONArray("todaysTaskIds");
                if (todaysArray != null) {
                    for (int i = 0; i < todaysArray.length(); i++) {
                        todaysTaskIds.add(todaysArray.getString(i));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading tasks: " + e.getMessage());
            }
        }
    }

    private Task reconstructTask(JSONObject json) {
        try {
            // Create Info using Builder
            String name = json.getString("name");
            Info.Builder infoBuilder = new Info.Builder(name);

            if (json.has("description") && !json.isNull("description")) {
                infoBuilder.description(json.getString("description"));
            }
            if (json.has("category") && !json.isNull("category")) {
                infoBuilder.category(json.getString("category"));
            }

            Info info = infoBuilder.build();

            // Create BeginAndDueDates
            LocalDate beginDate = null;
            LocalDate dueDate = null;

            if (json.has("beginDate") && !json.isNull("beginDate")) {
                beginDate = LocalDate.parse(json.getString("beginDate"), DATE_FORMATTER);
            }
            if (json.has("dueDate") && !json.isNull("dueDate")) {
                dueDate = LocalDate.parse(json.getString("dueDate"), DATE_FORMATTER);
            }

            BeginAndDueDates dates = new BeginAndDueDates(beginDate, dueDate);

            // Create Task
            Task task = new Task(info, dates);

            // Set additional properties
            if (json.has("priority") && !json.isNull("priority")) {
                task.setPriority(Task.Priority.valueOf(json.getString("priority")));
            }

            if (json.has("isCompleted") && json.getBoolean("isCompleted")) {
                task.editStatus(true);
                if (json.has("completedDateTime") && !json.isNull("completedDateTime")) {
                    task.setCompletedDateTime(LocalDateTime.parse(json.getString("completedDateTime"), DATETIME_FORMATTER));
                }
            }

            if (json.has("oneTime")) {
                task.setOneTime(json.getBoolean("oneTime"));
            }

            return task;
        } catch (Exception e) {
            System.err.println("Error reconstructing task: " + e.getMessage());
            return null;
        }
    }

    private void saveTasks() {
        try {
            JSONObject root = new JSONObject();

            // Save available tasks
            JSONArray tasksArray = new JSONArray();
            for (Task task : allTasks.values()) {
                tasksArray.put(taskToJson(task));
            }
            root.put("availableTasks", tasksArray);

            // Save today's task IDs
            JSONArray todaysArray = new JSONArray(todaysTaskIds);
            root.put("todaysTaskIds", todaysArray);

            // Write to file
            try (FileWriter writer = new FileWriter(getTasksFilePath())) {
                writer.write(root.toString(2)); // Pretty print with indent
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks", e);
        }
    }

    private JSONObject taskToJson(Task task) {
        JSONObject json = new JSONObject();

        // Info fields
        json.put("id", task.getInfo().getId());
        json.put("name", task.getInfo().getName());
        json.put("description", task.getInfo().getDescription());
        json.put("category", task.getInfo().getCategory());
        json.put("createdDate", task.getInfo().getCreatedDate().format(DATE_FORMATTER));

        // Dates
        BeginAndDueDates dates = task.getBeginAndDueDates();
        json.put("beginDate", dates.getBeginDate() != null ?
                dates.getBeginDate().format(DATE_FORMATTER) : JSONObject.NULL);
        json.put("dueDate", dates.getDueDate() != null ?
                dates.getDueDate().format(DATE_FORMATTER) : JSONObject.NULL);

        // Task properties
        json.put("priority", task.getPriority() != null ?
                task.getPriority().name() : JSONObject.NULL);
        json.put("isCompleted", task.isComplete());
        json.put("completedDateTime", task.getCompletedDateTime() != null ?
                task.getCompletedDateTime().format(DATETIME_FORMATTER) : JSONObject.NULL);
        json.put("oneTime", task.isOneTime());

        return json;
    }

    @Override
    public void save(Task task) {
        allTasks.put(task.getInfo().getId(), task);
        saveTasks();
    }

    @Override
    public void update(Task task) {
        allTasks.put(task.getInfo().getId(), task);
        saveTasks();
    }

    @Override
    public Task findById(String taskId) {
        return allTasks.get(taskId);
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(allTasks.values());
    }

    @Override
    public List<Task> findTodaysTasks() {
        LocalDate today = LocalDate.now();
        List<Task> todaysTasks = new ArrayList<>();

        // Add explicitly added tasks
        for (String taskId : todaysTaskIds) {
            Task task = allTasks.get(taskId);
            if (task != null && !task.isComplete()) {
                todaysTasks.add(task);
            }
        }

        // Add tasks with due dates (appear every day until completed)
        for (Task task : allTasks.values()) {
            if (!task.isComplete() &&
                    task.getBeginAndDueDates().getDueDate() != null &&
                    !todaysTaskIds.contains(task.getInfo().getId())) {

                LocalDate beginDate = task.getBeginAndDueDates().getBeginDate();
                LocalDate dueDate = task.getBeginAndDueDates().getDueDate();

                // Task appears if today is between begin and due date
                if (beginDate != null && !today.isBefore(beginDate) && !today.isAfter(dueDate)) {
                    todaysTasks.add(task);
                }
            }
        }

        return todaysTasks;
    }

    @Override
    public List<Task> findOverdueTasks() {
        LocalDate today = LocalDate.now();
        return allTasks.values().stream()
                .filter(task -> !task.isComplete())
                .filter(task -> {
                    LocalDate dueDate = task.getBeginAndDueDates().getDueDate();
                    return dueDate != null && dueDate.isBefore(today);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addToTodaysTasks(Task task) {
        todaysTaskIds.add(task.getInfo().getId());
        saveTasks();
    }

    @Override
    public List<Task> findByCategory(String category) {
        return allTasks.values().stream()
                .filter(task -> Objects.equals(task.getInfo().getCategory(), category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByPriority(Task.Priority priority) {
        return allTasks.values().stream()
                .filter(task -> task.getPriority() == priority)
                .collect(Collectors.toList());
    }

    public void removeCompletedOneTimeTasks() {
        List<String> toRemove = allTasks.values().stream()
                .filter(task -> task.isOneTime() && task.isComplete())
                .map(task -> task.getInfo().getId())
                .collect(Collectors.toList());

        for (String id : toRemove) {
            allTasks.remove(id);
            todaysTaskIds.remove(id);
        }

        if (!toRemove.isEmpty()) {
            saveTasks();
        }
    }

    public void clearTodaysTasks() {
        todaysTaskIds.clear();
        saveTasks();
    }

    protected String getTasksFilePath() {
        return TASKS_FILE;
    }
}