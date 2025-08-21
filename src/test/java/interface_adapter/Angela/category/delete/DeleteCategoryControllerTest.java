package interface_adapter.Angela.category.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.category.delete.DeleteCategoryInputBoundary;

import static org.mockito.Mockito.*;

class DeleteCategoryControllerTest {

    private DeleteCategoryInputBoundary mockInteractor;
    private DeleteCategoryController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(DeleteCategoryInputBoundary.class);
        controller = new DeleteCategoryController(mockInteractor);
    }

    @Test
    void testExecute_validCategoryId_callsInteractor() {
        String categoryId = "cat123";

        controller.execute(categoryId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId)
        ));
    }

    @Test
    void testExecute_emptyCategoryId_callsInteractor() {
        String categoryId = "";

        controller.execute(categoryId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId)
        ));
    }

    @Test
    void testExecute_nullCategoryId_callsInteractor() {
        String categoryId = null;

        controller.execute(categoryId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId() == null
        ));
    }

    @Test
    void testExecute_longCategoryId_callsInteractor() {
        String categoryId = "very-long-category-id-123456789";

        controller.execute(categoryId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId)
        ));
    }

    @Test
    void testExecute_specialCharactersInId_callsInteractor() {
        String categoryId = "cat@#$%123";

        controller.execute(categoryId);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryId().equals(categoryId)
        ));
    }
}