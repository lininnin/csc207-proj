//package interface_adapter.view_model;
//
//import interface_adapter.ViewModel;
//import interface_adapter.controller.*;
//import entity.Angela.Task.Task;
//import entity.info.Info;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * View model for task management.
// * Holds the state for both Available Tasks and Today's Tasks.
// */
//public class TaskViewModel extends ViewModel<TaskViewModel.TaskState> {
//
//    public static final String TITLE_LABEL = "Task Management";
//    public static final String CREATE_BUTTON_LABEL = "Create Task";
//    public static final String ADD_TO_TODAY_LABEL = "Add to Today";
//
//    // Controllers
//    private CreateTaskController createController;
//    private DeleteTaskController deleteController;
//    private EditAvailableTaskController editAvailableController;
//
//    public TaskViewModel() {
//        super("task");
//        setState(new TaskState());
//    }
//
//    /**
//     * State class for task view model.
//     */
//    public static class TaskState {
//        private final List<TaskItem> availableTasks = new ArrayList<>();
//        private final List<TodayTaskItem> todaysTasks = new ArrayList<>();
//        private String errorMessage = "";
//        private String successMessage = "";
//
//        // Form fields
//        private String newTaskName = "";
//        private String newTaskDescription = "";
//        private String selectedCategoryId = "";
//        private boolean isOneTime = false;
//
//        // Warning dialog state
//        private boolean warningDialogVisible = false;
//        private String warningDialogTitle = "";
//        private String warningDialogMessage = "";
//        private String pendingDeleteTaskId = "";
//
//        // Getters and setters
//        public List<TaskItem> getAvailableTasks() {
//            return new ArrayList<>(availableTasks);
//        }
//
//        public List<TodayTaskItem> getTodaysTasks() {
//            return new ArrayList<>(todaysTasks);
//        }
//
//        public String getErrorMessage() {
//            return errorMessage;
//        }
//
//        public void setErrorMessage(String errorMessage) {
//            this.errorMessage = errorMessage;
//        }
//
//        public String getSuccessMessage() {
//            return successMessage;
//        }
//
//        public void setSuccessMessage(String successMessage) {
//            this.successMessage = successMessage;
//        }
//
//        public String getNewTaskName() {
//            return newTaskName;
//        }
//
//        public void setNewTaskName(String newTaskName) {
//            this.newTaskName = newTaskName;
//        }
//
//        public String getNewTaskDescription() {
//            return newTaskDescription;
//        }
//
//        public void setNewTaskDescription(String newTaskDescription) {
//            this.newTaskDescription = newTaskDescription;
//        }
//
//        public String getSelectedCategoryId() {
//            return selectedCategoryId;
//        }
//
//        public void setSelectedCategoryId(String selectedCategoryId) {
//            this.selectedCategoryId = selectedCategoryId;
//        }
//
//        public boolean isOneTime() {
//            return isOneTime;
//        }
//
//        public void setOneTime(boolean oneTime) {
//            this.isOneTime = oneTime;
//        }
//
//        public boolean isWarningDialogVisible() {
//            return warningDialogVisible;
//        }
//
//        public void setWarningDialogVisible(boolean warningDialogVisible) {
//            this.warningDialogVisible = warningDialogVisible;
//        }
//
//        public String getWarningDialogTitle() {
//            return warningDialogTitle;
//        }
//
//        public void setWarningDialogTitle(String warningDialogTitle) {
//            this.warningDialogTitle = warningDialogTitle;
//        }
//
//        public String getWarningDialogMessage() {
//            return warningDialogMessage;
//        }
//
//        public void setWarningDialogMessage(String warningDialogMessage) {
//            this.warningDialogMessage = warningDialogMessage;
//        }
//
//        public String getPendingDeleteTaskId() {
//            return pendingDeleteTaskId;
//        }
//
//        public void setPendingDeleteTaskId(String pendingDeleteTaskId) {
//            this.pendingDeleteTaskId = pendingDeleteTaskId;
//        }
//    }
//
//    /**
//     * Represents an available task item.
//     */
//    public static class TaskItem {
//        private final String id;
//        private String name;
//        private String description;
//        private String category;
//        private boolean oneTime;
//
//        public TaskItem(String id, String name, String description, String category, boolean oneTime) {
//            this.id = id;
//            this.name = name;
//            this.description = description;
//            this.category = category;
//            this.oneTime = oneTime;
//        }
//
//        // Getters and setters
//        public String getId() { return id; }
//        public String getName() { return name; }
//        public String getDescription() { return description; }
//        public String getCategory() { return category; }
//        public boolean isOneTime() { return oneTime; }
//
//        public void setName(String name) { this.name = name; }
//        public void setDescription(String description) { this.description = description; }
//        public void setCategory(String category) { this.category = category; }
//    }
//
//    /**
//     * Represents a today's task item.
//     */
//    public static class TodayTaskItem extends TaskItem {
//        private Task.Priority priority;
//        private String dueDate;
//        private boolean isComplete;
//
//        public TodayTaskItem(String id, String name, String description, String category,
//                             Task.Priority priority, String dueDate, boolean isComplete) {
//            super(id, name, description, category, false);
//            this.priority = priority;
//            this.dueDate = dueDate;
//            this.isComplete = isComplete;
//        }
//
//        public Task.Priority getPriority() { return priority; }
//        public String getDueDate() { return dueDate; }
//        public boolean isComplete() { return isComplete; }
//
//        public void setPriority(Task.Priority priority) { this.priority = priority; }
//        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
//        public void setComplete(boolean complete) { this.isComplete = complete; }
//    }
//
//    // Convenience methods that modify state
//
//    public void addAvailableTask(Info taskInfo) {
//        TaskState state = getState();
//        state.availableTasks.add(new TaskItem(
//                taskInfo.getId(),
//                taskInfo.getName(),
//                taskInfo.getDescription(),
//                taskInfo.getCategory(),
//                false // TODO: Add one-time flag to Info
//        ));
//        firePropertyChanged();
//    }
//
//    public void removeAvailableTask(String taskId) {
//        TaskState state = getState();
//        state.availableTasks.removeIf(task -> task.getId().equals(taskId));
//        firePropertyChanged();
//    }
//
//    public void addTodaysTask(Task task) {
//        TaskState state = getState();
//        state.todaysTasks.add(new TodayTaskItem(
//                task.getInfo().getId(),
//                task.getInfo().getName(),
//                task.getInfo().getDescription(),
//                task.getInfo().getCategory(),
//                task.getTaskPriority(),
//                task.getBeginAndDueDates().getDueDate() != null ?
//                        task.getBeginAndDueDates().getDueDate().toString() : "",
//                task.isComplete()
//        ));
//        firePropertyChanged();
//    }
//
//    public void removeTodaysTask(String taskId) {
//        TaskState state = getState();
//        state.todaysTasks.removeIf(task -> task.getId().equals(taskId));
//        firePropertyChanged();
//    }
//
//    // Convenience getters for common operations
//
//    public List<TaskItem> getAvailableTasks() {
//        return getState().getAvailableTasks();
//    }
//
//    public List<TodayTaskItem> getTodaysTasks() {
//        return getState().getTodaysTasks();
//    }
//
//    public void setError(String error) {
//        TaskState state = getState();
//        state.setErrorMessage(error);
//        state.setSuccessMessage("");
//        firePropertyChanged();
//    }
//
//    public void clearError() {
//        getState().setErrorMessage("");
//    }
//
//    public void setSuccessMessage(String message) {
//        TaskState state = getState();
//        state.setSuccessMessage(message);
//        state.setErrorMessage("");
//        firePropertyChanged();
//    }
//
//    // Form field convenience methods
//
//    public void setNewTaskName(String name) {
//        getState().setNewTaskName(name);
//    }
//
//    public void setNewTaskDescription(String description) {
//        getState().setNewTaskDescription(description);
//    }
//
//    public void setSelectedCategoryId(String categoryId) {
//        getState().setSelectedCategoryId(categoryId);
//    }
//
//    public void setOneTime(boolean oneTime) {
//        getState().setOneTime(oneTime);
//    }
//
//    // Warning dialog convenience methods
//
//    public void setWarningDialogVisible(boolean visible) {
//        getState().setWarningDialogVisible(visible);
//    }
//
//    public void setWarningDialogTitle(String title) {
//        getState().setWarningDialogTitle(title);
//    }
//
//    public void setWarningDialogMessage(String message) {
//        getState().setWarningDialogMessage(message);
//    }
//
//    public void setPendingDeleteTaskId(String taskId) {
//        getState().setPendingDeleteTaskId(taskId);
//    }
//
//    public void clearForm() {
//        TaskState state = getState();
//        state.setNewTaskName("");
//        state.setNewTaskDescription("");
//        state.setSelectedCategoryId("");
//        state.setOneTime(false);
//    }
//
//    // Controller setters
//
//    public void setCreateController(CreateTaskController controller) {
//        this.createController = controller;
//    }
//
//    public void setDeleteController(DeleteTaskController controller) {
//        this.deleteController = controller;
//    }
//
//    public void setEditAvailableController(EditAvailableTaskController controller) {
//        this.editAvailableController = controller;
//    }
//}