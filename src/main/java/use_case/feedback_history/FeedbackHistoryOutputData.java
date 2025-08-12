package use_case.feedback_history;

import java.util.List;

import entity.feedback_entry.FeedbackEntry;

public class FeedbackHistoryOutputData {
    private final List<FeedbackEntry> entries;

    public FeedbackHistoryOutputData(List<FeedbackEntry> entries) {
        this.entries = entries;
    }

    public List<FeedbackEntry> getEntries() {
        return entries;
    }
}
