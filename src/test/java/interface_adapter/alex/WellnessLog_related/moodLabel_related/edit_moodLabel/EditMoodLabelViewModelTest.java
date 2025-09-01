package interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditMoodLabelViewModelTest {

    private EditMoodLabelViewModel viewModel;
    private PropertyChangeListener mockListener;

    @BeforeEach
    void setUp() {
        viewModel = new EditMoodLabelViewModel();
        mockListener = mock(PropertyChangeListener.class);
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("Edit Mood Label View", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertEquals("", viewModel.getState().getName());
        assertEquals("", viewModel.getState().getType());
    }

    @Test
    void testUpdateStateAndFiresPropertyChange() {
        // Arrange
        viewModel.addPropertyChangeListener(mockListener);
        EditMoodLabelState newState = new EditMoodLabelState();
        newState.setName("Happy");
        newState.setType("Positive");

        // Act
        viewModel.updateState(newState);

        // Assert
        assertEquals("Happy", viewModel.getState().getName());
        assertEquals("Positive", viewModel.getState().getType());
        
        ArgumentCaptor<PropertyChangeEvent> captor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(mockListener, times(2)).propertyChange(captor.capture());
        
        // Get the second event (which should be the specific property we care about)
        PropertyChangeEvent event = captor.getAllValues().get(1);
        assertEquals(EditMoodLabelViewModel.EDIT_MOOD_LABEL_STATE_PROPERTY, event.getPropertyName());
    }

    @Test
    void testUpdateStateWithNullValues() {
        // Arrange
        viewModel.addPropertyChangeListener(mockListener);
        EditMoodLabelState newState = new EditMoodLabelState();
        newState.setName(null);
        newState.setType(null);
        newState.setEditError("Some error");

        // Act
        viewModel.updateState(newState);

        // Assert
        assertNull(viewModel.getState().getName());
        assertNull(viewModel.getState().getType());
        assertEquals("Some error", viewModel.getState().getEditError());
        verify(mockListener, times(2)).propertyChange(any());
    }

    @Test
    void testUpdateStateFiresCorrectPropertyName() {
        // Arrange
        viewModel.addPropertyChangeListener(mockListener);
        EditMoodLabelState newState = new EditMoodLabelState();

        // Act
        viewModel.updateState(newState);

        // Assert
        ArgumentCaptor<PropertyChangeEvent> captor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
        verify(mockListener, times(2)).propertyChange(captor.capture());
        // Check the specific property name from the second event
        assertEquals(EditMoodLabelViewModel.EDIT_MOOD_LABEL_STATE_PROPERTY, captor.getAllValues().get(1).getPropertyName());
    }

    @Test
    void testPropertyNameConstant() {
        assertEquals("editMoodLabelState", EditMoodLabelViewModel.EDIT_MOOD_LABEL_STATE_PROPERTY);
    }

    @Test
    void testMultipleStateUpdates() {
        // Arrange
        viewModel.addPropertyChangeListener(mockListener);

        EditMoodLabelState state1 = new EditMoodLabelState();
        state1.setName("Happy");
        state1.setType("Positive");

        EditMoodLabelState state2 = new EditMoodLabelState();
        state2.setName("Sad");
        state2.setType("Negative");

        // Act
        viewModel.updateState(state1);
        viewModel.updateState(state2);

        // Assert
        assertEquals("Sad", viewModel.getState().getName());
        assertEquals("Negative", viewModel.getState().getType());
        verify(mockListener, times(4)).propertyChange(any()); // 2 updates * 2 events each
    }

    @Test
    void testUpdateStateWithErrorState() {
        // Arrange
        viewModel.addPropertyChangeListener(mockListener);
        EditMoodLabelState errorState = new EditMoodLabelState();
        errorState.setName("FailedLabel");
        errorState.setType("InvalidType");
        errorState.setEditError("Edit failed: input may be invalid or label not found.");

        // Act
        viewModel.updateState(errorState);

        // Assert
        assertEquals("FailedLabel", viewModel.getState().getName());
        assertEquals("InvalidType", viewModel.getState().getType());
        assertEquals("Edit failed: input may be invalid or label not found.", viewModel.getState().getEditError());
        verify(mockListener, times(2)).propertyChange(any());
    }
}