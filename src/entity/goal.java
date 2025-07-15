import java.time.LocalDateTime;
public class goal extends task {
    private task targetTask;
    private String timePeriod; // "weekly", "monthly"
    private int frequency;
    private int currentProgress;

    public goal(info info, LocalDateTime beginDate, LocalDateTime dueDate,
                task targetTask, String timePeriod, int frequency) {
        super(info, LocalDate.now(), dueDate);
        this.targetTask = targetTask;
        this.timePeriod = timePeriod;
        this.frequency = frequency;
        this.currentProgress = 0;
    }

    public void incrementProgress() {
        currentProgress++;
    }

    public boolean isGoalAchieved() {
        return currentProgress >= frequency;
    }

    public String getGoalStatus() {
        return "Progress: " + currentProgress + "/" + frequency;
    }

    public task getTargetTask() {
        return targetTask;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public String getCategory() {
        return super.getCategory();
    }
}