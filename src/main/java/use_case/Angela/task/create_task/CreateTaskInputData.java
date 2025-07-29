package use_case.Angela.task.create_task;

/**
 * Input data for creating a task.
 * Contains only the fields available at creation time per design.
 */
public class CreateTaskInputData {
    private final String name;
    private final String description;
    private final String category;
    private final boolean oneTime;

    public CreateTaskInputData(String name, String description, String category, boolean oneTime) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.oneTime = oneTime;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isOneTime() {
        return oneTime;
    }
}