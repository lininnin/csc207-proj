package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import entity.alex.MoodLabel.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogInputBoundary;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditWellnessLogControllerTest {

    @Mock
    private EditWellnessLogInputBoundary mockInteractor;

    private EditWellnessLogController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EditWellnessLogController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        // Constructor coverage is achieved by setUp() method
    }

    @Test
    void testExecuteWithValidPositiveMood() {
        // Arrange
        String logId = "log-123";
        int energyLevel = 8;
        int stressLevel = 3;
        int fatigueLevel = 2;
        String moodName = "Happy";
        Type moodType = Type.Positive;
        String note = "Feeling great today!";

        // Act
        controller.execute(logId, energyLevel, stressLevel, fatigueLevel, moodName, moodType, note);

        // Assert
        ArgumentCaptor<EditWellnessLogInputData> captor = ArgumentCaptor.forClass(EditWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
        assertEquals(energyLevel, capturedInputData.getEnergyLevel());
        assertEquals(stressLevel, capturedInputData.getStressLevel());
        assertEquals(fatigueLevel, capturedInputData.getFatigueLevel());
        assertEquals(moodName, capturedInputData.getMoodName());
        assertEquals(moodType, capturedInputData.getMoodType());
        assertEquals(note, capturedInputData.getNote());
    }

    @Test
    void testExecuteWithValidNegativeMood() {
        // Arrange
        String logId = "wellness-456";
        int energyLevel = 4;
        int stressLevel = 7;
        int fatigueLevel = 8;
        String moodName = "Anxious";
        Type moodType = Type.Negative;
        String note = "Had a tough day at work";

        // Act
        controller.execute(logId, energyLevel, stressLevel, fatigueLevel, moodName, moodType, note);

        // Assert
        ArgumentCaptor<EditWellnessLogInputData> captor = ArgumentCaptor.forClass(EditWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
        assertEquals(energyLevel, capturedInputData.getEnergyLevel());
        assertEquals(stressLevel, capturedInputData.getStressLevel());
        assertEquals(fatigueLevel, capturedInputData.getFatigueLevel());
        assertEquals(moodName, capturedInputData.getMoodName());
        assertEquals(moodType, capturedInputData.getMoodType());
        assertEquals(note, capturedInputData.getNote());
    }

    @Test
    void testExecuteWithMinimumValues() {
        // Arrange
        String logId = "min-log";
        int energyLevel = 1;
        int stressLevel = 1;
        int fatigueLevel = 1;
        String moodName = "Calm";
        Type moodType = Type.Positive;
        String note = "";

        // Act
        controller.execute(logId, energyLevel, stressLevel, fatigueLevel, moodName, moodType, note);

        // Assert
        ArgumentCaptor<EditWellnessLogInputData> captor = ArgumentCaptor.forClass(EditWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
        assertEquals(energyLevel, capturedInputData.getEnergyLevel());
        assertEquals(stressLevel, capturedInputData.getStressLevel());
        assertEquals(fatigueLevel, capturedInputData.getFatigueLevel());
        assertEquals(moodName, capturedInputData.getMoodName());
        assertEquals(moodType, capturedInputData.getMoodType());
        assertEquals(note, capturedInputData.getNote());
    }

    @Test
    void testExecuteWithMaximumValues() {
        // Arrange
        String logId = "max-log";
        int energyLevel = 10;
        int stressLevel = 10;
        int fatigueLevel = 10;
        String moodName = "Overwhelmed";
        Type moodType = Type.Negative;
        String note = "This is a very long note describing all the feelings and experiences of today";

        // Act
        controller.execute(logId, energyLevel, stressLevel, fatigueLevel, moodName, moodType, note);

        // Assert
        ArgumentCaptor<EditWellnessLogInputData> captor = ArgumentCaptor.forClass(EditWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(logId, capturedInputData.getLogId());
        assertEquals(energyLevel, capturedInputData.getEnergyLevel());
        assertEquals(stressLevel, capturedInputData.getStressLevel());
        assertEquals(fatigueLevel, capturedInputData.getFatigueLevel());
        assertEquals(moodName, capturedInputData.getMoodName());
        assertEquals(moodType, capturedInputData.getMoodType());
        assertEquals(note, capturedInputData.getNote());
    }

    @Test
    void testExecuteWithNullValues() {
        // Act
        controller.execute(null, 5, 5, 5, null, null, null);

        // Assert
        ArgumentCaptor<EditWellnessLogInputData> captor = ArgumentCaptor.forClass(EditWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditWellnessLogInputData capturedInputData = captor.getValue();
        assertNull(capturedInputData.getLogId());
        assertEquals(5, capturedInputData.getEnergyLevel());
        assertEquals(5, capturedInputData.getStressLevel());
        assertEquals(5, capturedInputData.getFatigueLevel());
        assertNull(capturedInputData.getMoodName());
        assertNull(capturedInputData.getMoodType());
        assertNull(capturedInputData.getNote());
    }

    @Test
    void testExecuteWithUUIDLogId() {
        // Arrange
        String uuidLogId = "550e8400-e29b-41d4-a716-446655440000";
        int energyLevel = 6;
        int stressLevel = 4;
        int fatigueLevel = 3;
        String moodName = "Content";
        Type moodType = Type.Positive;
        String note = "UUID test";

        // Act
        controller.execute(uuidLogId, energyLevel, stressLevel, fatigueLevel, moodName, moodType, note);

        // Assert
        ArgumentCaptor<EditWellnessLogInputData> captor = ArgumentCaptor.forClass(EditWellnessLogInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditWellnessLogInputData capturedInputData = captor.getValue();
        assertEquals(uuidLogId, capturedInputData.getLogId());
        assertEquals(energyLevel, capturedInputData.getEnergyLevel());
        assertEquals(stressLevel, capturedInputData.getStressLevel());
        assertEquals(fatigueLevel, capturedInputData.getFatigueLevel());
        assertEquals(moodName, capturedInputData.getMoodName());
        assertEquals(moodType, capturedInputData.getMoodType());
        assertEquals(note, capturedInputData.getNote());
    }

    @Test
    void testMultipleExecuteCalls() {
        // Arrange
        String logId1 = "log-001";
        String logId2 = "log-002";

        // Act
        controller.execute(logId1, 7, 3, 2, "Excited", Type.Positive, "First call");
        controller.execute(logId2, 4, 8, 6, "Stressed", Type.Negative, "Second call");

        // Assert
        ArgumentCaptor<EditWellnessLogInputData> captor = ArgumentCaptor.forClass(EditWellnessLogInputData.class);
        verify(mockInteractor, times(2)).execute(captor.capture());

        var capturedValues = captor.getAllValues();
        assertEquals(logId1, capturedValues.get(0).getLogId());
        assertEquals(logId2, capturedValues.get(1).getLogId());
        assertEquals("Excited", capturedValues.get(0).getMoodName());
        assertEquals("Stressed", capturedValues.get(1).getMoodName());
    }
}