package interface_adapter.alex.event_related.create_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreatedEventStateTest {

    private CreatedEventState state;

    @BeforeEach
    void setUp() {
        state = new CreatedEventState();
    }

    @Test
    void testDefaultValues() {
        // Assert default values
        assertEquals("", state.getName());
        assertEquals("", state.getNameError());
        assertEquals("", state.getDescription());
        assertEquals("", state.getDescriptionError());
        assertEquals("", state.getCategory());
        assertEquals("", state.getCategoryError());
    }

    // ==================== Name Tests ====================

    @Test
    void testSetAndGetName() {
        // Act
        state.setName("Team Meeting");

        // Assert
        assertEquals("Team Meeting", state.getName());
    }

    @Test
    void testSetNameWithNull() {
        // Act
        state.setName(null);

        // Assert
        assertEquals("", state.getName());
    }

    @Test
    void testSetNameWithWhitespace() {
        // Act
        state.setName("  Team Meeting  ");

        // Assert
        assertEquals("Team Meeting", state.getName());
    }

    @Test
    void testSetNameWithEmptyString() {
        // Act
        state.setName("");

        // Assert
        assertEquals("", state.getName());
    }

    @Test
    void testSetNameWithTabsAndNewlines() {
        // Act
        state.setName("\t\nTeam Meeting\n\t");

        // Assert
        assertEquals("Team Meeting", state.getName());
    }

    // ==================== Name Error Tests ====================

    @Test
    void testSetAndGetNameError() {
        // Act
        state.setNameError("Name is required");

        // Assert
        assertEquals("Name is required", state.getNameError());
    }

    @Test
    void testSetNameErrorWithNull() {
        // Act
        state.setNameError(null);

        // Assert
        assertEquals("", state.getNameError());
    }

    @Test
    void testSetNameErrorWithWhitespace() {
        // Act
        state.setNameError("  Name is required  ");

        // Assert
        assertEquals("Name is required", state.getNameError());
    }

    // ==================== Description Tests ====================

    @Test
    void testSetAndGetDescription() {
        // Act
        state.setDescription("Weekly standup meeting");

        // Assert
        assertEquals("Weekly standup meeting", state.getDescription());
    }

    @Test
    void testSetDescriptionWithNull() {
        // Act
        state.setDescription(null);

        // Assert
        assertEquals("", state.getDescription());
    }

    @Test
    void testSetDescriptionWithWhitespace() {
        // Act
        state.setDescription("  Weekly standup meeting  ");

        // Assert
        assertEquals("Weekly standup meeting", state.getDescription());
    }

    @Test
    void testSetDescriptionWithEmptyString() {
        // Act
        state.setDescription("");

        // Assert
        assertEquals("", state.getDescription());
    }

    @Test
    void testSetDescriptionWithLongText() {
        // Arrange
        String longDescription = "A".repeat(500);

        // Act
        state.setDescription(longDescription);

        // Assert
        assertEquals(longDescription, state.getDescription());
    }

    // ==================== Description Error Tests ====================

    @Test
    void testSetAndGetDescriptionError() {
        // Act
        state.setDescriptionError("Description too long");

        // Assert
        assertEquals("Description too long", state.getDescriptionError());
    }

    @Test
    void testSetDescriptionErrorWithNull() {
        // Act
        state.setDescriptionError(null);

        // Assert
        assertEquals("", state.getDescriptionError());
    }

    @Test
    void testSetDescriptionErrorWithWhitespace() {
        // Act
        state.setDescriptionError("  Description too long  ");

        // Assert
        assertEquals("Description too long", state.getDescriptionError());
    }

    // ==================== Category Tests ====================

    @Test
    void testSetAndGetCategory() {
        // Act
        state.setCategory("Work");

        // Assert
        assertEquals("Work", state.getCategory());
    }

    @Test
    void testSetCategoryWithNull() {
        // Act
        state.setCategory(null);

        // Assert
        assertEquals("", state.getCategory());
    }

    @Test
    void testSetCategoryWithWhitespace() {
        // Act
        state.setCategory("  Work  ");

        // Assert
        assertEquals("Work", state.getCategory());
    }

    @Test
    void testSetCategoryWithEmptyString() {
        // Act
        state.setCategory("");

        // Assert
        assertEquals("", state.getCategory());
    }

    @Test
    void testSetCategoryWithSpecialCharacters() {
        // Act
        state.setCategory("Work/Business");

        // Assert
        assertEquals("Work/Business", state.getCategory());
    }

    // ==================== Category Error Tests ====================

    @Test
    void testSetAndGetCategoryError() {
        // Act
        state.setCategoryError("Invalid category");

        // Assert
        assertEquals("Invalid category", state.getCategoryError());
    }

    @Test
    void testSetCategoryErrorWithNull() {
        // Act
        state.setCategoryError(null);

        // Assert
        assertEquals("", state.getCategoryError());
    }

    @Test
    void testSetCategoryErrorWithWhitespace() {
        // Act
        state.setCategoryError("  Invalid category  ");

        // Assert
        assertEquals("Invalid category", state.getCategoryError());
    }

    // ==================== ToString Test ====================

    @Test
    void testToString() {
        // Arrange
        state.setName("Test Event");
        state.setDescription("Test Description");
        state.setCategory("Test Category");

        // Act
        String result = state.toString();

        // Assert
        assertTrue(result.contains("Test Event"));
        assertTrue(result.contains("Test Description"));
        assertTrue(result.contains("Test Category"));
        assertTrue(result.contains("CreatedEventState"));
    }

    @Test
    void testToStringWithEmptyValues() {
        // Act
        String result = state.toString();

        // Assert
        assertTrue(result.contains("CreatedEventState"));
        assertTrue(result.contains("name=''"));
        assertTrue(result.contains("description=''"));
        assertTrue(result.contains("category=''"));
    }

    @Test
    void testToStringAfterUpdate() {
        // Arrange
        state.setName("Initial Name");
        String initialString = state.toString();

        // Act
        state.setName("Updated Name");
        String updatedString = state.toString();

        // Assert
        assertNotEquals(initialString, updatedString);
        assertTrue(updatedString.contains("Updated Name"));
        assertFalse(updatedString.contains("Initial Name"));
    }

    // ==================== Complex State Tests ====================

    @Test
    void testCompleteState() {
        // Act
        state.setName("Complete Event");
        state.setNameError("Name warning");
        state.setDescription("Complete description");
        state.setDescriptionError("Description warning");
        state.setCategory("Complete category");
        state.setCategoryError("Category warning");

        // Assert
        assertEquals("Complete Event", state.getName());
        assertEquals("Name warning", state.getNameError());
        assertEquals("Complete description", state.getDescription());
        assertEquals("Description warning", state.getDescriptionError());
        assertEquals("Complete category", state.getCategory());
        assertEquals("Category warning", state.getCategoryError());
    }

    @Test
    void testStateTransitions() {
        // Initial state
        assertEquals("", state.getName());

        // Set values
        state.setName("Event 1");
        state.setDescription("Description 1");
        assertEquals("Event 1", state.getName());
        assertEquals("Description 1", state.getDescription());

        // Update values
        state.setName("Event 2");
        state.setDescription("Description 2");
        assertEquals("Event 2", state.getName());
        assertEquals("Description 2", state.getDescription());

        // Clear values
        state.setName("");
        state.setDescription("");
        assertEquals("", state.getName());
        assertEquals("", state.getDescription());
    }

    @Test
    void testErrorStateHandling() {
        // Set errors
        state.setNameError("Name required");
        state.setDescriptionError("Description too short");
        state.setCategoryError("Category invalid");

        // Assert errors are set
        assertEquals("Name required", state.getNameError());
        assertEquals("Description too short", state.getDescriptionError());
        assertEquals("Category invalid", state.getCategoryError());

        // Clear errors
        state.setNameError("");
        state.setDescriptionError("");
        state.setCategoryError("");

        // Assert errors are cleared
        assertEquals("", state.getNameError());
        assertEquals("", state.getDescriptionError());
        assertEquals("", state.getCategoryError());
    }

    @Test
    void testWhitespaceHandling() {
        // Test various whitespace scenarios
        state.setName("   ");
        state.setDescription("\t\t");
        state.setCategory("\n\n");

        assertEquals("", state.getName());
        assertEquals("", state.getDescription());
        assertEquals("", state.getCategory());
    }

    @Test
    void testMixedWhitespace() {
        // Test mixed whitespace scenarios
        state.setName(" \t Event Name \n ");
        state.setDescription("\n\t Description Text \t\n");
        state.setCategory(" \t Category Name \n ");

        assertEquals("Event Name", state.getName());
        assertEquals("Description Text", state.getDescription());
        assertEquals("Category Name", state.getCategory());
    }

    @Test
    void testUnicodeAndSpecialCharacters() {
        // Test unicode and special characters
        state.setName("Event 🎉");
        state.setDescription("Description with émojis 😊");
        state.setCategory("Catégory ñame");

        assertEquals("Event 🎉", state.getName());
        assertEquals("Description with émojis 😊", state.getDescription());
        assertEquals("Catégory ñame", state.getCategory());
    }
}