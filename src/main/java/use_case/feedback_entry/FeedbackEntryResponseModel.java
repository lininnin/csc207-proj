package use_case.feedback_entry;

import entity.feedback_entry.FeedbackEntryInterf;

public class FeedbackEntryResponseModel {
    private final FeedbackEntryInterf entry;

    public FeedbackEntryResponseModel(FeedbackEntryInterf entry) {
        this.entry = entry;
    }

    public FeedbackEntryInterf getEntry() {
        return entry;
    }
}
