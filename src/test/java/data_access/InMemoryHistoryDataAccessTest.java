package data_access;

import entity.Angela.TodaySoFarSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test for InMemoryHistoryDataAccess.
 * Tests snapshot storage, retrieval, and history management functionality.
 */
class InMemoryHistoryDataAccessTest {

    @Mock
    private TodaySoFarSnapshot mockSnapshot1;
    
    @Mock
    private TodaySoFarSnapshot mockSnapshot2;
    
    @Mock
    private TodaySoFarSnapshot mockSnapshot3;

    private InMemoryHistoryDataAccess dataAccess;
    private LocalDate today;
    private LocalDate yesterday;
    private LocalDate weekAgo;
    private LocalDate monthAgo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataAccess = new InMemoryHistoryDataAccess();
        
        today = LocalDate.now();
        yesterday = today.minusDays(1);
        weekAgo = today.minusDays(7);
        monthAgo = today.minusDays(30);
        
        setupMockSnapshots();
    }

    private void setupMockSnapshots() {
        when(mockSnapshot1.getDate()).thenReturn(today);
        when(mockSnapshot2.getDate()).thenReturn(yesterday);
        when(mockSnapshot3.getDate()).thenReturn(weekAgo);
    }

    @Test
    void testEmptyInitialization() {
        assertEquals(0, dataAccess.size());
        assertTrue(dataAccess.getAvailableDates().isEmpty());
        assertNull(dataAccess.getSnapshot(today));
        assertFalse(dataAccess.hasSnapshot(today));
    }

    @Test
    void testSaveSnapshotSuccess() {
        boolean result = dataAccess.saveSnapshot(mockSnapshot1);
        
        assertTrue(result);
        assertEquals(1, dataAccess.size());
        assertEquals(mockSnapshot1, dataAccess.getSnapshot(today));
        assertTrue(dataAccess.hasSnapshot(today));
    }

    @Test
    void testSaveNullSnapshot() {
        boolean result = dataAccess.saveSnapshot(null);
        
        assertFalse(result);
        assertEquals(0, dataAccess.size());
    }

    @Test
    void testSaveSnapshotWithNullDate() {
        when(mockSnapshot1.getDate()).thenReturn(null);
        
        boolean result = dataAccess.saveSnapshot(mockSnapshot1);
        
        assertFalse(result);
        assertEquals(0, dataAccess.size());
    }

    @Test
    void testSaveMultipleSnapshots() {
        dataAccess.saveSnapshot(mockSnapshot1);
        dataAccess.saveSnapshot(mockSnapshot2);
        dataAccess.saveSnapshot(mockSnapshot3);
        
        assertEquals(3, dataAccess.size());
        assertEquals(mockSnapshot1, dataAccess.getSnapshot(today));
        assertEquals(mockSnapshot2, dataAccess.getSnapshot(yesterday));
        assertEquals(mockSnapshot3, dataAccess.getSnapshot(weekAgo));
    }

    @Test
    void testOverwriteSnapshotForSameDate() {
        // Save initial snapshot
        dataAccess.saveSnapshot(mockSnapshot1);
        assertEquals(mockSnapshot1, dataAccess.getSnapshot(today));
        
        // Create a new snapshot for the same date
        TodaySoFarSnapshot newSnapshot = mock(TodaySoFarSnapshot.class);
        when(newSnapshot.getDate()).thenReturn(today);
        
        // Save new snapshot - should overwrite
        boolean result = dataAccess.saveSnapshot(newSnapshot);
        
        assertTrue(result);
        assertEquals(1, dataAccess.size()); // Size should remain 1
        assertEquals(newSnapshot, dataAccess.getSnapshot(today)); // Should return new snapshot
    }

    @Test
    void testGetSnapshotNonExistent() {
        assertNull(dataAccess.getSnapshot(today));
        assertNull(dataAccess.getSnapshot(null));
    }

    @Test
    void testHasSnapshotTrue() {
        dataAccess.saveSnapshot(mockSnapshot1);
        
        assertTrue(dataAccess.hasSnapshot(today));
        assertFalse(dataAccess.hasSnapshot(yesterday));
    }

    @Test
    void testHasSnapshotFalse() {
        assertFalse(dataAccess.hasSnapshot(today));
        assertFalse(dataAccess.hasSnapshot(null));
    }

    @Test
    void testGetAvailableDatesEmpty() {
        List<LocalDate> dates = dataAccess.getAvailableDates();
        
        assertTrue(dates.isEmpty());
    }

    @Test
    void testGetAvailableDatesSortedDescending() {
        // Add snapshots in random order
        dataAccess.saveSnapshot(mockSnapshot3); // Week ago
        dataAccess.saveSnapshot(mockSnapshot1); // Today
        dataAccess.saveSnapshot(mockSnapshot2); // Yesterday
        
        List<LocalDate> dates = dataAccess.getAvailableDates();
        
        assertEquals(3, dates.size());
        assertEquals(today, dates.get(0));        // Most recent first
        assertEquals(yesterday, dates.get(1));
        assertEquals(weekAgo, dates.get(2));      // Oldest last
    }

    @Test
    void testGetAvailableDatesSingleSnapshot() {
        dataAccess.saveSnapshot(mockSnapshot1);
        
        List<LocalDate> dates = dataAccess.getAvailableDates();
        
        assertEquals(1, dates.size());
        assertEquals(today, dates.get(0));
    }

    @Test
    void testCleanupOldSnapshotsNoneToRemove() {
        // Add recent snapshots within retention period
        dataAccess.saveSnapshot(mockSnapshot1); // Today
        dataAccess.saveSnapshot(mockSnapshot2); // Yesterday
        
        int removed = dataAccess.cleanupOldSnapshots(7); // Keep 7 days
        
        assertEquals(0, removed);
        assertEquals(2, dataAccess.size());
    }

    @Test
    void testCleanupOldSnapshotsRemovesSome() {
        // Add snapshots spanning different time periods
        dataAccess.saveSnapshot(mockSnapshot1); // Today
        dataAccess.saveSnapshot(mockSnapshot2); // Yesterday
        dataAccess.saveSnapshot(mockSnapshot3); // Week ago
        
        TodaySoFarSnapshot oldSnapshot = mock(TodaySoFarSnapshot.class);
        when(oldSnapshot.getDate()).thenReturn(monthAgo);
        dataAccess.saveSnapshot(oldSnapshot); // Month ago
        
        assertEquals(4, dataAccess.size());
        
        // Keep only 10 days - should remove month-ago snapshot
        int removed = dataAccess.cleanupOldSnapshots(10);
        
        assertEquals(1, removed);
        assertEquals(3, dataAccess.size());
        assertNull(dataAccess.getSnapshot(monthAgo));
        assertNotNull(dataAccess.getSnapshot(today));
        assertNotNull(dataAccess.getSnapshot(yesterday));
        assertNotNull(dataAccess.getSnapshot(weekAgo));
    }

    @Test
    void testCleanupOldSnapshotsRemovesAll() {
        dataAccess.saveSnapshot(mockSnapshot3); // Week ago
        
        TodaySoFarSnapshot oldSnapshot = mock(TodaySoFarSnapshot.class);
        when(oldSnapshot.getDate()).thenReturn(monthAgo);
        dataAccess.saveSnapshot(oldSnapshot);
        
        assertEquals(2, dataAccess.size());
        
        // Keep only 1 day - should remove both old snapshots
        int removed = dataAccess.cleanupOldSnapshots(1);
        
        assertEquals(2, removed);
        assertEquals(0, dataAccess.size());
    }

    @Test
    void testCleanupOldSnapshotsZeroDaysToKeep() {
        dataAccess.saveSnapshot(mockSnapshot1); // Today
        dataAccess.saveSnapshot(mockSnapshot2); // Yesterday
        
        // Keep 0 days - should remove even yesterday's snapshot
        int removed = dataAccess.cleanupOldSnapshots(0);
        
        assertEquals(1, removed); // Yesterday's snapshot should be removed
        assertEquals(1, dataAccess.size());
        assertNotNull(dataAccess.getSnapshot(today));
        assertNull(dataAccess.getSnapshot(yesterday));
    }

    @Test
    void testCleanupOldSnapshotsEmptyRepository() {
        int removed = dataAccess.cleanupOldSnapshots(7);
        
        assertEquals(0, removed);
        assertEquals(0, dataAccess.size());
    }

    @Test
    void testClear() {
        dataAccess.saveSnapshot(mockSnapshot1);
        dataAccess.saveSnapshot(mockSnapshot2);
        dataAccess.saveSnapshot(mockSnapshot3);
        
        assertEquals(3, dataAccess.size());
        
        dataAccess.clear();
        
        assertEquals(0, dataAccess.size());
        assertTrue(dataAccess.getAvailableDates().isEmpty());
        assertFalse(dataAccess.hasSnapshot(today));
        assertNull(dataAccess.getSnapshot(today));
    }

    @Test
    void testSizeAccuracy() {
        assertEquals(0, dataAccess.size());
        
        dataAccess.saveSnapshot(mockSnapshot1);
        assertEquals(1, dataAccess.size());
        
        dataAccess.saveSnapshot(mockSnapshot2);
        assertEquals(2, dataAccess.size());
        
        // Overwrite existing - size should not change
        TodaySoFarSnapshot newSnapshot = mock(TodaySoFarSnapshot.class);
        when(newSnapshot.getDate()).thenReturn(today);
        dataAccess.saveSnapshot(newSnapshot);
        assertEquals(2, dataAccess.size());
    }

    @Test
    void testCompleteWorkflow() {
        // Start with empty repository
        assertEquals(0, dataAccess.size());
        
        // Add multiple snapshots over time
        dataAccess.saveSnapshot(mockSnapshot1); // Today
        dataAccess.saveSnapshot(mockSnapshot2); // Yesterday
        dataAccess.saveSnapshot(mockSnapshot3); // Week ago
        
        // Add an old snapshot
        TodaySoFarSnapshot oldSnapshot = mock(TodaySoFarSnapshot.class);
        when(oldSnapshot.getDate()).thenReturn(monthAgo);
        dataAccess.saveSnapshot(oldSnapshot);
        
        assertEquals(4, dataAccess.size());
        
        // Verify retrieval
        assertTrue(dataAccess.hasSnapshot(today));
        assertTrue(dataAccess.hasSnapshot(yesterday));
        assertTrue(dataAccess.hasSnapshot(weekAgo));
        assertTrue(dataAccess.hasSnapshot(monthAgo));
        
        // Check sorted dates
        List<LocalDate> dates = dataAccess.getAvailableDates();
        assertEquals(4, dates.size());
        assertEquals(today, dates.get(0));
        
        // Cleanup old data
        int removed = dataAccess.cleanupOldSnapshots(14); // Keep 2 weeks
        assertEquals(1, removed); // Should remove month-ago snapshot
        assertEquals(3, dataAccess.size());
        
        // Verify cleanup worked
        assertFalse(dataAccess.hasSnapshot(monthAgo));
        assertTrue(dataAccess.hasSnapshot(today));
        assertTrue(dataAccess.hasSnapshot(yesterday));
        assertTrue(dataAccess.hasSnapshot(weekAgo));
        
        // Clear everything
        dataAccess.clear();
        assertEquals(0, dataAccess.size());
        assertTrue(dataAccess.getAvailableDates().isEmpty());
    }

    @Test
    void testEdgeCaseDateHandling() {
        LocalDate futureDate = today.plusDays(30);
        TodaySoFarSnapshot futureSnapshot = mock(TodaySoFarSnapshot.class);
        when(futureSnapshot.getDate()).thenReturn(futureDate);
        
        // Save future snapshot
        boolean result = dataAccess.saveSnapshot(futureSnapshot);
        
        assertTrue(result);
        assertEquals(1, dataAccess.size());
        assertTrue(dataAccess.hasSnapshot(futureDate));
        
        // Future snapshots should not be cleaned up
        int removed = dataAccess.cleanupOldSnapshots(7);
        assertEquals(0, removed);
        assertEquals(1, dataAccess.size());
    }

    @Test
    void testBoundaryDateForCleanup() {
        // Create snapshot exactly at the boundary
        LocalDate boundaryDate = today.minusDays(7); // Exactly 7 days ago
        TodaySoFarSnapshot boundarySnapshot = mock(TodaySoFarSnapshot.class);
        when(boundarySnapshot.getDate()).thenReturn(boundaryDate);
        
        dataAccess.saveSnapshot(boundarySnapshot);
        
        // Keep 7 days - boundary date should NOT be removed (>= cutoff)
        int removed = dataAccess.cleanupOldSnapshots(7);
        
        assertEquals(0, removed);
        assertEquals(1, dataAccess.size());
        assertTrue(dataAccess.hasSnapshot(boundaryDate));
    }

    @Test
    void testConcurrentModification() {
        // This test ensures the implementation handles concurrent scenarios gracefully
        dataAccess.saveSnapshot(mockSnapshot1);
        dataAccess.saveSnapshot(mockSnapshot2);
        
        // Get available dates
        List<LocalDate> dates = dataAccess.getAvailableDates();
        assertEquals(2, dates.size());
        
        // Add another snapshot after getting dates list
        dataAccess.saveSnapshot(mockSnapshot3);
        
        // Original dates list should not be affected (defensive copy)
        assertEquals(2, dates.size());
        
        // But getting dates again should show the new snapshot
        List<LocalDate> updatedDates = dataAccess.getAvailableDates();
        assertEquals(3, updatedDates.size());
    }
}