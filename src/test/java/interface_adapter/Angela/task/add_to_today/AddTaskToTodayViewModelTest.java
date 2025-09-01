package interface_adapter.Angela.task.add_to_today;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddTaskToTodayViewModelTest {

    private AddTaskToTodayViewModel viewModel;
    private PropertyChangeListener mockPropertyChangeListener;
    private AtomicInteger propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new AddTaskToTodayViewModel();
        mockPropertyChangeListener = mock(PropertyChangeListener.class);
        propertyChangeCount = new AtomicInteger(0);

        // Add real listener to count property changes
        viewModel.addPropertyChangeListener(evt -> propertyChangeCount.incrementAndGet());
        viewModel.addPropertyChangeListener(mockPropertyChangeListener);
    }

    @Test
    void testConstructor() {
        // Assert
        assertEquals("add to today", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertInstanceOf(AddTaskToTodayState.class, viewModel.getState());
    }

    @Test
    void testConstants() {
        assertEquals("addToTodayState", AddTaskToTodayViewModel.ADD_TO_TODAY_STATE_PROPERTY);
    }

    @Test
    void testInitialState() {
        // Act
        AddTaskToTodayState initialState = viewModel.getState();

        // Assert
        assertNotNull(initialState);
        assertNull(initialState.getError());
        assertNull(initialState.getSuccessMessage());
        assertFalse(initialState.isRefreshNeeded());
    }

    @Test
    void testSetState() {
        // Arrange
        AddTaskToTodayState newState = new AddTaskToTodayState();
        newState.setError("Test error");
        newState.setSuccessMessage("Test success");
        newState.setRefreshNeeded(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertSame(newState, viewModel.getState());
        assertEquals("Test error", viewModel.getState().getError());
        assertEquals("Test success", viewModel.getState().getSuccessMessage());
        assertTrue(viewModel.getState().isRefreshNeeded());
    }

    @Test
    void testSetStateWithNull() {
        // Act
        viewModel.setState(null);

        // Assert
        assertNull(viewModel.getState());
    }

    @Test
    void testFirePropertyChanged() {
        // Arrange
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(initialCount + 1, propertyChangeCount.get());
        verify(mockPropertyChangeListener, times(1)).propertyChange(any());
    }

    @Test
    void testFirePropertyChangedMultipleTimes() {
        // Arrange
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(initialCount + 3, propertyChangeCount.get());
        verify(mockPropertyChangeListener, times(3)).propertyChange(any());
    }

    @Test
    void testAddPropertyChangeListener() {
        // Arrange
        PropertyChangeListener additionalListener = mock(PropertyChangeListener.class);

        // Act
        viewModel.addPropertyChangeListener(additionalListener);
        viewModel.firePropertyChanged();

        // Assert
        verify(mockPropertyChangeListener, times(1)).propertyChange(any());
        verify(additionalListener, times(1)).propertyChange(any());
    }

    @Test
    void testMultiplePropertyChangeListeners() {
        // Arrange
        PropertyChangeListener listener1 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener2 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener3 = mock(PropertyChangeListener.class);

        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);
        viewModel.addPropertyChangeListener(listener3);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        verify(listener1, times(1)).propertyChange(any());
        verify(listener2, times(1)).propertyChange(any());
        verify(listener3, times(1)).propertyChange(any());
    }

    @Test
    void testStateTransitions() {
        // Arrange
        AddTaskToTodayState state1 = new AddTaskToTodayState();
        state1.setError("First error");

        AddTaskToTodayState state2 = new AddTaskToTodayState();
        state2.setSuccessMessage("Success message");

        AddTaskToTodayState state3 = new AddTaskToTodayState();
        state3.setRefreshNeeded(true);

        // Act & Assert
        viewModel.setState(state1);
        assertEquals("First error", viewModel.getState().getError());
        assertNull(viewModel.getState().getSuccessMessage());

        viewModel.setState(state2);
        assertNull(viewModel.getState().getError());
        assertEquals("Success message", viewModel.getState().getSuccessMessage());

        viewModel.setState(state3);
        assertNull(viewModel.getState().getError());
        assertNull(viewModel.getState().getSuccessMessage());
        assertTrue(viewModel.getState().isRefreshNeeded());
    }

    @Test
    void testSetSameStateReference() {
        // Arrange
        AddTaskToTodayState state = new AddTaskToTodayState();
        state.setError("Test error");

        // Act
        viewModel.setState(state);
        AddTaskToTodayState retrievedState1 = viewModel.getState();
        
        viewModel.setState(state); // Set same reference again
        AddTaskToTodayState retrievedState2 = viewModel.getState();

        // Assert
        assertSame(state, retrievedState1);
        assertSame(state, retrievedState2);
        assertSame(retrievedState1, retrievedState2);
    }

    @Test
    void testFirePropertyChangedWithDifferentStates() {
        // Arrange
        AddTaskToTodayState errorState = new AddTaskToTodayState();
        errorState.setError("Some error");

        AddTaskToTodayState successState = new AddTaskToTodayState();
        successState.setSuccessMessage("Success");

        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.setState(errorState);
        viewModel.firePropertyChanged();

        viewModel.setState(successState);
        viewModel.firePropertyChanged();

        // Assert - Verify property changes were fired
        verify(mockPropertyChangeListener, atLeast(2)).propertyChange(any());
        assertEquals("Success", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());
    }

    @Test
    void testFirePropertyChangedWithNullState() {
        // Arrange
        viewModel.setState(null);
        reset(mockPropertyChangeListener); // Reset to start fresh

        // Act
        viewModel.firePropertyChanged();

        // Assert - Verify property change was fired
        verify(mockPropertyChangeListener, atLeast(1)).propertyChange(any());
    }

    @Test
    void testComplexStateUpdatesWithPropertyChanges() {
        // Arrange
        AddTaskToTodayState complexState = new AddTaskToTodayState();
        complexState.setError("Complex error");
        complexState.setSuccessMessage("Complex success");
        complexState.setRefreshNeeded(true);
        reset(mockPropertyChangeListener); // Reset to start fresh

        // Act
        viewModel.setState(complexState);
        viewModel.firePropertyChanged();

        // Assert - Verify property changes were fired
        verify(mockPropertyChangeListener, atLeast(1)).propertyChange(any());
        assertEquals("Complex error", viewModel.getState().getError());
        assertEquals("Complex success", viewModel.getState().getSuccessMessage());
        assertTrue(viewModel.getState().isRefreshNeeded());
    }

    @Test
    void testViewModelInheritance() {
        // Assert that it properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        assertEquals("add to today", viewModel.getViewName());
    }

    @Test
    void testPropertyChangePropertyName() {
        // This test verifies that the correct property name is used
        // by checking the constant value
        assertEquals("addToTodayState", AddTaskToTodayViewModel.ADD_TO_TODAY_STATE_PROPERTY);
    }

    @Test
    void testStateModificationAfterSetting() {
        // Arrange
        AddTaskToTodayState state = new AddTaskToTodayState();
        viewModel.setState(state);

        // Act - Modify the state after setting it
        state.setError("Modified error");
        state.setRefreshNeeded(true);

        // Assert - The view model should reflect the changes since it holds the same reference
        assertEquals("Modified error", viewModel.getState().getError());
        assertTrue(viewModel.getState().isRefreshNeeded());
    }

    @Test
    void testMultipleStateReplacements() {
        // Arrange
        AddTaskToTodayState state1 = new AddTaskToTodayState();
        state1.setError("Error 1");

        AddTaskToTodayState state2 = new AddTaskToTodayState();
        state2.setError("Error 2");

        AddTaskToTodayState state3 = new AddTaskToTodayState();
        state3.setError("Error 3");

        // Act
        viewModel.setState(state1);
        assertEquals("Error 1", viewModel.getState().getError());

        viewModel.setState(state2);
        assertEquals("Error 2", viewModel.getState().getError());

        viewModel.setState(state3);
        assertEquals("Error 3", viewModel.getState().getError());

        // Assert - Final state
        assertSame(state3, viewModel.getState());
    }
}