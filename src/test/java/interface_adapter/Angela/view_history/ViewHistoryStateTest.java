package interface_adapter.Angela.view_history;

import entity.Angela.Task.Task;
import entity.info.Info;
import entity.Angela.TodaySoFarSnapshot;
import entity.alex.WellnessLogEntry.WellnessLogEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewHistoryState.
 */
class ViewHistoryStateTest {

    private ViewHistoryState state;

    @BeforeEach
    void setUp() {
        state = new ViewHistoryState();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(state);
        assertNull(state.getSelectedDate());
        assertTrue(state.getAvailableDates().isEmpty());
        assertFalse(state.hasData());
        assertNull(state.getErrorMessage());
        assertTrue(state.getTodaysTasks().isEmpty());
        assertTrue(state.getCompletedTasks().isEmpty());
        assertEquals(0, state.getTaskCompletionRate());
        assertTrue(state.getOverdueTasks().isEmpty());
        assertTrue(state.getTodaysEvents().isEmpty());
        assertTrue(state.getGoalProgress().isEmpty());
        assertTrue(state.getWellnessEntries().isEmpty());
        assertNull(state.getExportMessage());
    }

    @Test
    void testBuilderDefaultConstructor() {
        ViewHistoryState.Builder builder = new ViewHistoryState.Builder();
        ViewHistoryState newState = builder.build();
        
        assertNotNull(newState);
        assertNull(newState.getSelectedDate());
        assertTrue(newState.getAvailableDates().isEmpty());
        assertFalse(newState.hasData());
        assertNull(newState.getErrorMessage());
        assertTrue(newState.getTodaysTasks().isEmpty());
        assertTrue(newState.getCompletedTasks().isEmpty());
        assertEquals(0, newState.getTaskCompletionRate());
        assertTrue(newState.getOverdueTasks().isEmpty());
        assertTrue(newState.getTodaysEvents().isEmpty());
        assertTrue(newState.getGoalProgress().isEmpty());
        assertTrue(newState.getWellnessEntries().isEmpty());
        assertNull(newState.getExportMessage());
    }

    @Test
    void testBuilderCopyConstructor() {
        // Create original state with some data
        LocalDate date = LocalDate.of(2023, 12, 25);
        List<LocalDate> dates = Arrays.asList(date, LocalDate.of(2023, 12, 24));
        
        ViewHistoryState originalState = new ViewHistoryState.Builder()
                .selectedDate(date)
                .availableDates(dates)
                .hasData(true)
                .errorMessage("Test error")
                .taskCompletionRate(75)
                .exportMessage("Export successful")
                .build();

        // Create copy using copy constructor
        ViewHistoryState.Builder copyBuilder = new ViewHistoryState.Builder(originalState);
        ViewHistoryState copiedState = copyBuilder.build();

        // Verify copied state
        assertEquals(originalState.getSelectedDate(), copiedState.getSelectedDate());
        assertEquals(originalState.getAvailableDates(), copiedState.getAvailableDates());
        assertEquals(originalState.hasData(), copiedState.hasData());
        assertEquals(originalState.getErrorMessage(), copiedState.getErrorMessage());
        assertEquals(originalState.getTaskCompletionRate(), copiedState.getTaskCompletionRate());
        assertEquals(originalState.getExportMessage(), copiedState.getExportMessage());
    }

    @Test
    void testBuilderFluentInterface() {
        LocalDate selectedDate = LocalDate.of(2023, 12, 25);
        List<LocalDate> availableDates = Arrays.asList(selectedDate, LocalDate.of(2023, 12, 24));
        
        ViewHistoryState newState = new ViewHistoryState.Builder()
                .selectedDate(selectedDate)
                .availableDates(availableDates)
                .hasData(true)
                .errorMessage("Test error")
                .taskCompletionRate(85)
                .exportMessage("Export completed")
                .build();

        assertEquals(selectedDate, newState.getSelectedDate());
        assertEquals(availableDates, newState.getAvailableDates());
        assertTrue(newState.hasData());
        assertEquals("Test error", newState.getErrorMessage());
        assertEquals(85, newState.getTaskCompletionRate());
        assertEquals("Export completed", newState.getExportMessage());
    }

