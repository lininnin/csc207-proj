package interface_adapter.Angela.task.create;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayState;
import interface_adapter.ViewManagerModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.task.create.CreateTaskOutputData;
import test_utils.TestDataResetUtil;

import static org.mockito.Mockito.*;

class CreateTaskPresenterTest {

    private CreateTaskViewModel mockCreateTaskViewModel;
    private AvailableTasksViewModel mockAvailableTasksViewModel;
    private ViewManagerModel mockViewManagerModel;
    private AddTaskToTodayViewModel mockAddTaskToTodayViewModel;
    private CreateTaskPresenter presenter;
    private CreateTaskState mockCreateTaskState;
    private AvailableTasksState mockAvailableTasksState;
    private AddTaskToTodayState mockAddTaskToTodayState;

    @BeforeEach
    void setUp() {
        // Reset all shared singleton data for test isolation
        TestDataResetUtil.resetAllSharedData();
        
        mockCreateTaskViewModel = mock(CreateTaskViewModel.class);
        mockAvailableTasksViewModel = mock(AvailableTasksViewModel.class);
        mockViewManagerModel = mock(ViewManagerModel.class);
        mockAddTaskToTodayViewModel = mock(AddTaskToTodayViewModel.class);
        
        mockCreateTaskState = mock(CreateTaskState.class);
        mockAvailableTasksState = mock(AvailableTasksState.class);
        mockAddTaskToTodayState = mock(AddTaskToTodayState.class);
        
        when(mockCreateTaskViewModel.getState()).thenReturn(mockCreateTaskState);
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableTasksState);
        when(mockAddTaskToTodayViewModel.getState()).thenReturn(mockAddTaskToTodayState);
        
        presenter = new CreateTaskPresenter(mockCreateTaskViewModel, mockAvailableTasksViewModel, mockViewManagerModel);
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);
    }

    @AfterEach
    void tearDown() {
        // Clear all mocks to ensure no state leakage
        reset(mockCreateTaskViewModel, mockAvailableTasksViewModel, mockViewManagerModel, 
              mockAddTaskToTodayViewModel, mockCreateTaskState, mockAvailableTasksState,
              mockAddTaskToTodayState);
        presenter = null;
    }

    @Test
    void testPresentSuccess_validOutputData_updatesAllViewModels() {
        String message = "Task created successfully";
        CreateTaskOutputData outputData = new CreateTaskOutputData("task123", "Test Task", message);

        presenter.presentSuccess(outputData);

        // Verify create task view model state is cleared and success message set
        verify(mockCreateTaskState).setTaskName("");
        verify(mockCreateTaskState).setDescription("");
        verify(mockCreateTaskState).setCategoryId("");
        verify(mockCreateTaskState).setOneTime(false);
        verify(mockCreateTaskState).setError(null);
        verify(mockCreateTaskState).setSuccessMessage(message);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);

        // Verify available tasks view model refresh
        verify(mockAvailableTasksState).setRefreshNeeded(true);
        verify(mockAvailableTasksViewModel).setState(mockAvailableTasksState);
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Verify add to today view model refresh
        verify(mockAddTaskToTodayState).setRefreshNeeded(true);
        verify(mockAddTaskToTodayViewModel).setState(mockAddTaskToTodayState);
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_withNullAddTaskToTodayState_createsNewState() {
        when(mockAddTaskToTodayViewModel.getState()).thenReturn(null);
        String message = "Task created successfully";
        CreateTaskOutputData outputData = new CreateTaskOutputData("task123", "Test Task", message);

        presenter.presentSuccess(outputData);

        // Verify new AddTaskToTodayState is created and used
        verify(mockAddTaskToTodayViewModel).setState(any(AddTaskToTodayState.class));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_withoutAddTaskToTodayViewModel_stillWorksCorrectly() {
        presenter = new CreateTaskPresenter(mockCreateTaskViewModel, mockAvailableTasksViewModel, mockViewManagerModel);
        String message = "Task created successfully";
        CreateTaskOutputData outputData = new CreateTaskOutputData("task123", "Test Task", message);

        presenter.presentSuccess(outputData);

        // Verify other view models are still updated
        verify(mockCreateTaskState).setTaskName("");
        verify(mockCreateTaskState).setDescription("");
        verify(mockCreateTaskState).setCategoryId("");
        verify(mockCreateTaskState).setOneTime(false);
        verify(mockCreateTaskState).setError(null);
        verify(mockCreateTaskState).setSuccessMessage(message);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);

        verify(mockAvailableTasksState).setRefreshNeeded(true);
        verify(mockAvailableTasksViewModel).setState(mockAvailableTasksState);
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Verify no interaction with add to today view model
        verifyNoInteractions(mockAddTaskToTodayViewModel);
    }

    @Test
    void testPresentSuccess_emptyMessage_updatesViewModels() {
        String message = "";
        CreateTaskOutputData outputData = new CreateTaskOutputData("task123", "Test Task", message);

        presenter.presentSuccess(outputData);

        verify(mockCreateTaskState).setSuccessMessage(message);
        verify(mockCreateTaskState).setError(null);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }

    @Test
    void testPresentSuccess_nullMessage_updatesViewModels() {
        String message = null;
        CreateTaskOutputData outputData = new CreateTaskOutputData("task123", "Test Task", message);

        presenter.presentSuccess(outputData);

        verify(mockCreateTaskState).setSuccessMessage(message);
        verify(mockCreateTaskState).setError(null);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }

    @Test
    void testPresentError_errorMessage_updatesCreateTaskViewModel() {
        String error = "Task name is required";

        presenter.presentError(error);

        verify(mockCreateTaskState).setError(error);
        verify(mockCreateTaskState).setSuccessMessage(null);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }

    @Test
    void testPresentError_emptyErrorMessage_updatesCreateTaskViewModel() {
        String error = "";

        presenter.presentError(error);

        verify(mockCreateTaskState).setError(error);
        verify(mockCreateTaskState).setSuccessMessage(null);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }

    @Test
    void testPresentError_nullErrorMessage_updatesCreateTaskViewModel() {
        String error = null;

        presenter.presentError(error);

        verify(mockCreateTaskState).setError(error);
        verify(mockCreateTaskState).setSuccessMessage(null);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }

    @Test
    void testPresentError_longErrorMessage_updatesCreateTaskViewModel() {
        String error = "This is a very long error message that contains detailed information about what went wrong";

        presenter.presentError(error);

        verify(mockCreateTaskState).setError(error);
        verify(mockCreateTaskState).setSuccessMessage(null);
        verify(mockCreateTaskViewModel).setState(mockCreateTaskState);
        verify(mockCreateTaskViewModel).firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }

    @Test
    void testSetAddTaskToTodayViewModel_storesReference() {
        presenter = new CreateTaskPresenter(mockCreateTaskViewModel, mockAvailableTasksViewModel, mockViewManagerModel);
        
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);

        // Test that dependency is properly set by calling presentSuccess
        String message = "Test message";
        CreateTaskOutputData outputData = new CreateTaskOutputData("task123", "Test Task", message);
        presenter.presentSuccess(outputData);

        // If setter worked properly, this should be called
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }
}