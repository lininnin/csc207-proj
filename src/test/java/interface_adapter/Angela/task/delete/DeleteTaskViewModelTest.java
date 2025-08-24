package interface_adapter.Angela.task.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteTaskViewModelTest {

    private DeleteTaskViewModel viewModel;
    private PropertyChangeListener mockPropertyChangeListener;
    private AtomicInteger propertyChangeCount;

    @BeforeEach
    void setUp() {
        viewModel = new DeleteTaskViewModel();
        mockPropertyChangeListener = mock(PropertyChangeListener.class);
        propertyChangeCount = new AtomicInteger(0);

        // Add real listener to count property changes
        viewModel.addPropertyChangeListener(evt -> propertyChangeCount.incrementAndGet());
        viewModel.addPropertyChangeListener(mockPropertyChangeListener);
    }

    @Test
    void testConstructor() {
        // Assert
        assertEquals("delete task", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertInstanceOf(DeleteTaskState.class, viewModel.getState());
    }

    @Test
    void testConstants() {
        assertEquals("deleteTaskState", DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Test
    void testInitialState() {
        // Act
        DeleteTaskState initialState = viewModel.getState();

        // Assert
        assertNotNull(initialState);
        assertNull(initialState.getLastDeletedTaskId());
        assertNull(initialState.getSuccessMessage());
        assertNull(initialState.getError());
        assertFalse(initialState.isShowWarningDialog());
        assertNull(initialState.getPendingDeleteTaskId());
        assertNull(initialState.getPendingDeleteTaskName());
    }

    @Test
    void testSetState() {
        // Arrange
        DeleteTaskState newState = new DeleteTaskState();
        newState.setLastDeletedTaskId("test-task-123");
        newState.setSuccessMessage("Task deleted successfully");
        newState.setError(null);

        // Act
        viewModel.setState(newState);

        // Assert
        assertSame(newState, viewModel.getState());
        assertEquals("test-task-123", viewModel.getState().getLastDeletedTaskId());
        assertEquals("Task deleted successfully", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());
    }

    @Test
    void testSetStateWithNull() {
        // Act
        viewModel.setState(null);

        // Assert
        assertNull(viewModel.getState());
    }

    @Test
    void testUpdateState() {
        // Arrange
        DeleteTaskState newState = new DeleteTaskState();
        newState.setLastDeletedTaskId("updated-task-456");
        newState.setSuccessMessage("Update successful");
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.updateState(newState);

        // Assert
        assertSame(newState, viewModel.getState());
        assertEquals("updated-task-456", viewModel.getState().getLastDeletedTaskId());
        assertEquals("Update successful", viewModel.getState().getSuccessMessage());
        
        // Verify property change was fired
        verify(mockPropertyChangeListener, atLeast(1)).propertyChange(any());
    }

    @Test
    void testUpdateStateWithNullState() {
        // Arrange
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.updateState(null);

        // Assert
        assertNull(viewModel.getState());
        assertEquals(initialCount + 2, propertyChangeCount.get());
        verify(mockPropertyChangeListener, times(2)).propertyChange(any());
    }

    @Test
    void testUpdateStateWithErrorState() {
        // Arrange
        DeleteTaskState errorState = new DeleteTaskState();
        errorState.setError("Task not found");
        errorState.setLastDeletedTaskId(null);
        errorState.setSuccessMessage(null);
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.updateState(errorState);

        // Assert
        assertEquals("Task not found", viewModel.getState().getError());
        assertNull(viewModel.getState().getLastDeletedTaskId());
        assertNull(viewModel.getState().getSuccessMessage());
        assertEquals(initialCount + 2, propertyChangeCount.get());
    }

    @Test
    void testUpdateStateWithPendingDeleteState() {
        // Arrange
        DeleteTaskState pendingState = new DeleteTaskState();
        pendingState.setPendingDeleteTaskId("pending-123");
        pendingState.setPendingDeleteTaskName("Task to Delete");
        pendingState.setShowWarningDialog(true);
        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.updateState(pendingState);

        // Assert
        assertEquals("pending-123", viewModel.getState().getPendingDeleteTaskId());
        assertEquals("Task to Delete", viewModel.getState().getPendingDeleteTaskName());
        assertTrue(viewModel.getState().isShowWarningDialog());
        assertEquals(initialCount + 2, propertyChangeCount.get());
    }

    @Test
    void testMultipleUpdateStateCalls() {
        // Arrange
        DeleteTaskState state1 = new DeleteTaskState();
        state1.setLastDeletedTaskId("task-1");
        state1.setSuccessMessage("First success");

        DeleteTaskState state2 = new DeleteTaskState();
        state2.setLastDeletedTaskId("task-2");
        state2.setSuccessMessage("Second success");

        DeleteTaskState state3 = new DeleteTaskState();
        state3.setError("Third error");

        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.updateState(state1);
        viewModel.updateState(state2);
        viewModel.updateState(state3);

        // Assert
        assertEquals(initialCount + 6, propertyChangeCount.get());
        verify(mockPropertyChangeListener, times(6)).propertyChange(any());
        
        // Final state should be state3
        assertSame(state3, viewModel.getState());
        assertEquals("Third error", viewModel.getState().getError());
    }

    @Test
    void testUpdateStateVsSetState() {
        // Arrange
        DeleteTaskState state = new DeleteTaskState();
        state.setLastDeletedTaskId("test-comparison");
        int initialCount = propertyChangeCount.get();

        // Act - setState doesn't fire property change automatically
        viewModel.setState(state);
        int countAfterSetState = propertyChangeCount.get();

        // updateState fires property change automatically
        viewModel.updateState(state);
        int countAfterUpdateState = propertyChangeCount.get();

        // Assert
        assertEquals(initialCount + 1, countAfterSetState); // setState fires 1 property change
        assertEquals(countAfterSetState + 1, countAfterUpdateState); // updateState fires 1 additional property change
    }

    @Test
    void testPropertyChangeListeners() {
        // Arrange
        PropertyChangeListener listener1 = mock(PropertyChangeListener.class);
        PropertyChangeListener listener2 = mock(PropertyChangeListener.class);
        
        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);

        DeleteTaskState state = new DeleteTaskState();
        state.setLastDeletedTaskId("multi-listener-test");

        // Act
        viewModel.updateState(state);

        // Assert
        verify(listener1, times(2)).propertyChange(any());
        verify(listener2, times(2)).propertyChange(any());
    }

    @Test
    void testComplexStateTransitions() {
        // Arrange
        DeleteTaskState pendingState = new DeleteTaskState();
        pendingState.setPendingDeleteTaskId("complex-123");
        pendingState.setPendingDeleteTaskName("Complex Task");
        pendingState.setShowWarningDialog(true);

        DeleteTaskState errorState = new DeleteTaskState();
        errorState.setError("Deletion failed");
        errorState.setShowWarningDialog(false);

        DeleteTaskState successState = new DeleteTaskState();
        successState.setLastDeletedTaskId("complex-123");
        successState.setSuccessMessage("Successfully deleted");
        successState.setError(null);

        int initialCount = propertyChangeCount.get();

        // Act - Simulate complete deletion workflow
        viewModel.updateState(pendingState); // Show warning dialog
        viewModel.updateState(errorState);   // Failed to delete
        viewModel.updateState(successState); // Successfully deleted

        // Assert
        assertEquals(initialCount + 6, propertyChangeCount.get());
        assertEquals("complex-123", viewModel.getState().getLastDeletedTaskId());
        assertEquals("Successfully deleted", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());
    }

    @Test
    void testStateIndependence() {
        // Arrange
        DeleteTaskState originalState = new DeleteTaskState();
        originalState.setLastDeletedTaskId("original-123");

        // Act
        viewModel.updateState(originalState);
        
        // Modify the original state after setting it
        originalState.setLastDeletedTaskId("modified-123");

        // Assert - The view model should reflect the change since it holds the same reference
        assertEquals("modified-123", viewModel.getState().getLastDeletedTaskId());
    }

    @Test
    void testViewModelInheritance() {
        // Assert that it properly extends ViewModel
        assertTrue(viewModel instanceof interface_adapter.ViewModel);
        assertEquals("delete task", viewModel.getViewName());
    }

    @Test
    void testUpdateStateWithComplexState() {
        // Arrange
        DeleteTaskState complexState = new DeleteTaskState();
        complexState.setLastDeletedTaskId("complex-delete-task");
        complexState.setSuccessMessage("Complex task deleted successfully");
        complexState.setError(null);
        complexState.setShowWarningDialog(false);
        complexState.setPendingDeleteTaskId(null);
        complexState.setPendingDeleteTaskName(null);

        int initialCount = propertyChangeCount.get();

        // Act
        viewModel.updateState(complexState);

        // Assert
        assertEquals(initialCount + 2, propertyChangeCount.get());
        assertEquals("complex-delete-task", viewModel.getState().getLastDeletedTaskId());
        assertEquals("Complex task deleted successfully", viewModel.getState().getSuccessMessage());
        assertNull(viewModel.getState().getError());
        assertFalse(viewModel.getState().isShowWarningDialog());
        assertNull(viewModel.getState().getPendingDeleteTaskId());
        assertNull(viewModel.getState().getPendingDeleteTaskName());
    }

    @Test
    void testUpdateStateSameReference() {
        // Arrange
        DeleteTaskState state = new DeleteTaskState();
        state.setLastDeletedTaskId("same-reference");
        viewModel.updateState(state);
        int countAfterFirst = propertyChangeCount.get();

        // Act - Update with same reference
        viewModel.updateState(state);

        // Assert
        assertEquals(countAfterFirst + 1, propertyChangeCount.get()); // Still fires property change
        assertSame(state, viewModel.getState());
    }
}