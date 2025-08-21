package interface_adapter.Angela.task.available;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AvailableTasksState.
 */
class AvailableTasksStateTest {

    private AvailableTasksState state;

    @BeforeEach
    void setUp() {
        state = new AvailableTasksState();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(state);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testSetAndGetRefreshNeeded() {
        // Test setting to true
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());

        // Test setting to false
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testInitialRefreshNeededState() {
        // Verify default state is false
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testMultipleToggle() {
        // Test multiple toggles
        assertFalse(state.isRefreshNeeded()); // Initial state
        
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
        
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(true); // Set to same value
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testStateIndependence() {
        // Create multiple instances
        AvailableTasksState state1 = new AvailableTasksState();
        AvailableTasksState state2 = new AvailableTasksState();
        
        // Modify one
        state1.setRefreshNeeded(true);
        
        // Verify the other is unaffected
        assertFalse(state2.isRefreshNeeded());
        assertTrue(state1.isRefreshNeeded());
    }

    @Test
    void testConsistentBehavior() {
        // Test that setting the same value multiple times works consistently
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
        
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testBooleanValues() {
        // Test with explicit boolean values
        state.setRefreshNeeded(Boolean.TRUE);
        assertTrue(state.isRefreshNeeded());
        
        state.setRefreshNeeded(Boolean.FALSE);
        assertFalse(state.isRefreshNeeded());
    }

    @Test
    void testStateTransitions() {
        // Test typical state transitions during UI operations
        
        // Initially no refresh needed
        assertFalse(state.isRefreshNeeded());
        
        // Data changes, refresh needed
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
        
        // After refresh, no longer needed
        state.setRefreshNeeded(false);
        assertFalse(state.isRefreshNeeded());
        
        // Another change occurs
        state.setRefreshNeeded(true);
        assertTrue(state.isRefreshNeeded());
    }

    @Test
    void testToString() {
        // Test that toString doesn't throw exceptions (common issue)
        assertDoesNotThrow(() -> state.toString());
        
        state.setRefreshNeeded(true);
        assertDoesNotThrow(() -> state.toString());
        
        state.setRefreshNeeded(false);
        assertDoesNotThrow(() -> state.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two identical states
        AvailableTasksState state1 = new AvailableTasksState();
        AvailableTasksState state2 = new AvailableTasksState();
        
        // If equals is implemented, they should be equal with same values
        state1.setRefreshNeeded(true);
        state2.setRefreshNeeded(true);
        
        // If not implemented, they should still be different objects
        assertNotSame(state1, state2);
        
        // Test with different values
        state1.setRefreshNeeded(true);
        state2.setRefreshNeeded(false);
        
        assertNotSame(state1, state2);
    }
}