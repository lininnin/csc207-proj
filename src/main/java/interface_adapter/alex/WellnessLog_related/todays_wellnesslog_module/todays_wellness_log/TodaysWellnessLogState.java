package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log;

import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;

import java.util.ArrayList;
import java.util.List;

public class TodaysWellnessLogState {

    private final List<WellnessLogEntryInterf> entries;

    public TodaysWellnessLogState() {
        this.entries = new ArrayList<>();
    }

    public void setEntries(List<WellnessLogEntryInterf> newEntries) {
        entries.clear();
        entries.addAll(newEntries);
    }

    public void addEntry(WellnessLogEntryInterf entry) {
        entries.add(entry);
    }

    public List<WellnessLogEntryInterf> getEntries() {
        return new ArrayList<>(entries);
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }
}



