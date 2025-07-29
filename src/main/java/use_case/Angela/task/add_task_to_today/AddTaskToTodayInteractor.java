package use_case.Angela.task.add_task_to_today;

import entity.Angela.Task.Task;
import entity.Angela.DailyLog;
import use_case.repository.TaskRepository;
import use_case.repository.DailyLogRepository;

import java.time.LocalDate;

/**
 * Use case for adding a task from available tasks to today's tasks.
 * This is where priority and dates are set per design requirements.
 */
public class AddTaskToTodayInteractor implements AddTaskToTodayInputBoundary {
    private final TaskRepository taskRepository;
    private final DailyLogRepository dailyLogRepository;
    private final AddTaskToTodayOutputBoundary outputBoundary;

    public AddTaskToTodayInteractor(TaskRepository taskRepository,
                                    DailyLogRepository dailyLogRepository,
                                    AddTaskToTodayOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.dailyLogRepository = dailyLogRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(AddTaskToTodayInputData inputData) {
        try {
            // Find the task
            Task task = taskRepository.findById(inputData.getTaskId());
            if (task == null) {
                outputBoundary.presentError("Task not found");
                return;
            }

            // Validate dates
            String dateError = validateDates(inputData.getBeginDate(), inputData.getDueDate());
            if (dateError != null) {
                outputBoundary.presentError(dateError);
                return;
            }

            // Check if task is already in today
            if (isTaskAlreadyInToday(task)) {
                outputBoundary.presentError("Task is already in today's tasks");
                return;
            }

            // Set priority (converting string to enum)
            Task.Priority priority = parsePriority(inputData.getPriority());
            task.setPriority(priority);

            // Update task dates
            task.getBeginAndDueDates().setBeginDate(inputData.getBeginDate());
            if (inputData.getDueDate() != null) {
                task.getBeginAndDueDates().setDueDate(inputData.getDueDate());
            }

            // Add to today's tasks
            taskRepository.addToTodaysTasks(task);
            taskRepository.update(task);

            // Update daily log
            DailyLog todayLog = dailyLogRepository.findByDate(LocalDate.now());
            if (todayLog == null) {
                todayLog = new DailyLog(LocalDate.now());
            }
            todayLog.addTask(task);
            dailyLogRepository.save(todayLog);

            // Prepare output
            AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(
                    task.getInfo().getName(),
                    String.format("Task '%s' added to today's tasks with %s priority",
                            task.getInfo().getName(),
                            priority.name())
            );

            outputBoundary.presentSuccess(outputData);

        } catch (Exception e) {
            outputBoundary.presentError("Failed to add task: " + e.getMessage());
        }
    }

    /**
     * Validates the begin and due dates.
     *
     * @param beginDate The begin date
     * @param dueDate The due date (optional)
     * @return Error message if invalid, null if valid
     */
    private String validateDates(LocalDate beginDate, LocalDate dueDate) {
        if (beginDate == null) {
            return "Begin date is required";
        }

        // Begin date cannot be in the past
        if (beginDate.isBefore(LocalDate.now())) {
            return "Begin date cannot be in the past";
        }

        // Due date must be after begin date
        if (dueDate != null && dueDate.isBefore(beginDate)) {
            return "Due date must be after begin date";
        }

        return null;
    }

    /**
     * Checks if the task is already in today's tasks.
     *
     * @param task The task to check
     * @return true if already in today, false otherwise
     */
    private boolean isTaskAlreadyInToday(Task task) {
        return taskRepository.findTodaysTasks().stream()
                .anyMatch(t -> t.getInfo().getId().equals(task.getInfo().getId()));
    }

    /**
     * Parses priority string to enum.
     *
     * @param priorityStr The priority string
     * @return The priority enum value
     */
    private Task.Priority parsePriority(String priorityStr) {
        if (priorityStr == null || priorityStr.isEmpty()) {
            return Task.Priority.MEDIUM; // Default
        }

        try {
            return Task.Priority.valueOf(priorityStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Task.Priority.MEDIUM; // Default on error
        }
    }
}