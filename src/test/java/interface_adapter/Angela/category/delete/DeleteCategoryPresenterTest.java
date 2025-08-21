package interface_adapter.Angela.category.delete;

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
import test_utils.TestDataResetUtil;
import use_case.Angela.category.delete.DeleteCategoryOutputData;

import static org.mockito.Mockito.*;

class DeleteCategoryPresenterTest {

    private CategoryManagementViewModel mockCategoryViewModel;
    private AvailableTasksViewModel mockAvailableTasksViewModel;
    private TodayTasksViewModel mockTodayTasksViewModel;
    private OverdueTasksController mockOverdueTasksController;
    private TodaySoFarController mockTodaySoFarController;
    private AvailableEventViewModel mockAvailableEventViewModel;
    private TodaysEventsViewModel mockTodaysEventsViewModel;
    private DeleteCategoryPresenter presenter;
    private CategoryManagementState mockCategoryState;
    private AvailableTasksState mockAvailableTasksState;
    private TodayTasksState mockTodayTasksState;

    @BeforeEach
    void setUp() {
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
        
        presenter = new DeleteCategoryPresenter(mockCategoryViewModel);
        presenter.setAvailableTasksViewModel(mockAvailableTasksViewModel);
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
        presenter.setAvailableEventViewModel(mockAvailableEventViewModel);
        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);
    }

    @AfterEach
    void tearDown() {
        reset(mockCategoryViewModel);
        reset(mockAvailableTasksViewModel);
        reset(mockTodayTasksViewModel);
        reset(mockOverdueTasksController);
        reset(mockTodaySoFarController);
        reset(mockAvailableEventViewModel);
        reset(mockTodaysEventsViewModel);
        reset(mockCategoryState);
        reset(mockAvailableTasksState);
        reset(mockTodayTasksState);
        presenter = null;
    }

    @Test
    void testPrepareSuccessView_withAllDependencies_updatesAllViewModels() {
        String message = "Category deleted successfully";
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData("cat123", message);

        presenter.prepareSuccessView(outputData);

        // Verify main category view model update
        verify(mockCategoryState).setLastAction("DELETE_SUCCESS");
        verify(mockCategoryState).setMessage(message);
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
        String message = "Category deleted successfully";
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData("cat123", message);

        presenter.prepareSuccessView(outputData);

        // Verify new TodayTasksState is created and used
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged(TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY);
    }

    @Test
    void testPrepareSuccessView_withNoDependencies_updatesOnlyCategoryViewModel() {
        presenter = new DeleteCategoryPresenter(mockCategoryViewModel);
        String message = "Category deleted successfully";
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData("cat123", message);

        presenter.prepareSuccessView(outputData);

        // Verify only main category view model update
        verify(mockCategoryState).setLastAction("DELETE_SUCCESS");
        verify(mockCategoryState).setMessage(message);
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
        String errorMessage = "Cannot delete category - not enough categories remaining";

        presenter.prepareFailView(errorMessage);

        verify(mockCategoryState).setLastAction("DELETE_FAIL");
        verify(mockCategoryState).setMessage(errorMessage);
        verify(mockCategoryState).setRefreshNeeded(false);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_emptyErrorMessage_updatesViewModel() {
        String errorMessage = "";

        presenter.prepareFailView(errorMessage);

        verify(mockCategoryState).setLastAction("DELETE_FAIL");
        verify(mockCategoryState).setMessage(errorMessage);
        verify(mockCategoryState).setRefreshNeeded(false);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareFailView_nullErrorMessage_updatesViewModel() {
        String errorMessage = null;

        presenter.prepareFailView(errorMessage);

        verify(mockCategoryState).setLastAction("DELETE_FAIL");
        verify(mockCategoryState).setMessage(errorMessage);
        verify(mockCategoryState).setRefreshNeeded(false);
        verify(mockCategoryViewModel).setState(mockCategoryState);
        verify(mockCategoryViewModel).firePropertyChanged();
    }

    @Test
    void testSetters_dependencyInjection_storesReferences() {
        presenter = new DeleteCategoryPresenter(mockCategoryViewModel);

        presenter.setAvailableTasksViewModel(mockAvailableTasksViewModel);
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
        presenter.setAvailableEventViewModel(mockAvailableEventViewModel);
        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);

        // Test that dependencies are properly set by calling prepareSuccessView
        String message = "Test message";
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData("cat123", message);
        presenter.prepareSuccessView(outputData);

        // If setters worked properly, these should be called
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
        verify(mockAvailableEventViewModel).firePropertyChanged();
        verify(mockTodaysEventsViewModel).firePropertyChanged();
    }
}