package interface_adapter.alex.event_related.available_event_module.edit_event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import use_case.alex.event_related.avaliable_events_module.edit_event.EditEventInputBoundary;
import use_case.alex.event_related.avaliable_events_module.edit_event.EditEventInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditEventControllerTest {

    @Mock
    private EditEventInputBoundary mockInteractor;

    private EditEventController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EditEventController(mockInteractor);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    @Test
    void testExecuteWithValidParameters() {
        // Arrange
        String id = "test-event-123";
        String name = "Updated Event Name";
        String category = "Work";
        String description = "Updated description for the event";

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals(id, capturedInputData.getId());
        assertEquals(name, capturedInputData.getName());
        assertEquals(category, capturedInputData.getCategory());
        assertEquals(description, capturedInputData.getDescription());
    }

    @Test
    void testExecuteWithNullParameters() {
        // Arrange
        String id = null;
        String name = null;
        String category = null;
        String description = null;

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertNull(capturedInputData.getId());
        assertNull(capturedInputData.getName());
        assertNull(capturedInputData.getCategory());
        assertNull(capturedInputData.getDescription());
    }

    @Test
    void testExecuteWithEmptyStrings() {
        // Arrange
        String id = "";
        String name = "";
        String category = "";
        String description = "";

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals("", capturedInputData.getId());
        assertEquals("", capturedInputData.getName());
        assertEquals("", capturedInputData.getCategory());
        assertEquals("", capturedInputData.getDescription());
    }

    @Test
    void testExecuteWithMixedNullAndValidParameters() {
        // Arrange
        String id = "valid-id";
        String name = null;
        String category = "ValidCategory";
        String description = "";

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals("valid-id", capturedInputData.getId());
        assertNull(capturedInputData.getName());
        assertEquals("ValidCategory", capturedInputData.getCategory());
        assertEquals("", capturedInputData.getDescription());
    }

    @Test
    void testExecuteWithLongStrings() {
        // Arrange
        String id = "very-long-id-" + "x".repeat(100);
        String name = "Very Long Event Name " + "N".repeat(200);
        String category = "Very Long Category " + "C".repeat(150);
        String description = "Very Long Description " + "D".repeat(500);

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals(id, capturedInputData.getId());
        assertEquals(name, capturedInputData.getName());
        assertEquals(category, capturedInputData.getCategory());
        assertEquals(description, capturedInputData.getDescription());
    }

    @Test
    void testExecuteWithSpecialCharacters() {
        // Arrange
        String id = "event@#$%^&*()_+";
        String name = "Event Name with Spécial Cháracters & Symbols!";
        String category = "Category-with_under.scores";
        String description = "Description with\nnewlines\tand\ttabs";

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals(id, capturedInputData.getId());
        assertEquals(name, capturedInputData.getName());
        assertEquals(category, capturedInputData.getCategory());
        assertEquals(description, capturedInputData.getDescription());
    }

    @Test
    void testExecuteCallsInteractorExactlyOnce() {
        // Arrange
        String id = "single-call-test";
        String name = "Single Call Event";
        String category = "Test";
        String description = "Test description";

        // Act
        controller.execute(id, name, category, description);

        // Assert
        verify(mockInteractor, times(1)).execute(any(EditEventInputData.class));
        verifyNoMoreInteractions(mockInteractor);
    }

    @Test
    void testMultipleExecuteCallsCreateSeparateInputData() {
        // Arrange
        String id1 = "event-001";
        String name1 = "First Event";
        String category1 = "Category1";
        String description1 = "First description";

        String id2 = "event-002";
        String name2 = "Second Event";
        String category2 = "Category2";
        String description2 = "Second description";

        // Act
        controller.execute(id1, name1, category1, description1);
        controller.execute(id2, name2, category2, description2);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(2)).execute(captor.capture());

        var capturedInputs = captor.getAllValues();
        assertEquals(2, capturedInputs.size());

        // Verify first call
        assertEquals(id1, capturedInputs.get(0).getId());
        assertEquals(name1, capturedInputs.get(0).getName());
        assertEquals(category1, capturedInputs.get(0).getCategory());
        assertEquals(description1, capturedInputs.get(0).getDescription());

        // Verify second call
        assertEquals(id2, capturedInputs.get(1).getId());
        assertEquals(name2, capturedInputs.get(1).getName());
        assertEquals(category2, capturedInputs.get(1).getCategory());
        assertEquals(description2, capturedInputs.get(1).getDescription());
    }

    @Test
    void testExecuteInteractorExceptionPropagates() {
        // Arrange
        String id = "exception-event";
        String name = "Exception Event";
        String category = "Test";
        String description = "Test description";
        RuntimeException expectedException = new RuntimeException("Interactor failed");
        doThrow(expectedException).when(mockInteractor).execute(any(EditEventInputData.class));

        // Act & Assert
        RuntimeException actualException = assertThrows(RuntimeException.class, () -> {
            controller.execute(id, name, category, description);
        });

        assertEquals("Interactor failed", actualException.getMessage());
        assertEquals(expectedException, actualException);
    }

    @Test
    void testExecuteWithWhitespaceStrings() {
        // Arrange
        String id = "   whitespace-id   ";
        String name = "\t\tTabbed Event Name\t\t";
        String category = "  Spaced Category  ";
        String description = "\n\nNewlined Description\n\n";

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals(id, capturedInputData.getId());
        assertEquals(name, capturedInputData.getName());
        assertEquals(category, capturedInputData.getCategory());
        assertEquals(description, capturedInputData.getDescription());
    }

    @Test
    void testExecuteWithUnicodeCharacters() {
        // Arrange
        String id = "事件-123";
        String name = "이벤트 名前 Événement";
        String category = "카테고리 類別";
        String description = "説明 설명 Description avec accénts ñ";

        // Act
        controller.execute(id, name, category, description);

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals(id, capturedInputData.getId());
        assertEquals(name, capturedInputData.getName());
        assertEquals(category, capturedInputData.getCategory());
        assertEquals(description, capturedInputData.getDescription());
    }

    @Test
    void testExecuteConsistencyAcrossMultipleCalls() {
        // Arrange
        String id = "consistency-test";
        String name = "Consistent Event";
        String category = "Consistency";
        String description = "Testing consistency";

        // Act - Call multiple times with same parameters
        for (int i = 0; i < 5; i++) {
            controller.execute(id, name, category, description);
        }

        // Assert
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(5)).execute(captor.capture());

        var capturedInputs = captor.getAllValues();
        assertEquals(5, capturedInputs.size());

        // Verify all calls have consistent data
        for (EditEventInputData inputData : capturedInputs) {
            assertEquals(id, inputData.getId());
            assertEquals(name, inputData.getName());
            assertEquals(category, inputData.getCategory());
            assertEquals(description, inputData.getDescription());
        }
    }

    @Test
    void testExecuteParameterIsolation() {
        // Arrange
        String originalId = "original-id";
        String originalName = "Original Name";
        String originalCategory = "Original Category";
        String originalDescription = "Original Description";

        // Act
        controller.execute(originalId, originalName, originalCategory, originalDescription);

        // Modify the original strings (shouldn't affect captured data)
        originalId = "modified-id";
        originalName = "Modified Name";

        // Assert - Captured data should still have original values
        ArgumentCaptor<EditEventInputData> captor = ArgumentCaptor.forClass(EditEventInputData.class);
        verify(mockInteractor, times(1)).execute(captor.capture());

        EditEventInputData capturedInputData = captor.getValue();
        assertEquals("original-id", capturedInputData.getId()); // Should still be original
        assertEquals("Original Name", capturedInputData.getName()); // Should still be original
        assertEquals("Original Category", capturedInputData.getCategory());
        assertEquals("Original Description", capturedInputData.getDescription());
    }
}