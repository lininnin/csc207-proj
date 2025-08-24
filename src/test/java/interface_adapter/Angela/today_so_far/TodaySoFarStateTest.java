package interface_adapter.Angela.today_so_far;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.today_so_far.TodaySoFarOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodaySoFarStateTest {

    private TodaySoFarState state;

    @BeforeEach
    void setUp() {
        state = new TodaySoFarState();
    }

    @Test
    void testConstructor() {
        assertNotNull(state);
        assertNotNull(state.getGoals());
        assertNotNull(state.getCompletedItems());
        assertNotNull(state.getWellnessEntries());
        assertTrue(state.getGoals().isEmpty());
        assertTrue(state.getCompletedItems().isEmpty());
        assertTrue(state.getWellnessEntries().isEmpty());
        assertEquals(0, state.getCompletionRate());
        assertNull(state.getError());
    }

    @Test
    void testSetAndGetGoals() {
        // Arrange
        List<TodaySoFarOutputData.GoalProgress> goals = new ArrayList<>();
        TodaySoFarOutputData.GoalProgress goal1 = mock(TodaySoFarOutputData.GoalProgress.class);
        TodaySoFarOutputData.GoalProgress goal2 = mock(TodaySoFarOutputData.GoalProgress.class);
        goals.add(goal1);
        goals.add(goal2);

        // Act
        state.setGoals(goals);

        // Assert
        assertEquals(goals, state.getGoals());
        assertEquals(2, state.getGoals().size());
        assertSame(goal1, state.getGoals().get(0));
        assertSame(goal2, state.getGoals().get(1));
    }

    @Test
    void testSetGoalsWithNull() {
        // Act
        state.setGoals(null);

        // Assert
        assertNull(state.getGoals());
    }

    @Test
    void testSetGoalsWithEmptyList() {
        // Arrange
        List<TodaySoFarOutputData.GoalProgress> emptyGoals = new ArrayList<>();

        // Act
        state.setGoals(emptyGoals);

        // Assert
        assertEquals(emptyGoals, state.getGoals());
        assertTrue(state.getGoals().isEmpty());
    }

    @Test
    void testSetAndGetCompletedItems() {
        // Arrange
        List<TodaySoFarOutputData.CompletedItem> completedItems = new ArrayList<>();
        TodaySoFarOutputData.CompletedItem item1 = mock(TodaySoFarOutputData.CompletedItem.class);
        TodaySoFarOutputData.CompletedItem item2 = mock(TodaySoFarOutputData.CompletedItem.class);
        completedItems.add(item1);
        completedItems.add(item2);

        // Act
        state.setCompletedItems(completedItems);

        // Assert
        assertEquals(completedItems, state.getCompletedItems());
        assertEquals(2, state.getCompletedItems().size());
        assertSame(item1, state.getCompletedItems().get(0));
        assertSame(item2, state.getCompletedItems().get(1));
    }

    @Test
    void testSetCompletedItemsWithNull() {
        // Act
        state.setCompletedItems(null);

        // Assert
        assertNull(state.getCompletedItems());
    }

    @Test
    void testSetCompletedItemsWithEmptyList() {
        // Arrange
        List<TodaySoFarOutputData.CompletedItem> emptyItems = new ArrayList<>();

        // Act
        state.setCompletedItems(emptyItems);

        // Assert
        assertEquals(emptyItems, state.getCompletedItems());
        assertTrue(state.getCompletedItems().isEmpty());
    }

    @Test
    void testSetAndGetCompletionRate() {
        // Test various completion rates
        int[] testRates = {0, 25, 50, 75, 100, 150, -10};

        for (int rate : testRates) {
            // Act
            state.setCompletionRate(rate);

            // Assert
            assertEquals(rate, state.getCompletionRate());
        }
    }

    @Test
    void testSetAndGetWellnessEntries() {
        // Arrange
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = new ArrayList<>();
        TodaySoFarOutputData.WellnessEntry entry1 = mock(TodaySoFarOutputData.WellnessEntry.class);
        TodaySoFarOutputData.WellnessEntry entry2 = mock(TodaySoFarOutputData.WellnessEntry.class);
        wellnessEntries.add(entry1);
        wellnessEntries.add(entry2);

        // Act
        state.setWellnessEntries(wellnessEntries);

        // Assert
        assertEquals(wellnessEntries, state.getWellnessEntries());
        assertEquals(2, state.getWellnessEntries().size());
        assertSame(entry1, state.getWellnessEntries().get(0));
        assertSame(entry2, state.getWellnessEntries().get(1));
    }

    @Test
    void testSetWellnessEntriesWithNull() {
        // Act
        state.setWellnessEntries(null);

        // Assert
        assertNull(state.getWellnessEntries());
    }

    @Test
    void testSetWellnessEntriesWithEmptyList() {
        // Arrange
        List<TodaySoFarOutputData.WellnessEntry> emptyEntries = new ArrayList<>();

        // Act
        state.setWellnessEntries(emptyEntries);

        // Assert
        assertEquals(emptyEntries, state.getWellnessEntries());
        assertTrue(state.getWellnessEntries().isEmpty());
    }

    @Test
    void testSetAndGetError() {
        // Test with null
        state.setError(null);
        assertNull(state.getError());

        // Test with empty string
        state.setError("");
        assertEquals("", state.getError());

        // Test with normal error message
        String errorMessage = "Failed to load today's summary";
        state.setError(errorMessage);
        assertEquals(errorMessage, state.getError());

        // Test with long error message
        String longError = "A very long error message that might occur during data loading process";
        state.setError(longError);
        assertEquals(longError, state.getError());
    }

    @Test
    void testCompleteStateUpdate() {
        // Arrange
        List<TodaySoFarOutputData.GoalProgress> goals = new ArrayList<>();
        goals.add(mock(TodaySoFarOutputData.GoalProgress.class));
        
        List<TodaySoFarOutputData.CompletedItem> completedItems = new ArrayList<>();
        completedItems.add(mock(TodaySoFarOutputData.CompletedItem.class));
        
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = new ArrayList<>();
        wellnessEntries.add(mock(TodaySoFarOutputData.WellnessEntry.class));
        
        int completionRate = 85;
        String error = null;

        // Act
        state.setGoals(goals);
        state.setCompletedItems(completedItems);
        state.setWellnessEntries(wellnessEntries);
        state.setCompletionRate(completionRate);
        state.setError(error);

        // Assert
        assertEquals(goals, state.getGoals());
        assertEquals(completedItems, state.getCompletedItems());
        assertEquals(wellnessEntries, state.getWellnessEntries());
        assertEquals(completionRate, state.getCompletionRate());
        assertEquals(error, state.getError());
    }

    @Test
    void testStateWithErrorCondition() {
        // Arrange
        String errorMessage = "Database connection failed";

        // Act
        state.setError(errorMessage);

        // Assert
        assertEquals(errorMessage, state.getError());
        // Other fields should remain in their default state
        assertNotNull(state.getGoals());
        assertNotNull(state.getCompletedItems());
        assertNotNull(state.getWellnessEntries());
        assertTrue(state.getGoals().isEmpty());
        assertTrue(state.getCompletedItems().isEmpty());
        assertTrue(state.getWellnessEntries().isEmpty());
        assertEquals(0, state.getCompletionRate());
    }

    @Test
    void testMultipleUpdatesToSameProperty() {
        // Test goals
        List<TodaySoFarOutputData.GoalProgress> goals1 = new ArrayList<>();
        goals1.add(mock(TodaySoFarOutputData.GoalProgress.class));
        
        List<TodaySoFarOutputData.GoalProgress> goals2 = new ArrayList<>();
        goals2.add(mock(TodaySoFarOutputData.GoalProgress.class));
        goals2.add(mock(TodaySoFarOutputData.GoalProgress.class));

        state.setGoals(goals1);
        assertEquals(1, state.getGoals().size());

        state.setGoals(goals2);
        assertEquals(2, state.getGoals().size());

        // Test completion rate
        state.setCompletionRate(50);
        assertEquals(50, state.getCompletionRate());

        state.setCompletionRate(100);
        assertEquals(100, state.getCompletionRate());
    }

    @Test
    void testGoalsListIndependence() {
        // Arrange
        List<TodaySoFarOutputData.GoalProgress> originalGoals = new ArrayList<>();
        originalGoals.add(mock(TodaySoFarOutputData.GoalProgress.class));

        // Act
        state.setGoals(originalGoals);
        List<TodaySoFarOutputData.GoalProgress> retrievedGoals = state.getGoals();

        // Modify the original list
        originalGoals.add(mock(TodaySoFarOutputData.GoalProgress.class));

        // Assert - The state should be affected because we're holding the same reference
        assertEquals(2, retrievedGoals.size());
        assertEquals(originalGoals, state.getGoals());
    }
}