package interface_adapter.Angela.task.overdue;

import use_case.Angela.task.overdue.OverdueTasksOutputData.OverdueTaskData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for OverdueTasksState following Clean Architecture testing principles.
 */
class OverdueTasksStateTest {

    private OverdueTasksState state;

    @BeforeEach
    void setUp() {
        state = new OverdueTasksState();
    }

    @Test
    @DisplayName("Should initialize with empty overdue tasks list")
    void testInitialState() {
        // Then
        assertNotNull(state.getOverdueTasks());
        assertTrue(state.getOverdueTasks().isEmpty());
        assertEquals(0, state.getTotalOverdueTasks());
        assertNull(state.getError());
    }

    @Test
    @DisplayName("Should set and get overdue tasks")
    void testSetOverdueTasks() {
        // Given
        OverdueTaskData task1 = new OverdueTaskData(
            "task1", "Overdue Task 1", "Description 1", "cat1", "HIGH", 
            LocalDate.now().minusDays(2), 2
        );
        OverdueTaskData task2 = new OverdueTaskData(
            "task2", "Overdue Task 2", "Description 2", "cat2", "LOW", 
            LocalDate.now().minusDays(1), 1
        );
        List<OverdueTaskData> overdueTasks = Arrays.asList(task1, task2);

        // When
        state.setOverdueTasks(overdueTasks);

        // Then
        assertEquals(2, state.getOverdueTasks().size());
        assertEquals("Overdue Task 1", state.getOverdueTasks().get(0).getTaskName());
        assertEquals("Overdue Task 2", state.getOverdueTasks().get(1).getTaskName());
    }

    @Test
    @DisplayName("Should set and get total overdue tasks count")
    void testSetTotalOverdueTasks() {
        // When
        state.setTotalOverdueTasks(5);

        // Then
        assertEquals(5, state.getTotalOverdueTasks());
    }

    @Test
    @DisplayName("Should set and get error message")
    void testSetError() {
        // Given
        String errorMessage = "Failed to load overdue tasks";

        // When
        state.setError(errorMessage);

        // Then
        assertEquals(errorMessage, state.getError());
    }

    @Test
    @DisplayName("Should handle null overdue tasks list")
    void testSetNullOverdueTasks() {
        // When
        state.setOverdueTasks(null);

        // Then
        assertNull(state.getOverdueTasks());
    }

    @Test
    @DisplayName("Should handle zero total overdue tasks")
    void testZeroTotalOverdueTasks() {
        // Given
        state.setTotalOverdueTasks(10);

        // When
        state.setTotalOverdueTasks(0);

        // Then
        assertEquals(0, state.getTotalOverdueTasks());
    }

    @Test
    @DisplayName("Should handle negative total overdue tasks")
    void testNegativeTotalOverdueTasks() {
        // When
        state.setTotalOverdueTasks(-1);

        // Then
        assertEquals(-1, state.getTotalOverdueTasks());
    }

    @Test
    @DisplayName("Should clear error message")
    void testClearError() {
        // Given
        state.setError("Some error");

        // When
        state.setError(null);

        // Then
        assertNull(state.getError());
    }

    @Test
    @DisplayName("Should update state multiple times")
    void testMultipleUpdates() {
        // Given
        OverdueTaskData task1 = new OverdueTaskData(
            "task1", "Task 1", "Desc 1", "cat1", "MEDIUM", 
            LocalDate.now().minusDays(1), 1
        );
        OverdueTaskData task2 = new OverdueTaskData(
            "task2", "Task 2", "Desc 2", "cat2", "HIGH", 
            LocalDate.now().minusDays(3), 3
        );

        // When - First update
        state.setOverdueTasks(Arrays.asList(task1));
        state.setTotalOverdueTasks(1);
        state.setError(null);

        // Then - First state
        assertEquals(1, state.getOverdueTasks().size());
        assertEquals(1, state.getTotalOverdueTasks());
        assertNull(state.getError());

        // When - Second update
        state.setOverdueTasks(Arrays.asList(task1, task2));
        state.setTotalOverdueTasks(2);
        state.setError("Warning: Multiple overdue tasks");

        // Then - Second state
        assertEquals(2, state.getOverdueTasks().size());
        assertEquals(2, state.getTotalOverdueTasks());
        assertEquals("Warning: Multiple overdue tasks", state.getError());
    }

    @Test
    @DisplayName("Should preserve overdue task data integrity")
    void testTaskDataIntegrity() {
        // Given
        LocalDate overdueDate = LocalDate.now().minusDays(3);
        OverdueTaskData task = new OverdueTaskData(
            "task123", "Important Task", "Very important description", 
            "work", "HIGH", overdueDate, 3
        );

        // When
        state.setOverdueTasks(Arrays.asList(task));

        // Then
        OverdueTaskData retrievedTask = state.getOverdueTasks().get(0);
        assertEquals("task123", retrievedTask.getTaskId());
        assertEquals("Important Task", retrievedTask.getTaskName());
        assertEquals("Very important description", retrievedTask.getTaskDescription());
        assertEquals("work", retrievedTask.getCategoryName());
        assertEquals(overdueDate, retrievedTask.getDueDate());
        assertEquals(3, retrievedTask.getDaysOverdue());
    }
}