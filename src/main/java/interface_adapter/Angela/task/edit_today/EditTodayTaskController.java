package interface_adapter.Angela.task.edit_today;

import entity.Angela.Task.Task;
import use_case.Angela.task.edit_today.EditTodayTaskInputBoundary;
import use_case.Angela.task.edit_today.EditTodayTaskInputData;
import java.time.LocalDate;

/**
 * Controller for the edit today task use case.
 * Converts user input into use case input data and executes the use case.
 */
public class EditTodayTaskController {
    private final EditTodayTaskInputBoundary editTodayTaskInteractor;

    /**
     * Constructs the controller with its use case interactor.
     * 
     * @param editTodayTaskInteractor The use case interactor
     */
    public EditTodayTaskController(EditTodayTaskInputBoundary editTodayTaskInteractor) {
        this.editTodayTaskInteractor = editTodayTaskInteractor;
    }

    /**
     * Executes the edit today task use case.
     * 
     * @param taskId The ID of the task to edit
     * @param priority The new priority (can be null)
     * @param dueDate The new due date (can be null)
     */
    public void execute(String taskId, Task.Priority priority, LocalDate dueDate) {
        System.out.println("DEBUG: EditTodayTaskController.execute - taskId: " + taskId + 
                          ", priority: " + priority + ", dueDate: " + dueDate);
        
        EditTodayTaskInputData inputData = new EditTodayTaskInputData(taskId, priority, dueDate);
        editTodayTaskInteractor.execute(inputData);
    }
}