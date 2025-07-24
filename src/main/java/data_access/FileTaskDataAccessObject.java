package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.AvailableTask;
import entity.TodaysTask;
import use_case.task.TaskRepository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * File-based implementation of TaskRepository.
 * Stores available tasks and today's tasks in separate JSON files.
 */
public class FileTaskDataAccessObject implements TaskRepository {
    private static final String AVAILABLE_TASKS_FILE = "available-tasks.json";
    private static final String TODAYS_TASKS_FILE = "todays-tasks.json";

    private final Gson gson;
    private Map<String, AvailableTask> availableTasks;
    private Map<String, TodaysTask> todaysTasks;

    public FileTaskDataAccessObject() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();

        loadData();
    }

    // Available Task operations

    @Override
    public void saveAvailableTask(AvailableTask task) {
        if (availableTasks.containsKey(task.getInfo().getId())) {
            throw new IllegalArgumentException("Task with ID already exists: " + task.getInfo().getId());
        }
        availableTasks.put(task.getInfo().getId(), task);
        saveAvailableTasks();
    }

    @Override
    public void updateAvailableTask(AvailableTask task) {
        if (!availableTasks.containsKey(task.getInfo().getId())) {
            throw new IllegalArgumentException("Task not found: " + task.getInfo().getId());
        }
        availableTasks.put(task.getInfo().getId(), task);
        saveAvailableTasks();
    }

    @Override
    public boolean deleteAvailableTask(String taskId) {
        boolean removed = availableTasks.remove(taskId) != null;
        if (removed) {
            saveAvailableTasks();
        }
        return removed;
    }

    @Override
    public AvailableTask findAvailableTaskById(String taskId) {
        return availableTasks.get(taskId);
    }

    @Override
    public List<AvailableTask> findAllAvailableTasks() {
        return new ArrayList<>(availableTasks.values());
    }

    @Override
    public boolean availableTaskNameExists(String name) {
        return availableTasks.values().stream()
                .anyMatch(task -> task.getInfo().getName().equals(name));
    }

    // Today's Task operations

    @Override
    public void addTodaysTask(TodaysTask task) {
        todaysTasks.put(task.getInfo().getId(), task);
        saveTodaysTasks();
    }

    @Override
    public void updateTodaysTask(TodaysTask task) {
        if (!todaysTasks.containsKey(task.getInfo().getId())) {
            throw new IllegalArgumentException("Task not in today's list: " + task.getInfo().getId());
        }
        todaysTasks.put(task.getInfo().getId(), task);
        saveTodaysTasks();
    }

    @Override
    public boolean removeTodaysTask(String taskId) {
        boolean removed = todaysTasks.remove(taskId) != null;
        if (removed) {
            saveTodaysTasks();
        }
        return removed;
    }

    @Override
    public List<TodaysTask> findAllTodaysTasks() {
        return new ArrayList<>(todaysTasks.values());
    }

    @Override
    public List<TodaysTask> findTodaysTasksByStatus(boolean completed) {
        return todaysTasks.values().stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodaysTask> findOverdueTasks() {
        return todaysTasks.values().stream()
                .filter(TodaysTask::isOverdue)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isInTodaysList(String taskId) {
        return todaysTasks.containsKey(taskId);
    }

    // Daily reset operations

    @Override
    public void clearTodaysTasks() {
        todaysTasks.clear();
        saveTodaysTasks();
    }

    @Override
    public void removeOneTimeTasks(List<String> taskIds) {
        taskIds.forEach(availableTasks::remove);
        saveAvailableTasks();
    }

    // Private helper methods

    private void loadData() {
        availableTasks = loadAvailableTasks();
        todaysTasks = loadTodaysTasks();
    }

    private Map<String, AvailableTask> loadAvailableTasks() {
        File file = new File(AVAILABLE_TASKS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, AvailableTask>>(){}.getType();
            Map<String, AvailableTask> tasks = gson.fromJson(reader, type);
            return tasks != null ? tasks : new HashMap<>();
        } catch (IOException e) {
            System.err.println("Error loading available tasks: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private Map<String, TodaysTask> loadTodaysTasks() {
        File file = new File(TODAYS_TASKS_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, TodaysTask>>(){}.getType();
            Map<String, TodaysTask> tasks = gson.fromJson(reader, type);
            return tasks != null ? tasks : new HashMap<>();
        } catch (IOException e) {
            System.err.println("Error loading today's tasks: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveAvailableTasks() {
        try (FileWriter writer = new FileWriter(AVAILABLE_TASKS_FILE)) {
            gson.toJson(availableTasks, writer);
        } catch (IOException e) {
            System.err.println("Error saving available tasks: " + e.getMessage());
        }
    }

    private void saveTodaysTasks() {
        try (FileWriter writer = new FileWriter(TODAYS_TASKS_FILE)) {
            gson.toJson(todaysTasks, writer);
        } catch (IOException e) {
            System.err.println("Error saving today's tasks: " + e.getMessage());
        }
    }
}