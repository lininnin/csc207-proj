package use_case.goal;

public class CreateGoalInputData {
    public final String name;
    public final String description;
    public final String category;
    public final String targetTask;
    public final int frequency;
    public final String timePeriod;

    public CreateGoalInputData(String name, String description, String category,
                               String targetTask, int frequency, String timePeriod) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.targetTask = targetTask;
        this.frequency = frequency;
        this.timePeriod = timePeriod;
    }
}