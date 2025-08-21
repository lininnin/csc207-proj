package interface_adapter.Angela.category.create;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.category.create.CreateCategoryInputBoundary;
import use_case.Angela.category.create.CreateCategoryInputData;

import static org.mockito.Mockito.*;

class CreateCategoryControllerTest {

    private CreateCategoryInputBoundary mockInteractor;
    private CreateCategoryController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(CreateCategoryInputBoundary.class);
        controller = new CreateCategoryController(mockInteractor);
    }

    @Test
    void testExecute_validCategoryName_callsInteractor() {
        String categoryName = "Work";

        controller.execute(categoryName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryName().equals(categoryName)
        ));
    }

    @Test
    void testExecute_emptyCategoryName_callsInteractor() {
        String categoryName = "";

        controller.execute(categoryName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryName().equals(categoryName)
        ));
    }

    @Test
    void testExecute_nullCategoryName_callsInteractor() {
        String categoryName = null;

        controller.execute(categoryName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryName() == null
        ));
    }

    @Test
    void testExecute_longCategoryName_callsInteractor() {
        String categoryName = "Very long category name that exceeds the limit";

        controller.execute(categoryName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryName().equals(categoryName)
        ));
    }

    @Test
    void testExecute_specialCharacters_callsInteractor() {
        String categoryName = "Work@#$%";

        controller.execute(categoryName);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getCategoryName().equals(categoryName)
        ));
    }
}