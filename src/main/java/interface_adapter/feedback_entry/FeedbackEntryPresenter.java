package interface_adapter.feedback_entry;

import javax.swing.SwingUtilities;

import entity.feedback_entry.FeedbackEntryInterf;
import use_case.feedback_entry.FeedbackEntryOutputBoundary;
import use_case.feedback_entry.FeedbackEntryResponseModel;

public class FeedbackEntryPresenter implements FeedbackEntryOutputBoundary {

    private final FeedbackEntryView view;

    public FeedbackEntryPresenter(FeedbackEntryView view) {
        this.view = view;
    }

    @Override
    public void present(FeedbackEntryResponseModel response) {
        // Adapt ResponseModel → View (and ensure EDT)
        final FeedbackEntryInterf entry = response.getEntry();
        SwingUtilities.invokeLater(() -> view.displayEntry(entry));
    }
}
