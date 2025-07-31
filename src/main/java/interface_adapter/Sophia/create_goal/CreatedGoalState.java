package interface_adapter.Sophia.create_goal;

public class CreatedGoalState {
    private String name = "";
    private String description = "";
    private String category = "";
    private String targetTask = "";
    private int frequency = 1;
    private String timePeriod = "Weekly";
    private String error = null;

    public CreatedGoalState(CreatedGoalState copy) {
        this.name = copy.name;
        this.description = copy.description;
        this.category = copy.category;
        this.targetTask = copy.targetTask;
        this.frequency = copy.frequency;
        this.timePeriod = copy.timePeriod;
        this.error = copy.error;
    }

    // Default constructor
    public CreatedGoalState() {}

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTargetTask() {
        return targetTask;
    }

    public void setTargetTask(String targetTask) {
        this.targetTask = targetTask;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
