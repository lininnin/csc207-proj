package use_case.feedback_history;

import entity.Ina.FeedbackEntry;
import java.util.List;

public class FeedbackHistoryOutputData {
    private final List<FeedbackEntry> entries;
    public FeedbackHistoryOutputData(List<FeedbackEntry> entries) { this.entries = entries; }
    public List<FeedbackEntry> getEntries() { return entries; }
}