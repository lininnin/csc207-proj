package interface_adapter.Angela.view_history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ViewHistoryViewModel.
 */
class ViewHistoryViewModelTest {

    private ViewHistoryViewModel viewModel;
    private TestPropertyChangeListener propertyChangeListener;

    @BeforeEach
    void setUp() {
        viewModel = new ViewHistoryViewModel();
        propertyChangeListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(propertyChangeListener);
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertNotNull(viewModel.getState());
        
        // Verify initial state is empty/default
        ViewHistoryState initialState = viewModel.getState();
        assertNull(initialState.getSelectedDate());
        assertTrue(initialState.getAvailableDates().isEmpty());
        assertFalse(initialState.hasData());
        assertNull(initialState.getErrorMessage());
        assertEquals(0, initialState.getTaskCompletionRate());
        assertNull(initialState.getExportMessage());
    }

    @Test
    void testSetState() {
        // Arrange
        ViewHistoryState newState = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 12, 25))
                .hasData(true)
                .taskCompletionRate(75)
                .errorMessage("Test error")
                .build();

        // Act
        viewModel.setState(newState);

        // Assert
        assertEquals(newState, viewModel.getState());
        assertEquals(LocalDate.of(2023, 12, 25), viewModel.getState().getSelectedDate());
        assertTrue(viewModel.getState().hasData());
        assertEquals(75, viewModel.getState().getTaskCompletionRate());
        assertEquals("Test error", viewModel.getState().getErrorMessage());
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testSetStateFiresPropertyChange() {
        // Arrange
        ViewHistoryState originalState = viewModel.getState();
        ViewHistoryState newState = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.now())
                .hasData(true)
                .build();
        propertyChangeListener.reset();

        // Act
        viewModel.setState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("state", propertyChangeListener.getLastPropertyName());
        assertEquals(originalState, propertyChangeListener.getLastOldValue());
        assertEquals(newState, propertyChangeListener.getLastNewValue());
    }

    @Test
    void testSetNullState() {
        // Act
        viewModel.setState(null);

        // Assert
        assertNull(viewModel.getState());
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("state", propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testMultipleStateChanges() {
        // Arrange
        ViewHistoryState state1 = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 1, 1))
                .taskCompletionRate(25)
                .build();
        
        ViewHistoryState state2 = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 6, 15))
                .taskCompletionRate(50)
                .build();
        
        ViewHistoryState state3 = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 12, 31))
                .taskCompletionRate(100)
                .build();
        
        propertyChangeListener.reset();

        // Act
        viewModel.setState(state1);
        viewModel.setState(state2);
        viewModel.setState(state3);

        // Assert
        assertEquals(3, propertyChangeListener.getEventCount());
        assertEquals(state3, viewModel.getState());
        assertEquals(LocalDate.of(2023, 12, 31), viewModel.getState().getSelectedDate());
        assertEquals(100, viewModel.getState().getTaskCompletionRate());
    }

    @Test
    void testFirePropertyChanged() {
        // Arrange
        String propertyName = "customProperty";
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged(propertyName);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(propertyName, propertyChangeListener.getLastPropertyName());
        assertNull(propertyChangeListener.getLastOldValue());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testFirePropertyChangedWithVariousNames() {
        // Test different property names
        String[] propertyNames = {"dateChanged", "dataLoaded", "exportCompleted", "errorOccurred"};
        
        for (String propertyName : propertyNames) {
            propertyChangeListener.reset();
            
            viewModel.firePropertyChanged(propertyName);
            
            assertTrue(propertyChangeListener.wasEventFired());
            assertEquals(propertyName, propertyChangeListener.getLastPropertyName());
        }
    }

    @Test
    void testFirePropertyChangedWithNullName() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged(null);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertNull(propertyChangeListener.getLastPropertyName());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testFirePropertyChangedWithEmptyName() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged("");

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("", propertyChangeListener.getLastPropertyName());
    }

    @Test
    void testPropertyChangeListenerManagement() {
        // Arrange
        TestPropertyChangeListener secondListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(secondListener);

        ViewHistoryState newState = new ViewHistoryState.Builder()
                .hasData(true)
                .build();

        // Act
        viewModel.setState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertTrue(secondListener.wasEventFired());

        // Test removal
        viewModel.removePropertyChangeListener(secondListener);
        propertyChangeListener.reset();
        secondListener.reset();

        ViewHistoryState anotherState = new ViewHistoryState.Builder()
                .hasData(false)
                .build();
        viewModel.setState(anotherState);

        assertTrue(propertyChangeListener.wasEventFired());
        assertFalse(secondListener.wasEventFired());
    }

    @Test
    void testMultiplePropertyChangeListeners() {
        // Arrange
        TestPropertyChangeListener listener2 = new TestPropertyChangeListener();
        TestPropertyChangeListener listener3 = new TestPropertyChangeListener();
        
        viewModel.addPropertyChangeListener(listener2);
        viewModel.addPropertyChangeListener(listener3);

        ViewHistoryState newState = new ViewHistoryState.Builder()
                .errorMessage("Test error")
                .build();

        // Act
        viewModel.setState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertTrue(listener2.wasEventFired());
        assertTrue(listener3.wasEventFired());
        
        assertEquals("state", propertyChangeListener.getLastPropertyName());
        assertEquals("state", listener2.getLastPropertyName());
        assertEquals("state", listener3.getLastPropertyName());
    }

    @Test
    void testStateIndependence() {
        // Arrange
        ViewHistoryState originalState = viewModel.getState();
        ViewHistoryState newState = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 5, 10))
                .taskCompletionRate(60)
                .build();

        // Act
        viewModel.setState(newState);

        // Assert
        assertNotSame(originalState, viewModel.getState());
        assertNull(originalState.getSelectedDate());
        assertEquals(0, originalState.getTaskCompletionRate());
        assertEquals(LocalDate.of(2023, 5, 10), viewModel.getState().getSelectedDate());
        assertEquals(60, viewModel.getState().getTaskCompletionRate());
    }

    @Test
    void testComplexStateHandling() {
        // Arrange
        ViewHistoryState complexState = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 8, 15))
                .availableDates(Arrays.asList(
                        LocalDate.of(2023, 8, 15),
                        LocalDate.of(2023, 8, 14),
                        LocalDate.of(2023, 8, 13)
                ))
                .hasData(true)
                .errorMessage("Complex test error")
                .taskCompletionRate(88)
                .exportMessage("Export successful")
                .build();

        // Act
        viewModel.setState(complexState);

        // Assert
        ViewHistoryState retrievedState = viewModel.getState();
        assertEquals(LocalDate.of(2023, 8, 15), retrievedState.getSelectedDate());
        assertEquals(3, retrievedState.getAvailableDates().size());
        assertTrue(retrievedState.hasData());
        assertEquals("Complex test error", retrievedState.getErrorMessage());
        assertEquals(88, retrievedState.getTaskCompletionRate());
        assertEquals("Export successful", retrievedState.getExportMessage());
    }

    @Test
    void testStateAfterDirectModification() {
        // Test that the view model handles state references correctly
        ViewHistoryState state = new ViewHistoryState.Builder()
                .selectedDate(LocalDate.of(2023, 3, 20))
                .build();
        
        viewModel.setState(state);
        
        // Since ViewHistoryState is immutable, this test verifies 
        // that the state reference is maintained correctly
        assertEquals(state, viewModel.getState());
        assertEquals(LocalDate.of(2023, 3, 20), viewModel.getState().getSelectedDate());
    }

    @Test
    void testRemoveNonExistentListener() {
        // Arrange
        TestPropertyChangeListener nonExistentListener = new TestPropertyChangeListener();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> viewModel.removePropertyChangeListener(nonExistentListener));
        
        // Verify existing listener still works
        ViewHistoryState newState = new ViewHistoryState.Builder().hasData(true).build();
        viewModel.setState(newState);
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testAddSameListenerMultipleTimes() {
        // Arrange
        viewModel.addPropertyChangeListener(propertyChangeListener); // Add again
        propertyChangeListener.reset();

        ViewHistoryState newState = new ViewHistoryState.Builder()
                .hasData(true)
                .build();

        // Act
        viewModel.setState(newState);

        // Assert - should receive event twice
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(2, propertyChangeListener.getEventCount());
    }

    @Test
    void testPropertyChangeWithNullState() {
        // Set state to null first
        viewModel.setState(null);
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged("testProperty");

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals("testProperty", propertyChangeListener.getLastPropertyName());
        assertNull(propertyChangeListener.getLastNewValue());
    }

    @Test
    void testMixedStateAndPropertyChanges() {
        // Test combination of setState and firePropertyChanged
        ViewHistoryState state1 = new ViewHistoryState.Builder().taskCompletionRate(30).build();
        
        propertyChangeListener.reset();
        
        // Act
        viewModel.setState(state1);
        viewModel.firePropertyChanged("customEvent");
        
        // Assert
        assertEquals(2, propertyChangeListener.getEventCount());
        
        // Last event should be the firePropertyChanged
        assertEquals("customEvent", propertyChangeListener.getLastPropertyName());
        assertEquals(state1, propertyChangeListener.getLastNewValue());
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