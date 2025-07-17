package interface_adapter.controller;

import use_case.add_task_to_today.AddTaskToTodayUseCase;
import use_case.add_task_to_today.AddTaskToTodayInputData;

/**
 * Controller for adding tasks to today's task list.
 */
public class AddTaskToTodayController {
    private final AddTaskToTodayUseCase addTaskToTodayUseCase;

    public AddTaskToTodayController(AddTaskToTodayUseCase addTaskToTodayUseCase) {
        this.addTaskToTodayUseCase = addTaskToTodayUseCase;
    }

    /**
     * Adds a task to today's task list.
     *
     * @param taskId The ID of the task to add
     */
    public void addTaskToToday(String taskId) {
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(taskId);
        addTaskToTodayUseCase.execute(inputData);
    }
}
