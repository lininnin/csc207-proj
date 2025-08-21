package interface_adapter.Angela.task.add_to_today;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_utils.TestDataResetUtil;
import use_case.Angela.task.add_to_today.AddTaskToTodayOutputData;
import entity.Angela.Task.Task;

import static org.mockito.Mockito.*;

class AddTaskToTodayPresenterTest {

    private AddTaskToTodayViewModel mockAddTaskToTodayViewModel;
    private TodayTasksViewModel mockTodayTasksViewModel;
    private OverdueTasksController mockOverdueTasksController;
    private TodaySoFarController mockTodaySoFarController;
    private AddTaskToTodayPresenter presenter;
    private TodayTasksState mockTodayTasksState;

    @BeforeEach
    void setUp() {
        TestDataResetUtil.resetAllSharedData();
        mockAddTaskToTodayViewModel = mock(AddTaskToTodayViewModel.class);
        mockTodayTasksViewModel = mock(TodayTasksViewModel.class);
        mockOverdueTasksController = mock(OverdueTasksController.class);
        mockTodaySoFarController = mock(TodaySoFarController.class);
        
        mockTodayTasksState = mock(TodayTasksState.class);
        when(mockTodayTasksViewModel.getState()).thenReturn(mockTodayTasksState);
        
        presenter = new AddTaskToTodayPresenter(mockAddTaskToTodayViewModel, mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
    }

    @AfterEach
    void tearDown() {
        reset(mockAddTaskToTodayViewModel);
        reset(mockTodayTasksViewModel);
        reset(mockOverdueTasksController);
        reset(mockTodaySoFarController);
        reset(mockTodayTasksState);
        presenter = null;
    }

    @Test
    void testPresentSuccess_validOutputData_updatesViewModels() {
        String taskName = "Complete project";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);

        presenter.presentSuccess(outputData);

        // Verify add to today view model update
        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getSuccessMessage().equals("Task 'Complete project' added to Today's Tasks") &&
                state.getError() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();

        // Verify today tasks view model refresh
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();

        // Verify controllers called
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPresentSuccess_withNullTodayTasksState_createsNewState() {
        when(mockTodayTasksViewModel.getState()).thenReturn(null);
        String taskName = "Complete project";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);

        presenter.presentSuccess(outputData);

        // Verify new TodayTasksState is created and used
        verify(mockTodayTasksViewModel).setState(any(TodayTasksState.class));
        verify(mockTodayTasksViewModel).firePropertyChanged();

        // Verify other operations still work
        verify(mockAddTaskToTodayViewModel).setState(any(AddTaskToTodayState.class));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
        verify(mockOverdueTasksController).execute(7);
        verify(mockTodaySoFarController).refresh();
    }

    @Test
    void testPresentSuccess_emptyTaskName_updatesViewModels() {
        String taskName = "";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);

        presenter.presentSuccess(outputData);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getSuccessMessage().equals("Task '' added to Today's Tasks") &&
                state.getError() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_nullTaskName_updatesViewModels() {
        String taskName = null;
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);

        presenter.presentSuccess(outputData);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getSuccessMessage().equals("Task 'null' added to Today's Tasks") &&
                state.getError() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_specialCharactersInTaskName_updatesViewModels() {
        String taskName = "Task@#$%!";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);

        presenter.presentSuccess(outputData);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getSuccessMessage().equals("Task 'Task@#$%!' added to Today's Tasks") &&
                state.getError() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_longTaskName_updatesViewModels() {
        String taskName = "Very long task name that exceeds normal length and contains many words";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);

        presenter.presentSuccess(outputData);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getSuccessMessage().contains(taskName) &&
                state.getError() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentSuccess_withoutControllers_stillUpdatesViewModels() {
        presenter = new AddTaskToTodayPresenter(mockAddTaskToTodayViewModel, mockTodayTasksViewModel);
        String taskName = "Complete project";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);

        presenter.presentSuccess(outputData);

        // Verify view models are still updated
        verify(mockAddTaskToTodayViewModel).setState(any(AddTaskToTodayState.class));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
        verify(mockTodayTasksState).setRefreshNeeded(true);
        verify(mockTodayTasksViewModel).setState(mockTodayTasksState);
        verify(mockTodayTasksViewModel).firePropertyChanged();

        // Verify no interactions with controllers
        verifyNoInteractions(mockOverdueTasksController);
        verifyNoInteractions(mockTodaySoFarController);
    }

    @Test
    void testPresentError_errorMessage_updatesAddTaskToTodayViewModel() {
        String error = "Task already exists in today's list";

        presenter.presentError(error);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getError().equals(error) &&
                state.getSuccessMessage() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_emptyErrorMessage_updatesAddTaskToTodayViewModel() {
        String error = "";

        presenter.presentError(error);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getError().equals(error) &&
                state.getSuccessMessage() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_nullErrorMessage_updatesAddTaskToTodayViewModel() {
        String error = null;

        presenter.presentError(error);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getError() == null &&
                state.getSuccessMessage() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_longErrorMessage_updatesAddTaskToTodayViewModel() {
        String error = "This is a very long error message that contains detailed information about what went wrong";

        presenter.presentError(error);

        verify(mockAddTaskToTodayViewModel).setState(argThat(state ->
                state.getError().equals(error) &&
                state.getSuccessMessage() == null
        ));
        verify(mockAddTaskToTodayViewModel).firePropertyChanged();
    }

    @Test
    void testSetOverdueTasksController_storesReference() {
        presenter = new AddTaskToTodayPresenter(mockAddTaskToTodayViewModel, mockTodayTasksViewModel);
        
        presenter.setOverdueTasksController(mockOverdueTasksController);

        // Test that dependency is properly set by calling presentSuccess
        String taskName = "Test task";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);
        presenter.presentSuccess(outputData);

        // If setter worked properly, this should be called
        verify(mockOverdueTasksController).execute(7);
    }

    @Test
    void testSetTodaySoFarController_storesReference() {
        presenter = new AddTaskToTodayPresenter(mockAddTaskToTodayViewModel, mockTodayTasksViewModel);
        
        presenter.setTodaySoFarController(mockTodaySoFarController);

        // Test that dependency is properly set by calling presentSuccess
        String taskName = "Test task";
        Task mockTask = mock(Task.class);
        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(mockTask, taskName);
        presenter.presentSuccess(outputData);

        // If setter worked properly, this should be called
        verify(mockTodaySoFarController).refresh();
    }
}