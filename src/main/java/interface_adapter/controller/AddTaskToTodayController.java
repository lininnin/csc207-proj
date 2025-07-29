package interface_adapter.controller;

import use_case.Angela.task.add_to_today.AddTaskToTodayInputBoundary;
import use_case.Angela.task.add_to_today.AddTaskToTodayInputData;
import entity.Angela.Task.Task;
import java.time.LocalDate;

/**
 * Controller for adding tasks to today.
 */
public class AddTaskToTodayController {
    private final AddTaskToTodayInputBoundary addTaskToTodayUseCase;

    public AddTaskToTodayController(AddTaskToTodayInputBoundary addTaskToTodayUseCase) {
        this.addTaskToTodayUseCase = addTaskToTodayUseCase;
    }

    /**
     * Adds a task to today with the given parameters.
     *
     * @param taskId The ID of the task to add
     * @param priority The priority level
     * @param dueDate The optional due date
     */
    public void addTaskToToday(String taskId, Task.Priority priority, LocalDate dueDate) {
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId, priority, dueDate);
        addTaskToTodayUseCase.execute(inputData);
    }
}