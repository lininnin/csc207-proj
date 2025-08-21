package interface_adapter.Angela.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CategoryManagementViewModel.
 */
class CategoryManagementViewModelTest {

    private CategoryManagementViewModel viewModel;
    private TestPropertyChangeListener propertyChangeListener;

    @BeforeEach
    void setUp() {
        viewModel = new CategoryManagementViewModel();
        propertyChangeListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(propertyChangeListener);
    }

    @Test
    void testConstructor() {
        assertNotNull(viewModel);
        assertEquals("category management", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        
        // Verify initial state
        CategoryManagementState initialState = viewModel.getState();
        assertNull(initialState.getLastAction());
        assertNull(initialState.getMessage());
        assertNull(initialState.getError());
        assertFalse(initialState.isRefreshNeeded());
        assertFalse(initialState.isDialogOpen());
    }

    @Test
    void testGetViewName() {
        assertEquals("category management", viewModel.getViewName());
    }

    @Test
    void testStaticConstants() {
        assertEquals("categoryState", CategoryManagementViewModel.CATEGORY_STATE_PROPERTY);
    }

    @Test
    void testSetState() {
        // Arrange
        CategoryManagementState newState = new CategoryManagementState();
        newState.setLastAction("create");
        newState.setMessage("Category created");
        newState.setRefreshNeeded(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertEquals(newState, viewModel.getState());
        assertEquals("create", viewModel.getState().getLastAction());
        assertEquals("Category created", viewModel.getState().getMessage());
        assertTrue(viewModel.getState().isRefreshNeeded());
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testSetStateFiresPropertyChange() {
        // Arrange
        CategoryManagementState originalState = viewModel.getState();
        CategoryManagementState newState = new CategoryManagementState();
        newState.setLastAction("edit");
        newState.setDialogOpen(true);
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
        CategoryManagementState state1 = new CategoryManagementState();
        state1.setLastAction("create");
        state1.setMessage("Creating category");
        
        CategoryManagementState state2 = new CategoryManagementState();
        state2.setLastAction("edit");
        state2.setError("Validation error");
        
        CategoryManagementState state3 = new CategoryManagementState();
        state3.setLastAction("delete");
        state3.setRefreshNeeded(true);
        
        propertyChangeListener.reset();

        // Act
        viewModel.setState(state1);
        viewModel.setState(state2);
        viewModel.setState(state3);

        // Assert
        assertEquals(3, propertyChangeListener.getEventCount());
        assertEquals(state3, viewModel.getState());
        assertEquals("delete", viewModel.getState().getLastAction());
        assertTrue(viewModel.getState().isRefreshNeeded());
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
        assertNull(propertyChangeListener.getLastOldValue());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testFirePropertyChangedWithPropertyName() {
        // Arrange
        String propertyName = CategoryManagementViewModel.CATEGORY_STATE_PROPERTY;
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
    void testFirePropertyChangedWithCustomPropertyName() {
        // Arrange
        String customPropertyName = "customCategoryEvent";
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged(customPropertyName);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertEquals(customPropertyName, propertyChangeListener.getLastPropertyName());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testFirePropertyChangedWithNullPropertyName() {
        // Arrange
        propertyChangeListener.reset();

        // Act
        viewModel.firePropertyChanged((String) null);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertNull(propertyChangeListener.getLastPropertyName());
        assertEquals(viewModel.getState(), propertyChangeListener.getLastNewValue());
    }

    @Test
    void testPropertyChangeListenerManagement() {
        // Arrange
        TestPropertyChangeListener secondListener = new TestPropertyChangeListener();
        viewModel.addPropertyChangeListener(secondListener);

        CategoryManagementState newState = new CategoryManagementState();
        newState.setMessage("Test message");

        // Act
        viewModel.setState(newState);

        // Assert
        assertTrue(propertyChangeListener.wasEventFired());
        assertTrue(secondListener.wasEventFired());

        // Test removal
        viewModel.removePropertyChangeListener(secondListener);
        propertyChangeListener.reset();
        secondListener.reset();

        CategoryManagementState anotherState = new CategoryManagementState();
        anotherState.setError("Test error");
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

        CategoryManagementState newState = new CategoryManagementState();
        newState.setLastAction("create");

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
        CategoryManagementState originalState = viewModel.getState();
        CategoryManagementState newState = new CategoryManagementState();
        newState.setLastAction("edit");
        newState.setDialogOpen(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertNotSame(originalState, viewModel.getState());
        assertNull(originalState.getLastAction());
        assertFalse(originalState.isDialogOpen());
        assertEquals("edit", viewModel.getState().getLastAction());
        assertTrue(viewModel.getState().isDialogOpen());
    }

    @Test
    void testComplexStateHandling() {
        // Arrange
        CategoryManagementState complexState = new CategoryManagementState();
        complexState.setLastAction("delete");
        complexState.setMessage("Category 'Work' deleted successfully");
        complexState.setError(null);
        complexState.setRefreshNeeded(true);
        complexState.setDialogOpen(false);

        // Act
        viewModel.setState(complexState);

        // Assert
        CategoryManagementState retrievedState = viewModel.getState();
        assertEquals("delete", retrievedState.getLastAction());
        assertEquals("Category 'Work' deleted successfully", retrievedState.getMessage());
        assertNull(retrievedState.getError());
        assertTrue(retrievedState.isRefreshNeeded());
        assertFalse(retrievedState.isDialogOpen());
    }

    @Test
    void testRemoveNonExistentListener() {
        // Arrange
        TestPropertyChangeListener nonExistentListener = new TestPropertyChangeListener();

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> viewModel.removePropertyChangeListener(nonExistentListener));
        
        // Verify existing listener still works
        CategoryManagementState newState = new CategoryManagementState();
        newState.setRefreshNeeded(true);
        viewModel.setState(newState);
        assertTrue(propertyChangeListener.wasEventFired());
    }

    @Test
    void testAddSameListenerMultipleTimes() {
        // Arrange
        viewModel.addPropertyChangeListener(propertyChangeListener); // Add again
        propertyChangeListener.reset();

        CategoryManagementState newState = new CategoryManagementState();
        newState.setDialogOpen(true);

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
        CategoryManagementState state1 = new CategoryManagementState();
        state1.setLastAction("create");
        
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

    @Test
    void testConstantUsage() {
        // Test that the constant can be used for property change events
        CategoryManagementState newState = new CategoryManagementState();
        newState.setError("Test error message");
        
        propertyChangeListener.reset();
        viewModel.setState(newState);
        viewModel.firePropertyChanged(CategoryManagementViewModel.CATEGORY_STATE_PROPERTY);
        
        assertEquals(2, propertyChangeListener.getEventCount());
        assertEquals("categoryState", propertyChangeListener.getLastPropertyName());
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