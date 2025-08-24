package interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel.EditMoodLabelInputBoundary;
import use_case.alex.wellness_log_related.moodlabel_related.edit_moodLabel.EditMoodLabelInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditMoodLabelControllerTest {

    @Mock
    private EditMoodLabelInputBoundary mockInteractor;

    private EditMoodLabelController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EditMoodLabelController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
        // Constructor coverage is achieved by setUp() method
    }

    @Test
    void testExecuteWithValidInput() {
        // Arrange
        String originalName = "Happy";
        String newName = "Joyful";
        String newType = "Positive";

        // Act
        controller.execute(originalName, newName, newType);

        // Assert
        ArgumentCaptor<EditMoodLabelInputData> captor = ArgumentCaptor.forClass(EditMoodLabelInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditMoodLabelInputData capturedInputData = captor.getValue();
        assertEquals(originalName, capturedInputData.getOriginalName());
        assertEquals(newName, capturedInputData.getNewName());
        assertEquals(newType, capturedInputData.getNewType());
    }

    @Test
    void testExecuteWithNegativeType() {
        // Arrange
        String originalName = "Sad";
        String newName = "Depressed";
        String newType = "Negative";

        // Act
        controller.execute(originalName, newName, newType);

        // Assert
        ArgumentCaptor<EditMoodLabelInputData> captor = ArgumentCaptor.forClass(EditMoodLabelInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditMoodLabelInputData capturedInputData = captor.getValue();
        assertEquals(originalName, capturedInputData.getOriginalName());
        assertEquals(newName, capturedInputData.getNewName());
        assertEquals(newType, capturedInputData.getNewType());
    }

    @Test
    void testExecuteWithSameName() {
        // Arrange
        String originalName = "Neutral";
        String newName = "Neutral";
        String newType = "Positive";

        // Act
        controller.execute(originalName, newName, newType);

        // Assert
        ArgumentCaptor<EditMoodLabelInputData> captor = ArgumentCaptor.forClass(EditMoodLabelInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditMoodLabelInputData capturedInputData = captor.getValue();
        assertEquals(originalName, capturedInputData.getOriginalName());
        assertEquals(newName, capturedInputData.getNewName());
        assertEquals(newType, capturedInputData.getNewType());
    }

    @Test
    void testExecuteWithEmptyStrings() {
        // Arrange
        String originalName = "";
        String newName = "";
        String newType = "";

        // Act
        controller.execute(originalName, newName, newType);

        // Assert
        ArgumentCaptor<EditMoodLabelInputData> captor = ArgumentCaptor.forClass(EditMoodLabelInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditMoodLabelInputData capturedInputData = captor.getValue();
        assertEquals(originalName, capturedInputData.getOriginalName());
        assertEquals(newName, capturedInputData.getNewName());
        assertEquals(newType, capturedInputData.getNewType());
    }

    @Test
    void testExecuteWithNullValues() {
        // Act
        controller.execute(null, null, null);

        // Assert
        ArgumentCaptor<EditMoodLabelInputData> captor = ArgumentCaptor.forClass(EditMoodLabelInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditMoodLabelInputData capturedInputData = captor.getValue();
        assertNull(capturedInputData.getOriginalName());
        assertNull(capturedInputData.getNewName());
        assertNull(capturedInputData.getNewType());
    }
}