package interface_adapter.Angela.task.today;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TodayTasksViewModel.
 */
class TodayTasksViewModelTest {

    private TodayTasksViewModel viewModel;
    private TestPropertyChangeListener propertyChangeListener;

    @BeforeEach
    void setUp() {
        viewModel = new TodayTasksViewModel();
        propertyChangeListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(propertyChangeListener);
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertEquals("today tasks", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertFalse(viewModel.getState().isRefreshNeeded());
        assertNull(viewModel.getState().getError());
        assertNull(viewModel.getState().getSuccessMessage());
    }

    @Test
    void testGetViewName() {
        assertEquals("today tasks", viewModel.getViewName());
    }

    @Test
    void testInitialState() {
        TodayTasksState state = viewModel.getState();
        assertNotNull(state);
        assertFalse(state.isRefreshNeeded());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testSetState() {
        // Arrange
        TodayTasksState newState = new TodayTasksState();
        newState.setRefreshNeeded(true);
        newState.setSuccessMessage("Test success");

        // Act
        viewModel.setState(newState);

        // Assert
        assertEquals(newState, viewModel.getState());
        assertTrue(viewModel.getState().isRefreshNeeded());
        assertEquals("Test success", viewModel.getState().getSuccessMessage());
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testSetStateFiresPropertyChange() {
        // Arrange
        TodayTasksState newState = new TodayTasksState();
        newState.setError("Test error");
        propertyChangeListener.reset();

        // Act
        viewModel.setState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("state", propertyChangeListener.getLastPropertyName());
        assertEquals(newState, propertyChangeListener.getLastNewValue());
    }

    @Test
    void testFirePropertyChanged() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged();

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY, propertyChangeListener.getLastPropertyName());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testFirePropertyChangedOverride() {
        // Arrange
        propertyChangeListener.reset();

        // Act - call the overridden firePropertyChanged method
        viewModel.firePropertyChanged();

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("todayTasksState", propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testMultipleStateChanges() {
        // Arrange
        TodayTasksState state1 = new TodayTasksState();
        state1.setRefreshNeeded(true);
        state1.setSuccessMessage("First success");
        
        TodayTasksState state2 = new TodayTasksState();
        state2.setRefreshNeeded(false);
        state2.setError("Some error");
        
        propertyChangeListener.reset();

        // Act
        viewModel.setState(state1);
        viewModel.setState(state2);

        // Assert
        assertEquals(2, propertyChangeListener.getEventCount());
        assertEquals(state2, viewModel.getState());
        assertFalse(viewModel.getState().isRefreshNeeded());
        assertEquals("Some error", viewModel.getState().getError());
        assertNull(viewModel.getState().getSuccessMessage());
    }

    @Test
    void testPropertyChangeListenerManagement() {
        // Arrange
        TestPropertyChangeListener secondListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(secondListener);

        TodayTasksState newState = new TodayTasksState();
        newState.setRefreshNeeded(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertTrue(secondListener.wasEventFired());

        // Test removal
        viewModel.removePropertyChangeListener(secondListener);
        propertyChangeListener.reset();
        secondListener.reset();

        TodayTasksState anotherState = new TodayTasksState();
        viewModel.setState(anotherState);

        assertTrue(propertyChangeListener.wasEventFired());
        assertFalse(secondListener.wasEventFired());
    }

    @Test
    void testSetNullState() {
        // Act
        viewModel.setState(null);

        // Assert
        assertNull(viewModel.getState());
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testConstantValue() {
        assertEquals("todayTasksState", TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY);
    }

    @Test
    void testStateIndependence() {
        // Arrange
        TodayTasksState originalState = viewModel.getState();
        TodayTasksState newState = new TodayTasksState();
        newState.setRefreshNeeded(true);
        newState.setError("Test error");

        // Act
        viewModel.setState(newState);

        // Assert
        assertNotSame(originalState, viewModel.getState());
        assertFalse(originalState.isRefreshNeeded());
        assertNull(originalState.getError());
        assertTrue(viewModel.getState().isRefreshNeeded());
        assertEquals("Test error", viewModel.getState().getError());
    }

    @Test
    void testFirePropertyChangedWithStateChanges() {
        // Test manual firePropertyChanged after state modifications
        TodayTasksState state = viewModel.getState();
        state.setSuccessMessage("Manual change");
        
        propertyChangeListener.reset();
        
        // Act
        viewModel.firePropertyChanged();
        
        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("todayTasksState", propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testSetStateWithCopyConstructor() {
        // Arrange
        TodayTasksState originalState = new TodayTasksState();
        originalState.setRefreshNeeded(true);
        originalState.setError("Original error");
        originalState.setSuccessMessage("Original success");
        
        // Act
        TodayTasksState copiedState = new TodayTasksState(originalState);
        viewModel.setState(copiedState);
        
        // Assert
        assertEquals(copiedState, viewModel.getState());
        assertTrue(viewModel.getState().isRefreshNeeded());
        assertEquals("Original error", viewModel.getState().getError());
        assertEquals("Original success", viewModel.getState().getSuccessMessage());
    }

    @Test
    void testStateModificationAfterSetting() {
        // Test that modifying state after setting it affects the view model
        TodayTasksState state = new TodayTasksState();
        viewModel.setState(state);
        
        // Modify the state object
        state.setError("Added after setting");
        
        // Assert the view model reflects the change
        assertEquals("Added after setting", viewModel.getState().getError());
    }

    @Test
    void testMultipleFirePropertyChangedCalls() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(3, propertyChangeListener.getEventCount());
        assertEquals("todayTasksState", propertyChangeListener.getLastPropertyName());
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