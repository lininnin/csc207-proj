package interface_adapter.feedback_history;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import entity.feedback_entry.FeedbackEntryInterf;

public final class FeedbackHistoryViewModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private List<FeedbackEntryInterf> entries = List.of();

    public void setEntries(List<FeedbackEntryInterf> newEntries) {
        final List<FeedbackEntryInterf> old = this.entries;
        this.entries = (newEntries == null) ? List.of() : List.copyOf(newEntries);
        pcs.firePropertyChange("entries", old, this.entries);
    }

    public List<FeedbackEntryInterf> getEntries() {
        return entries;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
