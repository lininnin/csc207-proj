package use_case.Angela.today_so_far;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import use_case.Angela.today_so_far.TodaySoFarOutputData.GoalProgress;
import use_case.Angela.today_so_far.TodaySoFarOutputData.CompletedItem;
import use_case.Angela.today_so_far.TodaySoFarOutputData.WellnessEntry;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for TodaySoFarOutputData.
 * Tests immutability and getter functionality for the use case layer.
 */
class TodaySoFarOutputDataTest {

    @Test
    @DisplayName("Should create output data with valid parameters")
    void testValidCreation() {
        // Given
        List<GoalProgress> goals = Arrays.asList(
            new GoalProgress("Exercise", "Daily", "2/3 completed"),
            new GoalProgress("Study", "Weekly", "5/7 hours")
        );
        List<CompletedItem> completedItems = Arrays.asList(
            new CompletedItem("Task", "Morning workout", "Health"),
            new CompletedItem("Event", "Team meeting", "Work")
        );
        int completionRate = 75;
        List<WellnessEntry> wellnessEntries = Arrays.asList(
            new WellnessEntry("Happy", 3, 8, 2, LocalTime.of(9, 0)),
            new WellnessEntry("Focused", 4, 7, 3, LocalTime.of(14, 30))
        );

        // When
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(goals, completedItems, completionRate, wellnessEntries);

        // Then
        assertEquals(goals, outputData.getGoals(), "Goals should match");
        assertEquals(completedItems, outputData.getCompletedItems(), "Completed items should match");
        assertEquals(completionRate, outputData.getCompletionRate(), "Completion rate should match");
        assertEquals(wellnessEntries, outputData.getWellnessEntries(), "Wellness entries should match");
    }

    @Test
    @DisplayName("Should accept null lists")
    void testNullLists() {
        // Given
        List<GoalProgress> goals = null;
        List<CompletedItem> completedItems = null;
        int completionRate = 0;
        List<WellnessEntry> wellnessEntries = null;

        // When
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(goals, completedItems, completionRate, wellnessEntries);

        // Then
        assertNull(outputData.getGoals(), "Goals should be null");
        assertNull(outputData.getCompletedItems(), "Completed items should be null");
        assertEquals(0, outputData.getCompletionRate(), "Completion rate should be preserved");
        assertNull(outputData.getWellnessEntries(), "Wellness entries should be null");
    }

    @Test
    @DisplayName("Should handle empty lists")
    void testEmptyLists() {
        // Given
        List<GoalProgress> goals = Collections.emptyList();
        List<CompletedItem> completedItems = Collections.emptyList();
        int completionRate = 100;
        List<WellnessEntry> wellnessEntries = Collections.emptyList();

        // When
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(goals, completedItems, completionRate, wellnessEntries);

        // Then
        assertTrue(outputData.getGoals().isEmpty(), "Goals should be empty");
        assertTrue(outputData.getCompletedItems().isEmpty(), "Completed items should be empty");
        assertEquals(100, outputData.getCompletionRate(), "Completion rate should be preserved");
        assertTrue(outputData.getWellnessEntries().isEmpty(), "Wellness entries should be empty");
    }

