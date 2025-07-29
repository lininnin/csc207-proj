package interface_adapter.feedback_history;

import entity.Ina.FeedbackEntry;
import java.util.ArrayList;
import java.util.List;

public class FeedbackHistoryViewModel {
    private List<FeedbackEntry> entries = new ArrayList<>();
    private final List<Runnable> listeners = new ArrayList<>();

    public List<FeedbackEntry> getEntries() {
        return new ArrayList<>(entries);
    }
    public void setEntries(List<FeedbackEntry> entries) {
        this.entries = new ArrayList<>(entries);
        listeners.forEach(Runnable::run);
    }
    public void addListener(Runnable listener) {
        listeners.add(listener);
    }
}
