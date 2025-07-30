package interface_adapter.feedback_history;

import entity.Ina.FeedbackEntry;
import java.util.List;

public class FeedbackHistoryViewModel {
    private List<FeedbackEntry> entries;
    public void setEntries(List<FeedbackEntry> entries) { this.entries = entries; }
    public List<FeedbackEntry> getEntries() { return entries; }
}