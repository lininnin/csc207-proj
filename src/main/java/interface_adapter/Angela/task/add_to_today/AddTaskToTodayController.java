package interface_adapter.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import use_case.Angela.task.add_to_today.AddTaskToTodayInputBoundary;
import use_case.Angela.task.add_to_today.AddTaskToTodayInputData;

import java.time.LocalDate;

/**
 * Controller for adding a task to today's list.
 * Converts UI input to use case input data.
 */
public class AddTaskToTodayController {
    private final AddTaskToTodayInputBoundary inputBoundary;

    public AddTaskToTodayController(AddTaskToTodayInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Executes the add to today use case.
     *
     * @param taskId the ID of the task to add
     * @param priority the priority (can be null)
     * @param dueDate the due date (can be null)
     */
    public void execute(String taskId, Task.Priority priority, LocalDate dueDate) {
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);
        inputBoundary.execute(inputData);
    }
}