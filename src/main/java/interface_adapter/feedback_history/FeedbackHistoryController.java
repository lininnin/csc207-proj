package interface_adapter.feedback_history;

import use_case.feedback_history.FeedbackHistoryInteractor;

public class FeedbackHistoryController {
    private final FeedbackHistoryInteractor interactor;

    public FeedbackHistoryController(FeedbackHistoryInteractor interactor) {
        this.interactor = interactor;
    }

    public void requestHistory() {
        interactor.execute();
    }
}
