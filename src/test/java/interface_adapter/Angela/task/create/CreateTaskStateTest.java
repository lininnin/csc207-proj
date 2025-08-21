package interface_adapter.Angela.task.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreateTaskState.
 */
class CreateTaskStateTest {

    private CreateTaskState state;

    @BeforeEach
    void setUp() {
        state = new CreateTaskState();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(state);
        assertEquals("", state.getTaskName());
        assertEquals("", state.getDescription());
        assertEquals("", state.getCategoryId());
        assertFalse(state.isOneTime());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testSetAndGetTaskName() {
        // Test setting and getting a valid task name
        String taskName = "Complete Project";
        state.setTaskName(taskName);
        assertEquals(taskName, state.getTaskName());

        // Test setting null
        state.setTaskName(null);
        assertNull(state.getTaskName());

        // Test setting empty string
        state.setTaskName("");
        assertEquals("", state.getTaskName());
    }

    @Test
    void testSetAndGetDescription() {
        // Test setting and getting a valid description
        String description = "Finish the final project report";
        state.setDescription(description);
        assertEquals(description, state.getDescription());

        // Test setting null
        state.setDescription(null);
        assertNull(state.getDescription());

        // Test setting empty string
        state.setDescription("");
        assertEquals("", state.getDescription());
    }

    @Test
    void testSetAndGetCategoryId() {
        // Test setting and getting a valid category ID
        String categoryId = "work-123";
        state.setCategoryId(categoryId);
        assertEquals(categoryId, state.getCategoryId());

        // Test setting null
        state.setCategoryId(null);
        assertNull(state.getCategoryId());

        // Test setting empty string
        state.setCategoryId("");
        assertEquals("", state.getCategoryId());
    }

    @Test
    void testSetAndGetOneTime() {
        // Test setting to true
        state.setOneTime(true);
        assertTrue(state.isOneTime());

        // Test setting to false
        state.setOneTime(false);
        assertFalse(state.isOneTime());
    }

    @Test
    void testSetAndGetError() {
        // Test setting and getting a valid error message
        String error = "Task name is required";
        state.setError(error);
        assertEquals(error, state.getError());

        // Test setting null
        state.setError(null);
        assertNull(state.getError());

        // Test setting empty string
        state.setError("");
        assertEquals("", state.getError());
    }

    @Test
    void testSetAndGetSuccessMessage() {
        // Test setting and getting a valid success message
        String successMessage = "Task created successfully";
        state.setSuccessMessage(successMessage);
        assertEquals(successMessage, state.getSuccessMessage());

        // Test setting null
        state.setSuccessMessage(null);
        assertNull(state.getSuccessMessage());

        // Test setting empty string
        state.setSuccessMessage("");
        assertEquals("", state.getSuccessMessage());
    }

    @Test
    void testCompleteTaskCreationFlow() {
        // Test a complete task creation workflow
        
        // Set task details
        state.setTaskName("Read Book");
        state.setDescription("Read the assigned chapter");
        state.setCategoryId("education");
        state.setOneTime(true);

        // Verify all values are set correctly
        assertEquals("Read Book", state.getTaskName());
        assertEquals("Read the assigned chapter", state.getDescription());
        assertEquals("education", state.getCategoryId());
        assertTrue(state.isOneTime());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());

        // Simulate a validation error
        state.setError("Description too long");
        assertEquals("Description too long", state.getError());
        assertNull(state.getSuccessMessage());

        // Clear error and set success
        state.setError(null);
        state.setSuccessMessage("Task created successfully");
        assertNull(state.getError());
        assertEquals("Task created successfully", state.getSuccessMessage());
    }

