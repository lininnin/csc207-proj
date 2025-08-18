package use_case.feedback_history;

import java.util.List;

import entity.feedback_entry.FeedbackEntryInterf;

public class FeedbackHistoryOutputData {
    private final List<FeedbackEntryInterf> entries;

    public FeedbackHistoryOutputData(List<FeedbackEntryInterf> entries) {
        this.entries = entries;
    }

    public List<FeedbackEntryInterf> getEntries() {
        return entries;
    }
}
