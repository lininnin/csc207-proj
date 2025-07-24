package use_case.task.add_to_today;

import entity.AvailableTask;
import entity.TodaysTask;
import entity.BeginAndDueDates;
import use_case.task.TaskRepository;
import java.time.LocalDate;

/**
 * Interactor for adding available tasks to today's list.
 */
public class AddTaskToTodayInteractor implements AddTaskToTodayInputBoundary {
    private final TaskRepository taskRepository;
    private final AddTaskToTodayOutputBoundary outputBoundary;

    public AddTaskToTodayInteractor(TaskRepository taskRepository,
                                    AddTaskToTodayOutputBoundary outputBoundary) {
        this.taskRepository = taskRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void addTaskToToday(AddTaskToTodayInputData inputData) {
        try {
            // Find the available task
            AvailableTask availableTask = taskRepository.findAvailableTaskById(inputData.getTaskId());
            if (availableTask == null) {
                outputBoundary.presentError("Task not found in available tasks");
                return;
            }

            // Check if already in today's list
            if (taskRepository.isInTodaysList(inputData.getTaskId())) {
                outputBoundary.presentError("Task is already in today's list");
                return;
            }

            // Validate due date
            if (inputData.getDueDate() != null && inputData.getDueDate().isBefore(LocalDate.now())) {
                outputBoundary.presentError("Due date cannot be in the past");
                return;
            }

            // Create today's task
            BeginAndDueDates dates = BeginAndDueDates.startingToday(inputData.getDueDate());
            TodaysTask todaysTask = new TodaysTask(availableTask, inputData.getPriority(), dates);

            // Add to repository
            taskRepository.addTodaysTask(todaysTask);

            // Present success
            AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(
                    inputData.getTaskId(),
                    availableTask.getInfo().getName(),
                    true
            );
            outputBoundary.presentSuccess(outputData);

        } catch (Exception e) {
            outputBoundary.presentError("Failed to add task to today: " + e.getMessage());
        }
    }
}