    @Test
    @DisplayName("Should handle various completion rates")
    void testVariousCompletionRates() {
        List<GoalProgress> goals = Collections.emptyList();
        List<CompletedItem> completedItems = Collections.emptyList();
        List<WellnessEntry> wellnessEntries = Collections.emptyList();

        // Test 0% completion
        TodaySoFarOutputData zeroData = new TodaySoFarOutputData(goals, completedItems, 0, wellnessEntries);
        assertEquals(0, zeroData.getCompletionRate(), "Zero completion rate should be preserved");

        // Test 100% completion
        TodaySoFarOutputData fullData = new TodaySoFarOutputData(goals, completedItems, 100, wellnessEntries);
        assertEquals(100, fullData.getCompletionRate(), "Full completion rate should be preserved");

        // Test negative completion (edge case)
        TodaySoFarOutputData negativeData = new TodaySoFarOutputData(goals, completedItems, -10, wellnessEntries);
        assertEquals(-10, negativeData.getCompletionRate(), "Negative completion rate should be preserved");

        // Test over 100% completion (edge case)
        TodaySoFarOutputData overData = new TodaySoFarOutputData(goals, completedItems, 150, wellnessEntries);
        assertEquals(150, overData.getCompletionRate(), "Over 100% completion rate should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        List<GoalProgress> originalGoals = Arrays.asList(new GoalProgress("Goal 1", "Daily", "Progress 1"));
        List<CompletedItem> originalCompleted = Arrays.asList(new CompletedItem("Task", "Task 1", "Category 1"));
        int originalRate = 50;
        List<WellnessEntry> originalWellness = Arrays.asList(new WellnessEntry("Happy", 5, 8, 3, LocalTime.now()));
        
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(originalGoals, originalCompleted, originalRate, originalWellness);

        // Then
        assertSame(originalGoals, outputData.getGoals(), "Goals reference should remain unchanged");
        assertSame(originalCompleted, outputData.getCompletedItems(), "Completed items reference should remain unchanged");
        assertEquals(originalRate, outputData.getCompletionRate(), "Completion rate should remain unchanged");
        assertSame(originalWellness, outputData.getWellnessEntries(), "Wellness entries reference should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        TodaySoFarOutputData outputData1 = new TodaySoFarOutputData(
            Arrays.asList(new GoalProgress("Goal 1", "Daily", "Progress 1")),
            Arrays.asList(new CompletedItem("Task", "Task 1", "Category 1")),
            75,
            Arrays.asList(new WellnessEntry("Happy", 5, 8, 3, LocalTime.of(9, 0)))
        );
        
        TodaySoFarOutputData outputData2 = new TodaySoFarOutputData(
            Arrays.asList(new GoalProgress("Goal 2", "Weekly", "Progress 2")),
            Arrays.asList(new CompletedItem("Event", "Event 1", "Category 2")),
            85,
            Arrays.asList(new WellnessEntry("Focused", 3, 7, 4, LocalTime.of(14, 0)))
        );

        // Then
        assertNotEquals(outputData1.getGoals(), outputData2.getGoals(), "Different instances should have different goals");
        assertNotEquals(outputData1.getCompletedItems(), outputData2.getCompletedItems(), "Different instances should have different completed items");
        assertNotEquals(outputData1.getCompletionRate(), outputData2.getCompletionRate(), "Different instances should have different completion rates");
        assertNotEquals(outputData1.getWellnessEntries(), outputData2.getWellnessEntries(), "Different instances should have different wellness entries");
        assertNotSame(outputData1, outputData2, "Should be different object instances");
    }

    @Test
    @DisplayName("Should handle consistent getter behavior")
    void testConsistentGetters() {
        // Given
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(
            Arrays.asList(new GoalProgress("Goal", "Daily", "Progress")),
            Arrays.asList(new CompletedItem("Task", "Task", "Category")),
            50,
            Arrays.asList(new WellnessEntry("Happy", 5, 8, 3, LocalTime.now()))
        );

        // Then - Multiple calls should return same values
        assertSame(outputData.getGoals(), outputData.getGoals(), "getGoals should be consistent");
        assertSame(outputData.getCompletedItems(), outputData.getCompletedItems(), "getCompletedItems should be consistent");
        assertEquals(outputData.getCompletionRate(), outputData.getCompletionRate(), "getCompletionRate should be consistent");
        assertSame(outputData.getWellnessEntries(), outputData.getWellnessEntries(), "getWellnessEntries should be consistent");
    }

    // GoalProgress nested class tests
    @Test
    @DisplayName("Should create GoalProgress with valid parameters")
    void testGoalProgressCreation() {
        // Given
        String name = "Exercise Daily";
        String period = "Daily";
        String progress = "5/7 completed";

        // When
        GoalProgress goalProgress = new GoalProgress(name, period, progress);

        // Then
        assertEquals(name, goalProgress.getName(), "Name should match");
        assertEquals(period, goalProgress.getPeriod(), "Period should match");
        assertEquals(progress, goalProgress.getProgress(), "Progress should match");
    }

    @Test
    @DisplayName("Should handle null values in GoalProgress")
    void testGoalProgressNullValues() {
        // Given
        GoalProgress goalProgress = new GoalProgress(null, null, null);

        // Then
        assertNull(goalProgress.getName(), "Name should be null");
        assertNull(goalProgress.getPeriod(), "Period should be null");
        assertNull(goalProgress.getProgress(), "Progress should be null");
    }

    @Test
    @DisplayName("Should handle empty strings in GoalProgress")
    void testGoalProgressEmptyStrings() {
        // Given
        GoalProgress goalProgress = new GoalProgress("", "", "");

        // Then
        assertEquals("", goalProgress.getName(), "Name should be empty");
        assertEquals("", goalProgress.getPeriod(), "Period should be empty");
        assertEquals("", goalProgress.getProgress(), "Progress should be empty");
    }

    @Test
    @DisplayName("Should handle special characters in GoalProgress")
    void testGoalProgressSpecialCharacters() {
        // Given
        String name = "Exercise & Fitness Goals!";
        String period = "Daily/Weekly";
        String progress = "75% (3/4) completed";

        GoalProgress goalProgress = new GoalProgress(name, period, progress);

        // Then
        assertEquals(name, goalProgress.getName(), "Special characters in name should be preserved");
        assertEquals(period, goalProgress.getPeriod(), "Special characters in period should be preserved");
        assertEquals(progress, goalProgress.getProgress(), "Special characters in progress should be preserved");
    }

    // CompletedItem nested class tests
    @Test
    @DisplayName("Should create CompletedItem with valid parameters")
    void testCompletedItemCreation() {
        // Given
        String type = "Task";
        String name = "Complete project";
        String category = "Work";

        // When
        CompletedItem completedItem = new CompletedItem(type, name, category);

        // Then
        assertEquals(type, completedItem.getType(), "Type should match");
        assertEquals(name, completedItem.getName(), "Name should match");
        assertEquals(category, completedItem.getCategory(), "Category should match");
    }

    @Test
    @DisplayName("Should handle null values in CompletedItem")
    void testCompletedItemNullValues() {
        // Given
        CompletedItem completedItem = new CompletedItem(null, null, null);

        // Then
        assertNull(completedItem.getType(), "Type should be null");
        assertNull(completedItem.getName(), "Name should be null");
        assertNull(completedItem.getCategory(), "Category should be null");
    }

    @Test
    @DisplayName("Should handle different item types in CompletedItem")
    void testCompletedItemDifferentTypes() {
        // Given
        CompletedItem taskItem = new CompletedItem("Task", "Task Name", "Category");
        CompletedItem eventItem = new CompletedItem("Event", "Event Name", "Category");

        // Then
        assertEquals("Task", taskItem.getType(), "Task type should be preserved");
        assertEquals("Event", eventItem.getType(), "Event type should be preserved");
    }

    // WellnessEntry nested class tests
    @Test
    @DisplayName("Should create WellnessEntry with valid parameters")
    void testWellnessEntryCreation() {
        // Given
        String mood = "Happy";
        int stress = 3;
        int energy = 8;
        int fatigue = 2;
        LocalTime time = LocalTime.of(10, 30);

        // When
        WellnessEntry wellnessEntry = new WellnessEntry(mood, stress, energy, fatigue, time);

        // Then
        assertEquals(mood, wellnessEntry.getMood(), "Mood should match");
        assertEquals(stress, wellnessEntry.getStress(), "Stress should match");
        assertEquals(energy, wellnessEntry.getEnergy(), "Energy should match");
        assertEquals(fatigue, wellnessEntry.getFatigue(), "Fatigue should match");
        assertEquals(time, wellnessEntry.getTime(), "Time should match");
    }

    @Test
    @DisplayName("Should handle null values in WellnessEntry")
    void testWellnessEntryNullValues() {
        // Given
        WellnessEntry wellnessEntry = new WellnessEntry(null, 0, 0, 0, null);

        // Then
        assertNull(wellnessEntry.getMood(), "Mood should be null");
        assertEquals(0, wellnessEntry.getStress(), "Stress should be zero");
        assertEquals(0, wellnessEntry.getEnergy(), "Energy should be zero");
        assertEquals(0, wellnessEntry.getFatigue(), "Fatigue should be zero");
        assertNull(wellnessEntry.getTime(), "Time should be null");
    }

    @Test
    @DisplayName("Should handle extreme wellness values")
    void testWellnessEntryExtremeValues() {
        // Given
        WellnessEntry negativeEntry = new WellnessEntry("Sad", -10, -5, -3, LocalTime.of(0, 0));
        WellnessEntry highEntry = new WellnessEntry("Excited", 100, 999, 50, LocalTime.of(23, 59));

        // Then
        assertEquals(-10, negativeEntry.getStress(), "Negative stress should be preserved");
        assertEquals(-5, negativeEntry.getEnergy(), "Negative energy should be preserved");
        assertEquals(-3, negativeEntry.getFatigue(), "Negative fatigue should be preserved");
        
        assertEquals(100, highEntry.getStress(), "High stress should be preserved");
        assertEquals(999, highEntry.getEnergy(), "High energy should be preserved");
        assertEquals(50, highEntry.getFatigue(), "High fatigue should be preserved");
    }

    @Test
    @DisplayName("Should handle various time values in WellnessEntry")
    void testWellnessEntryTimeValues() {
        // Given
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime noon = LocalTime.of(12, 0);
        LocalTime almostMidnight = LocalTime.of(23, 59, 59);

        WellnessEntry midnightEntry = new WellnessEntry("Tired", 1, 2, 8, midnight);
        WellnessEntry noonEntry = new WellnessEntry("Energetic", 4, 9, 3, noon);
        WellnessEntry lateEntry = new WellnessEntry("Relaxed", 2, 4, 6, almostMidnight);

        // Then
        assertEquals(midnight, midnightEntry.getTime(), "Midnight time should be preserved");
        assertEquals(noon, noonEntry.getTime(), "Noon time should be preserved");
        assertEquals(almostMidnight, lateEntry.getTime(), "Late time should be preserved");
    }

    @Test
    @DisplayName("Should handle various mood values in WellnessEntry")
    void testWellnessEntryMoodValues() {
        // Given
        WellnessEntry happyEntry = new WellnessEntry("Happy", 3, 8, 2, LocalTime.now());
        WellnessEntry sadEntry = new WellnessEntry("Sad", 7, 3, 8, LocalTime.now());
        WellnessEntry neutralEntry = new WellnessEntry("Neutral", 5, 5, 5, LocalTime.now());
        WellnessEntry unicodeEntry = new WellnessEntry("😊 Happy", 2, 9, 1, LocalTime.now());

        // Then
        assertEquals("Happy", happyEntry.getMood(), "Happy mood should be preserved");
        assertEquals("Sad", sadEntry.getMood(), "Sad mood should be preserved");
        assertEquals("Neutral", neutralEntry.getMood(), "Neutral mood should be preserved");
        assertEquals("😊 Happy", unicodeEntry.getMood(), "Unicode mood should be preserved");
    }

    @Test
    @DisplayName("Should handle lists with mixed content types")
    void testMixedContentLists() {
        // Given
        List<GoalProgress> goals = Arrays.asList(
            new GoalProgress("Goal 1", "Daily", "Complete"),
            new GoalProgress(null, "", "Incomplete"),
            new GoalProgress("Goal 3", "Weekly", null)
        );
        
        List<CompletedItem> items = Arrays.asList(
            new CompletedItem("Task", "Task 1", "Work"),
            new CompletedItem("Event", null, "Personal"),
            new CompletedItem(null, "Item 3", null)
        );
        
        List<WellnessEntry> wellness = Arrays.asList(
            new WellnessEntry("Happy", 3, 8, 2, LocalTime.of(9, 0)),
            new WellnessEntry(null, 0, 0, 0, null),
            new WellnessEntry("Focused", -1, 15, 99, LocalTime.of(23, 59))
        );

        // When
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(goals, items, 66, wellness);

        // Then
        assertEquals(3, outputData.getGoals().size(), "Should preserve all goal entries");
        assertEquals(3, outputData.getCompletedItems().size(), "Should preserve all completed items");
        assertEquals(3, outputData.getWellnessEntries().size(), "Should preserve all wellness entries");
        assertEquals(66, outputData.getCompletionRate(), "Completion rate should be preserved");
    }
}