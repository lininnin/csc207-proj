package interface_adapter.controller;

import use_case.Angela.task.add_task_to_today.AddTaskToTodayInputBoundary;
import use_case.Angela.task.add_task_to_today.AddTaskToTodayInputData;

import java.time.LocalDate;

/**
 * Controller for adding tasks to today's task list.
 * This is where priority and dates are set per design.
 */
public class AddTaskToTodayController {
    private final AddTaskToTodayInputBoundary addTaskToTodayUseCase;

    public AddTaskToTodayController(AddTaskToTodayInputBoundary addTaskToTodayUseCase) {
        this.addTaskToTodayUseCase = addTaskToTodayUseCase;
    }

    /**
     * Adds a task to today's task list with priority and dates.
     *
     * @param taskId The ID of the task to add
     * @param priority The priority level (HIGH, MEDIUM, LOW)
     * @param beginDate The begin date (usually today)
     * @param dueDate The optional due date
     */
    public void addTaskToToday(String taskId, String priority, LocalDate beginDate, LocalDate dueDate) {
        AddTaskToTodayInputData inputData = new AddTaskToTodayInputData(
                taskId,
                priority,
                beginDate,
                dueDate
        );
        addTaskToTodayUseCase.execute(inputData);
    }
}