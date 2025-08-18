package interface_adapter.feedback_entry;

import java.time.LocalDate;

import use_case.feedback_entry.FeedbackEntryInputBoundary;
import use_case.feedback_entry.FeedbackEntryRequestModel;

public class FeedbackEntryController {
    private final FeedbackEntryInputBoundary interactor;

    public FeedbackEntryController(FeedbackEntryInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Handles view action when users selects to view a feedback history.
     * @param date the date of feedback entry the user wants to view
     */
    public void onViewPressed(LocalDate date) {
        interactor.viewEntry(new FeedbackEntryRequestModel(date));
    }
}
