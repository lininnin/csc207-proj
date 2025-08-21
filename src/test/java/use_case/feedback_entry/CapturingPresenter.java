package use_case.feedback_entry;

import use_case.feedback_entry.FeedbackEntryOutputBoundary;
import use_case.feedback_entry.FeedbackEntryResponseModel;

/** Presenter that just captures the last response and call count. */
final class CapturingPresenter implements FeedbackEntryOutputBoundary {
    FeedbackEntryResponseModel last;
    int calls = 0;
    @Override public void present(FeedbackEntryResponseModel response) { last = response; calls++; }
}