package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.alex.WellnessLogEntry.Levels;
import entity.alex.MoodLabel.MoodLabelInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditWellnessLogStateTest {

    @Mock
    private WellnessLogEntryInterf mockEntry;
    @Mock
    private MoodLabelInterf mockMoodLabel;

    private EditWellnessLogState state;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        state = new EditWellnessLogState();
    }

    @Test
    void testDefaultConstructor() {
        assertEquals("", state.getLogId());
        assertNull(state.getMoodLabel());
        assertEquals(0, state.getEnergyLevel());
        assertEquals(0, state.getStressLevel());
        assertEquals(0, state.getFatigueLevel());
        assertEquals("", state.getNote());
        assertEquals("", state.getErrorMessage());
    }

    @Test
    void testConstructorWithWellnessLogEntry() {
        // Arrange
        when(mockEntry.getId()).thenReturn("entry-123");
        when(mockEntry.getMoodLabel()).thenReturn(mockMoodLabel);
        when(mockMoodLabel.toString()).thenReturn("Happy");
        when(mockEntry.getEnergyLevel()).thenReturn(Levels.EIGHT);
        when(mockEntry.getStressLevel()).thenReturn(Levels.THREE);
        when(mockEntry.getFatigueLevel()).thenReturn(Levels.TWO);
        when(mockEntry.getUserNote()).thenReturn("Feeling great today!");

        // Act
        EditWellnessLogState stateWithEntry = new EditWellnessLogState(mockEntry);

        // Assert
        assertEquals("entry-123", stateWithEntry.getLogId());
        assertEquals("Happy", stateWithEntry.getMoodLabel());
        assertEquals(8, stateWithEntry.getEnergyLevel());
        assertEquals(3, stateWithEntry.getStressLevel());
        assertEquals(2, stateWithEntry.getFatigueLevel());
        assertEquals("Feeling great today!", stateWithEntry.getNote());
        assertEquals("", stateWithEntry.getErrorMessage());
    }

    @Test
    void testConstructorWithNullEntry() {
        // Act
        EditWellnessLogState stateWithNull = new EditWellnessLogState(null);

        // Assert
        assertEquals("", stateWithNull.getLogId());
        assertNull(stateWithNull.getMoodLabel());
        assertEquals(0, stateWithNull.getEnergyLevel());
        assertEquals(0, stateWithNull.getStressLevel());
        assertEquals(0, stateWithNull.getFatigueLevel());
        assertEquals("", stateWithNull.getNote());
        assertEquals("", stateWithNull.getErrorMessage());
    }

    @Test
    void testLogIdSetterAndGetter() {
        state.setLogId("wellness-456");
        assertEquals("wellness-456", state.getLogId());
    }

    @Test
    void testLogIdWithWhitespace() {
        state.setLogId("  log-id-789  ");
        assertEquals("log-id-789", state.getLogId()); // Should be trimmed
    }

    @Test
    void testLogIdWithNull() {
        state.setLogId(null);
        assertEquals("", state.getLogId()); // Should default to empty string
    }

    @Test
    void testMoodLabelSetterAndGetter() {
        state.setMoodLevel("Excited");
        assertEquals("Excited", state.getMoodLabel());
    }

    @Test
    void testMoodLabelWithNull() {
        state.setMoodLevel(null);
        assertNull(state.getMoodLabel());
    }

    @Test
    void testEnergyLevelSetterAndGetter() {
        state.setEnergyLevel(7);
        assertEquals(7, state.getEnergyLevel());
    }

    @Test
    void testEnergyLevelWithMinMaxValues() {
        state.setEnergyLevel(1);
        assertEquals(1, state.getEnergyLevel());

        state.setEnergyLevel(10);
        assertEquals(10, state.getEnergyLevel());

        state.setEnergyLevel(0);
        assertEquals(0, state.getEnergyLevel());
    }

    @Test
    void testStressLevelSetterAndGetter() {
        state.setStressLevel(5);
        assertEquals(5, state.getStressLevel());
    }

    @Test
    void testStressLevelWithMinMaxValues() {
        state.setStressLevel(1);
        assertEquals(1, state.getStressLevel());

        state.setStressLevel(10);
        assertEquals(10, state.getStressLevel());

        state.setStressLevel(0);
        assertEquals(0, state.getStressLevel());
    }

    @Test
    void testFatigueLevelSetterAndGetter() {
        state.setFatigueLevel(6);
        assertEquals(6, state.getFatigueLevel());
    }

    @Test
    void testFatigueLevelWithMinMaxValues() {
        state.setFatigueLevel(1);
        assertEquals(1, state.getFatigueLevel());

        state.setFatigueLevel(10);
        assertEquals(10, state.getFatigueLevel());

        state.setFatigueLevel(0);
        assertEquals(0, state.getFatigueLevel());
    }

    @Test
    void testNoteSetterAndGetter() {
        String note = "Had a productive day at work";
        state.setNote(note);
        assertEquals(note, state.getNote());
    }

    @Test
    void testNoteWithWhitespace() {
        state.setNote("  My note with spaces  ");
        assertEquals("My note with spaces", state.getNote()); // Should be trimmed
    }

    @Test
    void testNoteWithNull() {
        state.setNote(null);
        assertEquals("", state.getNote()); // Should default to empty string
    }

    @Test
    void testErrorMessageSetterAndGetter() {
        String errorMessage = "Invalid energy level range";
        state.setErrorMessage(errorMessage);
        assertEquals(errorMessage, state.getErrorMessage());
    }

    @Test
    void testErrorMessageWithWhitespace() {
        state.setErrorMessage("  Error message with spaces  ");
        assertEquals("Error message with spaces", state.getErrorMessage()); // Should be trimmed
    }

    @Test
    void testErrorMessageWithNull() {
        state.setErrorMessage(null);
        assertEquals("", state.getErrorMessage()); // Should default to empty string
    }

    @Test
    void testAllFieldsIndependence() {
        // Set all fields to different values
        state.setLogId("test-log");
        state.setMoodLevel("Content");
        state.setEnergyLevel(7);
        state.setStressLevel(4);
        state.setFatigueLevel(3);
        state.setNote("Test note");
        state.setErrorMessage("Test error");

        // Verify they are all independent and correct
        assertEquals("test-log", state.getLogId());
        assertEquals("Content", state.getMoodLabel());
        assertEquals(7, state.getEnergyLevel());
        assertEquals(4, state.getStressLevel());
        assertEquals(3, state.getFatigueLevel());
        assertEquals("Test note", state.getNote());
        assertEquals("Test error", state.getErrorMessage());

        // Change one and verify others remain unchanged
        state.setEnergyLevel(9);
        assertEquals("test-log", state.getLogId()); // Unchanged
        assertEquals("Content", state.getMoodLabel()); // Unchanged
        assertEquals(9, state.getEnergyLevel()); // Changed
        assertEquals(4, state.getStressLevel()); // Unchanged
        assertEquals(3, state.getFatigueLevel()); // Unchanged
        assertEquals("Test note", state.getNote()); // Unchanged
        assertEquals("Test error", state.getErrorMessage()); // Unchanged
    }

    @Test
    void testConstructorWithEntryNullFields() {
        // Arrange - entry with some null fields
        when(mockEntry.getId()).thenReturn(null);
        when(mockEntry.getMoodLabel()).thenReturn(mockMoodLabel);
        when(mockMoodLabel.toString()).thenReturn(null);
        when(mockEntry.getEnergyLevel()).thenReturn(Levels.FIVE);
        when(mockEntry.getStressLevel()).thenReturn(Levels.SIX);
        when(mockEntry.getFatigueLevel()).thenReturn(Levels.SEVEN);
        when(mockEntry.getUserNote()).thenReturn(null);

        // Act
        EditWellnessLogState stateWithNulls = new EditWellnessLogState(mockEntry);

        // Assert
        assertNull(stateWithNulls.getLogId());
        assertNull(stateWithNulls.getMoodLabel());
        assertEquals(5, stateWithNulls.getEnergyLevel());
        assertEquals(6, stateWithNulls.getStressLevel());
        assertEquals(7, stateWithNulls.getFatigueLevel());
        assertNull(stateWithNulls.getNote());
    }

}