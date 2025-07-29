package data_access;

import entity.Angela.Task.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import use_case.repository.TaskRepository;

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
 * File-based implementation of TaskRepository.
 * Stores tasks in tasks.json using simple JSON format.
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
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                boolean inAvailableTasks = false;
                boolean inTodaysTasks = false;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();

                    if (line.equals("\"availableTasks\": [")) {
                        inAvailableTasks = true;
                        continue;
                    } else if (line.equals("\"todaysTaskIds\": [")) {
                        inTodaysTasks = true;
                        inAvailableTasks = false;
                        continue;
                    } else if (line.equals("]") || line.equals("],")) {
                        inAvailableTasks = false;
                        inTodaysTasks = false;
                        continue;
                    }

                    if (inAvailableTasks && line.startsWith("{")) {
                        Task task = parseTaskFromJson(reader, line);
                        if (task != null) {
                            allTasks.put(task.getInfo().getId(), task);
                        }
                    } else if (inTodaysTasks && line.startsWith("\"")) {
                        String taskId = line.replace("\"", "").replace(",", "").trim();
                        todaysTaskIds.add(taskId);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading tasks: " + e.getMessage());
            }
        }
    }

    private Task parseTaskFromJson(BufferedReader reader, String firstLine) throws IOException {
        Map<String, String> taskData = new HashMap<>();
        String line = firstLine;

        while (line != null && !line.contains("}")) {
            if (line.contains(":")) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim().replace("\"", "");
                    String value = parts[1].trim().replace("\"", "").replace(",", "");
                    taskData.put(key, value);
                }
            }
            line = reader.readLine();
        }

        // Parse the last line if it contains data
        if (line != null && line.contains(":")) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].trim().replace("\"", "");
                String value = parts[1].trim()
                        .replace("\"", "")
                        .replace(",", "")
                        .replace("}", "")
                        .replace("]", "");
                taskData.put(key, value);
            }
        }

        // Create task from parsed data
        return reconstructTask(taskData);
    }

    private Task reconstructTask(Map<String, String> data) {
        try {
            // Create Info using Builder
            Info info = new Info.Builder()
                    .id(data.get("id"))
                    .name(data.get("name"))
                    .description(data.getOrDefault("description", ""))
                    .category(data.get("category").equals("null") ? null : data.get("category"))
                    .createdDate(LocalDate.parse(data.get("createdDate"), DATE_FORMATTER))
                    .build();

            // Create BeginAndDueDates
            LocalDate beginDate = data.get("beginDate").equals("null") ?
                    null : LocalDate.parse(data.get("beginDate"), DATE_FORMATTER);
            LocalDate dueDate = data.get("dueDate").equals("null") ?
                    null : LocalDate.parse(data.get("dueDate"), DATE_FORMATTER);
            BeginAndDueDates dates = new BeginAndDueDates(beginDate, dueDate);

            // Create Task
            Task task = new Task(info, dates);

            // Set additional properties
            String priority = data.get("priority");
            if (priority != null && !priority.equals("null")) {
                task.setPriority(Task.Priority.valueOf(priority));
            }

            boolean isCompleted = Boolean.parseBoolean(data.get("isCompleted"));
            if (isCompleted) {
                task.editStatus(true);
                String completedDateTime = data.get("completedDateTime");
                if (completedDateTime != null && !completedDateTime.equals("null")) {
                    task.setCompletedDateTime(LocalDateTime.parse(completedDateTime, DATETIME_FORMATTER));
                }
            }

            task.setOneTime(Boolean.parseBoolean(data.get("oneTime")));

            return task;
        } catch (Exception e) {
            System.err.println("Error reconstructing task: " + e.getMessage());
            return null;
        }
    }

    private void saveTasks() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(getTasksFilePath()))) {
            writer.println("{");
            writer.println("  \"availableTasks\": [");

            // Write available tasks
            List<Task> taskList = new ArrayList<>(allTasks.values());
            for (int i = 0; i < taskList.size(); i++) {
                writeTask(writer, taskList.get(i), i < taskList.size() - 1);
            }

            writer.println("  ],");
            writer.println("  \"todaysTaskIds\": [");

            // Write today's task IDs
            List<String> todaysList = new ArrayList<>(todaysTaskIds);
            for (int i = 0; i < todaysList.size(); i++) {
                writer.print("    \"" + todaysList.get(i) + "\"");
                if (i < todaysList.size() - 1) {
                    writer.println(",");
                } else {
                    writer.println();
                }
            }

            writer.println("  ]");
            writer.println("}");
        } catch (IOException e) {
            throw new RuntimeException("Failed to save tasks", e);
        }
    }

    private void writeTask(PrintWriter writer, Task task, boolean hasNext) {
        writer.println("    {");
        writer.println("      \"id\": \"" + task.getInfo().getId() + "\",");
        writer.println("      \"name\": \"" + escapeJson(task.getInfo().getName()) + "\",");
        writer.println("      \"description\": \"" + escapeJson(task.getInfo().getDescription()) + "\",");
        writer.println("      \"category\": " + (task.getInfo().getCategory() != null ?
                "\"" + escapeJson(task.getInfo().getCategory()) + "\"" : "null") + ",");
        writer.println("      \"createdDate\": \"" + task.getInfo().getCreatedDate().format(DATE_FORMATTER) + "\",");

        BeginAndDueDates dates = task.getBeginAndDueDates();
        writer.println("      \"beginDate\": " + (dates.getBeginDate() != null ?
                "\"" + dates.getBeginDate().format(DATE_FORMATTER) + "\"" : "null") + ",");
        writer.println("      \"dueDate\": " + (dates.getDueDate() != null ?
                "\"" + dates.getDueDate().format(DATE_FORMATTER) + "\"" : "null") + ",");

        writer.println("      \"priority\": " + (task.getPriority() != null ?
                "\"" + task.getPriority().name() + "\"" : "null") + ",");
        writer.println("      \"isCompleted\": " + task.isComplete() + ",");
        writer.println("      \"completedDateTime\": " + (task.getCompletedDateTime() != null ?
                "\"" + task.getCompletedDateTime().format(DATETIME_FORMATTER) + "\"" : "null") + ",");
        writer.println("      \"oneTime\": " + task.isOneTime());

        writer.print("    }");
        if (hasNext) {
            writer.println(",");
        } else {
            writer.println();
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
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

    /**
     * Removes completed one-time tasks at day end.
     */
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

    /**
     * Clears today's task list (except tasks with due dates).
     */
    public void clearTodaysTasks() {
        todaysTaskIds.clear();
        saveTasks();
    }

    /**
     * Gets the file path for testing purposes.
     */
    protected String getTasksFilePath() {
        return TASKS_FILE;
    }
}