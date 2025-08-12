package interface_adapter.feedback_history;

import java.util.List;

import entity.feedback_entry.FeedbackEntry;

public class FeedbackHistoryViewModel {
    private List<FeedbackEntry> entries;

    public void setEntries(List<FeedbackEntry> entries) {
        this.entries = entries;
    }

    public List<FeedbackEntry> getEntries() {
        return entries;
    }
}
