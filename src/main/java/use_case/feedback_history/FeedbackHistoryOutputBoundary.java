package use_case.feedback_history;

public interface FeedbackHistoryOutputBoundary {

    /**
     * Presents the feedback history to the user.
     * @param outputData the feedback history to present
     */
    void present(FeedbackHistoryOutputData outputData);
}
