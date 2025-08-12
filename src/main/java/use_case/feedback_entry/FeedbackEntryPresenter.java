package use_case.feedback_entry;

import interface_adapter.feedback_entry.FeedbackEntryView;

public class FeedbackEntryPresenter implements FeedbackEntryOutputBoundary {
    private final FeedbackEntryView view;

    public FeedbackEntryPresenter(FeedbackEntryView view) {
        this.view = view;
    }

    @Override
    public void present(FeedbackEntryResponseModel response) {
        view.displayEntry(response.getEntry());
    }
}
