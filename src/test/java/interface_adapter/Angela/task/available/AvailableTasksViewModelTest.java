package interface_adapter.Angela.task.available;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AvailableTasksViewModel.
 */
class AvailableTasksViewModelTest {

    private AvailableTasksViewModel viewModel;
    private TestPropertyChangeListener propertyChangeListener;

    @BeforeEach
    void setUp() {
        viewModel = new AvailableTasksViewModel();
        propertyChangeListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(propertyChangeListener);
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertEquals("available tasks", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertFalse(viewModel.getState().isRefreshNeeded());
    }

    @Test
    void testGetViewName() {
        assertEquals("available tasks", viewModel.getViewName());
    }

    @Test
    void testInitialState() {
        AvailableTasksState state = viewModel.getState();
        assertNotNull(state);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testSetState() {
        // Arrange
        AvailableTasksState newState = new AvailableTasksState();
        newState.setRefreshNeeded(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertEquals(newState, viewModel.getState());
        assertTrue(viewModel.getState().isRefreshNeeded());
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testSetStateFiresPropertyChange() {
        // Arrange
        AvailableTasksState newState = new AvailableTasksState();
        newState.setRefreshNeeded(true);
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
        assertEquals("state", propertyChangeListener.getLastPropertyName());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testFirePropertyChangedWithSpecificProperty() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY, propertyChangeListener.getLastPropertyName());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testMultipleStateChanges() {
        // Arrange
        AvailableTasksState state1 = new AvailableTasksState();
        state1.setRefreshNeeded(true);
        
        AvailableTasksState state2 = new AvailableTasksState();
        state2.setRefreshNeeded(false);
        
        propertyChangeListener.reset();

        // Act
        viewModel.setState(state1);
        viewModel.setState(state2);

        // Assert
        assertEquals(2, propertyChangeListener.getEventCount());
        assertEquals(state2, viewModel.getState());
        assertFalse(viewModel.getState().isRefreshNeeded());
    }

    @Test
    void testPropertyChangeListenerManagement() {
        // Arrange
        TestPropertyChangeListener secondListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(secondListener);

        AvailableTasksState newState = new AvailableTasksState();
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

        AvailableTasksState anotherState = new AvailableTasksState();
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
        assertEquals("availableTasksState", AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
    }

    @Test
    void testStateIndependence() {
        // Arrange
        AvailableTasksState originalState = viewModel.getState();
        AvailableTasksState newState = new AvailableTasksState();
        newState.setRefreshNeeded(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertNotSame(originalState, viewModel.getState());
        assertFalse(originalState.isRefreshNeeded());
        assertTrue(viewModel.getState().isRefreshNeeded());
    }

    @Test
    void testMultiplePropertyChangeEvents() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(3, propertyChangeListener.getEventCount());
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