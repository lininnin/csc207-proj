package entity;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a wellness log for a specific day.
 * It holds a list of WellnessLogEntry objects recorded on that date,
 * each of which captures the user's mood, energy, stress, etc.
 */
public class DailyWellnessLog {

    private final String id;
    private final LocalDate date;
    private final List<WellnessLogEntry> entries;

    /**
     * Constructs a new DailyWellnessLog for the specified date.
     *
     * @param date The date this wellness log corresponds to
     */
    public DailyWellnessLog(LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.entries = new ArrayList<>();
    }

    /**
     * Adds a wellness log entry to the list for this day.
     * If the entry is null or already exists, it is not added.
     *
     * @param entry The WellnessLogEntry to add
     */
    public void addEntry(WellnessLogEntry entry) {
        if (entry != null && !entries.contains(entry)) {
            entries.add(entry);
        }
    }

    /**
     * Removes a wellness log entry from the list by its ID.
     * If the ID is null or no matching entry is found, nothing is removed.
     *
     * @param id The ID of the WellnessLogEntry to remove
     */
    public void removeEntry(String id) {
        if (id == null) return;

        entries.removeIf(entry -> id.equals(entry.getId()));
    }


    // ------------------ Getters ------------------

    /**
     * @return The unique identifier of this daily wellness log
     */
    public String getId() {
        return id;
    }

    /**
     * @return The date associated with this log
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return A list of all wellness log entries for this date
     */
    public List<WellnessLogEntry> getEntries() {
        return new ArrayList<>(entries); // defensive copy
    }
}

