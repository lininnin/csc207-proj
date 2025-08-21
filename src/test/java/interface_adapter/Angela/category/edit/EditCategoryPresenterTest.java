package interface_adapter.Angela.category.edit;

import interface_adapter.Angela.category.CategoryManagementViewModel;
import interface_adapter.Angela.category.CategoryManagementState;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.category.edit.EditCategoryOutputData;
import test_utils.TestDataResetUtil;

import static org.mockito.Mockito.*;

class EditCategoryPresenterTest {

    private CategoryManagementViewModel mockCategoryViewModel;
    private AvailableTasksViewModel mockAvailableTasksViewModel;
    private TodayTasksViewModel mockTodayTasksViewModel;
    private OverdueTasksController mockOverdueTasksController;
    private TodaySoFarController mockTodaySoFarController;
    private AvailableEventViewModel mockAvailableEventViewModel;
    private TodaysEventsViewModel mockTodaysEventsViewModel;
    private EditCategoryPresenter presenter;
    private CategoryManagementState mockCategoryState;
    private AvailableTasksState mockAvailableTasksState;
    private TodayTasksState mockTodayTasksState;

    @BeforeEach
    void setUp() {
        // Reset all shared singleton data for test isolation
        TestDataResetUtil.resetAllSharedData();
        
        mockCategoryViewModel = mock(CategoryManagementViewModel.class);
        mockAvailableTasksViewModel = mock(AvailableTasksViewModel.class);
        mockTodayTasksViewModel = mock(TodayTasksViewModel.class);
        mockOverdueTasksController = mock(OverdueTasksController.class);
        mockTodaySoFarController = mock(TodaySoFarController.class);
        mockAvailableEventViewModel = mock(AvailableEventViewModel.class);
        mockTodaysEventsViewModel = mock(TodaysEventsViewModel.class);
        
        mockCategoryState = mock(CategoryManagementState.class);
        mockAvailableTasksState = mock(AvailableTasksState.class);
        mockTodayTasksState = mock(TodayTasksState.class);
        
        when(mockCategoryViewModel.getState()).thenReturn(mockCategoryState);
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableTasksState);
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayTasksState);
        
        presenter = new EditCategoryPresenter(mockCategoryViewModel);
        presenter.setAvailableTasksViewModel(mockAvailableTasksViewModel);
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
        presenter.setAvailableEventViewModel(mockAvailableEventViewModel);
        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);
    }

    @AfterEach
    void tearDown() {
        // Clear all mocks to ensure no state leakage
        reset(mockCategoryViewModel, mockAvailableTasksViewModel, mockTodayTasksViewModel,
              mockOverdueTasksController, mockTodaySoFarController, 
              mockAvailableEventViewModel, mockTodaysEventsViewModel,
              mockCategoryState, mockAvailableTasksState, mockTodayTasksState);
        presenter = null;
    }

    @Test
    void testPrepareSuccessView_validData_updatesAllViewModels() {
        String oldName = "Old Work";
        String newName = "New Work";
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat123", oldName, newName);

        presenter.prepareSuccessView(outputData);

        // Verify main category view model update
        verify(mockCategoryState).setLastAction("EDIT_SUCCESS");
        verify(mockCategoryState).setMessage("Category 'New Work' updated successfully");
        verify(mockCategoryState).setRefreshNeeded(true);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();

        // Verify available tasks refresh
        verify(mockAvailableTasksState).setRefreshNeeded(true);
        verify(mockAvailableTasksViewModel).setState(mockAvailableTasksState);
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Verify today tasks refresh
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged(TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY);

        // Verify controllers called
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();

        // Verify event view models refreshed
        verify(mockAvailableEventViewModel).firePropertyChanged();
        verify(mockTodaysEventsViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessView_withNullTodayTasksState_createsNewState() {
        when(mockTodayTasksViewModel.getState()).thenReturn(null);
        String oldName = "Old Work";
        String newName = "New Work";
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat123", oldName, newName);

        presenter.prepareSuccessView(outputData);

        // Verify new TodayTasksState is created and used
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged(TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY);
    }

    @Test
    void testPrepareSuccessView_withSpecialCharacters_updatesViewModel() {
        String oldName = "Old@Work";
        String newName = "New@Work#1";
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat123", oldName, newName);

        presenter.prepareSuccessView(outputData);

        verify(mockCategoryState).setLastAction("EDIT_SUCCESS");
        verify(mockCategoryState).setMessage("Category 'New@Work#1' updated successfully");
        verify(mockCategoryState).setRefreshNeeded(true);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessView_withEmptyNewName_updatesViewModel() {
        String oldName = "Old Work";
        String newName = "";
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat123", oldName, newName);

        presenter.prepareSuccessView(outputData);

        verify(mockCategoryState).setLastAction("EDIT_SUCCESS");
        verify(mockCategoryState).setMessage("Category '' updated successfully");
        verify(mockCategoryState).setRefreshNeeded(true);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessView_withNoDependencies_updatesOnlyCategoryViewModel() {
        presenter = new EditCategoryPresenter(mockCategoryViewModel);
        String oldName = "Old Work";
        String newName = "New Work";
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat123", oldName, newName);

        presenter.prepareSuccessView(outputData);

        // Verify only main category view model update
        verify(mockCategoryState).setLastAction("EDIT_SUCCESS");
        verify(mockCategoryState).setMessage("Category 'New Work' updated successfully");
        verify(mockCategoryState).setRefreshNeeded(true);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();

        // Verify no other interactions
        verifyNoInteractions(mockAvailableTasksViewModel);
        verifyNoInteractions(mockTodayTasksViewModel);
        verifyNoInteractions(mockOverdueTasksController);
        verifyNoInteractions(mockTodaySoFarController);
        verifyNoInteractions(mockAvailableEventViewModel);
        verifyNoInteractions(mockTodaysEventsViewModel);
    }

    @Test
    void testPrepareFailView_errorMessage_updatesViewModel() {
        String errorMessage = "Category name already exists";

        presenter.prepareFailView(errorMessage);

        verify(mockCategoryState).setLastAction("EDIT_FAIL");
        verify(mockCategoryState).setMessage(errorMessage);
        verify(mockCategoryState).setRefreshNeeded(false);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_emptyErrorMessage_updatesViewModel() {
        String errorMessage = "";

        presenter.prepareFailView(errorMessage);

        verify(mockCategoryState).setLastAction("EDIT_FAIL");
        verify(mockCategoryState).setMessage(errorMessage);
        verify(mockCategoryState).setRefreshNeeded(false);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_nullErrorMessage_updatesViewModel() {
        String errorMessage = null;

        presenter.prepareFailView(errorMessage);

        verify(mockCategoryState).setLastAction("EDIT_FAIL");
        verify(mockCategoryState).setMessage(errorMessage);
        verify(mockCategoryState).setRefreshNeeded(false);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testSetters_dependencyInjection_storesReferences() {
        presenter = new EditCategoryPresenter(mockCategoryViewModel);

        presenter.setAvailableTasksViewModel(mockAvailableTasksViewModel);
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
        presenter.setAvailableEventViewModel(mockAvailableEventViewModel);
        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);

        // Test that dependencies are properly set by calling prepareSuccessView
        String oldName = "Old";
        String newName = "New";
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat123", oldName, newName);
        presenter.prepareSuccessView(outputData);

        // If setters worked properly, these should be called
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
        verify(mockAvailableEventViewModel).firePropertyChanged();
        verify(mockTodaysEventsViewModel).firePropertyChanged();
    }
}