package interface_adapter.controller;

import use_case.create_task.CreateTaskUseCase;
import use_case.create_task.CreateTaskInputData;
import entity.Task;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller for creating tasks.
 * Handles user input and delegates to the use case.
 */
public class CreateTaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CreateTaskController(CreateTaskUseCase createTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
    }

    /**
     * Creates a new task from user input.
     *
     * @param name Task name
     * @param description Task description
     * @param category Task category
     * @param priorityString Priority as string ("HIGH", "MEDIUM", "LOW")
     * @param beginDateString Begin date as string (yyyy-MM-dd)
     * @param dueDateString Due date as string (yyyy-MM-dd), can be empty
     */
    public void createTask(String name, String description, String category,
                           String priorityString, String beginDateString, String dueDateString) {
        try {
            // Parse priority
            Task.Priority priority = Task.Priority.MEDIUM; // default
            if (priorityString != null && !priorityString.isEmpty()) {
                priority = Task.Priority.valueOf(priorityString.toUpperCase());
            }

            // Parse dates
            LocalDate beginDate = LocalDate.parse(beginDateString, DATE_FORMATTER);
            LocalDate dueDate = null;
            if (dueDateString != null && !dueDateString.isEmpty()) {
                dueDate = LocalDate.parse(dueDateString, DATE_FORMATTER);
            }

            // Create input data
            CreateTaskInputData inputData = new CreateTaskInputData(
                    name, description, category, priority, beginDate, dueDate
            );

            // Execute use case
            createTaskUseCase.execute(inputData);

        } catch (Exception e) {
            // Handle parsing errors
            throw new IllegalArgumentException("Invalid input: " + e.getMessage());
        }
    }
}