package use_case.alex.wellness_log_related.add_wellnessLog;

import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.WellnessLogEntry.Levels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for AddWellnessLogInputData.
 * Tests constructor behavior, getter functionality, and null handling.
 */
class AddWellnessLogInputDataTest {

    @Test
    @DisplayName("Should create input data with all valid parameters")
    void testValidCreation() {
        // Given
        LocalDateTime time = LocalDateTime.of(2024, 8, 24, 10, 30);
        Levels stressLevel = Levels.THREE;
        Levels energyLevel = Levels.SEVEN;
        Levels fatigueLevel = Levels.FIVE;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Feeling good today";

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, stressLevel, energyLevel, fatigueLevel, moodLabel, userNote);

        // Then
        assertEquals(time, inputData.getTime(), "Time should match");
        assertEquals(stressLevel, inputData.getStressLevel(), "Stress level should match");
        assertEquals(energyLevel, inputData.getEnergyLevel(), "Energy level should match");
        assertEquals(fatigueLevel, inputData.getFatigueLevel(), "Fatigue level should match");
        assertEquals(moodLabel, inputData.getMoodLabel(), "Mood label should match");
        assertEquals(userNote, inputData.getUserNote(), "User note should match");
    }

    @Test
    @DisplayName("Should accept null time")
    void testNullTime() {
        // Given
        LocalDateTime time = null;
        Levels stressLevel = Levels.ONE;
        Levels energyLevel = Levels.TWO;
        Levels fatigueLevel = Levels.THREE;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test note";

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, stressLevel, energyLevel, fatigueLevel, moodLabel, userNote);

        // Then
        assertNull(inputData.getTime(), "Time should be null");
        assertEquals(stressLevel, inputData.getStressLevel(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null stress level")
    void testNullStressLevel() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stressLevel = null;
        Levels energyLevel = Levels.FOUR;
        Levels fatigueLevel = Levels.FIVE;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test note";

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, stressLevel, energyLevel, fatigueLevel, moodLabel, userNote);

        // Then
        assertNull(inputData.getStressLevel(), "Stress level should be null");
        assertEquals(energyLevel, inputData.getEnergyLevel(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null energy level")
    void testNullEnergyLevel() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stressLevel = Levels.TWO;
        Levels energyLevel = null;
        Levels fatigueLevel = Levels.SIX;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test note";

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, stressLevel, energyLevel, fatigueLevel, moodLabel, userNote);

        // Then
        assertNull(inputData.getEnergyLevel(), "Energy level should be null");
        assertEquals(fatigueLevel, inputData.getFatigueLevel(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null fatigue level")
    void testNullFatigueLevel() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stressLevel = Levels.THREE;
        Levels energyLevel = Levels.SEVEN;
        Levels fatigueLevel = null;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test note";

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, stressLevel, energyLevel, fatigueLevel, moodLabel, userNote);

        // Then
        assertNull(inputData.getFatigueLevel(), "Fatigue level should be null");
        assertEquals(stressLevel, inputData.getStressLevel(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null mood label")
    void testNullMoodLabel() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stressLevel = Levels.FOUR;
        Levels energyLevel = Levels.EIGHT;
        Levels fatigueLevel = Levels.TWO;
        MoodLabelInterf moodLabel = null;
        String userNote = "Test note";

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, stressLevel, energyLevel, fatigueLevel, moodLabel, userNote);

        // Then
        assertNull(inputData.getMoodLabel(), "Mood label should be null");
        assertEquals(userNote, inputData.getUserNote(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept null user note")
    void testNullUserNote() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        Levels stressLevel = Levels.FIVE;
        Levels energyLevel = Levels.NINE;
        Levels fatigueLevel = Levels.ONE;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = null;

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, stressLevel, energyLevel, fatigueLevel, moodLabel, userNote);

        // Then
        assertNull(inputData.getUserNote(), "User note should be null");
        assertEquals(moodLabel, inputData.getMoodLabel(), "Other fields should be preserved");
    }

    @Test
    @DisplayName("Should accept all null parameters")
    void testAllNullParameters() {
        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                null, null, null, null, null, null);

        // Then
        assertNull(inputData.getTime(), "Time should be null");
        assertNull(inputData.getStressLevel(), "Stress level should be null");
        assertNull(inputData.getEnergyLevel(), "Energy level should be null");
        assertNull(inputData.getFatigueLevel(), "Fatigue level should be null");
        assertNull(inputData.getMoodLabel(), "Mood label should be null");
        assertNull(inputData.getUserNote(), "User note should be null");
    }

    @Test
    @DisplayName("Should handle all possible Levels enum values")
    void testAllLevelsValues() {
        LocalDateTime time = LocalDateTime.now();
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test";

        // Test all stress levels
        for (Levels level : Levels.values()) {
            AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                    time, level, Levels.FIVE, Levels.FIVE, moodLabel, userNote);
            assertEquals(level, inputData.getStressLevel(), "Stress level " + level + " should be preserved");
        }

        // Test all energy levels
        for (Levels level : Levels.values()) {
            AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                    time, Levels.FIVE, level, Levels.FIVE, moodLabel, userNote);
            assertEquals(level, inputData.getEnergyLevel(), "Energy level " + level + " should be preserved");
        }

        // Test all fatigue levels
        for (Levels level : Levels.values()) {
            AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                    time, Levels.FIVE, Levels.FIVE, level, moodLabel, userNote);
            assertEquals(level, inputData.getFatigueLevel(), "Fatigue level " + level + " should be preserved");
        }
    }

    @Test
    @DisplayName("Should handle various time values")
    void testVariousTimeValues() {
        Levels defaultLevel = Levels.FIVE;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test";

        // Test past time
        LocalDateTime pastTime = LocalDateTime.of(2020, 1, 1, 12, 0);
        AddWellnessLogInputData pastInput = new AddWellnessLogInputData(
                pastTime, defaultLevel, defaultLevel, defaultLevel, moodLabel, userNote);
        assertEquals(pastTime, pastInput.getTime(), "Past time should be preserved");

        // Test future time
        LocalDateTime futureTime = LocalDateTime.of(2030, 12, 31, 23, 59);
        AddWellnessLogInputData futureInput = new AddWellnessLogInputData(
                futureTime, defaultLevel, defaultLevel, defaultLevel, moodLabel, userNote);
        assertEquals(futureTime, futureInput.getTime(), "Future time should be preserved");

        // Test current time
        LocalDateTime currentTime = LocalDateTime.now();
        AddWellnessLogInputData currentInput = new AddWellnessLogInputData(
                currentTime, defaultLevel, defaultLevel, defaultLevel, moodLabel, userNote);
        assertEquals(currentTime, currentInput.getTime(), "Current time should be preserved");
    }

    @Test
    @DisplayName("Should handle empty and whitespace user notes")
    void testUserNoteVariations() {
        LocalDateTime time = LocalDateTime.now();
        Levels defaultLevel = Levels.FIVE;
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);

        // Test empty string
        AddWellnessLogInputData emptyInput = new AddWellnessLogInputData(
                time, defaultLevel, defaultLevel, defaultLevel, moodLabel, "");
        assertEquals("", emptyInput.getUserNote(), "Empty user note should be preserved");

        // Test whitespace string
        String whitespaceNote = "   ";
        AddWellnessLogInputData whitespaceInput = new AddWellnessLogInputData(
                time, defaultLevel, defaultLevel, defaultLevel, moodLabel, whitespaceNote);
        assertEquals(whitespaceNote, whitespaceInput.getUserNote(), "Whitespace user note should be preserved");

        // Test long note
        String longNote = "A".repeat(500);
        AddWellnessLogInputData longInput = new AddWellnessLogInputData(
                time, defaultLevel, defaultLevel, defaultLevel, moodLabel, longNote);
        assertEquals(longNote, longInput.getUserNote(), "Long user note should be preserved");

        // Test note with special characters
        String specialNote = "Note with émojis 😀 and symbols !@#$%^&*()";
        AddWellnessLogInputData specialInput = new AddWellnessLogInputData(
                time, defaultLevel, defaultLevel, defaultLevel, moodLabel, specialNote);
        assertEquals(specialNote, specialInput.getUserNote(), "Special character note should be preserved");
    }

    @Test
    @DisplayName("Should be immutable after creation")
    void testImmutability() {
        // Given
        LocalDateTime originalTime = LocalDateTime.of(2024, 8, 24, 10, 30);
        Levels originalStressLevel = Levels.THREE;
        Levels originalEnergyLevel = Levels.SEVEN;
        Levels originalFatigueLevel = Levels.FIVE;
        MoodLabelInterf originalMoodLabel = mock(MoodLabelInterf.class);
        String originalNote = "Original note";

        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                originalTime, originalStressLevel, originalEnergyLevel, 
                originalFatigueLevel, originalMoodLabel, originalNote);

        // Then - values should remain unchanged
        assertEquals(originalTime, inputData.getTime(), "Time should remain unchanged");
        assertEquals(originalStressLevel, inputData.getStressLevel(), "Stress level should remain unchanged");
        assertEquals(originalEnergyLevel, inputData.getEnergyLevel(), "Energy level should remain unchanged");
        assertEquals(originalFatigueLevel, inputData.getFatigueLevel(), "Fatigue level should remain unchanged");
        assertEquals(originalMoodLabel, inputData.getMoodLabel(), "Mood label should remain unchanged");
        assertEquals(originalNote, inputData.getUserNote(), "User note should remain unchanged");
    }

    @Test
    @DisplayName("Should create multiple distinct instances")
    void testMultipleInstances() {
        // Given
        AddWellnessLogInputData inputData1 = new AddWellnessLogInputData(
                LocalDateTime.now(), Levels.ONE, Levels.TWO, Levels.THREE, mock(MoodLabelInterf.class), "Note1");
        AddWellnessLogInputData inputData2 = new AddWellnessLogInputData(
                LocalDateTime.now(), Levels.FOUR, Levels.FIVE, Levels.SIX, mock(MoodLabelInterf.class), "Note2");

        // Then
        assertNotSame(inputData1, inputData2, "Should be different object instances");
        assertNotEquals(inputData1.getStressLevel(), inputData2.getStressLevel(), "Should have different stress levels");
        assertNotEquals(inputData1.getUserNote(), inputData2.getUserNote(), "Should have different notes");
    }

    @Test
    @DisplayName("Should preserve exact parameter references where applicable")
    void testParameterReferencePreservation() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test note";

        // When
        AddWellnessLogInputData inputData = new AddWellnessLogInputData(
                time, Levels.FIVE, Levels.FIVE, Levels.FIVE, moodLabel, userNote);

        // Then - Should preserve references for object types
        assertSame(time, inputData.getTime(), "Time reference should be preserved");
        assertSame(moodLabel, inputData.getMoodLabel(), "Mood label reference should be preserved");
        assertSame(userNote, inputData.getUserNote(), "User note reference should be preserved");
    }

    @Test
    @DisplayName("Should handle extreme enum values")
    void testExtremeEnumValues() {
        // Given
        LocalDateTime time = LocalDateTime.now();
        MoodLabelInterf moodLabel = mock(MoodLabelInterf.class);
        String userNote = "Test";

        // Test minimum and maximum enum values
        AddWellnessLogInputData minInput = new AddWellnessLogInputData(
                time, Levels.ONE, Levels.ONE, Levels.ONE, moodLabel, userNote);
        
        AddWellnessLogInputData maxInput = new AddWellnessLogInputData(
                time, Levels.TEN, Levels.TEN, Levels.TEN, moodLabel, userNote);

        // Then
        assertEquals(Levels.ONE, minInput.getStressLevel(), "Minimum stress level should be preserved");
        assertEquals(Levels.ONE, minInput.getEnergyLevel(), "Minimum energy level should be preserved");
        assertEquals(Levels.ONE, minInput.getFatigueLevel(), "Minimum fatigue level should be preserved");
        
        assertEquals(Levels.TEN, maxInput.getStressLevel(), "Maximum stress level should be preserved");
        assertEquals(Levels.TEN, maxInput.getEnergyLevel(), "Maximum energy level should be preserved");
        assertEquals(Levels.TEN, maxInput.getFatigueLevel(), "Maximum fatigue level should be preserved");
    }
}