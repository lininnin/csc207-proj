package use_case.feedback_Entry;

import java.time.LocalDate;

public class FeedbackEntryController {
    private final FeedbackEntryInputBoundary interactor;

    public FeedbackEntryController(FeedbackEntryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void onViewPressed(LocalDate date) {
        interactor.viewEntry(new FeedbackEntryRequestModel(date));
    }
}
