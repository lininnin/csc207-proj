package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditWellnessLogViewModelTest {

    private EditWellnessLogViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new EditWellnessLogViewModel();
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals(EditWellnessLogViewModel.EDIT_WELLNESS_LOG_PROPERTY, viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertEquals("", viewModel.getState().getLogId());
        assertEquals("", viewModel.getState().getNote());
        assertEquals("", viewModel.getState().getErrorMessage());
    }

    @Test
    void testPropertyNameConstant() {
        assertEquals("editWellnessLog", EditWellnessLogViewModel.EDIT_WELLNESS_LOG_PROPERTY);
    }

    @Test
    void testStateManagement() {
        // Create new state
        EditWellnessLogState newState = new EditWellnessLogState();
        newState.setLogId("log-123");
        newState.setMoodLevel("Happy");
        newState.setEnergyLevel(8);
        newState.setStressLevel(3);
        newState.setFatigueLevel(2);
        newState.setNote("Test note");
        newState.setErrorMessage("Test error");

        // Set state
        viewModel.setState(newState);

        // Verify state is set correctly
        assertEquals("log-123", viewModel.getState().getLogId());
        assertEquals("Happy", viewModel.getState().getMoodLabel());
        assertEquals(8, viewModel.getState().getEnergyLevel());
        assertEquals(3, viewModel.getState().getStressLevel());
        assertEquals(2, viewModel.getState().getFatigueLevel());
        assertEquals("Test note", viewModel.getState().getNote());
        assertEquals("Test error", viewModel.getState().getErrorMessage());
    }

    @Test
    void testSetStateFiresPropertyChange() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        EditWellnessLogState newState = new EditWellnessLogState();
        newState.setLogId("test-state-id");

        // Act
        viewModel.setState(newState);

        // Assert - base ViewModel fires "state" property change
        verify(mockListener, atLeastOnce()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void testFireStateChanged() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);

        // Act
        viewModel.fireStateChanged();

        // Assert
        verify(mockListener, times(1)).propertyChange(argThat(event ->
            EditWellnessLogViewModel.EDIT_WELLNESS_LOG_PROPERTY.equals(event.getPropertyName())
        ));
    }

    @Test
    void testFirePropertyChangedWithSpecificProperty() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);

        // Act
        viewModel.firePropertyChanged(EditWellnessLogViewModel.EDIT_WELLNESS_LOG_PROPERTY);

        // Assert
        verify(mockListener, times(1)).propertyChange(argThat(event ->
            EditWellnessLogViewModel.EDIT_WELLNESS_LOG_PROPERTY.equals(event.getPropertyName())
        ));
    }

    @Test
    void testFirePropertyChangedDefault() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        verify(mockListener, times(1)).propertyChange(argThat(event ->
            "state".equals(event.getPropertyName())
        ));
    }

    @Test
    void testMultipleStateUpdates() {
        // First state
        EditWellnessLogState state1 = new EditWellnessLogState();
        state1.setLogId("log-001");
        state1.setEnergyLevel(5);
        
        viewModel.setState(state1);
        assertEquals("log-001", viewModel.getState().getLogId());
        assertEquals(5, viewModel.getState().getEnergyLevel());

        // Second state
        EditWellnessLogState state2 = new EditWellnessLogState();
        state2.setLogId("log-002");
        state2.setEnergyLevel(8);
        state2.setErrorMessage("Update error");
        
        viewModel.setState(state2);
        assertEquals("log-002", viewModel.getState().getLogId());
        assertEquals(8, viewModel.getState().getEnergyLevel());
        assertEquals("Update error", viewModel.getState().getErrorMessage());
    }

    @Test
    void testAddPropertyChangeListenerOverride() {
        // Arrange
        PropertyChangeListener listener1 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener2 = mock(PropertyChangeListener.class);
        
        // Add listeners using the overridden method
        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);
        
        // Act
        viewModel.fireStateChanged();
        
        // Assert both listeners are notified
        verify(listener1, times(1)).propertyChange(any());
        verify(listener2, times(1)).propertyChange(any());
    }

    @Test
    void testPropertyChangeListenerManagement() {
        // Arrange
        PropertyChangeListener listener1 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener2 = mock(PropertyChangeListener.class);
        
        // Add listeners
        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);
        
        // Act
        viewModel.fireStateChanged();
        
        // Assert both listeners are notified
        verify(listener1, times(1)).propertyChange(any());
        verify(listener2, times(1)).propertyChange(any());
        
        // Remove one listener
        viewModel.removePropertyChangeListener(listener1);
        
        // Fire again
        viewModel.fireStateChanged();
        
        // Assert only remaining listener is notified
        verify(listener1, times(1)).propertyChange(any()); // Still only 1 call
        verify(listener2, times(2)).propertyChange(any()); // Now 2 calls
    }

    @Test
    void testStateChangedWithComplexState() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        EditWellnessLogState complexState = new EditWellnessLogState();
        complexState.setLogId("complex-log-id");
        complexState.setMoodLevel("Anxious");
        complexState.setEnergyLevel(3);
        complexState.setStressLevel(9);
        complexState.setFatigueLevel(8);
        complexState.setNote("Very detailed note about wellness state");
        complexState.setErrorMessage("Validation failed for multiple fields");
        
        // Act
        viewModel.setState(complexState);
        viewModel.fireStateChanged();

        // Assert
        verify(mockListener, atLeastOnce()).propertyChange(any());
        
        // Verify state is correctly maintained
        EditWellnessLogState resultState = viewModel.getState();
        assertEquals("complex-log-id", resultState.getLogId());
        assertEquals("Anxious", resultState.getMoodLabel());
        assertEquals(3, resultState.getEnergyLevel());
        assertEquals(9, resultState.getStressLevel());
        assertEquals(8, resultState.getFatigueLevel());
        assertEquals("Very detailed note about wellness state", resultState.getNote());
        assertEquals("Validation failed for multiple fields", resultState.getErrorMessage());
    }
}