package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log;

import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for TodaysWellnessLogState.
 * Tests state management and immutability principles.
 */
class TodaysWellnessLogStateTest {

    private TodaysWellnessLogState state;
    private WellnessLogEntryInterf mockEntry1;
    private WellnessLogEntryInterf mockEntry2;
    private WellnessLogEntryInterf mockEntry3;

    @BeforeEach
    void setUp() {
        state = new TodaysWellnessLogState();
        mockEntry1 = mock(WellnessLogEntryInterf.class);
        mockEntry2 = mock(WellnessLogEntryInterf.class);
        mockEntry3 = mock(WellnessLogEntryInterf.class);
    }

    @Test
    @DisplayName("Should initialize with empty entries list")
    void testInitialization() {
        assertTrue(state.isEmpty());
        assertTrue(state.getEntries().isEmpty());
        assertEquals(0, state.getEntries().size());
    }

    @Test
    @DisplayName("Should add single entry correctly")
    void testAddSingleEntry() {
        // When
        state.addEntry(mockEntry1);

        // Then
        assertFalse(state.isEmpty());
        assertEquals(1, state.getEntries().size());
        assertTrue(state.getEntries().contains(mockEntry1));
    }

    @Test
    @DisplayName("Should add multiple entries in correct order")
    void testAddMultipleEntries() {
        // When
        state.addEntry(mockEntry1);
        state.addEntry(mockEntry2);
        state.addEntry(mockEntry3);

        // Then
        List<WellnessLogEntryInterf> entries = state.getEntries();
        assertEquals(3, entries.size());
        assertEquals(mockEntry1, entries.get(0));
        assertEquals(mockEntry2, entries.get(1));
        assertEquals(mockEntry3, entries.get(2));
    }

    @Test
    @DisplayName("Should handle null entry addition")
    void testAddNullEntry() {
        // When
        state.addEntry(null);

        // Then
        assertFalse(state.isEmpty());
        assertEquals(1, state.getEntries().size());
        assertNull(state.getEntries().get(0));
    }

    @Test
    @DisplayName("Should allow duplicate entries")
    void testAddDuplicateEntries() {
        // When
        state.addEntry(mockEntry1);
        state.addEntry(mockEntry1);
        state.addEntry(mockEntry1);

        // Then
        assertEquals(3, state.getEntries().size());
        assertTrue(state.getEntries().contains(mockEntry1));
    }

    @Test
    @DisplayName("Should set entries by replacing existing ones")
    void testSetEntries() {
        // Given
        state.addEntry(mockEntry1);
        List<WellnessLogEntryInterf> newEntries = Arrays.asList(mockEntry2, mockEntry3);

        // When
        state.setEntries(newEntries);

        // Then
        assertEquals(2, state.getEntries().size());
        assertFalse(state.getEntries().contains(mockEntry1));
        assertTrue(state.getEntries().contains(mockEntry2));
        assertTrue(state.getEntries().contains(mockEntry3));
    }

    @Test
    @DisplayName("Should clear existing entries when setting new ones")
    void testSetEntriesClearsExisting() {
        // Given
        state.addEntry(mockEntry1);
        state.addEntry(mockEntry2);
        assertEquals(2, state.getEntries().size());

        List<WellnessLogEntryInterf> newEntries = Arrays.asList(mockEntry3);

        // When
        state.setEntries(newEntries);

        // Then
        assertEquals(1, state.getEntries().size());
        assertEquals(mockEntry3, state.getEntries().get(0));
    }

    @Test
    @DisplayName("Should handle empty list when setting entries")
    void testSetEmptyEntries() {
        // Given
        state.addEntry(mockEntry1);
        state.addEntry(mockEntry2);

        // When
        state.setEntries(new ArrayList<>());

        // Then
        assertTrue(state.isEmpty());
        assertEquals(0, state.getEntries().size());
    }

    @Test
    @DisplayName("Should handle null list when setting entries")
    void testSetNullEntries() {
        // Given
        state.addEntry(mockEntry1);

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            state.setEntries(null);
        });
    }

    @Test
    @DisplayName("Should return defensive copy of entries list")
    void testGetEntriesReturnsDefensiveCopy() {
        // Given
        state.addEntry(mockEntry1);
        state.addEntry(mockEntry2);

        // When
        List<WellnessLogEntryInterf> entries1 = state.getEntries();
        List<WellnessLogEntryInterf> entries2 = state.getEntries();

        // Then
        assertNotSame(entries1, entries2);
        assertEquals(entries1, entries2);
        
        // Modifying returned list should not affect state
        entries1.clear();
        assertEquals(2, state.getEntries().size());
    }

    @Test
    @DisplayName("Should correctly report empty status")
    void testIsEmpty() {
        // Initially empty
        assertTrue(state.isEmpty());

        // After adding entry
        state.addEntry(mockEntry1);
        assertFalse(state.isEmpty());

        // After setting empty list
        state.setEntries(new ArrayList<>());
        assertTrue(state.isEmpty());

        // After adding null entry
        state.addEntry(null);
        assertFalse(state.isEmpty());
    }

    @Test
    @DisplayName("Should maintain entries order with mixed operations")
    void testMixedOperationsMaintainOrder() {
        // Given
        List<WellnessLogEntryInterf> initialEntries = Arrays.asList(mockEntry1, mockEntry2);

        // When
        state.setEntries(initialEntries);
        state.addEntry(mockEntry3);

        // Then
        List<WellnessLogEntryInterf> finalEntries = state.getEntries();
        assertEquals(3, finalEntries.size());
        assertEquals(mockEntry1, finalEntries.get(0));
        assertEquals(mockEntry2, finalEntries.get(1));
        assertEquals(mockEntry3, finalEntries.get(2));
    }

    @Test
    @DisplayName("Should handle large number of entries")
    void testLargeNumberOfEntries() {
        // Given
        List<WellnessLogEntryInterf> largeList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeList.add(mock(WellnessLogEntryInterf.class));
        }

        // When
        state.setEntries(largeList);

        // Then
        assertEquals(1000, state.getEntries().size());
        assertFalse(state.isEmpty());
    }

    @Test
    @DisplayName("Should preserve entry references correctly")
    void testEntryReferencePreservation() {
        // When
        state.addEntry(mockEntry1);

        // Then
        List<WellnessLogEntryInterf> entries = state.getEntries();
        assertSame(mockEntry1, entries.get(0));
    }

    @Test
    @DisplayName("Should handle concurrent-style operations safely")
    void testConcurrentStyleOperations() {
        // Given
        state.addEntry(mockEntry1);
        List<WellnessLogEntryInterf> snapshot1 = state.getEntries();

        // When
        state.addEntry(mockEntry2);
        List<WellnessLogEntryInterf> snapshot2 = state.getEntries();

        // Then
        assertEquals(1, snapshot1.size());
        assertEquals(2, snapshot2.size());
        assertNotEquals(snapshot1, snapshot2);
    }

    @Test
    @DisplayName("Should support entries with mixed null and non-null values")
    void testMixedNullAndNonNullEntries() {
        // When
        state.addEntry(mockEntry1);
        state.addEntry(null);
        state.addEntry(mockEntry2);

        // Then
        List<WellnessLogEntryInterf> entries = state.getEntries();
        assertEquals(3, entries.size());
        assertEquals(mockEntry1, entries.get(0));
        assertNull(entries.get(1));
        assertEquals(mockEntry2, entries.get(2));
    }
}