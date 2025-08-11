package use_case.feedback_Entry;

import entity.Ina.FeedbackEntry;


public class FeedbackEntryResponseModel {
    private final FeedbackEntry entry;
    public FeedbackEntryResponseModel(FeedbackEntry entry) { this.entry = entry; }
    public FeedbackEntry getEntry() { return entry; }
}
