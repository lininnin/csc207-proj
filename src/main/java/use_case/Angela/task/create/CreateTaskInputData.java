package use_case.Angela.task.create;

/**
 * Input data for the create task use case.
 * Contains all the information needed to create a new task.
 */
public class CreateTaskInputData {
    private final String taskName;
    private final String description;
    private final String categoryId;
    private final boolean isOneTime;

    /**
     * Creates input data for creating a task.
     *
     * @param taskName The name of the task (required, max 20 chars)
     * @param description The task description (optional, max 100 chars)
     * @param categoryId The ID of the category (optional)
     * @param isOneTime Whether this is a one-time task
     */
    public CreateTaskInputData(String taskName, String description,
                               String categoryId, boolean isOneTime) {
        this.taskName = taskName;
        this.description = description;
        this.categoryId = categoryId;
        this.isOneTime = isOneTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public boolean isOneTime() {
        return isOneTime;
    }
}