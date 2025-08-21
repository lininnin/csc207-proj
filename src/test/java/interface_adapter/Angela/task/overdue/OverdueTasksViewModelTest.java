package interface_adapter.Angela.task.overdue;

import use_case.Angela.task.overdue.OverdueTasksOutputData.OverdueTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for OverdueTasksViewModel following Clean Architecture testing principles.
 * Tests view model behavior, property change notifications, and state management.
 */
class OverdueTasksViewModelTest {

    private OverdueTasksViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new OverdueTasksViewModel();
    }

    @Test
    @DisplayName("Should initialize with correct view name and empty state")
    void testInitialization() {
        // Then
        assertEquals("overdueTasks", viewModel.getViewName());
        assertNotNull(viewModel.getState());
        assertTrue(viewModel.getState().getOverdueTasks().isEmpty());
        assertEquals(0, viewModel.getState().getTotalOverdueTasks());
        assertNull(viewModel.getState().getError());
    }

    @Test
    @DisplayName("Should set and get state")
    void testSetState() {
        // Given
        OverdueTasksState newState = new OverdueTasksState();
        OverdueTaskData task = new OverdueTaskData(
            "task1", "Overdue Task", "Description", "cat1", "HIGH",
            LocalDate.now().minusDays(2), 2
        );
        newState.setOverdueTasks(Arrays.asList(task));
        newState.setTotalOverdueTasks(1);
        newState.setError("Test error");

        // When
        viewModel.setState(newState);

        // Then
        OverdueTasksState retrievedState = viewModel.getState();
        assertEquals(1, retrievedState.getOverdueTasks().size());
        assertEquals("Overdue Task", retrievedState.getOverdueTasks().get(0).getTaskName());
        assertEquals(1, retrievedState.getTotalOverdueTasks());
        assertEquals("Test error", retrievedState.getError());
    }

    @Test
    @DisplayName("Should fire property change when firePropertyChanged is called")
    void testFirePropertyChanged() {
        // Given
        AtomicBoolean propertyChangeFired = new AtomicBoolean(false);
        AtomicReference<OverdueTasksState> firedState = new AtomicReference<>();

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
                    propertyChangeFired.set(true);
                    firedState.set((OverdueTasksState) evt.getNewValue());
                }
            }
        };

        viewModel.addPropertyChangeListener(listener);

        // When
        viewModel.firePropertyChanged();

        // Then
        assertTrue(propertyChangeFired.get());
        assertEquals(viewModel.getState(), firedState.get());
    }

    @Test
    @DisplayName("Should add multiple property change listeners")
    void testMultiplePropertyChangeListeners() {
        // Given
        AtomicBoolean listener1Fired = new AtomicBoolean(false);
        AtomicBoolean listener2Fired = new AtomicBoolean(false);

        PropertyChangeListener listener1 = evt -> {
            if (OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
                listener1Fired.set(true);
            }
        };

        PropertyChangeListener listener2 = evt -> {
            if (OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
                listener2Fired.set(true);
            }
        };

        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);

        // When
        viewModel.firePropertyChanged();

        // Then
        assertTrue(listener1Fired.get());
        assertTrue(listener2Fired.get());
    }

    @Test
    @DisplayName("Should fire property change with correct property name")
    void testPropertyChangeEventDetails() {
        // Given
        AtomicReference<String> propertyName = new AtomicReference<>();
        AtomicReference<Object> oldValue = new AtomicReference<>();
        AtomicReference<Object> newValue = new AtomicReference<>();

        PropertyChangeListener listener = evt -> {
            propertyName.set(evt.getPropertyName());
            oldValue.set(evt.getOldValue());
            newValue.set(evt.getNewValue());
        };

        viewModel.addPropertyChangeListener(listener);

        // When
        viewModel.firePropertyChanged();

        // Then
        assertEquals(OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY, propertyName.get());
        assertNull(oldValue.get()); // Old value should be null as per implementation
        assertEquals(viewModel.getState(), newValue.get());
    }

    @Test
    @DisplayName("Should handle state changes and fire property change")
    void testStateChangeScenario() {
        // Given
        AtomicBoolean propertyChangeFired = new AtomicBoolean(false);
        AtomicReference<OverdueTasksState> capturedState = new AtomicReference<>();

        PropertyChangeListener listener = evt -> {
            if (OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
                propertyChangeFired.set(true);
                capturedState.set((OverdueTasksState) evt.getNewValue());
            }
        };

        viewModel.addPropertyChangeListener(listener);

        // Create new state with overdue tasks
        OverdueTasksState newState = new OverdueTasksState();
        OverdueTaskData task1 = new OverdueTaskData(
            "task1", "First Overdue Task", "Description 1", "work", "MEDIUM",
            LocalDate.now().minusDays(1), 1
        );
        OverdueTaskData task2 = new OverdueTaskData(
            "task2", "Second Overdue Task", "Description 2", "personal", "LOW",
            LocalDate.now().minusDays(3), 3
        );
        newState.setOverdueTasks(Arrays.asList(task1, task2));
        newState.setTotalOverdueTasks(2);

        // When
        viewModel.setState(newState);
        viewModel.firePropertyChanged();

        // Then
        assertTrue(propertyChangeFired.get());
        OverdueTasksState fired = capturedState.get();
        assertEquals(2, fired.getOverdueTasks().size());
        assertEquals(2, fired.getTotalOverdueTasks());
        assertEquals("First Overdue Task", fired.getOverdueTasks().get(0).getTaskName());
        assertEquals("Second Overdue Task", fired.getOverdueTasks().get(1).getTaskName());
    }

    @Test
    @DisplayName("Should handle error state changes")
    void testErrorStateHandling() {
        // Given
        AtomicReference<OverdueTasksState> capturedState = new AtomicReference<>();

        PropertyChangeListener listener = evt -> {
            if (OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
                capturedState.set((OverdueTasksState) evt.getNewValue());
            }
        };

        viewModel.addPropertyChangeListener(listener);

        // When
        OverdueTasksState errorState = new OverdueTasksState();
        errorState.setError("Failed to load overdue tasks from database");
        viewModel.setState(errorState);
        viewModel.firePropertyChanged();

        // Then
        OverdueTasksState fired = capturedState.get();
        assertEquals("Failed to load overdue tasks from database", fired.getError());
        assertTrue(fired.getOverdueTasks().isEmpty());
        assertEquals(0, fired.getTotalOverdueTasks());
    }

    @Test
    @DisplayName("Should maintain state reference consistency")
    void testStateReferenceConsistency() {
        // Given
        OverdueTasksState originalState = viewModel.getState();
        
        // When
        OverdueTasksState newState = new OverdueTasksState();
        newState.setTotalOverdueTasks(5);
        viewModel.setState(newState);

        // Then
        assertNotSame(originalState, viewModel.getState());
        assertEquals(newState, viewModel.getState());
        assertEquals(5, viewModel.getState().getTotalOverdueTasks());
    }

    @Test
    @DisplayName("Should handle property change without listeners gracefully")
    void testFirePropertyChangedWithoutListeners() {
        // When - no listeners added
        assertDoesNotThrow(() -> {
            viewModel.firePropertyChanged();
        });

        // Then - should complete without exception
        assertNotNull(viewModel.getState());
    }

    @Test
    @DisplayName("Should handle null state appropriately")
    void testNullState() {
        // When
        viewModel.setState(null);

        // Then
        assertNull(viewModel.getState());

        // Property change should still work
        assertDoesNotThrow(() -> {
            viewModel.firePropertyChanged();
        });
    }

    @Test
    @DisplayName("Should verify constant values")
    void testConstants() {
        // Then
        assertEquals("overdueTasks", OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY);
    }
}