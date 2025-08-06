package use_case.Angela.task.edit_available;

/**
 * Input data for editing an available task.
 */
public class EditAvailableTaskInputData {
    private final String taskId;
    private final String name;
    private final String description;
    private final String categoryId;
    private final boolean isOneTime;

    public EditAvailableTaskInputData(String taskId, String name,
                                      String description, String categoryId,
                                      boolean isOneTime) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.isOneTime = isOneTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
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