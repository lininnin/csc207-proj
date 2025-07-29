package interface_adapter.view_model;

import interface_adapter.ViewModel;
import interface_adapter.controller.*;
import entity.Angela.Task.Task;
import entity.info.Info;
import java.util.ArrayList;
import java.util.List;

/**
 * View model for task management.
 * Holds the state for both Available Tasks and Today's Tasks.
 */
public class TaskViewModel extends ViewModel {

    public static final String TITLE_LABEL = "Task Management";
    public static final String CREATE_BUTTON_LABEL = "Create Task";
    public static final String ADD_TO_TODAY_LABEL = "Add to Today";

    private final List<TaskItem> availableTasks;
    private final List<TodayTaskItem> todaysTasks;
    private String errorMessage = "";
    private String successMessage = "";

    // Form fields
    private String newTaskName = "";
    private String newTaskDescription = "";
    private String selectedCategoryId = "";
    private boolean isOneTime = false;

    // Warning dialog state
    private boolean warningDialogVisible = false;
    private String warningDialogTitle = "";
    private String warningDialogMessage = "";
    private String pendingDeleteTaskId = "";

    // Controllers
    private CreateTaskController createController;
    private DeleteTaskController deleteController;
    private EditAvailableTaskController editAvailableController;

    public TaskViewModel() {
        super("task");
        this.availableTasks = new ArrayList<>();
        this.todaysTasks = new ArrayList<>();
    }

    /**
     * Represents an available task item.
     */
    public static class TaskItem {
        private final String id;
        private String name;
        private String description;
        private String category;
        private boolean oneTime;

        public TaskItem(String id, String name, String description, String category, boolean oneTime) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.category = category;
            this.oneTime = oneTime;
        }

        // Getters and setters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public boolean isOneTime() { return oneTime; }

        public void setName(String name) { this.name = name; }
        public void setDescription(String description) { this.description = description; }
        public void setCategory(String category) { this.category = category; }
    }

    /**
     * Represents a today's task item.
     */
    public static class TodayTaskItem extends TaskItem {
        private Task.Priority priority;
        private String dueDate;
        private boolean isComplete;

        public TodayTaskItem(String id, String name, String description, String category,
                             Task.Priority priority, String dueDate, boolean isComplete) {
            super(id, name, description, category, false);
            this.priority = priority;
            this.dueDate = dueDate;
            this.isComplete = isComplete;
        }

        public Task.Priority getPriority() { return priority; }
        public String getDueDate() { return dueDate; }
        public boolean isComplete() { return isComplete; }

        public void setPriority(Task.Priority priority) { this.priority = priority; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        public void setComplete(boolean complete) { this.isComplete = complete; }
    }

    // Task management methods

    public void addAvailableTask(Info taskInfo) {
        availableTasks.add(new TaskItem(
                taskInfo.getId(),
                taskInfo.getName(),
                taskInfo.getDescription(),
                taskInfo.getCategory(),
                false // TODO: Add one-time flag to Info
        ));
    }

    public void removeAvailableTask(String taskId) {
        availableTasks.removeIf(task -> task.getId().equals(taskId));
    }

    public void addTodaysTask(Task task) {
        todaysTasks.add(new TodayTaskItem(
                task.getInfo().getId(),
                task.getInfo().getName(),
                task.getInfo().getDescription(),
                task.getInfo().getCategory(),
                task.getTaskPriority(),
                task.getBeginAndDueDates().getDueDate() != null ?
                        task.getBeginAndDueDates().getDueDate().toString() : "",
                task.isComplete()
        ));
    }

    public void removeTodaysTask(String taskId) {
        todaysTasks.removeIf(task -> task.getId().equals(taskId));
    }

    // Getters and setters

    public List<TaskItem> getAvailableTasks() {
        return new ArrayList<>(availableTasks);
    }

    public List<TodayTaskItem> getTodaysTasks() {
        return new ArrayList<>(todaysTasks);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setError(String error) {
        this.errorMessage = error;
        this.successMessage = "";
    }

    public void clearError() {
        this.errorMessage = "";
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String message) {
        this.successMessage = message;
        this.errorMessage = "";
    }

    // Form field getters/setters

    public String getNewTaskName() {
        return newTaskName;
    }

    public void setNewTaskName(String name) {
        this.newTaskName = name;
    }

    public String getNewTaskDescription() {
        return newTaskDescription;
    }

    public void setNewTaskDescription(String description) {
        this.newTaskDescription = description;
    }

    public String getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(String categoryId) {
        this.selectedCategoryId = categoryId;
    }

    public boolean isOneTime() {
        return isOneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.isOneTime = oneTime;
    }

    // Warning dialog getters/setters

    public boolean isWarningDialogVisible() {
        return warningDialogVisible;
    }

    public void setWarningDialogVisible(boolean visible) {
        this.warningDialogVisible = visible;
    }

    public String getWarningDialogTitle() {
        return warningDialogTitle;
    }

    public void setWarningDialogTitle(String title) {
        this.warningDialogTitle = title;
    }

    public String getWarningDialogMessage() {
        return warningDialogMessage;
    }

    public void setWarningDialogMessage(String message) {
        this.warningDialogMessage = message;
    }

    public String getPendingDeleteTaskId() {
        return pendingDeleteTaskId;
    }

    public void setPendingDeleteTaskId(String taskId) {
        this.pendingDeleteTaskId = taskId;
    }

    // Controller setters

    public void setCreateController(CreateTaskController controller) {
        this.createController = controller;
    }

    public void setDeleteController(DeleteTaskController controller) {
        this.deleteController = controller;
    }

    public void setEditAvailableController(EditAvailableTaskController controller) {
        this.editAvailableController = controller;
    }

    public void clearForm() {
        this.newTaskName = "";
        this.newTaskDescription = "";
        this.selectedCategoryId = "";
        this.isOneTime = false;
    }
}