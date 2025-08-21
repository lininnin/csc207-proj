package data_access;

import entity.Angela.TodaySoFarSnapshot;
import use_case.Angela.view_history.ViewHistoryDataAccessInterface;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the history data access.
 * Stores historical snapshots in memory.
 */
public class InMemoryHistoryDataAccess implements ViewHistoryDataAccessInterface {
    private final Map<LocalDate, TodaySoFarSnapshot> snapshots = new HashMap<>();
    
    @Override
    public boolean saveSnapshot(TodaySoFarSnapshot snapshot) {
        if (snapshot == null || snapshot.getDate() == null) {
            return false;
        }
        
        snapshots.put(snapshot.getDate(), snapshot);
        return true;
    }
    
    @Override
    public TodaySoFarSnapshot getSnapshot(LocalDate date) {
        return snapshots.get(date);
    }
    
    @Override
    public List<LocalDate> getAvailableDates() {
        return snapshots.keySet().stream()
            .sorted(Comparator.reverseOrder()) // Most recent first
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean hasSnapshot(LocalDate date) {
        return snapshots.containsKey(date);
    }
    
    @Override
    public int cleanupOldSnapshots(int daysToKeep) {
        LocalDate cutoffDate = LocalDate.now().minusDays(daysToKeep);
        List<LocalDate> datesToRemove = snapshots.keySet().stream()
            .filter(date -> date.isBefore(cutoffDate))
            .collect(Collectors.toList());
        
        datesToRemove.forEach(snapshots::remove);
        return datesToRemove.size();
    }
    
    /**
     * Clears all snapshots (useful for testing).
     */
    public void clear() {
        snapshots.clear();
    }
    
    /**
     * Gets the total number of snapshots stored.
     * @return The number of snapshots
     */
    public int size() {
        return snapshots.size();
    }
}