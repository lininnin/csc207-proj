package interface_adapter.Angela.task.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreateTaskViewModel.
 */
class CreateTaskViewModelTest {

    private CreateTaskViewModel viewModel;
    private TestPropertyChangeListener propertyChangeListener;

    @BeforeEach
    void setUp() {
        viewModel = new CreateTaskViewModel();
        propertyChangeListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(propertyChangeListener);
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertEquals("create task", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        
        // Verify initial state
        CreateTaskState state = viewModel.getState();
        assertEquals("", state.getTaskName());
        assertEquals("", state.getDescription());
        assertEquals("", state.getCategoryId());
        assertFalse(state.isOneTime());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testGetViewName() {
        assertEquals("create task", viewModel.getViewName());
    }

    @Test
    void testStaticConstants() {
        assertEquals("Create New Task", CreateTaskViewModel.TITLE_LABEL);
        assertEquals("Task Name:", CreateTaskViewModel.TASK_NAME_LABEL);
        assertEquals("Description:", CreateTaskViewModel.DESCRIPTION_LABEL);
        assertEquals("Category:", CreateTaskViewModel.CATEGORY_LABEL);
        assertEquals("One-Time Task", CreateTaskViewModel.ONE_TIME_LABEL);
        assertEquals("Create", CreateTaskViewModel.CREATE_BUTTON_LABEL);
        assertEquals("Cancel", CreateTaskViewModel.CANCEL_BUTTON_LABEL);
        assertEquals("createTaskState", CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }

    @Test
    void testSetState() {
        // Arrange
        CreateTaskState newState = new CreateTaskState();
        newState.setTaskName("Test Task");
        newState.setDescription("Test Description");
        newState.setOneTime(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertEquals(newState, viewModel.getState());
        assertEquals("Test Task", viewModel.getState().getTaskName());
        assertEquals("Test Description", viewModel.getState().getDescription());
        assertTrue(viewModel.getState().isOneTime());
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testUpdateState() {
        // Arrange
        CreateTaskState newState = new CreateTaskState();
        newState.setTaskName("Updated Task");
        newState.setError("Test error");
        propertyChangeListener.reset();

        // Act
        viewModel.updateState(newState);

        // Assert
        assertEquals(newState, viewModel.getState());
        assertEquals("Updated Task", viewModel.getState().getTaskName());
        assertEquals("Test error", viewModel.getState().getError());
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY, propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testUpdateStateFiresCorrectProperty() {
        // Arrange
        CreateTaskState newState = new CreateTaskState();
        newState.setSuccessMessage("Task created successfully");
        propertyChangeListener.reset();

        // Act
        viewModel.updateState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("createTaskState", propertyChangeListener.getLastPropertyName());
        assertEquals(newState, propertyChangeListener.getLastNewValue());
    }

    @Test
    void testMultipleStateUpdates() {
        // Arrange
        CreateTaskState state1 = new CreateTaskState();
        state1.setTaskName("Task 1");
        
        CreateTaskState state2 = new CreateTaskState();
        state2.setTaskName("Task 2");
        
        CreateTaskState state3 = new CreateTaskState();
        state3.setTaskName("Task 3");
        
        propertyChangeListener.reset();

        // Act
        viewModel.updateState(state1);
        viewModel.updateState(state2);
        viewModel.updateState(state3);

        // Assert (updateState calls both setState and firePropertyChanged, so 3 updates = 6 events)
        assertEquals(6, propertyChangeListener.getEventCount());
        assertEquals(state3, viewModel.getState());
        assertEquals("Task 3", viewModel.getState().getTaskName());
    }

    @Test
    void testPropertyChangeListenerManagement() {
        // Arrange
        TestPropertyChangeListener secondListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(secondListener);

        CreateTaskState newState = new CreateTaskState();
        newState.setTaskName("Test Task");

        // Act
        viewModel.updateState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertTrue(secondListener.wasEventFired());

        // Test removal
        viewModel.removePropertyChangeListener(secondListener);
        propertyChangeListener.reset();
        secondListener.reset();

        CreateTaskState anotherState = new CreateTaskState();
        anotherState.setTaskName("Another Task");
        viewModel.updateState(anotherState);

        assertTrue(propertyChangeListener.wasEventFired());
        assertFalse(secondListener.wasEventFired());
    }

    @Test
    void testUpdateStateWithNullState() {
        // Act
        viewModel.updateState(null);

        // Assert
        assertNull(viewModel.getState());
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testUpdateStateWithComplexState() {
        // Arrange
        CreateTaskState complexState = new CreateTaskState();
        complexState.setTaskName("Complex Task");
        complexState.setDescription("A very detailed description");
        complexState.setCategoryId("complex-category");
        complexState.setOneTime(true);
        complexState.setError("Some validation error");

        // Act
        viewModel.updateState(complexState);

        // Assert
        CreateTaskState retrievedState = viewModel.getState();
        assertEquals("Complex Task", retrievedState.getTaskName());
        assertEquals("A very detailed description", retrievedState.getDescription());
        assertEquals("complex-category", retrievedState.getCategoryId());
        assertTrue(retrievedState.isOneTime());
        assertEquals("Some validation error", retrievedState.getError());
        assertNull(retrievedState.getSuccessMessage());
    }

    @Test
    void testStateIndependence() {
        // Arrange
        CreateTaskState originalState = viewModel.getState();
        CreateTaskState newState = new CreateTaskState();
        newState.setTaskName("New Task");
        newState.setOneTime(true);

        // Act
        viewModel.updateState(newState);

        // Assert
        assertNotSame(originalState, viewModel.getState());
        assertEquals("", originalState.getTaskName());
        assertFalse(originalState.isOneTime());
        assertEquals("New Task", viewModel.getState().getTaskName());
        assertTrue(viewModel.getState().isOneTime());
    }

    @Test
    void testSuccessAndErrorStates() {
        // Test success state
        CreateTaskState successState = new CreateTaskState();
        successState.setTaskName("Successful Task");
        successState.setSuccessMessage("Task created successfully");
        
        viewModel.updateState(successState);
        
        assertEquals("Successful Task", viewModel.getState().getTaskName());
        assertEquals("Task created successfully", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());

        // Test error state
        CreateTaskState errorState = new CreateTaskState();
        errorState.setTaskName("Failed Task");
        errorState.setError("Task name too short");
        
        viewModel.updateState(errorState);
        
        assertEquals("Failed Task", viewModel.getState().getTaskName());
        assertEquals("Task name too short", viewModel.getState().getError());
        assertNull(viewModel.getState().getSuccessMessage());
    }

    @Test
    void testViewModelStateAfterDirectStateModification() {
        // Test that modifying state after setting it affects the view model
        CreateTaskState state = new CreateTaskState();
        state.setTaskName("Initial Task");
        viewModel.updateState(state);
        
        // Modify the state object directly
        state.setDescription("Added after setting");
        
        // Assert the view model reflects the change
        assertEquals("Added after setting", viewModel.getState().getDescription());
    }

    @Test
    void testMultiplePropertyChangeEvents() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.updateState(new CreateTaskState());
        viewModel.updateState(new CreateTaskState());
        viewModel.updateState(new CreateTaskState());

        // Assert (updateState calls both setState and firePropertyChanged, so 3 updates = 6 events)
        assertEquals(6, propertyChangeListener.getEventCount());
        assertEquals("createTaskState", propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testDefaultStateValues() {
        // Test that the default state has expected values
        CreateTaskState defaultState = viewModel.getState();
        
        assertEquals("", defaultState.getTaskName());
        assertEquals("", defaultState.getDescription());
        assertEquals("", defaultState.getCategoryId());
        assertFalse(defaultState.isOneTime());
        assertNull(defaultState.getError());
        assertNull(defaultState.getSuccessMessage());
    }

    @Test
    void testSetStateVsUpdateState() {
        // Test that both setState and updateState work correctly
        CreateTaskState state1 = new CreateTaskState();
        state1.setTaskName("State Task");
        
        CreateTaskState state2 = new CreateTaskState();
        state2.setTaskName("Update Task");
        
        propertyChangeListener.reset();
        
        // Use setState
        viewModel.setState(state1);
        assertEquals("State Task", viewModel.getState().getTaskName());
        assertTrue(propertyChangeListener.wasEventFired());
        
        propertyChangeListener.reset();
        
        // Use updateState
        viewModel.updateState(state2);
        assertEquals("Update Task", viewModel.getState().getTaskName());
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("createTaskState", propertyChangeListener.getLastPropertyName());
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