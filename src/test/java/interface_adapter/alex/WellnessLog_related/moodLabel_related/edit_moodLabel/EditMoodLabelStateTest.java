package interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EditMoodLabelStateTest {

    private EditMoodLabelState state;

    @BeforeEach
    void setUp() {
        state = new EditMoodLabelState();
    }

    @Test
    void testConstructorInitializesWithDefaults() {
        assertEquals("", state.getLabelId());
        assertEquals("", state.getName());
        assertEquals("", state.getType());
        assertNull(state.getNameError());
        assertNull(state.getTypeError());
        assertNull(state.getEditError());
    }

    @Test
    void testLabelIdSetterAndGetter() {
        String labelId = "test-label-id-123";
        state.setLabelId(labelId);
        assertEquals(labelId, state.getLabelId());
    }

    @Test
    void testNameSetterAndGetter() {
        String name = "Happy";
        state.setName(name);
        assertEquals(name, state.getName());
    }

    @Test
    void testNameErrorSetterAndGetter() {
        String nameError = "Name cannot be empty";
        state.setNameError(nameError);
        assertEquals(nameError, state.getNameError());
    }

    @Test
    void testTypeSetterAndGetter() {
        String type = "Positive";
        state.setType(type);
        assertEquals(type, state.getType());
    }

    @Test
    void testTypeErrorSetterAndGetter() {
        String typeError = "Type must be Positive or Negative";
        state.setTypeError(typeError);
        assertEquals(typeError, state.getTypeError());
    }

    @Test
    void testEditErrorSetterAndGetter() {
        String editError = "Edit failed: label not found";
        state.setEditError(editError);
        assertEquals(editError, state.getEditError());
    }

    @Test
    void testSetLabelIdWithNull() {
        state.setLabelId(null);
        assertNull(state.getLabelId());
    }

    @Test
    void testSetNameWithNull() {
        state.setName(null);
        assertNull(state.getName());
    }

    @Test
    void testSetTypeWithNull() {
        state.setType(null);
        assertNull(state.getType());
    }

    @Test
    void testSetNameErrorWithNull() {
        state.setNameError(null);
        assertNull(state.getNameError());
    }

    @Test
    void testSetTypeErrorWithNull() {
        state.setTypeError(null);
        assertNull(state.getTypeError());
    }

    @Test
    void testSetEditErrorWithNull() {
        state.setEditError(null);
        assertNull(state.getEditError());
    }

    @Test
    void testSetEmptyStrings() {
        state.setLabelId("");
        state.setName("");
        state.setType("");
        state.setNameError("");
        state.setTypeError("");
        state.setEditError("");

        assertEquals("", state.getLabelId());
        assertEquals("", state.getName());
        assertEquals("", state.getType());
        assertEquals("", state.getNameError());
        assertEquals("", state.getTypeError());
        assertEquals("", state.getEditError());
    }

    @Test
    void testAllFieldsIndependence() {
        // Set all fields to different values
        state.setLabelId("id123");
        state.setName("Excited");
        state.setType("Positive");
        state.setNameError("Name error");
        state.setTypeError("Type error");
        state.setEditError("Edit error");

        // Verify they are all independent and correct
        assertEquals("id123", state.getLabelId());
        assertEquals("Excited", state.getName());
        assertEquals("Positive", state.getType());
        assertEquals("Name error", state.getNameError());
        assertEquals("Type error", state.getTypeError());
        assertEquals("Edit error", state.getEditError());

        // Change one and verify others remain unchanged
        state.setName("Sad");
        assertEquals("id123", state.getLabelId());
        assertEquals("Sad", state.getName());
        assertEquals("Positive", state.getType());
        assertEquals("Name error", state.getNameError());
        assertEquals("Type error", state.getTypeError());
        assertEquals("Edit error", state.getEditError());
    }
}