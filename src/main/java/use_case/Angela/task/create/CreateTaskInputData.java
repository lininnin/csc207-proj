package use_case.Angela.task.create;

/**
 * Input data for creating a task.
 */
public class CreateTaskInputData {
    private final String taskName;
    private final String description;
    private final String categoryId;
    private final boolean isOneTime;

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