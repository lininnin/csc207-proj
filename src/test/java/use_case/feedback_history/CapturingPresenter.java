package use_case.feedback_history;

import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.feedback_history.FeedbackHistoryOutputData;

final class CapturingPresenter implements FeedbackHistoryOutputBoundary {
    FeedbackHistoryOutputData last;
    int calls = 0;
    @Override public void present(FeedbackHistoryOutputData out) { last = out; calls++; }
}