    @Test
    void testErrorAndSuccessMessageTogether() {
        // Test that both error and success message can be set
        state.setError("Some error");
        state.setSuccessMessage("Some success");
        
        assertEquals("Some error", state.getError());
        assertEquals("Some success", state.getSuccessMessage());
        
        // Clear error while keeping success
        state.setError(null);
        assertNull(state.getError());
        assertEquals("Some success", state.getSuccessMessage());
        
        // Clear success while setting error
        state.setSuccessMessage(null);
        state.setError("New error");
        assertEquals("New error", state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testLongStrings() {
        // Test with long task name
        String longTaskName = "This is a very long task name ".repeat(10);
        state.setTaskName(longTaskName);
        assertEquals(longTaskName, state.getTaskName());

        // Test with long description
        String longDescription = "This is a very long description ".repeat(20);
        state.setDescription(longDescription);
        assertEquals(longDescription, state.getDescription());

        // Test with long category ID
        String longCategoryId = "very-long-category-id-".repeat(5);
        state.setCategoryId(longCategoryId);
        assertEquals(longCategoryId, state.getCategoryId());
    }

    @Test
    void testSpecialCharacters() {
        // Test with special characters in task name
        String specialTaskName = "Task @#$%^&*()_+{}|:<>?[]\\;'\",./<>?";
        state.setTaskName(specialTaskName);
        assertEquals(specialTaskName, state.getTaskName());

        // Test with special characters in description
        String specialDescription = "Description with 你好 emoji 🎉 αβγ";
        state.setDescription(specialDescription);
        assertEquals(specialDescription, state.getDescription());

        // Test with special characters in category ID
        String specialCategoryId = "cat-!@#$%^&*()";
        state.setCategoryId(specialCategoryId);
        assertEquals(specialCategoryId, state.getCategoryId());
    }

    @Test
    void testWhitespaceHandling() {
        // Test with whitespace-only strings
        state.setTaskName("   ");
        assertEquals("   ", state.getTaskName());

        state.setDescription("\t\n\r");
        assertEquals("\t\n\r", state.getDescription());

        state.setCategoryId(" \t ");
        assertEquals(" \t ", state.getCategoryId());
    }

    @Test
    void testMultipleModifications() {
        // Test setting values multiple times
        state.setTaskName("Task 1");
        state.setTaskName("Task 2");
        state.setTaskName("Task 3");
        assertEquals("Task 3", state.getTaskName());

        state.setDescription("Desc 1");
        state.setDescription("Desc 2");
        assertEquals("Desc 2", state.getDescription());

        state.setCategoryId("cat1");
        state.setCategoryId("cat2");
        assertEquals("cat2", state.getCategoryId());

        state.setOneTime(true);
        state.setOneTime(false);
        state.setOneTime(true);
        assertTrue(state.isOneTime());
    }

    @Test
    void testStateReset() {
        // Set all values
        state.setTaskName("Test Task");
        state.setDescription("Test Description");
        state.setCategoryId("test-category");
        state.setOneTime(true);
        state.setError("Test Error");
        state.setSuccessMessage("Test Success");

        // Reset to initial state
        state.setTaskName("");
        state.setDescription("");
        state.setCategoryId("");
        state.setOneTime(false);
        state.setError(null);
        state.setSuccessMessage(null);

        // Verify reset
        assertEquals("", state.getTaskName());
        assertEquals("", state.getDescription());
        assertEquals("", state.getCategoryId());
        assertFalse(state.isOneTime());
        assertNull(state.getError());
        assertNull(state.getSuccessMessage());
    }

    @Test
    void testBooleanToggling() {
        // Test multiple boolean toggles
        assertFalse(state.isOneTime()); // Initial state
        
        state.setOneTime(true);
        assertTrue(state.isOneTime());
        
        state.setOneTime(false);
        assertFalse(state.isOneTime());
        
        state.setOneTime(true);
        assertTrue(state.isOneTime());
        
        state.setOneTime(true); // Set to same value
        assertTrue(state.isOneTime());
    }

    @Test
    void testToString() {
        // Test that toString doesn't throw exceptions (defensive programming)
        assertDoesNotThrow(() -> state.toString());
        
        state.setTaskName("Test Task");
        state.setDescription("Test Description");
        state.setCategoryId("test-category");
        state.setOneTime(true);
        state.setError("Test Error");
        state.setSuccessMessage("Test Success");
        assertDoesNotThrow(() -> state.toString());
    }
}