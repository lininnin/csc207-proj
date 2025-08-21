package interface_adapter.Angela.category.create;

import interface_adapter.Angela.category.CategoryManagementViewModel;
import interface_adapter.Angela.category.CategoryManagementState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.category.create.CreateCategoryOutputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCategoryPresenterTest {

    private CategoryManagementViewModel mockViewModel;
    private CreateCategoryPresenter presenter;
    private CategoryManagementState mockState;

    @BeforeEach
    void setUp() {
        mockViewModel = mock(CategoryManagementViewModel.class);
        mockState = mock(CategoryManagementState.class);
        when(mockViewModel.getState()).thenReturn(mockState);
        
        presenter = new CreateCategoryPresenter(mockViewModel);
    }

    @Test
    void testPrepareSuccessView_validOutputData_updatesViewModel() {
        String categoryName = "Work";
        CreateCategoryOutputData outputData = new CreateCategoryOutputData("cat123", categoryName, true);

        presenter.prepareSuccessView(outputData);

        verify(mockState).setLastAction("CREATE_SUCCESS");
        verify(mockState).setMessage("Category 'Work' created successfully");
        verify(mockState).setRefreshNeeded(true);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessView_categoryNameWithSpecialChars_updatesViewModel() {
        String categoryName = "Work@Home";
        CreateCategoryOutputData outputData = new CreateCategoryOutputData("cat123", categoryName, true);

        presenter.prepareSuccessView(outputData);

        verify(mockState).setLastAction("CREATE_SUCCESS");
        verify(mockState).setMessage("Category 'Work@Home' created successfully");
        verify(mockState).setRefreshNeeded(true);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessView_emptyCategoryName_updatesViewModel() {
        String categoryName = "";
        CreateCategoryOutputData outputData = new CreateCategoryOutputData("cat123", categoryName, true);

        presenter.prepareSuccessView(outputData);

        verify(mockState).setLastAction("CREATE_SUCCESS");
        verify(mockState).setMessage("Category '' created successfully");
        verify(mockState).setRefreshNeeded(true);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_errorMessage_updatesViewModel() {
        String errorMessage = "Category name already exists";

        presenter.prepareFailView(errorMessage);

        verify(mockState).setLastAction("CREATE_FAIL");
        verify(mockState).setMessage(errorMessage);
        verify(mockState).setRefreshNeeded(false);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_emptyErrorMessage_updatesViewModel() {
        String errorMessage = "";

        presenter.prepareFailView(errorMessage);

        verify(mockState).setLastAction("CREATE_FAIL");
        verify(mockState).setMessage(errorMessage);
        verify(mockState).setRefreshNeeded(false);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_nullErrorMessage_updatesViewModel() {
        String errorMessage = null;

        presenter.prepareFailView(errorMessage);

        verify(mockState).setLastAction("CREATE_FAIL");
        verify(mockState).setMessage(errorMessage);
        verify(mockState).setRefreshNeeded(false);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_longErrorMessage_updatesViewModel() {
        String errorMessage = "This is a very long error message that exceeds normal length to test handling";

        presenter.prepareFailView(errorMessage);

        verify(mockState).setLastAction("CREATE_FAIL");
        verify(mockState).setMessage(errorMessage);
        verify(mockState).setRefreshNeeded(false);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }
}