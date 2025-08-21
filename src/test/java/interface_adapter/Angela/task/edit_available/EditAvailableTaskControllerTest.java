package interface_adapter.Angela.task.edit_available;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.Angela.task.edit_available.EditAvailableTaskInputBoundary;
import use_case.Angela.task.edit_available.EditAvailableTaskInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for EditAvailableTaskController.
 */
class EditAvailableTaskControllerTest {

    @Mock
    private EditAvailableTaskInputBoundary mockInteractor;
    
    private EditAvailableTaskController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EditAvailableTaskController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithValidData() {
        // Arrange
        String taskId = "task-123";
        String name = "Updated Task";
        String description = "Updated description";
        String categoryId = "cat-456";
        boolean isOneTime = true;

        // Act
        controller.execute(taskId, name, description, categoryId, isOneTime);

        // Assert
        ArgumentCaptor<EditAvailableTaskInputData> captor = ArgumentCaptor.forClass(EditAvailableTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditAvailableTaskInputData capturedData = captor.getValue();
        assertEquals(taskId, capturedData.getTaskId());
        assertEquals(name, capturedData.getName());
        assertEquals(description, capturedData.getDescription());
        assertEquals(categoryId, capturedData.getCategoryId());
        assertEquals(isOneTime, capturedData.isOneTime());
    }

    @Test
    void testExecuteWithNullValues() {
        // Act
        controller.execute(null, null, null, null, false);

        // Assert
        ArgumentCaptor<EditAvailableTaskInputData> captor = ArgumentCaptor.forClass(EditAvailableTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditAvailableTaskInputData capturedData = captor.getValue();
        assertNull(capturedData.getTaskId());
        assertNull(capturedData.getName());
        assertNull(capturedData.getDescription());
        assertNull(capturedData.getCategoryId());
        assertFalse(capturedData.isOneTime());
    }

    @Test
    void testExecuteWithEmptyStrings() {
        // Act
        controller.execute("", "", "", "", true);

        // Assert
        ArgumentCaptor<EditAvailableTaskInputData> captor = ArgumentCaptor.forClass(EditAvailableTaskInputData.class);
        verify(mockInteractor).execute(captor.capture());
        
        EditAvailableTaskInputData capturedData = captor.getValue();
        assertEquals("", capturedData.getTaskId());
        assertEquals("", capturedData.getName());
        assertEquals("", capturedData.getDescription());
        assertEquals("", capturedData.getCategoryId());
        assertTrue(capturedData.isOneTime());
    }

    @Test
    void testExecuteCallsInteractorOnce() {
        // Act
        controller.execute("task-1", "Test Task", "Test Description", "cat-1", false);

        // Assert
        verify(mockInteractor, times(1)).execute(any(EditAvailableTaskInputData.class));
    }

    @Test
    void testExecuteWithDifferentParameterCombinations() {
        // Test case 1: Regular task
        controller.execute("task-1", "Regular Task", "Regular description", "cat-1", false);
        
        // Test case 2: One-time task
        controller.execute("task-2", "One-time Task", "One-time description", "cat-2", true);
        
        // Test case 3: Task with no category
        controller.execute("task-3", "No Category Task", "No category description", null, false);

        // Assert all calls were made
        verify(mockInteractor, times(3)).execute(any(EditAvailableTaskInputData.class));
    }

    @Test
    void testConstructorWithNullInteractor() {
        EditAvailableTaskController controller = new EditAvailableTaskController(null);
        assertNotNull(controller);
    }
}