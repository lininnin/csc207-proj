package interface_adapter.feedback_history;

import use_case.feedback_history.FeedbackHistoryInputBoundary;

public class FeedbackHistoryController {
    private final FeedbackHistoryInputBoundary interactor;
    public FeedbackHistoryController(FeedbackHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadFeedbackHistory() {
        interactor.loadFeedbackHistory();
    }
}