    @Test
    void testImmutabilityOfLists() {
        List<LocalDate> originalDates = new ArrayList<>(Arrays.asList(LocalDate.now(), LocalDate.now().minusDays(1)));
        
        ViewHistoryState newState = new ViewHistoryState.Builder()
                .availableDates(originalDates)
                .build();

        // Modify original list
        originalDates.add(LocalDate.now().minusDays(2));
        
        // State should not be affected
        assertEquals(2, newState.getAvailableDates().size());
        
        // Modify returned list
        List<LocalDate> returnedDates = newState.getAvailableDates();
        returnedDates.add(LocalDate.now().minusDays(3));
        
        // State should still not be affected
        assertEquals(2, newState.getAvailableDates().size());
    }

    @Test
    void testNullListHandling() {
        ViewHistoryState newState = new ViewHistoryState.Builder()
                .availableDates(null)
                .todaysTasks(null)
                .completedTasks(null)
                .overdueTasks(null)
                .todaysEvents(null)
                .goalProgress(null)
                .wellnessEntries(null)
                .build();

        // All should return empty lists, not null
        assertNotNull(newState.getAvailableDates());
        assertTrue(newState.getAvailableDates().isEmpty());
        assertNotNull(newState.getTodaysTasks());
        assertTrue(newState.getTodaysTasks().isEmpty());
        assertNotNull(newState.getCompletedTasks());
        assertTrue(newState.getCompletedTasks().isEmpty());
        assertNotNull(newState.getOverdueTasks());
        assertTrue(newState.getOverdueTasks().isEmpty());
        assertNotNull(newState.getTodaysEvents());
        assertTrue(newState.getTodaysEvents().isEmpty());
        assertNotNull(newState.getGoalProgress());
        assertTrue(newState.getGoalProgress().isEmpty());
        assertNotNull(newState.getWellnessEntries());
        assertTrue(newState.getWellnessEntries().isEmpty());
    }

    @Test
    void testTaskCompletionRateBoundaries() {
        // Test minimum value
        ViewHistoryState zeroState = new ViewHistoryState.Builder()
                .taskCompletionRate(0)
                .build();
        assertEquals(0, zeroState.getTaskCompletionRate());

        // Test maximum value
        ViewHistoryState hundredState = new ViewHistoryState.Builder()
                .taskCompletionRate(100)
                .build();
        assertEquals(100, hundredState.getTaskCompletionRate());

        // Test negative value (should be allowed)
        ViewHistoryState negativeState = new ViewHistoryState.Builder()
                .taskCompletionRate(-10)
                .build();
        assertEquals(-10, negativeState.getTaskCompletionRate());

        // Test over 100 value (should be allowed)
        ViewHistoryState overState = new ViewHistoryState.Builder()
                .taskCompletionRate(150)
                .build();
        assertEquals(150, overState.getTaskCompletionRate());
    }

    @Test
    void testDateHandling() {
        LocalDate pastDate = LocalDate.of(2020, 1, 1);
        LocalDate futureDate = LocalDate.of(2030, 12, 31);
        LocalDate today = LocalDate.now();

        ViewHistoryState pastState = new ViewHistoryState.Builder()
                .selectedDate(pastDate)
                .build();
        assertEquals(pastDate, pastState.getSelectedDate());

        ViewHistoryState futureState = new ViewHistoryState.Builder()
                .selectedDate(futureDate)
                .build();
        assertEquals(futureDate, futureState.getSelectedDate());

        ViewHistoryState todayState = new ViewHistoryState.Builder()
                .selectedDate(today)
                .build();
        assertEquals(today, todayState.getSelectedDate());
    }

    @Test
    void testStringFieldHandling() {
        // Test normal strings
        ViewHistoryState normalState = new ViewHistoryState.Builder()
                .errorMessage("Normal error message")
                .exportMessage("Normal export message")
                .build();
        assertEquals("Normal error message", normalState.getErrorMessage());
        assertEquals("Normal export message", normalState.getExportMessage());

        // Test empty strings
        ViewHistoryState emptyState = new ViewHistoryState.Builder()
                .errorMessage("")
                .exportMessage("")
                .build();
        assertEquals("", emptyState.getErrorMessage());
        assertEquals("", emptyState.getExportMessage());

        // Test null strings
        ViewHistoryState nullState = new ViewHistoryState.Builder()
                .errorMessage(null)
                .exportMessage(null)
                .build();
        assertNull(nullState.getErrorMessage());
        assertNull(nullState.getExportMessage());

        // Test special characters
        ViewHistoryState specialState = new ViewHistoryState.Builder()
                .errorMessage("Error: !@#$%^&*()")
                .exportMessage("Export 你好 🎉 αβγ")
                .build();
        assertEquals("Error: !@#$%^&*()", specialState.getErrorMessage());
        assertEquals("Export 你好 🎉 αβγ", specialState.getExportMessage());
    }

