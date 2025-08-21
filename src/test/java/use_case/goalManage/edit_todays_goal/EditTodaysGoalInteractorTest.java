package use_case.goalManage.edit_todays_goal;

import use_case.repository.GoalRepository;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class EditTodaysGoalInteractorTest {
    private GoalRepository goalRepository;
    private EditTodaysGoalOutputBoundary presenter;
    private use_case.goalManage.edit_todays_goal.EditTodaysGoalInteractor interactor;

    @BeforeEach
    void setUp() {
        goalRepository = mock(GoalRepository.class);
        presenter = mock(EditTodaysGoalOutputBoundary.class);
        interactor = new use_case.goalManage.edit_todays_goal.EditTodaysGoalInteractor(goalRepository, presenter);
    }

    @Test
    void execute_WithUnconfirmedEdit_RequestsConfirmation() {
        // Arrange
        Goal mockGoal = createValidMockGoal();
        when(goalRepository.findByName("Test Goal")).thenReturn(Optional.of(mockGoal));

        LocalDate newDueDate = LocalDate.now().plusDays(7);
        EditTodaysGoalInputData inputData = new EditTodaysGoalInputData("Test Goal", newDueDate, false);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter).prepareConfirmationView(argThat(output ->
                output.getMessage().contains("Update due date")));
        verify(goalRepository, never()).save(any());
    }

    @Test
    void execute_WithConfirmedEdit_UpdatesGoalSuccessfully() {
        // Arrange
        Goal mockGoal = createValidMockGoal();
        when(goalRepository.findByName("Test Goal")).thenReturn(Optional.of(mockGoal));

        LocalDate newDueDate = LocalDate.now().plusDays(7);
        EditTodaysGoalInputData inputData = new EditTodaysGoalInputData("Test Goal", newDueDate, true);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter).prepareSuccessView(argThat(output ->
                output.getMessage().contains("Due date updated")));
    }

    @Test
    void execute_WithNonexistentGoal_PreparesFailView() {
        // Arrange
        when(goalRepository.findByName("Nonexistent")).thenReturn(Optional.empty());
        EditTodaysGoalInputData inputData = new EditTodaysGoalInputData("Nonexistent", LocalDate.now(), true);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter).prepareFailView(contains("Goal not found"));
        verify(goalRepository, never()).save(any());
    }

    @Test
    void execute_WithInvalidGoalData_PreparesFailView() {
        // Arrange
        Goal invalidGoal = createInvalidMockGoal(); // Missing required fields
        when(goalRepository.findByName("Invalid Goal")).thenReturn(Optional.of(invalidGoal));
        EditTodaysGoalInputData inputData = new EditTodaysGoalInputData("Invalid Goal", LocalDate.now(), true);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter).prepareFailView(contains("Failed to edit goal"));
    }

    private Goal createValidMockGoal() {
        Goal mockGoal = mock(Goal.class);
        GoalInfo mockGoalInfo = mock(GoalInfo.class);
        BeginAndDueDates mockDates = mock(BeginAndDueDates.class);

        when(mockGoal.getGoalInfo()).thenReturn(mockGoalInfo);
        when(mockGoal.getBeginAndDueDates()).thenReturn(mockDates);
        when(mockGoal.getTimePeriod()).thenReturn(Goal.TimePeriod.WEEK);
        when(mockGoal.getFrequency()).thenReturn(1);
        when(mockGoal.getCurrentProgress()).thenReturn(3);
        when(mockGoalInfo.getInfo()).thenReturn(new Info.Builder("Test Goal").build());

        return mockGoal;
    }

    private Goal createInvalidMockGoal() {
        Goal mockGoal = mock(Goal.class);
        // Missing required fields like TimePeriod
        return mockGoal;
    }
}