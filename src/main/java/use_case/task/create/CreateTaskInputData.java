package use_case.task.create;

/**
 * Input data for creating a new available task.
 * Contains only business data, no UI concerns.
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