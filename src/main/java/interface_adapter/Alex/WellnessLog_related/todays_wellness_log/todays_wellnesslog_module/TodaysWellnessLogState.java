package interface_adapter.Alex.WellnessLog_related.todays_wellness_log.todays_wellnesslog_module;

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



