package use_case.feedback_entry;

import entity.feedback_entry.FeedbackEntry;

public class FeedbackEntryResponseModel {
    private final FeedbackEntry entry;

    public FeedbackEntryResponseModel(FeedbackEntry entry) {
        this.entry = entry;
    }

    public FeedbackEntry getEntry() {
        return entry;
    }
}
