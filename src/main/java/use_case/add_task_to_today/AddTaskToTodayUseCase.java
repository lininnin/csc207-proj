package use_case.add_task_to_today;

import entity.Task;
import entity.DailyLog;
import use_case.repository.TaskRepository;
import use_case.repository.DailyLogRepository;
import java.time.LocalDate;

/**
 * Use case for adding a task from available tasks to today's tasks.
 */
public class AddTaskToTodayUseCase {
    private final TaskRepository taskRepository;
    private final DailyLogRepository dailyLogRepository;
    private final AddTaskToTodayOutputBoundary outputBoundary;

    public AddTaskToTodayUseCase(TaskRepository taskRepository,
                                 DailyLogRepository dailyLogRepository,
                                 AddTaskToTodayOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.dailyLogRepository = dailyLogRepository;
        this.outputBoundary = outputBoundary;
    }

    public void execute(AddTaskToTodayInputData inputData) {
        try {
            // Find the task
            Task task = taskRepository.findById(inputData.getTaskId());
            if (task == null) {
                outputBoundary.presentError("Task not found");
                return;
            }

            // Check if task is within valid date range
            LocalDate today = LocalDate.now();
            LocalDate beginDate = task.getBeginAndDueDates().getBeginDate();
            LocalDate dueDate = task.getBeginAndDueDates().getDueDate();

            if (today.isBefore(beginDate)) {
                outputBoundary.presentError("Task cannot be added before its begin date");
                return;
            }

            if (dueDate != null && today.isAfter(dueDate)) {
                outputBoundary.presentError("Task cannot be added after its due date");
                return;
            }

            // Add to today's tasks
            taskRepository.addToTodaysTasks(task);

            // Update daily log
            DailyLog todayLog = dailyLogRepository.findByDate(today);
            if (todayLog == null) {
                todayLog = new DailyLog(today);
            }
            todayLog.addTask(task);
            dailyLogRepository.save(todayLog);

            // Prepare output
            AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(
                    task.getInfo().getName(),
                    "Task added to today's tasks"
            );

            outputBoundary.presentSuccess(outputData);

        } catch (Exception e) {
            outputBoundary.presentError("Failed to add task: " + e.getMessage());
        }
    }
}

