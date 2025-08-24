package interface_adapter.Angela.task.edit_today;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditTodayTaskViewModelTest {

    private EditTodayTaskViewModel viewModel;
    private PropertyChangeListener mockPropertyChangeListener;
    private AtomicInteger propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new EditTodayTaskViewModel();
        mockPropertyChangeListener = mock(PropertyChangeListener.class);
        propertyChangeCount = new AtomicInteger(0);

        // Add real listener to count property changes
        viewModel.addPropertyChangeListener(evt -> propertyChangeCount.incrementAndGet());
        viewModel.addPropertyChangeListener(mockPropertyChangeListener);
    }

    @Test
    void testConstructor() {
        // Assert
        assertEquals("edit today task", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertInstanceOf(EditTodayTaskState.class, viewModel.getState());
    }

    @Test
    void testConstants() {
        assertEquals("editTodayTaskState", EditTodayTaskViewModel.EDIT_TODAY_TASK_STATE_PROPERTY);
    }

    @Test
    void testInitialState() {
        // Act
        EditTodayTaskState initialState = viewModel.getState();

        // Assert
        assertNotNull(initialState);
        assertNull(initialState.getEditingTaskId());
        assertNull(initialState.getLastEditedTaskId());
        assertNull(initialState.getSuccessMessage());
        assertNull(initialState.getError());
        assertFalse(initialState.isShowDialog());
    }

    @Test
    void testSetState() {
        // Arrange
        EditTodayTaskState newState = new EditTodayTaskState();
        newState.setEditingTaskId("test-task-123");
        newState.setSuccessMessage("Task updated successfully");
        newState.setShowDialog(true);

        // Act
        viewModel.setState(newState);

        // Assert
        assertSame(newState, viewModel.getState());
        assertEquals("test-task-123", viewModel.getState().getEditingTaskId());
        assertEquals("Task updated successfully", viewModel.getState().getSuccessMessage());
        assertTrue(viewModel.getState().isShowDialog());
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
        EditTodayTaskState state1 = new EditTodayTaskState();
        state1.setEditingTaskId("task-1");
        state1.setError("");

        EditTodayTaskState state2 = new EditTodayTaskState();
        state2.setEditingTaskId("task-2");
        state2.setError("Validation failed");

        EditTodayTaskState state3 = new EditTodayTaskState();
        state3.setLastEditedTaskId("task-2");
        state3.setSuccessMessage("Task updated successfully");

        // Act & Assert
        viewModel.setState(state1);
        assertEquals("task-1", viewModel.getState().getEditingTaskId());
        assertEquals("", viewModel.getState().getError());

        viewModel.setState(state2);
        assertEquals("task-2", viewModel.getState().getEditingTaskId());
        assertEquals("Validation failed", viewModel.getState().getError());

        viewModel.setState(state3);
        assertEquals("task-2", viewModel.getState().getLastEditedTaskId());
        assertEquals("Task updated successfully", viewModel.getState().getSuccessMessage());
    }

    @Test
    void testSetSameStateReference() {
        // Arrange
        EditTodayTaskState state = new EditTodayTaskState();
        state.setEditingTaskId("same-task");
        state.setSuccessMessage("Test Success");

        // Act
        viewModel.setState(state);
        EditTodayTaskState retrievedState1 = viewModel.getState();
        
        viewModel.setState(state); // Set same reference again
        EditTodayTaskState retrievedState2 = viewModel.getState();

        // Assert
        assertSame(state, retrievedState1);
        assertSame(state, retrievedState2);
        assertSame(retrievedState1, retrievedState2);
    }

    @Test
    void testFirePropertyChangedWithDifferentStates() {
        // Arrange
        EditTodayTaskState errorState = new EditTodayTaskState();
        errorState.setError("Validation error");
        errorState.setEditingTaskId("error-task");

        EditTodayTaskState validState = new EditTodayTaskState();
        validState.setLastEditedTaskId("valid-task");
        validState.setSuccessMessage("Successfully updated");

        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.setState(errorState);
        viewModel.firePropertyChanged();

        viewModel.setState(validState);
        viewModel.firePropertyChanged();

        // Assert
        verify(mockPropertyChangeListener, atLeast(2)).propertyChange(any());
        assertEquals("valid-task", viewModel.getState().getLastEditedTaskId());
        assertEquals("Successfully updated", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());
    }

    @Test
    void testFirePropertyChangedWithNullState() {
        // Arrange
        viewModel.setState(null);
        reset(mockPropertyChangeListener);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        verify(mockPropertyChangeListener, atLeast(1)).propertyChange(any());
    }

    @Test
    void testComplexStateWithPropertyChanges() {
        // Arrange
        EditTodayTaskState complexState = new EditTodayTaskState();
        complexState.setEditingTaskId("complex-task-123");
        complexState.setLastEditedTaskId("previous-task");
        complexState.setSuccessMessage("Complex update successful");
        complexState.setError(null);
        complexState.setShowDialog(true);

        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.setState(complexState);
        viewModel.firePropertyChanged();

        // Assert
        verify(mockPropertyChangeListener, atLeast(1)).propertyChange(any());
        assertEquals("complex-task-123", viewModel.getState().getEditingTaskId());
        assertEquals("previous-task", viewModel.getState().getLastEditedTaskId());
        assertEquals("Complex update successful", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());
        assertTrue(viewModel.getState().isShowDialog());
    }

    @Test
    void testViewModelInheritance() {
        // Assert that it properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        assertEquals("edit today task", viewModel.getViewName());
    }

    @Test
    void testStateModificationAfterSetting() {
        // Arrange
        EditTodayTaskState state = new EditTodayTaskState();
        viewModel.setState(state);

        // Act - Modify the state after setting it
        state.setEditingTaskId("modified-task");
        state.setSuccessMessage("Modified success message");
        state.setError("Modified error");

        // Assert - The view model should reflect the changes since it holds the same reference
        assertEquals("modified-task", viewModel.getState().getEditingTaskId());
        assertEquals("Modified success message", viewModel.getState().getSuccessMessage());
        assertEquals("Modified error", viewModel.getState().getError());
    }

    @Test
    void testMultipleStateReplacements() {
        // Arrange
        EditTodayTaskState state1 = new EditTodayTaskState();
        state1.setEditingTaskId("state-1");

        EditTodayTaskState state2 = new EditTodayTaskState();
        state2.setEditingTaskId("state-2");

        EditTodayTaskState state3 = new EditTodayTaskState();
        state3.setEditingTaskId("state-3");

        // Act
        viewModel.setState(state1);
        assertEquals("state-1", viewModel.getState().getEditingTaskId());

        viewModel.setState(state2);
        assertEquals("state-2", viewModel.getState().getEditingTaskId());

        viewModel.setState(state3);
        assertEquals("state-3", viewModel.getState().getEditingTaskId());

        // Assert - Final state
        assertSame(state3, viewModel.getState());
    }

    @Test
    void testPropertyChangePropertyName() {
        // This test verifies that the correct property name is used
        // by checking the constant value
        assertEquals("editTodayTaskState", EditTodayTaskViewModel.EDIT_TODAY_TASK_STATE_PROPERTY);
    }

    @Test
    void testErrorStateHandling() {
        // Arrange
        EditTodayTaskState errorState = new EditTodayTaskState();
        errorState.setEditingTaskId("error-task");
        errorState.setError("Validation failed: Name is required");
        errorState.setShowDialog(true);

        // Act
        viewModel.setState(errorState);

        // Assert
        assertEquals("error-task", viewModel.getState().getEditingTaskId());
        assertEquals("Validation failed: Name is required", viewModel.getState().getError());
        assertTrue(viewModel.getState().isShowDialog());
        assertNull(viewModel.getState().getSuccessMessage());
    }

    @Test
    void testStateWithDialogAndMessages() {
        // Arrange
        EditTodayTaskState stateWithExtras = new EditTodayTaskState();
        stateWithExtras.setEditingTaskId("dialog-task");
        stateWithExtras.setShowDialog(true);
        stateWithExtras.setSuccessMessage("Task saved successfully");

        // Act
        viewModel.setState(stateWithExtras);

        // Assert
        assertEquals("dialog-task", viewModel.getState().getEditingTaskId());
        assertTrue(viewModel.getState().isShowDialog());
        assertEquals("Task saved successfully", viewModel.getState().getSuccessMessage());
    }
}