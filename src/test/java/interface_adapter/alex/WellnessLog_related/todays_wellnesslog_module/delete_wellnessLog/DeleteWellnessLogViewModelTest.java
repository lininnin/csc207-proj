package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteWellnessLogViewModelTest {

    private DeleteWellnessLogViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new DeleteWellnessLogViewModel();
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("DeleteWellnessLogView", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertEquals("", viewModel.getState().getDeletedLogId());
        assertEquals("", viewModel.getState().getDeleteError());
    }

    @Test
    void testPropertyNameConstant() {
        assertEquals("deleteWellnessLog", DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY);
    }

    @Test
    void testStateManagement() {
        // Create new state
        DeleteWellnessLogState newState = new DeleteWellnessLogState();
        newState.setDeletedLogId("log-123");
        newState.setDeleteError("Test error");

        // Set state
        viewModel.setState(newState);

        // Verify state is set correctly
        assertEquals("log-123", viewModel.getState().getDeletedLogId());
        assertEquals("Test error", viewModel.getState().getDeleteError());
    }

    @Test
    void testSetStateFiresPropertyChange() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        DeleteWellnessLogState newState = new DeleteWellnessLogState();
        newState.setDeletedLogId("test-log-id");

        // Act
        viewModel.setState(newState);

        // Assert
        verify(mockListener, atLeastOnce()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void testFirePropertyChangedWithSpecificProperty() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);

        // Act
        viewModel.firePropertyChanged(DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY);

        // Assert
        verify(mockListener, times(1)).propertyChange(argThat(event ->
            DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY.equals(event.getPropertyName())
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
        DeleteWellnessLogState state1 = new DeleteWellnessLogState();
        state1.setDeletedLogId("log-001");
        state1.setDeleteError("");
        
        viewModel.setState(state1);
        assertEquals("log-001", viewModel.getState().getDeletedLogId());
        assertEquals("", viewModel.getState().getDeleteError());

        // Second state
        DeleteWellnessLogState state2 = new DeleteWellnessLogState();
        state2.setDeletedLogId("log-002");
        state2.setDeleteError("Delete failed");
        
        viewModel.setState(state2);
        assertEquals("log-002", viewModel.getState().getDeletedLogId());
        assertEquals("Delete failed", viewModel.getState().getDeleteError());
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
        viewModel.firePropertyChanged(DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY);
        
        // Assert both listeners are notified
        verify(listener1, times(1)).propertyChange(any());
        verify(listener2, times(1)).propertyChange(any());
        
        // Remove one listener
        viewModel.removePropertyChangeListener(listener1);
        
        // Fire again
        viewModel.firePropertyChanged(DeleteWellnessLogViewModel.DELETE_WELLNESS_LOG_PROPERTY);
        
        // Assert only remaining listener is notified
        verify(listener1, times(1)).propertyChange(any()); // Still only 1 call
        verify(listener2, times(2)).propertyChange(any()); // Now 2 calls
    }
}