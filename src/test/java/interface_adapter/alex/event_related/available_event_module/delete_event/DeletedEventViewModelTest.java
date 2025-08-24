package interface_adapter.alex.event_related.available_event_module.delete_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeletedEventViewModelTest {

    private DeletedEventViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new DeletedEventViewModel();
    }

    @Test
    void testConstructorInitializesCorrectly() {
        assertEquals("deleted event view", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertNull(viewModel.getState().getDeletedEventId());
        assertNull(viewModel.getState().getDeletedEventName());
        assertFalse(viewModel.getState().isDeletedSuccessfully());
        assertNull(viewModel.getState().getDeleteError());
    }

    @Test
    void testPropertyNameConstant() {
        assertEquals("deletedEventState", DeletedEventViewModel.DELETE_EVENT_STATE_PROPERTY);
    }

    @Test
    void testStateManagement() {
        // Create new state
        DeletedEventState newState = new DeletedEventState();
        newState.setDeletedEventId("event-123");
        newState.setDeletedEventName("Test Event");
        newState.setDeletedSuccessfully(true);
        newState.setDeleteError(null);

        // Set state
        viewModel.setState(newState);

        // Verify state is set correctly
        assertEquals("event-123", viewModel.getState().getDeletedEventId());
        assertEquals("Test Event", viewModel.getState().getDeletedEventName());
        assertTrue(viewModel.getState().isDeletedSuccessfully());
        assertNull(viewModel.getState().getDeleteError());
    }

    @Test
    void testSetStateFiresPropertyChange() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        DeletedEventState newState = new DeletedEventState();
        newState.setDeletedEventId("test-state-id");

        // Act
        viewModel.setState(newState);

        // Assert - base ViewModel fires "state" property change
        verify(mockListener, atLeastOnce()).propertyChange(any(PropertyChangeEvent.class));
    }

    @Test
    void testFirePropertyChangedWithoutParameters() {
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
    void testFirePropertyChangedWithSpecificProperty() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);

        // Act
        viewModel.firePropertyChanged(DeletedEventViewModel.DELETE_EVENT_STATE_PROPERTY);

        // Assert
        verify(mockListener, times(1)).propertyChange(argThat(event ->
            DeletedEventViewModel.DELETE_EVENT_STATE_PROPERTY.equals(event.getPropertyName())
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
    void testUpdateStateMethod() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        DeletedEventState newState = new DeletedEventState();
        newState.setDeletedEventId("update-test-id");
        newState.setDeletedEventName("Update Test Event");
        newState.setDeletedSuccessfully(true);

        // Act
        viewModel.updateState(newState);

        // Assert
        assertEquals("update-test-id", viewModel.getState().getDeletedEventId());
        assertEquals("Update Test Event", viewModel.getState().getDeletedEventName());
        assertTrue(viewModel.getState().isDeletedSuccessfully());
        verify(mockListener, times(1)).propertyChange(argThat(event ->
            DeletedEventViewModel.DELETE_EVENT_STATE_PROPERTY.equals(event.getPropertyName())
        ));
    }

    @Test
    void testMultipleStateUpdates() {
        // First state
        DeletedEventState state1 = new DeletedEventState();
        state1.setDeletedEventId("event-001");
        state1.setDeletedSuccessfully(true);
        
        viewModel.setState(state1);
        assertEquals("event-001", viewModel.getState().getDeletedEventId());
        assertTrue(viewModel.getState().isDeletedSuccessfully());

        // Second state
        DeletedEventState state2 = new DeletedEventState();
        state2.setDeletedEventId("event-002");
        state2.setDeletedSuccessfully(false);
        state2.setDeleteError("Deletion failed");
        
        viewModel.setState(state2);
        assertEquals("event-002", viewModel.getState().getDeletedEventId());
        assertFalse(viewModel.getState().isDeletedSuccessfully());
        assertEquals("Deletion failed", viewModel.getState().getDeleteError());
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
        viewModel.firePropertyChanged();
        
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
        viewModel.firePropertyChanged();
        
        // Assert both listeners are notified
        verify(listener1, times(1)).propertyChange(any());
        verify(listener2, times(1)).propertyChange(any());
        
        // Remove one listener
        viewModel.removePropertyChangeListener(listener1);
        
        // Fire again
        viewModel.firePropertyChanged();
        
        // Assert only remaining listener is notified
        verify(listener1, times(1)).propertyChange(any()); // Still only 1 call
        verify(listener2, times(2)).propertyChange(any()); // Now 2 calls
    }

    @Test
    void testStateChangedWithComplexState() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        DeletedEventState complexState = new DeletedEventState();
        complexState.setDeletedEventId("complex-event-id-12345");
        complexState.setDeletedEventName("Complex Event: AI Conference 2023");
        complexState.setDeletedSuccessfully(false);
        complexState.setDeleteError("Cannot delete event with active registrations. Please cancel all registrations before attempting deletion.");
        
        // Act
        viewModel.setState(complexState);
        viewModel.firePropertyChanged();

        // Assert
        verify(mockListener, atLeastOnce()).propertyChange(any());
        
        // Verify state is correctly maintained
        DeletedEventState resultState = viewModel.getState();
        assertEquals("complex-event-id-12345", resultState.getDeletedEventId());
        assertEquals("Complex Event: AI Conference 2023", resultState.getDeletedEventName());
        assertFalse(resultState.isDeletedSuccessfully());
        assertEquals("Cannot delete event with active registrations. Please cancel all registrations before attempting deletion.", resultState.getDeleteError());
    }

    @Test
    void testSuccessfulDeletionViewModelState() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        DeletedEventState successState = new DeletedEventState();
        successState.setDeletedEventId("success-event-789");
        successState.setDeletedEventName("Successfully Deleted Event");
        successState.setDeletedSuccessfully(true);
        successState.setDeleteError(null);

        // Act
        viewModel.updateState(successState);

        // Assert
        verify(mockListener, times(2)).propertyChange(any()); // updateState fires 2 events
        
        DeletedEventState resultState = viewModel.getState();
        assertEquals("success-event-789", resultState.getDeletedEventId());
        assertEquals("Successfully Deleted Event", resultState.getDeletedEventName());
        assertTrue(resultState.isDeletedSuccessfully());
        assertNull(resultState.getDeleteError());
    }

    @Test
    void testFailedDeletionViewModelState() {
        // Arrange
        PropertyChangeListener mockListener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(mockListener);
        
        DeletedEventState failState = new DeletedEventState();
        failState.setDeletedEventId("fail-event-456");
        failState.setDeletedEventName(null);
        failState.setDeletedSuccessfully(false);
        failState.setDeleteError("Event not found");

        // Act
        viewModel.updateState(failState);

        // Assert
        verify(mockListener, times(2)).propertyChange(any()); // updateState fires 2 events
        
        DeletedEventState resultState = viewModel.getState();
        assertEquals("fail-event-456", resultState.getDeletedEventId());
        assertNull(resultState.getDeletedEventName());
        assertFalse(resultState.isDeletedSuccessfully());
        assertEquals("Event not found", resultState.getDeleteError());
    }

    @Test
    void testViewModelInheritanceProperties() {
        // Test that the ViewModel correctly inherits from base ViewModel
        assertNotNull(viewModel.getViewName());
        assertEquals("deleted event view", viewModel.getViewName());
        
        // Test that state management works via inheritance
        DeletedEventState testState = new DeletedEventState();
        testState.setDeletedEventId("inheritance-test");
        
        viewModel.setState(testState);
        assertEquals("inheritance-test", viewModel.getState().getDeletedEventId());
        
        // Test property change support via inheritance
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        viewModel.addPropertyChangeListener(listener);
        
        DeletedEventState newTestState = new DeletedEventState();
        newTestState.setDeletedEventId("inheritance-test-2");
        viewModel.setState(newTestState);
        verify(listener, atLeastOnce()).propertyChange(any());
    }
}