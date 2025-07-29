package use_case.goal_track;

public interface TrackGoalOutputBoundary {
    void presentSuccess(TrackGoalOutputData outputData);
    void presentError(String error);
}