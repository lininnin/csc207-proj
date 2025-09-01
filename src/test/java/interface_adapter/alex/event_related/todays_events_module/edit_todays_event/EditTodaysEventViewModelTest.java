package interface_adapter.alex.event_related.todays_events_module.edit_todays_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class EditTodaysEventViewModelTest {

    private EditTodaysEventViewModel viewModel;
    private PropertyChangeEvent lastEvent;
    private int propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new EditTodaysEventViewModel();
        lastEvent = null;
        propertyChangeCount = 0;
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertEquals("Edited Event View", viewModel.getViewName());
        assertNotNull(viewModel.getState());
    }

    @Test
    void testInitialState() {
        EditTodaysEventState state = viewModel.getState();
        assertNotNull(state);
        assertEquals("", state.getEventId());
        assertEquals("", state.getDueDate());
        assertNull(state.getDueDateError());
        assertNull(state.getEditError());
    }

    @Test
    void testSetState() {
        // Arrange
        EditTodaysEventState newState = new EditTodaysEventState();
        newState.setEventId("test-event-123");
        newState.setDueDate("2024-12-25");
        newState.setDueDateError(null);
        newState.setEditError(null);

        // Act
        viewModel.setState(newState);

        // Assert
        EditTodaysEventState retrievedState = viewModel.getState();
        assertEquals("test-event-123", retrievedState.getEventId());
        assertEquals("2024-12-25", retrievedState.getDueDate());
        assertNull(retrievedState.getDueDateError());
        assertNull(retrievedState.getEditError());
    }

    @Test
    void testSetStateWithNull() {
        // Arrange
        EditTodaysEventState initialState = viewModel.getState();
        
        // Act
        viewModel.setState(null);
        
        // Assert - State should now be null
        assertNull(viewModel.getState());
    }

    @Test
    void testSetStateWithErrorData() {
        // Arrange
        EditTodaysEventState errorState = new EditTodaysEventState();
        errorState.setEventId("failed-event-456");
        errorState.setDueDate("invalid-date");
        errorState.setDueDateError("Invalid date format");
        errorState.setEditError("Edit failed: invalid due date or event not found.");

        // Act
        viewModel.setState(errorState);

        // Assert
        EditTodaysEventState retrievedState = viewModel.getState();
        assertEquals("failed-event-456", retrievedState.getEventId());
        assertEquals("invalid-date", retrievedState.getDueDate());
        assertEquals("Invalid date format", retrievedState.getDueDateError());
        assertEquals("Edit failed: invalid due date or event not found.", retrievedState.getEditError());
    }

    @Test
    void testUpdateState() {
        // Arrange
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lastEvent = evt;
                propertyChangeCount++;
            }
        };
        viewModel.addPropertyChangeListener(listener);

        EditTodaysEventState newState = new EditTodaysEventState();
        newState.setEventId("update-test-789");
        newState.setDueDate("2024-08-15");

        // Act
        viewModel.updateState(newState);

        // Assert
        // Verify state was updated
        EditTodaysEventState retrievedState = viewModel.getState();
        assertEquals("update-test-789", retrievedState.getEventId());
        assertEquals("2024-08-15", retrievedState.getDueDate());

        // Verify property change was fired (updateState fires 2: setState + firePropertyChanged)
        assertNotNull(lastEvent);
        assertEquals(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
        assertEquals(2, propertyChangeCount);
    }

    @Test
    void testUpdateStateWithNull() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        // Act
        viewModel.updateState(null);

        // Assert - State should be null and property change should be fired (updateState fires 2)
        assertNull(viewModel.getState());
        assertEquals(2, propertyChangeCount);
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
        viewModel.firePropertyChanged(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
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
        viewModel.firePropertyChanged(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY);

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
        viewModel.firePropertyChanged(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY);

        // Assert - Only one listener should have been called
        assertEquals(1, propertyChangeCount);
    }

    @Test
    void testEditTodaysEventStatePropertyConstant() {
        assertEquals("editTodaysEventState", EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY);
    }

    @Test
    void testMultipleUpdateStateCalls() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        EditTodaysEventState state1 = new EditTodaysEventState();
        state1.setEventId("event-1");
        state1.setDueDate("2024-01-01");

        EditTodaysEventState state2 = new EditTodaysEventState();
        state2.setEventId("event-2");
        state2.setDueDate("2024-02-02");
        state2.setEditError("Some error");

        // Act
        viewModel.updateState(state1);
        viewModel.updateState(state2);

        // Assert (2 updateState calls x 2 property changes each = 4)
        assertEquals(4, propertyChangeCount);
        
        // Verify final state
        EditTodaysEventState finalState = viewModel.getState();
        assertEquals("event-2", finalState.getEventId());
        assertEquals("2024-02-02", finalState.getDueDate());
        assertEquals("Some error", finalState.getEditError());
    }

    @Test
    void testStateIndependence() {
        // Arrange
        EditTodaysEventState originalState = new EditTodaysEventState();
        originalState.setEventId("original-id");
        originalState.setDueDate("2024-05-15");
        
        viewModel.setState(originalState);

        // Act - Modify the original state object
        originalState.setEventId("modified-id");
        originalState.setDueDate("2024-06-16");

        // Assert - ViewModel should maintain its own reference
        EditTodaysEventState viewModelState = viewModel.getState();
        assertEquals("modified-id", viewModelState.getEventId()); // State objects are mutable
        assertEquals("2024-06-16", viewModelState.getDueDate());
    }

    @Test
    void testPropertyChangeWithoutListeners() {
        // Act - Fire property change without any listeners
        assertDoesNotThrow(() -> {
            viewModel.firePropertyChanged(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY);
        });
    }

    @Test
    void testUpdateStateWithoutListeners() {
        // Arrange
        EditTodaysEventState newState = new EditTodaysEventState();
        newState.setEventId("no-listeners-test");

        // Act - updateState without any listeners
        assertDoesNotThrow(() -> {
            viewModel.updateState(newState);
        });

        // Assert - State should still be updated
        assertEquals("no-listeners-test", viewModel.getState().getEventId());
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
        EditTodaysEventState successState = new EditTodaysEventState();
        successState.setEventId("success-123");
        successState.setDueDate("2024-12-25");
        
        viewModel.updateState(successState);
        assertEquals(2, propertyChangeCount); // updateState fires 2 property changes
        assertEquals("success-123", viewModel.getState().getEventId());
        assertEquals("2024-12-25", viewModel.getState().getDueDate());

        // Test failure scenario
        EditTodaysEventState failureState = new EditTodaysEventState();
        failureState.setEventId("failed-456");
        failureState.setDueDateError("Invalid date");
        failureState.setEditError("Edit failed: invalid due date or event not found.");
        
        viewModel.updateState(failureState);
        assertEquals(4, propertyChangeCount); // 2 updateState calls x 2 each = 4
        assertEquals("failed-456", viewModel.getState().getEventId());
        assertEquals("Invalid date", viewModel.getState().getDueDateError());
        assertEquals("Edit failed: invalid due date or event not found.", viewModel.getState().getEditError());

        // Test back to success
        EditTodaysEventState anotherSuccessState = new EditTodaysEventState();
        anotherSuccessState.setEventId("success-789");
        anotherSuccessState.setDueDate("2024-07-04");
        
        viewModel.updateState(anotherSuccessState);
        assertEquals(6, propertyChangeCount); // 3 updateState calls x 2 each = 6
        assertEquals("success-789", viewModel.getState().getEventId());
        assertEquals("2024-07-04", viewModel.getState().getDueDate());
        assertNull(viewModel.getState().getDueDateError());
        assertNull(viewModel.getState().getEditError());
    }

    @Test
    void testViewModelInheritance() {
        // Test that it properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        
        // Test the generic type by accessing state
        Object state = viewModel.getState();
        assertTrue(state instanceof EditTodaysEventState);
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
        viewModel.firePropertyChanged(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
        assertEquals(viewModel, lastEvent.getSource());
        // Note: oldValue and newValue are typically null for state change notifications
    }

    @Test
    void testUpdateStatePropertyChangeEventDetails() {
        // Arrange
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lastEvent = evt;
            }
        };
        viewModel.addPropertyChangeListener(listener);

        EditTodaysEventState newState = new EditTodaysEventState();
        newState.setEventId("property-test");

        // Act
        viewModel.updateState(newState);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
        assertEquals(viewModel, lastEvent.getSource());
    }

    @Test
    void testStateWithAllFields() {
        // Arrange
        EditTodaysEventState complexState = new EditTodaysEventState();
        complexState.setEventId("complex-event-id");
        complexState.setDueDate("2024-10-31");
        complexState.setDueDateError("Date validation failed");
        complexState.setEditError("Edit operation failed");

        // Act
        viewModel.updateState(complexState);

        // Assert
        EditTodaysEventState retrievedState = viewModel.getState();
        assertEquals("complex-event-id", retrievedState.getEventId());
        assertEquals("2024-10-31", retrievedState.getDueDate());
        assertEquals("Date validation failed", retrievedState.getDueDateError());
        assertEquals("Edit operation failed", retrievedState.getEditError());
    }

    @Test
    void testDirectFirePropertyChangedVsUpdateState() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        EditTodaysEventState newState = new EditTodaysEventState();
        newState.setEventId("comparison-test");

        // Act
        // Method 1: Direct firePropertyChanged
        viewModel.firePropertyChanged(EditTodaysEventViewModel.EDIT_TODAYS_EVENT_STATE_PROPERTY);
        
        // Method 2: updateState (which calls setState and firePropertyChanged)
        viewModel.updateState(newState);

        // Assert - Both methods should trigger property change events (1 + 2 = 3)
        assertEquals(3, propertyChangeCount);
        assertEquals("comparison-test", viewModel.getState().getEventId());
    }
}