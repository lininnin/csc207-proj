package interface_adapter.Angela.task.edit_today;

import entity.Angela.Task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.Angela.task.edit_today.EditTodayTaskInputBoundary;
import use_case.Angela.task.edit_today.EditTodayTaskInputData;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for EditTodayTaskController.
 */
class EditTodayTaskControllerTest {

    @Mock
    private EditTodayTaskInputBoundary mockInteractor;
    
    private EditTodayTaskController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EditTodayTaskController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithAllParameters() {
        // Arrange
        String taskId = "task-123";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate dueDate = LocalDate.of(2023, 12, 25);

        // Act
        controller.execute(taskId, priority, dueDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
        assertEquals(priority, capturedData.getPriority());
        assertEquals(dueDate, capturedData.getDueDate());
    }

    @Test
    void testExecuteWithNullPriority() {
        // Arrange
        String taskId = "task-456";
        Task.Priority priority = null;
        LocalDate dueDate = LocalDate.of(2023, 12, 31);

        // Act
        controller.execute(taskId, priority, dueDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
        assertNull(capturedData.getPriority());
        assertEquals(dueDate, capturedData.getDueDate());
    }

    @Test
    void testExecuteWithNullDueDate() {
        // Arrange
        String taskId = "task-789";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = null;

        // Act
        controller.execute(taskId, priority, dueDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
        assertEquals(priority, capturedData.getPriority());
        assertNull(capturedData.getDueDate());
    }

    @Test
    void testExecuteWithAllNullOptionalParameters() {
        // Arrange
        String taskId = "task-000";
        Task.Priority priority = null;
        LocalDate dueDate = null;

        // Act
        controller.execute(taskId, priority, dueDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
        assertNull(capturedData.getPriority());
        assertNull(capturedData.getDueDate());
    }

    @Test
    void testExecuteWithNullTaskId() {
        // Arrange
        String taskId = null;
        Task.Priority priority = Task.Priority.LOW;
        LocalDate dueDate = LocalDate.now();

        // Act
        controller.execute(taskId, priority, dueDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertNull(capturedData.getTaskId());
        assertEquals(priority, capturedData.getPriority());
        assertEquals(dueDate, capturedData.getDueDate());
    }

    @Test
    void testExecuteWithDifferentPriorities() {
        // Test with LOW priority
        controller.execute("task-1", Task.Priority.LOW, LocalDate.now());
        
        // Test with MEDIUM priority
        controller.execute("task-2", Task.Priority.MEDIUM, LocalDate.now());
        
        // Test with HIGH priority
        controller.execute("task-3", Task.Priority.HIGH, LocalDate.now());

        // Assert all calls were made
        verify(mockInteractor, times(3)).execute(any(EditTodayTaskInputData.class));
    }

    @Test
    void testExecuteWithDifferentDates() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusWeeks(1);

        // Test with different dates
        controller.execute("task-1", Task.Priority.HIGH, today);
        controller.execute("task-2", Task.Priority.MEDIUM, tomorrow);
        controller.execute("task-3", Task.Priority.LOW, nextWeek);

        // Assert all calls were made
        verify(mockInteractor, times(3)).execute(any(EditTodayTaskInputData.class));
    }

    @Test
    void testExecuteCallsInteractorOnce() {
        // Act
        controller.execute("task-test", Task.Priority.HIGH, LocalDate.now());

        // Assert
        verify(mockInteractor, times(1)).execute(any(EditTodayTaskInputData.class));
    }

    @Test
    void testExecuteWithEmptyTaskId() {
        // Arrange
        String taskId = "";
        Task.Priority priority = Task.Priority.MEDIUM;
        LocalDate dueDate = LocalDate.now();

        // Act
        controller.execute(taskId, priority, dueDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertEquals("", capturedData.getTaskId());
        assertEquals(priority, capturedData.getPriority());
        assertEquals(dueDate, capturedData.getDueDate());
    }

    @Test
    void testConstructorWithNullInteractor() {
        EditTodayTaskController controller = new EditTodayTaskController(null);
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithPastDate() {
        // Arrange
        String taskId = "task-past";
        Task.Priority priority = Task.Priority.HIGH;
        LocalDate pastDate = LocalDate.of(2020, 1, 1);

        // Act
        controller.execute(taskId, priority, pastDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
        assertEquals(priority, capturedData.getPriority());
        assertEquals(pastDate, capturedData.getDueDate());
    }

    @Test
    void testExecuteWithFutureDate() {
        // Arrange
        String taskId = "task-future";
        Task.Priority priority = Task.Priority.LOW;
        LocalDate futureDate = LocalDate.of(2030, 12, 31);

        // Act
        controller.execute(taskId, priority, futureDate);

        // Assert
        ArgumentCaptor<EditTodayTaskInputData> captor = ArgumentCaptor.forClass(EditTodayTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditTodayTaskInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
        assertEquals(priority, capturedData.getPriority());
        assertEquals(futureDate, capturedData.getDueDate());
    }
}