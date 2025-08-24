package interface_adapter.alex.event_related.todays_events_module.delete_todays_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class DeleteTodaysEventViewModelTest {

    private DeleteTodaysEventViewModel viewModel;
    private PropertyChangeEvent lastEvent;
    private int propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new DeleteTodaysEventViewModel();
        lastEvent = null;
        propertyChangeCount = 0;
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertEquals("deleted event view", viewModel.getViewName());
        assertNotNull(viewModel.getState());
    }

    @Test
    void testInitialState() {
        DeleteTodaysEventState state = viewModel.getState();
        assertNotNull(state);
        assertNull(state.getDeletedEventId());
        assertNull(state.getDeletedEventName());
        assertFalse(state.isDeletedSuccessfully());
        assertNull(state.getDeleteError());
    }

    @Test
    void testSetState() {
        // Arrange
        DeleteTodaysEventState newState = new DeleteTodaysEventState();
        newState.setDeletedEventId("test-event-123");
        newState.setDeletedEventName("Test Event");
        newState.setDeletedSuccessfully(true);
        newState.setDeleteError(null);

        // Act
        viewModel.setState(newState);

        // Assert
        DeleteTodaysEventState retrievedState = viewModel.getState();
        assertEquals("test-event-123", retrievedState.getDeletedEventId());
        assertEquals("Test Event", retrievedState.getDeletedEventName());
        assertTrue(retrievedState.isDeletedSuccessfully());
        assertNull(retrievedState.getDeleteError());
    }

    @Test
    void testSetStateWithNull() {
        // Arrange
        DeleteTodaysEventState initialState = viewModel.getState();
        
        // Act
        viewModel.setState(null);
        
        // Assert - State should now be null
        assertNull(viewModel.getState());
    }

    @Test
    void testSetStateWithFailureData() {
        // Arrange
        DeleteTodaysEventState failureState = new DeleteTodaysEventState();
        failureState.setDeletedEventId("failed-event-456");
        failureState.setDeletedEventName("Failed Event");
        failureState.setDeletedSuccessfully(false);
        failureState.setDeleteError("Event could not be deleted");

        // Act
        viewModel.setState(failureState);

        // Assert
        DeleteTodaysEventState retrievedState = viewModel.getState();
        assertEquals("failed-event-456", retrievedState.getDeletedEventId());
        assertEquals("Failed Event", retrievedState.getDeletedEventName());
        assertFalse(retrievedState.isDeletedSuccessfully());
        assertEquals("Event could not be deleted", retrievedState.getDeleteError());
    }

    @Test
    void testFirePropertyChanged() {
        // Arrange
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lastEvent = evt;
                propertyChangeCount++;
            }
        };
        viewModel.addPropertyChangeListener(listener);

        // Act
        viewModel.firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
        assertEquals(1, propertyChangeCount);
    }

    @Test
    void testFirePropertyChangedWithCustomPropertyName() {
        // Arrange
        String customProperty = "custom-property";
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lastEvent = evt;
                propertyChangeCount++;
            }
        };
        viewModel.addPropertyChangeListener(listener);

        // Act
        viewModel.firePropertyChanged(customProperty);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(customProperty, lastEvent.getPropertyName());
        assertEquals(1, propertyChangeCount);
    }

    @Test
    void testMultiplePropertyChangeListeners() {
        // Arrange
        int[] listenerCallCounts = {0, 0, 0};
        
        PropertyChangeListener listener1 = evt -> listenerCallCounts[0]++;
        PropertyChangeListener listener2 = evt -> listenerCallCounts[1]++;
        PropertyChangeListener listener3 = evt -> listenerCallCounts[2]++;
        
        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);
        viewModel.addPropertyChangeListener(listener3);

        // Act
        viewModel.firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        // Assert
        assertEquals(1, listenerCallCounts[0]);
        assertEquals(1, listenerCallCounts[1]);
        assertEquals(1, listenerCallCounts[2]);
    }

    @Test
    void testRemovePropertyChangeListener() {
        // Arrange
        PropertyChangeListener listener1 = evt -> propertyChangeCount++;
        PropertyChangeListener listener2 = evt -> propertyChangeCount++;
        
        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);

        // Act
        viewModel.removePropertyChangeListener(listener1);
        viewModel.firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        // Assert - Only one listener should have been called
        assertEquals(1, propertyChangeCount);
    }

    @Test
    void testDeleteTodaysEventStatePropertyConstant() {
        assertEquals("deletedEventState", DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
    }

    @Test
    void testMultipleStateChangesWithPropertyChanges() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        DeleteTodaysEventState state1 = new DeleteTodaysEventState();
        state1.setDeletedEventId("event-1");
        state1.setDeletedSuccessfully(true);

        DeleteTodaysEventState state2 = new DeleteTodaysEventState();
        state2.setDeletedEventId("event-2");
        state2.setDeletedSuccessfully(false);
        state2.setDeleteError("Failed");

        // Act
        viewModel.setState(state1); // Fires property change (count = 1)
        viewModel.setState(state2); // Fires property change (count = 2)

        // Assert
        assertEquals(2, propertyChangeCount);
        
        // Verify final state
        DeleteTodaysEventState finalState = viewModel.getState();
        assertEquals("event-2", finalState.getDeletedEventId());
        assertFalse(finalState.isDeletedSuccessfully());
        assertEquals("Failed", finalState.getDeleteError());
    }

    @Test
    void testStateIndependence() {
        // Arrange
        DeleteTodaysEventState originalState = new DeleteTodaysEventState();
        originalState.setDeletedEventId("original-id");
        originalState.setDeletedEventName("Original Event");
        originalState.setDeletedSuccessfully(true);
        
        viewModel.setState(originalState);

        // Act - Modify the original state object
        originalState.setDeletedEventId("modified-id");
        originalState.setDeletedSuccessfully(false);

        // Assert - ViewModel should maintain its own reference
        DeleteTodaysEventState viewModelState = viewModel.getState();
        assertEquals("modified-id", viewModelState.getDeletedEventId()); // State objects are mutable
        assertEquals("Original Event", viewModelState.getDeletedEventName());
        assertFalse(viewModelState.isDeletedSuccessfully());
    }

    @Test
    void testPropertyChangeWithoutListeners() {
        // Act - Fire property change without any listeners
        assertDoesNotThrow(() -> {
            viewModel.firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
        });
    }

    @Test
    void testComplexStateTransitions() {
        // Arrange
        PropertyChangeListener listener = evt -> {
            lastEvent = evt;
            propertyChangeCount++;
        };
        viewModel.addPropertyChangeListener(listener);

        // Test success scenario
        DeleteTodaysEventState successState = new DeleteTodaysEventState("success-123", "Success Event", true, null);
        viewModel.setState(successState);
        // setState already fires property change, so count should be 1

        assertEquals(1, propertyChangeCount);
        assertTrue(viewModel.getState().isDeletedSuccessfully());

        // Test failure scenario
        DeleteTodaysEventState failureState = new DeleteTodaysEventState("failed-456", "Failed Event", false, "Database error");
        viewModel.setState(failureState);
        // setState already fires property change, so count should be 2

        assertEquals(2, propertyChangeCount);
        assertFalse(viewModel.getState().isDeletedSuccessfully());
        assertEquals("Database error", viewModel.getState().getDeleteError());

        // Test back to success
        DeleteTodaysEventState anotherSuccessState = new DeleteTodaysEventState("success-789", "Another Success", true, null);
        viewModel.setState(anotherSuccessState);
        // setState already fires property change, so count should be 3

        assertEquals(3, propertyChangeCount);
        assertTrue(viewModel.getState().isDeletedSuccessfully());
        assertNull(viewModel.getState().getDeleteError());
    }

    @Test
    void testViewModelInheritance() {
        // Test that it properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        
        // Test the generic type by accessing state
        Object state = viewModel.getState();
        assertTrue(state instanceof DeleteTodaysEventState);
    }

    @Test
    void testPropertyChangeEventDetails() {
        // Arrange
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lastEvent = evt;
            }
        };
        viewModel.addPropertyChangeListener(listener);

        // Act
        viewModel.firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
        assertEquals(viewModel, lastEvent.getSource());
        // Note: oldValue and newValue are typically null for state change notifications
    }
}