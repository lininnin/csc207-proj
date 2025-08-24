package interface_adapter.alex.event_related.available_event_module.edit_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class EditedEventViewModelTest {

    private EditedEventViewModel viewModel;
    private PropertyChangeEvent lastEvent;
    private int propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new EditedEventViewModel();
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
        EditedEventState state = viewModel.getState();
        assertNotNull(state);
        assertEquals("", state.getEventId());
        assertEquals("", state.getName());
        assertNull(state.getNameError());
        assertEquals("", state.getCategory());
        assertNull(state.getCategoryError());
        assertEquals("", state.getDescription());
        assertNull(state.getDescriptionError());
        assertFalse(state.isOneTime());
        assertNull(state.getEditError());
    }

    @Test
    void testSetState() {
        // Arrange
        EditedEventState newState = new EditedEventState();
        newState.setEventId("test-event-123");
        newState.setName("Test Event Name");
        newState.setCategory("Work");
        newState.setDescription("Test description");
        newState.setOneTime(true);

        // Act
        viewModel.setState(newState);

        // Assert
        EditedEventState retrievedState = viewModel.getState();
        assertEquals("test-event-123", retrievedState.getEventId());
        assertEquals("Test Event Name", retrievedState.getName());
        assertEquals("Work", retrievedState.getCategory());
        assertEquals("Test description", retrievedState.getDescription());
        assertTrue(retrievedState.isOneTime());
        assertNull(retrievedState.getNameError());
        assertNull(retrievedState.getCategoryError());
        assertNull(retrievedState.getDescriptionError());
        assertNull(retrievedState.getEditError());
    }

    @Test
    void testSetStateWithNull() {
        // Arrange
        EditedEventState initialState = viewModel.getState();
        
        // Act
        viewModel.setState(null);
        
        // Assert - State should now be null
        assertNull(viewModel.getState());
    }

    @Test
    void testSetStateWithErrorData() {
        // Arrange
        EditedEventState errorState = new EditedEventState();
        errorState.setEventId("failed-event-456");
        errorState.setName("Failed Event");
        errorState.setNameError("Name validation failed");
        errorState.setCategory("InvalidCategory");
        errorState.setCategoryError("Category does not exist");
        errorState.setDescription("Short");
        errorState.setDescriptionError("Description too short");
        errorState.setEditError("General edit error");

        // Act
        viewModel.setState(errorState);

        // Assert
        EditedEventState retrievedState = viewModel.getState();
        assertEquals("failed-event-456", retrievedState.getEventId());
        assertEquals("Failed Event", retrievedState.getName());
        assertEquals("Name validation failed", retrievedState.getNameError());
        assertEquals("InvalidCategory", retrievedState.getCategory());
        assertEquals("Category does not exist", retrievedState.getCategoryError());
        assertEquals("Short", retrievedState.getDescription());
        assertEquals("Description too short", retrievedState.getDescriptionError());
        assertEquals("General edit error", retrievedState.getEditError());
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

        EditedEventState newState = new EditedEventState();
        newState.setEventId("update-test-789");
        newState.setName("Updated Event Name");
        newState.setCategory("Updated Category");
        newState.setDescription("Updated description");

        // Act
        viewModel.updateState(newState);

        // Assert
        // Verify state was updated
        EditedEventState retrievedState = viewModel.getState();
        assertEquals("update-test-789", retrievedState.getEventId());
        assertEquals("Updated Event Name", retrievedState.getName());
        assertEquals("Updated Category", retrievedState.getCategory());
        assertEquals("Updated description", retrievedState.getDescription());

        // Verify property change was fired (updateState fires 2: setState + firePropertyChanged)
        assertNotNull(lastEvent);
        assertEquals(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
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
    void testClearError() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        // Set initial state with errors
        EditedEventState initialState = new EditedEventState();
        initialState.setEventId("clear-error-test");
        initialState.setName("Test Event");
        initialState.setNameError("Name error");
        initialState.setCategoryError("Category error");
        initialState.setDescriptionError("Description error");
        initialState.setEditError("General error");
        viewModel.setState(initialState);

        // Act
        viewModel.clearError();

        // Assert
        EditedEventState clearedState = viewModel.getState();
        assertEquals("clear-error-test", clearedState.getEventId()); // ID should be preserved
        assertEquals("", clearedState.getName()); // Should be reset to empty
        assertEquals("", clearedState.getCategory()); // Should be reset to empty
        assertEquals("", clearedState.getDescription()); // Should be reset to empty
        assertFalse(clearedState.isOneTime()); // Should be reset to default
        assertNull(clearedState.getNameError()); // Errors should be cleared
        assertNull(clearedState.getCategoryError());
        assertNull(clearedState.getDescriptionError());
        assertNull(clearedState.getEditError());

        // Verify property change was fired (clearError fires 2: setState + firePropertyChanged)
        // But initially setState was called once, so total is 3
        assertEquals(3, propertyChangeCount);
    }

    @Test
    void testClearErrorWithEmptyInitialState() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        // Act - Clear error on default state
        viewModel.clearError();

        // Assert
        EditedEventState clearedState = viewModel.getState();
        assertEquals("", clearedState.getEventId()); // Should remain empty
        assertEquals("", clearedState.getName());
        assertEquals("", clearedState.getCategory());
        assertEquals("", clearedState.getDescription());
        assertFalse(clearedState.isOneTime());
        assertNull(clearedState.getNameError());
        assertNull(clearedState.getCategoryError());
        assertNull(clearedState.getDescriptionError());
        assertNull(clearedState.getEditError());

        // Verify property change was fired (clearError fires 2: setState + firePropertyChanged)
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
        viewModel.firePropertyChanged(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
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
        viewModel.firePropertyChanged(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY);

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
        viewModel.firePropertyChanged(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY);

        // Assert - Only one listener should have been called
        assertEquals(1, propertyChangeCount);
    }

    @Test
    void testEditedEventStatePropertyConstant() {
        assertEquals("editedEventState", EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY);
    }

    @Test
    void testMultipleUpdateStateCalls() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        EditedEventState state1 = new EditedEventState();
        state1.setEventId("event-1");
        state1.setName("Event One");

        EditedEventState state2 = new EditedEventState();
        state2.setEventId("event-2");
        state2.setName("Event Two");
        state2.setCategory("Category Two");

        // Act
        viewModel.updateState(state1);
        viewModel.updateState(state2);

        // Assert (2 updateState calls x 2 property changes each = 4)
        assertEquals(4, propertyChangeCount);
        
        // Verify final state
        EditedEventState finalState = viewModel.getState();
        assertEquals("event-2", finalState.getEventId());
        assertEquals("Event Two", finalState.getName());
        assertEquals("Category Two", finalState.getCategory());
    }

    @Test
    void testStateIndependence() {
        // Arrange
        EditedEventState originalState = new EditedEventState();
        originalState.setEventId("original-id");
        originalState.setName("Original Event");
        
        viewModel.setState(originalState);

        // Act - Modify the original state object
        originalState.setEventId("modified-id");
        originalState.setName("Modified Event");

        // Assert - ViewModel should maintain its own reference
        EditedEventState viewModelState = viewModel.getState();
        assertEquals("modified-id", viewModelState.getEventId()); // State objects are mutable
        assertEquals("Modified Event", viewModelState.getName());
    }

    @Test
    void testPropertyChangeWithoutListeners() {
        // Act - Fire property change without any listeners
        assertDoesNotThrow(() -> {
            viewModel.firePropertyChanged(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY);
        });
    }

    @Test
    void testUpdateStateWithoutListeners() {
        // Arrange
        EditedEventState newState = new EditedEventState();
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
        EditedEventState successState = new EditedEventState();
        successState.setEventId("success-123");
        successState.setName("Successful Event");
        successState.setCategory("Work");
        successState.setDescription("Successfully updated");
        
        viewModel.updateState(successState);
        assertEquals(2, propertyChangeCount); // updateState fires 2 property changes
        assertEquals("success-123", viewModel.getState().getEventId());
        assertEquals("Successful Event", viewModel.getState().getName());

        // Test failure scenario
        EditedEventState failureState = new EditedEventState();
        failureState.setEventId("failed-456");
        failureState.setName("Failed Event");
        failureState.setNameError("Name validation failed");
        failureState.setEditError("General edit failure");
        
        viewModel.updateState(failureState);
        assertEquals(4, propertyChangeCount); // 2 updateState calls x 2 each = 4
        assertEquals("failed-456", viewModel.getState().getEventId());
        assertEquals("Name validation failed", viewModel.getState().getNameError());
        assertEquals("General edit failure", viewModel.getState().getEditError());

        // Test clear error
        viewModel.clearError();
        assertEquals(6, propertyChangeCount); // 4 + clearError(2) = 6
        assertEquals("failed-456", viewModel.getState().getEventId()); // ID preserved
        assertNull(viewModel.getState().getNameError()); // Errors cleared
        assertNull(viewModel.getState().getEditError());
    }

    @Test
    void testViewModelInheritance() {
        // Test that it properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        
        // Test the generic type by accessing state
        Object state = viewModel.getState();
        assertTrue(state instanceof EditedEventState);
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
        viewModel.firePropertyChanged(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
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

        EditedEventState newState = new EditedEventState();
        newState.setEventId("property-test");

        // Act
        viewModel.updateState(newState);

        // Assert
        assertNotNull(lastEvent);
        assertEquals(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY, lastEvent.getPropertyName());
        assertEquals(viewModel, lastEvent.getSource());
    }

    @Test
    void testClearErrorPropertyChangeEventDetails() {
        // Arrange
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lastEvent = evt;
            }
        };
        viewModel.addPropertyChangeListener(listener);

        // Act
        viewModel.clearError();

        // Assert
        assertNotNull(lastEvent);
        assertEquals("state", lastEvent.getPropertyName()); // clearError uses "state" property
        assertEquals(viewModel, lastEvent.getSource());
    }

    @Test
    void testStateWithAllFields() {
        // Arrange
        EditedEventState complexState = new EditedEventState();
        complexState.setEventId("complex-event-id");
        complexState.setName("Complex Event Name");
        complexState.setNameError("Name error");
        complexState.setCategory("Complex Category");
        complexState.setCategoryError("Category error");
        complexState.setDescription("Complex description");
        complexState.setDescriptionError("Description error");
        complexState.setOneTime(true);
        complexState.setEditError("Edit error");

        // Act
        viewModel.updateState(complexState);

        // Assert
        EditedEventState retrievedState = viewModel.getState();
        assertEquals("complex-event-id", retrievedState.getEventId());
        assertEquals("Complex Event Name", retrievedState.getName());
        assertEquals("Name error", retrievedState.getNameError());
        assertEquals("Complex Category", retrievedState.getCategory());
        assertEquals("Category error", retrievedState.getCategoryError());
        assertEquals("Complex description", retrievedState.getDescription());
        assertEquals("Description error", retrievedState.getDescriptionError());
        assertTrue(retrievedState.isOneTime());
        assertEquals("Edit error", retrievedState.getEditError());
    }

    @Test
    void testDirectFirePropertyChangedVsUpdateState() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        EditedEventState newState = new EditedEventState();
        newState.setEventId("comparison-test");

        // Act
        // Method 1: Direct firePropertyChanged
        viewModel.firePropertyChanged(EditedEventViewModel.EDITED_EVENT_STATE_PROPERTY);
        
        // Method 2: updateState (which calls setState and firePropertyChanged)
        viewModel.updateState(newState);

        // Assert - Both methods should trigger property change events (1 + 2 = 3)
        assertEquals(3, propertyChangeCount);
        assertEquals("comparison-test", viewModel.getState().getEventId());
    }

    @Test
    void testClearErrorSequence() {
        // Arrange
        PropertyChangeListener listener = evt -> propertyChangeCount++;
        viewModel.addPropertyChangeListener(listener);

        // Set error state
        EditedEventState errorState = new EditedEventState();
        errorState.setEventId("error-sequence-test");
        errorState.setName("Error Event");
        errorState.setEditError("Some error");
        viewModel.setState(errorState);

        // Act - Clear error multiple times
        viewModel.clearError();
        viewModel.clearError();
        viewModel.clearError();

        // Assert - Each clearError should fire 2 property changes (setState called once, then 3 clearError x 2 each = 7)
        assertEquals(7, propertyChangeCount);
        assertEquals("error-sequence-test", viewModel.getState().getEventId()); // ID preserved
        assertEquals("", viewModel.getState().getName()); // Reset to default
        assertNull(viewModel.getState().getEditError()); // Error cleared
    }
}