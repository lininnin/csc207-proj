package interface_adapter.Angela.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CategoryManagementState.
 */
class CategoryManagementStateTest {

    private CategoryManagementState state;

    @BeforeEach
    void setUp() {
        state = new CategoryManagementState();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(state);
        assertNull(state.getLastAction());
        assertNull(state.getMessage());
        assertNull(state.getError());
        assertFalse(state.isRefreshNeeded());
        assertFalse(state.isDialogOpen());
    }

    @Test
    void testSetAndGetLastAction() {
        assertNull(state.getLastAction());
        
        state.setLastAction("create");
        assertEquals("create", state.getLastAction());
        
        state.setLastAction("delete");
        assertEquals("delete", state.getLastAction());
        
        state.setLastAction("edit");
        assertEquals("edit", state.getLastAction());
    }

    @Test
    void testSetAndGetMessage() {
        assertNull(state.getMessage());
        
        state.setMessage("Category created successfully");
        assertEquals("Category created successfully", state.getMessage());
        
        state.setMessage("Category updated");
        assertEquals("Category updated", state.getMessage());
        
        state.setMessage("");
        assertEquals("", state.getMessage());
    }

    @Test
    void testSetAndGetError() {
        assertNull(state.getError());
        
        state.setError("Category name cannot be empty");
        assertEquals("Category name cannot be empty", state.getError());
        
        state.setError("Category already exists");
        assertEquals("Category already exists", state.getError());
        
        state.setError("");
        assertEquals("", state.getError());
    }

    @Test
    void testSetAndGetRefreshNeeded() {
        assertFalse(state.isRefreshNeeded());
        
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testSetAndGetDialogOpen() {
        assertFalse(state.isDialogOpen());
        
        state.setDialogOpen(true);
        assertTrue(state.isDialogOpen());
        
        state.setDialogOpen(false);
        assertFalse(state.isDialogOpen());
    }

    @Test
    void testSetNullValues() {
        // Set some initial values
        state.setLastAction("create");
        state.setMessage("Test message");
        state.setError("Test error");
        
        // Set to null
        state.setLastAction(null);
        state.setMessage(null);
        state.setError(null);
        
        // Verify null values are accepted
        assertNull(state.getLastAction());
        assertNull(state.getMessage());
        assertNull(state.getError());
    }

    @Test
    void testStringFieldsAcceptEmptyStrings() {
        state.setLastAction("");
        state.setMessage("");
        state.setError("");
        
        assertEquals("", state.getLastAction());
        assertEquals("", state.getMessage());
        assertEquals("", state.getError());
    }

    @Test
    void testMultipleStateUpdates() {
        // Simulate a complete category operation flow
        state.setLastAction("create");
        state.setDialogOpen(true);
        
        assertEquals("create", state.getLastAction());
        assertTrue(state.isDialogOpen());
        assertFalse(state.isRefreshNeeded());
        
        // Success scenario
        state.setMessage("Category 'Work' created successfully");
        state.setRefreshNeeded(true);
        state.setDialogOpen(false);
        
        assertEquals("Category 'Work' created successfully", state.getMessage());
        assertTrue(state.isRefreshNeeded());
        assertFalse(state.isDialogOpen());
        assertNull(state.getError());
    }

    @Test
    void testErrorScenario() {
        // Simulate error scenario
        state.setLastAction("create");
        state.setDialogOpen(true);
        state.setError("Category name already exists");
        
        assertEquals("create", state.getLastAction());
        assertTrue(state.isDialogOpen());
        assertEquals("Category name already exists", state.getError());
        assertNull(state.getMessage());
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testComplexStateTransitions() {
        // Test state transitions for edit operation
        state.setLastAction("edit");
        state.setDialogOpen(true);
        assertNull(state.getError());
        assertNull(state.getMessage());
        
        // Validation error
        state.setError("Category name cannot be empty");
        assertEquals("Category name cannot be empty", state.getError());
        assertTrue(state.isDialogOpen());
        
        // Clear error and succeed
        state.setError(null);
        state.setMessage("Category updated successfully");
        state.setRefreshNeeded(true);
        state.setDialogOpen(false);
        
        assertNull(state.getError());
        assertEquals("Category updated successfully", state.getMessage());
        assertTrue(state.isRefreshNeeded());
        assertFalse(state.isDialogOpen());
    }

    @Test
    void testDeleteOperationFlow() {
        // Test delete operation state transitions
        state.setLastAction("delete");
        state.setDialogOpen(true);
        
        // Successful deletion
        state.setMessage("Category 'Personal' deleted successfully");
        state.setRefreshNeeded(true);
        state.setDialogOpen(false);
        
        assertEquals("delete", state.getLastAction());
        assertEquals("Category 'Personal' deleted successfully", state.getMessage());
        assertTrue(state.isRefreshNeeded());
        assertFalse(state.isDialogOpen());
        assertNull(state.getError());
    }

    @Test
    void testSpecialCharactersInStrings() {
        // Test with special characters
        state.setLastAction("create_with_special_chars");
        state.setMessage("Category 'Work & Personal' created! 🎉");
        state.setError("Error: Invalid characters (!@#$%)");
        
        assertEquals("create_with_special_chars", state.getLastAction());
        assertEquals("Category 'Work & Personal' created! 🎉", state.getMessage());
        assertEquals("Error: Invalid characters (!@#$%)", state.getError());
    }

    @Test
    void testVeryLongStrings() {
        String longAction = "very_long_action_name_that_exceeds_normal_length_limits";
        String longMessage = "This is a very long success message that might be displayed to the user when a category operation completes successfully";
        String longError = "This is a very long error message that describes in detail what went wrong during the category operation";
        
        state.setLastAction(longAction);
        state.setMessage(longMessage);
        state.setError(longError);
        
        assertEquals(longAction, state.getLastAction());
        assertEquals(longMessage, state.getMessage());
        assertEquals(longError, state.getError());
    }

    @Test
    void testIndependentStateFields() {
        // Test that all fields can be set independently
        state.setLastAction("test");
        assertEquals("test", state.getLastAction());
        assertNull(state.getMessage());
        assertNull(state.getError());
        assertFalse(state.isRefreshNeeded());
        assertFalse(state.isDialogOpen());
        
        state.setMessage("message");
        assertEquals("message", state.getMessage());
        assertEquals("test", state.getLastAction());
        
        state.setError("error");
        assertEquals("error", state.getError());
        assertEquals("message", state.getMessage());
        assertEquals("test", state.getLastAction());
        
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        state.setDialogOpen(true);
        assertTrue(state.isDialogOpen());
    }
}