package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;

import java.util.ArrayList;
import java.util.List;

public class TodaysWellnessLogState {

    private final List<WellnessLogEntry> entries;

    public TodaysWellnessLogState() {
        this.entries = new ArrayList<>();
    }

    public void setEntries(List<WellnessLogEntry> newEntries) {
        entries.clear();
        entries.addAll(newEntries);
    }

    public void addEntry(WellnessLogEntry entry) {
        entries.add(entry);
    }

    public List<WellnessLogEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }
}