    @Test
    void testComplexStateBuilding() {
        LocalDate selectedDate = LocalDate.of(2023, 12, 25);
        List<LocalDate> availableDates = Arrays.asList(
                selectedDate,
                selectedDate.minusDays(1),
                selectedDate.minusDays(2)
        );

        ViewHistoryState complexState = new ViewHistoryState.Builder()
                .selectedDate(selectedDate)
                .availableDates(availableDates)
                .hasData(true)
                .errorMessage("Complex error")
                .taskCompletionRate(67)
                .exportMessage("Complex export")
                .build();

        // Verify all fields
        assertEquals(selectedDate, complexState.getSelectedDate());
        assertEquals(3, complexState.getAvailableDates().size());
        assertTrue(complexState.hasData());
        assertEquals("Complex error", complexState.getErrorMessage());
        assertEquals(67, complexState.getTaskCompletionRate());
        assertEquals("Complex export", complexState.getExportMessage());
    }

    @Test
    void testBuilderModification() {
        ViewHistoryState.Builder builder = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 1, 1))
                .hasData(false)
                .taskCompletionRate(25);

        ViewHistoryState state1 = builder.build();

        // Modify builder and build again
        builder.selectedDate(LocalDate.of(2023, 12, 31))
               .hasData(true)
               .taskCompletionRate(90);

        ViewHistoryState state2 = builder.build();

        // Verify states are different
        assertNotEquals(state1.getSelectedDate(), state2.getSelectedDate());
        assertNotEquals(state1.hasData(), state2.hasData());
        assertNotEquals(state1.getTaskCompletionRate(), state2.getTaskCompletionRate());
    }

    @Test
    void testCopyBuilderModification() {
        ViewHistoryState originalState = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 6, 15))
                .hasData(true)
                .taskCompletionRate(50)
                .errorMessage("Original error")
                .build();

        // Create modified copy
        ViewHistoryState modifiedState = new ViewHistoryState.Builder(originalState)
                .selectedDate(LocalDate.of(2023, 6, 16))
                .taskCompletionRate(75)
                .errorMessage("Modified error")
                .build();

        // Verify original is unchanged and new state is modified
        assertEquals(LocalDate.of(2023, 6, 15), originalState.getSelectedDate());
        assertEquals(50, originalState.getTaskCompletionRate());
        assertEquals("Original error", originalState.getErrorMessage());

        assertEquals(LocalDate.of(2023, 6, 16), modifiedState.getSelectedDate());
        assertEquals(75, modifiedState.getTaskCompletionRate());
        assertEquals("Modified error", modifiedState.getErrorMessage());
        assertTrue(modifiedState.hasData()); // Should be copied from original
    }

    @Test
    void testStateIndependence() {
        List<LocalDate> sharedDates = new ArrayList<>(Arrays.asList(LocalDate.now()));
        
        ViewHistoryState state1 = new ViewHistoryState.Builder()
                .availableDates(sharedDates)
                .build();
        
        ViewHistoryState state2 = new ViewHistoryState.Builder()
                .availableDates(sharedDates)
                .build();

        // Modify one state's returned list
        List<LocalDate> dates1 = state1.getAvailableDates();
        dates1.clear();

        // Other state should be unaffected
        assertEquals(1, state2.getAvailableDates().size());
    }

    @Test
    void testBooleanHandling() {
        ViewHistoryState trueState = new ViewHistoryState.Builder()
                .hasData(true)
                .build();
        assertTrue(trueState.hasData());

        ViewHistoryState falseState = new ViewHistoryState.Builder()
                .hasData(false)
                .build();
        assertFalse(falseState.hasData());
    }

    @Test
    void testGettersReturnDefensiveCopies() {
        List<LocalDate> originalDates = Arrays.asList(LocalDate.now(), LocalDate.now().minusDays(1));
        
        ViewHistoryState state = new ViewHistoryState.Builder()
                .availableDates(originalDates)
                .build();

        // Get list multiple times
        List<LocalDate> dates1 = state.getAvailableDates();
        List<LocalDate> dates2 = state.getAvailableDates();

        // Should be different instances but equal content
        assertNotSame(dates1, dates2);
        assertEquals(dates1, dates2);

        // Modifying one shouldn't affect the other
        dates1.clear();
        assertEquals(2, dates2.size());
        assertEquals(2, state.getAvailableDates().size());
    }
}