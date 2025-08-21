package interface_adapter.Angela.task.delete;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.task.delete.DeleteTaskOutputData;

import static org.mockito.Mockito.*;

class DeleteTaskPresenterTest {

    private AvailableTasksViewModel mockAvailableTasksViewModel;
    private DeleteTaskViewModel mockDeleteTaskViewModel;
    private TodayTasksViewModel mockTodayTasksViewModel;
    private AddTaskToTodayViewModel mockAddTaskToTodayViewModel;
    private OverdueTasksController mockOverdueTasksController;
    private TodaySoFarController mockTodaySoFarController;
    private DeleteTaskPresenter presenter;
    private AvailableTasksState mockAvailableTasksState;
    private TodayTasksState mockTodayTasksState;
    private AddTaskToTodayState mockAddTaskToTodayState;

    @BeforeEach
    void setUp() {
        mockAvailableTasksViewModel = mock(AvailableTasksViewModel.class);
        mockDeleteTaskViewModel = mock(DeleteTaskViewModel.class);
        mockTodayTasksViewModel = mock(TodayTasksViewModel.class);
        mockAddTaskToTodayViewModel = mock(AddTaskToTodayViewModel.class);
        mockOverdueTasksController = mock(OverdueTasksController.class);
        mockTodaySoFarController = mock(TodaySoFarController.class);
        
        mockAvailableTasksState = mock(AvailableTasksState.class);
        mockTodayTasksState = mock(TodayTasksState.class);
        mockAddTaskToTodayState = mock(AddTaskToTodayState.class);
        
        when(mockAvailableTasksViewModel.getState()).thenReturn(mockAvailableTasksState);
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayTasksState);
        when(mockAddTaskToTodayViewModel.getState()).thenReturn(mockAddTaskToTodayState);
        
        presenter = new DeleteTaskPresenter(mockAvailableTasksViewModel, mockDeleteTaskViewModel);
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
    }

    @Test
    void testPrepareSuccessView_withAllDependencies_updatesAllViewModels() {
        String taskId = "task123";
        String message = "Task deleted successfully";
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, true);

        presenter.prepareSuccessView(outputData);

        // Verify delete task view model update
        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getLastDeletedTaskId().equals(taskId) &&
                state.getSuccessMessage().equals(message)
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);

        // Verify available tasks refresh
        verify(mockAvailableTasksState).setRefreshNeeded(true);
        verify(mockAvailableTasksViewModel).setState(mockAvailableTasksState);
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Verify today tasks refresh
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();

        // Verify add to today refresh
        verify(mockAddTaskToTodayState).setRefreshNeeded(true);
        verify(mockAddTaskToTodayViewModel).setState(mockAddTaskToTodayState);
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();

        // Verify controllers called
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPrepareSuccessView_withNullTodayTasksState_createsNewState() {
        when(mockTodayTasksViewModel.getState()).thenReturn(null);
        String taskId = "task123";
        String message = "Task deleted successfully";
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, true);

        presenter.prepareSuccessView(outputData);

        // Verify new TodayTasksState is created and used
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessView_withNullAddTaskToTodayState_createsNewState() {
        when(mockAddTaskToTodayViewModel.getState()).thenReturn(null);
        String taskId = "task123";
        String message = "Task deleted successfully";
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, true);

        presenter.prepareSuccessView(outputData);

        // Verify new AddTaskToTodayState is created and used
        verify(mockAddTaskToTodayViewModel).setState(any(AddTaskToTodayState.class));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPrepareSuccessView_withNoDependencies_updatesRequiredViewModels() {
        presenter = new DeleteTaskPresenter(mockAvailableTasksViewModel, mockDeleteTaskViewModel);
        String taskId = "task123";
        String message = "Task deleted successfully";
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, true);

        presenter.prepareSuccessView(outputData);

        // Verify required view models are still updated
        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getLastDeletedTaskId().equals(taskId) &&
                state.getSuccessMessage().equals(message)
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);

        verify(mockAvailableTasksState).setRefreshNeeded(true);
        verify(mockAvailableTasksViewModel).setState(mockAvailableTasksState);
        verify(mockAvailableTasksViewModel).firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Verify no interactions with optional dependencies
        verifyNoInteractions(mockTodayTasksViewModel);
        verifyNoInteractions(mockAddTaskToTodayViewModel);
        verifyNoInteractions(mockOverdueTasksController);
        verifyNoInteractions(mockTodaySoFarController);
    }

    @Test
    void testPrepareFailView_errorMessage_updatesDeleteTaskViewModel() {
        String error = "Task not found";

        presenter.prepareFailView(error);

        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getError().equals(error)
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Test
    void testPrepareFailView_emptyErrorMessage_updatesDeleteTaskViewModel() {
        String error = "";

        presenter.prepareFailView(error);

        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getError().equals(error)
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Test
    void testPrepareFailView_nullErrorMessage_updatesDeleteTaskViewModel() {
        String error = null;

        presenter.prepareFailView(error);

        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getError() == null
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Test
    void testShowDeleteFromBothWarning_validData_updatesDeleteTaskViewModel() {
        String taskId = "task123";
        String taskName = "Complete project";

        presenter.showDeleteFromBothWarning(taskId, taskName);

        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getPendingDeleteTaskId().equals(taskId) &&
                state.getPendingDeleteTaskName().equals(taskName) &&
                state.isShowWarningDialog() == true
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Test
    void testShowDeleteFromBothWarning_nullValues_updatesDeleteTaskViewModel() {
        String taskId = null;
        String taskName = null;

        presenter.showDeleteFromBothWarning(taskId, taskName);

        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getPendingDeleteTaskId() == null &&
                state.getPendingDeleteTaskName() == null &&
                state.isShowWarningDialog() == true
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Test
    void testShowDeleteFromBothWarning_emptyValues_updatesDeleteTaskViewModel() {
        String taskId = "";
        String taskName = "";

        presenter.showDeleteFromBothWarning(taskId, taskName);

        verify(mockDeleteTaskViewModel).setState(argThat(state ->
                state.getPendingDeleteTaskId().equals(taskId) &&
                state.getPendingDeleteTaskName().equals(taskName) &&
                state.isShowWarningDialog() == true
        ));
        verify(mockDeleteTaskViewModel).firePropertyChanged(DeleteTaskViewModel.DELETE_TASK_STATE_PROPERTY);
    }

    @Test
    void testSetters_dependencyInjection_storesReferences() {
        presenter = new DeleteTaskPresenter(mockAvailableTasksViewModel, mockDeleteTaskViewModel);

        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setAddTaskToTodayViewModel(mockAddTaskToTodayViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Test that dependencies are properly set by calling prepareSuccessView
        String taskId = "task123";
        String message = "Test message";
        DeleteTaskOutputData outputData = new DeleteTaskOutputData(taskId, message, true);
        presenter.prepareSuccessView(outputData);

        // If setters worked properly, these should be called
        verify(mockTodayTasksViewModel).firePropertyChanged();
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }
}