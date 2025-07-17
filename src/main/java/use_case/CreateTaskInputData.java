package use_case;

import entity.Task;

import java.time.LocalDate; /**
 * Input data for creating a task.
 */
public class CreateTaskInputData {
    private final String name;
    private final String description;
    private final String category;
    private final Task.Priority priority;
    private final LocalDate beginDate;
    private final LocalDate dueDate;

    public CreateTaskInputData(String name, String description, String category,
                               Task.Priority priority, LocalDate beginDate, LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Task.Priority getPriority() { return priority; }
    public LocalDate getBeginDate() { return beginDate; }
    public LocalDate getDueDate() { return dueDate; }
}
