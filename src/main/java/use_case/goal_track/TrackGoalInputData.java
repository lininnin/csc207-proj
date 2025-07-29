package use_case.goal_track;

public class TrackGoalInputData {
    private final String goalName;
    private final boolean increment;

    public TrackGoalInputData(String goalName, boolean increment) {
        this.goalName = goalName;
        this.increment = increment;
    }

    public String getGoalName() {
        return goalName;
    }

    public boolean isIncrement() {
        return increment;
    }
}