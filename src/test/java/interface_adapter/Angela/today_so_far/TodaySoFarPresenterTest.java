package interface_adapter.Angela.today_so_far;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.today_so_far.TodaySoFarOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class TodaySoFarPresenterTest {

    private TodaySoFarViewModel mockViewModel;
    private TodaySoFarPresenter presenter;
    private TodaySoFarState mockState;

    @BeforeEach
    void setUp() {
        mockViewModel = mock(TodaySoFarViewModel.class);
        mockState = mock(TodaySoFarState.class);
        when(mockViewModel.getState()).thenReturn(mockState);
        
        presenter = new TodaySoFarPresenter(mockViewModel);
    }

    @Test
    void testPresentTodaySoFar_validOutputData_updatesViewModel() {
        List<TodaySoFarOutputData.GoalProgress> goals = List.of(
            new TodaySoFarOutputData.GoalProgress("Goal 1", "Daily", "50%"),
            new TodaySoFarOutputData.GoalProgress("Goal 2", "Weekly", "75%")
        );
        List<TodaySoFarOutputData.CompletedItem> completedItems = List.of(
            new TodaySoFarOutputData.CompletedItem("Task", "Task 1", "Work"),
            new TodaySoFarOutputData.CompletedItem("Event", "Task 2", "Personal")
        );
        int completionRate = 75;
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = List.of(
            new TodaySoFarOutputData.WellnessEntry("Happy", 2, 8, 3, java.time.LocalTime.of(9, 0)),
            new TodaySoFarOutputData.WellnessEntry("Calm", 1, 7, 2, java.time.LocalTime.of(15, 30))
        );
        
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(
                goals, completedItems, completionRate, wellnessEntries
        );

        presenter.presentTodaySoFar(outputData);

        // Verify new state is created and set
        verify(mockViewModel).setState(argThat(state ->
                state.getGoals().size() == 2 &&
                state.getCompletedItems().size() == 2 &&
                state.getCompletionRate() == 75 &&
                state.getWellnessEntries().size() == 2 &&
                state.getError() == null
        ));
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentTodaySoFar_emptyLists_updatesViewModel() {
        List<TodaySoFarOutputData.GoalProgress> goals = new ArrayList<>();
        List<TodaySoFarOutputData.CompletedItem> completedItems = new ArrayList<>();
        int completionRate = 0;
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = new ArrayList<>();
        
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(
                goals, completedItems, completionRate, wellnessEntries
        );

        presenter.presentTodaySoFar(outputData);

        verify(mockViewModel).setState(argThat(state ->
                state.getGoals().isEmpty() &&
                state.getCompletedItems().isEmpty() &&
                state.getCompletionRate() == 0 &&
                state.getWellnessEntries().isEmpty() &&
                state.getError() == null
        ));
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentTodaySoFar_nullLists_updatesViewModel() {
        List<TodaySoFarOutputData.GoalProgress> goals = null;
        List<TodaySoFarOutputData.CompletedItem> completedItems = null;
        int completionRate = 100;
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = null;
        
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(
                goals, completedItems, completionRate, wellnessEntries
        );

        presenter.presentTodaySoFar(outputData);

        verify(mockViewModel).setState(argThat(state ->
                state.getGoals() == null &&
                state.getCompletedItems() == null &&
                state.getCompletionRate() == 100 &&
                state.getWellnessEntries() == null &&
                state.getError() == null
        ));
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentTodaySoFar_highCompletionRate_updatesViewModel() {
        List<TodaySoFarOutputData.GoalProgress> goals = List.of(
            new TodaySoFarOutputData.GoalProgress("Goal 1", "Daily", "100%")
        );
        List<TodaySoFarOutputData.CompletedItem> completedItems = List.of(
            new TodaySoFarOutputData.CompletedItem("Task", "Task 1", "Work")
        );
        int completionRate = 100; // 100% completion
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = List.of(
            new TodaySoFarOutputData.WellnessEntry("Happy", 1, 9, 1, java.time.LocalTime.of(10, 0))
        );
        
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(
                goals, completedItems, completionRate, wellnessEntries
        );

        presenter.presentTodaySoFar(outputData);

        verify(mockViewModel).setState(argThat(state ->
                state.getCompletionRate() == 100 &&
                state.getError() == null
        ));
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentTodaySoFar_lowCompletionRate_updatesViewModel() {
        List<TodaySoFarOutputData.GoalProgress> goals = List.of(
            new TodaySoFarOutputData.GoalProgress("Goal 1", "Daily", "10%"),
            new TodaySoFarOutputData.GoalProgress("Goal 2", "Weekly", "20%"),
            new TodaySoFarOutputData.GoalProgress("Goal 3", "Monthly", "5%")
        );
        List<TodaySoFarOutputData.CompletedItem> completedItems = List.of(
            new TodaySoFarOutputData.CompletedItem("Task", "Task 1", "Work")
        );
        int completionRate = 33; // 33% completion
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = List.of();
        
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(
                goals, completedItems, completionRate, wellnessEntries
        );

        presenter.presentTodaySoFar(outputData);

        verify(mockViewModel).setState(argThat(state ->
                state.getCompletionRate() == 33 &&
                state.getError() == null
        ));
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentTodaySoFar_largeDataSet_updatesViewModel() {
        List<TodaySoFarOutputData.GoalProgress> goals = List.of(
            new TodaySoFarOutputData.GoalProgress("Goal 1", "Daily", "80%"),
            new TodaySoFarOutputData.GoalProgress("Goal 2", "Daily", "90%"),
            new TodaySoFarOutputData.GoalProgress("Goal 3", "Weekly", "85%"),
            new TodaySoFarOutputData.GoalProgress("Goal 4", "Weekly", "75%"),
            new TodaySoFarOutputData.GoalProgress("Goal 5", "Monthly", "95%")
        );
        List<TodaySoFarOutputData.CompletedItem> completedItems = List.of(
            new TodaySoFarOutputData.CompletedItem("Task", "Task 1", "Work"),
            new TodaySoFarOutputData.CompletedItem("Task", "Task 2", "Personal"),
            new TodaySoFarOutputData.CompletedItem("Event", "Task 3", "Health"),
            new TodaySoFarOutputData.CompletedItem("Task", "Task 4", "Work"),
            new TodaySoFarOutputData.CompletedItem("Event", "Task 5", "Personal"),
            new TodaySoFarOutputData.CompletedItem("Task", "Task 6", "Study")
        );
        int completionRate = 85;
        List<TodaySoFarOutputData.WellnessEntry> wellnessEntries = List.of(
            new TodaySoFarOutputData.WellnessEntry("Happy", 2, 8, 2, java.time.LocalTime.of(9, 0)),
            new TodaySoFarOutputData.WellnessEntry("Energetic", 3, 9, 1, java.time.LocalTime.of(12, 0)),
            new TodaySoFarOutputData.WellnessEntry("Calm", 1, 7, 3, java.time.LocalTime.of(18, 0))
        );
        
        TodaySoFarOutputData outputData = new TodaySoFarOutputData(
                goals, completedItems, completionRate, wellnessEntries
        );

        presenter.presentTodaySoFar(outputData);

        verify(mockViewModel).setState(argThat(state ->
                state.getGoals().size() == 5 &&
                state.getCompletedItems().size() == 6 &&
                state.getCompletionRate() == 85 &&
                state.getWellnessEntries().size() == 3 &&
                state.getError() == null
        ));
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_errorMessage_updatesViewModel() {
        String error = "Failed to load today's data";

        presenter.presentError(error);

        verify(mockState).setError(error);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_withNullState_createsNewState() {
        when(mockViewModel.getState()).thenReturn(null);
        String error = "Failed to load today's data";

        presenter.presentError(error);

        verify(mockViewModel).setState(any(TodaySoFarState.class));
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_emptyErrorMessage_updatesViewModel() {
        String error = "";

        presenter.presentError(error);

        verify(mockState).setError(error);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_nullErrorMessage_updatesViewModel() {
        String error = null;

        presenter.presentError(error);

        verify(mockState).setError(error);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_longErrorMessage_updatesViewModel() {
        String error = "This is a very long error message that contains detailed information about what went wrong while loading today's data";

        presenter.presentError(error);

        verify(mockState).setError(error);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }

    @Test
    void testPresentError_specialCharactersInError_updatesViewModel() {
        String error = "Error: Failed to load data @#$%!";

        presenter.presentError(error);

        verify(mockState).setError(error);
        verify(mockViewModel).setState(mockState);
        verify(mockViewModel).firePropertyChanged();
    }
}