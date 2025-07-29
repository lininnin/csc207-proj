package use_case.goal_track;

public class TrackGoalOutputData {
    private final String goalName;
    private final int currentProgress;
    private final int frequency;
    private final boolean isCompleted;

    public TrackGoalOutputData(String goalName, int currentProgress, int frequency, boolean isCompleted) {
        this.goalName = goalName;
        this.currentProgress = currentProgress;
        this.frequency = frequency;
        this.isCompleted = isCompleted;
    }

    public String getGoalName() {
        return goalName;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public int getFrequency() {
        return frequency;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}