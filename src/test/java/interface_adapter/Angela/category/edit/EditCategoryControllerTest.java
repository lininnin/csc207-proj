package interface_adapter.Angela.category.edit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.category.edit.EditCategoryInputBoundary;

import static org.mockito.Mockito.*;

class EditCategoryControllerTest {

    private EditCategoryInputBoundary mockInteractor;
    private EditCategoryController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(EditCategoryInputBoundary.class);
        controller = new EditCategoryController(mockInteractor);
    }

    @Test
    void testExecute_validParameters_callsInteractor() {
        String categoryId = "cat123";
        String newName = "Updated Work";

        controller.execute(categoryId, newName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId) &&
                inputData.getNewCategoryName().equals(newName)
        ));
    }

    @Test
    void testExecute_emptyCategoryId_callsInteractor() {
        String categoryId = "";
        String newName = "Work";

        controller.execute(categoryId, newName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId) &&
                inputData.getNewCategoryName().equals(newName)
        ));
    }

    @Test
    void testExecute_nullCategoryId_callsInteractor() {
        String categoryId = null;
        String newName = "Work";

        controller.execute(categoryId, newName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId() == null &&
                inputData.getNewCategoryName().equals(newName)
        ));
    }

    @Test
    void testExecute_emptyNewName_callsInteractor() {
        String categoryId = "cat123";
        String newName = "";

        controller.execute(categoryId, newName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId) &&
                inputData.getNewCategoryName().equals(newName)
        ));
    }

    @Test
    void testExecute_nullNewName_callsInteractor() {
        String categoryId = "cat123";
        String newName = null;

        controller.execute(categoryId, newName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId) &&
                inputData.getNewCategoryName() == null
        ));
    }

    @Test
    void testExecute_specialCharacters_callsInteractor() {
        String categoryId = "cat@#$123";
        String newName = "Work@Home";

        controller.execute(categoryId, newName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId) &&
                inputData.getNewCategoryName().equals(newName)
        ));
    }

    @Test
    void testExecute_longStrings_callsInteractor() {
        String categoryId = "very-long-category-id-12345";
        String newName = "Very long category name that might exceed limits";

        controller.execute(categoryId, newName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId) &&
                inputData.getNewCategoryName().equals(newName)
        ));
    }
}