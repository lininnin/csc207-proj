package interface_adapter.Angela.task.edit_available;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditAvailableTaskViewModel.
 */
class EditAvailableTaskViewModelTest {

    private EditAvailableTaskViewModel viewModel;
    private TestPropertyChangeListener propertyChangeListener;

    @BeforeEach
    void setUp() {
        viewModel = new EditAvailableTaskViewModel();
        propertyChangeListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(propertyChangeListener);
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertNotNull(viewModel.getState());
        assertEquals("edit available task", viewModel.getViewName());
    }

    @Test
    void testInitialState() {
        EditAvailableTaskState initialState = viewModel.getState();
        assertNotNull(initialState);
        assertNull(initialState.getEditingTaskId());
        assertNull(initialState.getError());
        assertNull(initialState.getSuccessMessage());
    }

    @Test
    void testSetState() {
        // Arrange
        EditAvailableTaskState newState = new EditAvailableTaskState();
        newState.setEditingTaskId("task-123");
        newState.setError("Test error");
        newState.setSuccessMessage("Test success");

        // Act
        viewModel.setState(newState);

        // Assert
        EditAvailableTaskState currentState = viewModel.getState();
        assertEquals("task-123", currentState.getEditingTaskId());
        assertEquals("Test error", currentState.getError());
        assertEquals("Test success", currentState.getSuccessMessage());
    }

    @Test
    void testFirePropertyChanged() {
        // Arrange
        EditAvailableTaskState newState = new EditAvailableTaskState();
        newState.setEditingTaskId("task-456");
        viewModel.setState(newState);

        // Reset the listener
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged();

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(EditAvailableTaskViewModel.EDIT_AVAILABLE_TASK_STATE_PROPERTY, 
                    propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testPropertyChangeEvent() {
        // Arrange
        EditAvailableTaskState oldState = viewModel.getState();
        EditAvailableTaskState newState = new EditAvailableTaskState();
        newState.setEditingTaskId("task-789");
        
        // Act
        viewModel.setState(newState);
        viewModel.firePropertyChanged();

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(EditAvailableTaskViewModel.EDIT_AVAILABLE_TASK_STATE_PROPERTY, 
                    propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testMultiplePropertyChanges() {
        // Test multiple property change events
        EditAvailableTaskState state1 = new EditAvailableTaskState();
        state1.setEditingTaskId("task-1");
        
        EditAvailableTaskState state2 = new EditAvailableTaskState();
        state2.setEditingTaskId("task-2");
        
        EditAvailableTaskState state3 = new EditAvailableTaskState();
        state3.setEditingTaskId("task-3");

        // Act
        viewModel.setState(state1);
        viewModel.firePropertyChanged();
        
        viewModel.setState(state2);
        viewModel.firePropertyChanged();
        
        viewModel.setState(state3);
        viewModel.firePropertyChanged();

        // Assert (setState fires one event, firePropertyChanged fires another, so 3 state changes = 6 events)
        assertEquals(6, propertyChangeListener.getEventCount());
        assertEquals("task-3", viewModel.getState().getEditingTaskId());
    }

    @Test
    void testStateWithErrorMessage() {
        // Arrange
        EditAvailableTaskState errorState = new EditAvailableTaskState();
        errorState.setError("Task name is required");

        // Act
        viewModel.setState(errorState);

        // Assert
        assertEquals("Task name is required", viewModel.getState().getError());
        assertNull(viewModel.getState().getSuccessMessage());
    }

    @Test
    void testStateWithSuccessMessage() {
        // Arrange
        EditAvailableTaskState successState = new EditAvailableTaskState();
        successState.setSuccessMessage("Task updated successfully");

        // Act
        viewModel.setState(successState);

        // Assert
        assertEquals("Task updated successfully", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());
    }

    @Test
    void testClearState() {
        // Arrange
        EditAvailableTaskState state = new EditAvailableTaskState();
        state.setEditingTaskId("task-123");
        state.setError("Some error");
        state.setSuccessMessage("Some success");
        viewModel.setState(state);

        // Act
        EditAvailableTaskState clearedState = new EditAvailableTaskState();
        viewModel.setState(clearedState);

        // Assert
        EditAvailableTaskState currentState = viewModel.getState();
        assertNull(currentState.getEditingTaskId());
        assertNull(currentState.getError());
        assertNull(currentState.getSuccessMessage());
    }

    @Test
    void testConstantValue() {
        assertEquals("editAvailableTaskState", EditAvailableTaskViewModel.EDIT_AVAILABLE_TASK_STATE_PROPERTY);
    }

    @Test
    void testViewModelInheritance() {
        // Test that the view model properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        assertNotNull(viewModel.getViewName());
        assertNotNull(viewModel.getState());
    }

    /**
     * Helper class to track property change events
     */
    private static class TestPropertyChangeListener implements PropertyChangeListener {
        private boolean eventFired = false;
        private String lastPropertyName;
        private Object lastOldValue;
        private Object lastNewValue;
        private int eventCount = 0;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            eventFired = true;
            lastPropertyName = evt.getPropertyName();
            lastOldValue = evt.getOldValue();
            lastNewValue = evt.getNewValue();
            eventCount++;
        }

        public boolean wasEventFired() {
            return eventFired;
        }

        public String getLastPropertyName() {
            return lastPropertyName;
        }

        public Object getLastOldValue() {
            return lastOldValue;
        }

        public Object getLastNewValue() {
            return lastNewValue;
        }

        public int getEventCount() {
            return eventCount;
        }

        public void reset() {
            eventFired = false;
            lastPropertyName = null;
            lastOldValue = null;
            lastNewValue = null;
            eventCount = 0;
        }
    }
}