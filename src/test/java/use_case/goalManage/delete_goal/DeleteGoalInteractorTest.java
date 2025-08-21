package use_case.goalManage.delete_goal;

import use_case.repository.GoalRepository;
import entity.Sophia.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class DeleteGoalInteractorTest {
    private GoalRepository goalRepository;
    private DeleteGoalOutputBoundary outputBoundary;
    private use_case.goalManage.delete_goal.DeleteGoalInteractor interactor;

    @BeforeEach
    void setUp() {
        goalRepository = mock(GoalRepository.class);
        outputBoundary = mock(DeleteGoalOutputBoundary.class);
        interactor = new use_case.goalManage.delete_goal.DeleteGoalInteractor(goalRepository, outputBoundary);
    }

    @Test
    void execute_WithConfirmedDeletion_DeletesGoal() {
        // Arrange
        Goal mockGoal = mock(Goal.class);
        when(goalRepository.findByName("Test Goal")).thenReturn(Optional.of(mockGoal));
        when(goalRepository.isInCurrentGoals(mockGoal)).thenReturn(false);

        DeleteGoalInputData inputData = new DeleteGoalInputData("Test Goal", true);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(goalRepository).deleteByName("Test Goal");
        verify(outputBoundary).prepareSuccessView(any());
    }

    @Test
    void execute_WithUnconfirmedCurrentGoal_RequestsConfirmation() {
        // Arrange
        Goal mockGoal = mock(Goal.class);
        when(goalRepository.findByName("Test Goal")).thenReturn(Optional.of(mockGoal));
        when(goalRepository.isInCurrentGoals(mockGoal)).thenReturn(true);

        DeleteGoalInputData inputData = new DeleteGoalInputData("Test Goal", false);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(outputBoundary).prepareConfirmationView(any());
    }

    @Test
    void execute_WithNonexistentGoal_PreparesFailView() {
        // Arrange
        when(goalRepository.findByName("Nonexistent")).thenReturn(Optional.empty());
        DeleteGoalInputData inputData = new DeleteGoalInputData("Nonexistent", true);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(outputBoundary).prepareFailView(contains("Goal not found"));
    }

    @Test
    void executeConfirmedDeletion_WithValidGoal_DeletesAndShowsSuccess() {
        // Arrange
        Goal mockGoal = mock(Goal.class);
        when(goalRepository.findByName("Test Goal")).thenReturn(Optional.of(mockGoal));
        when(goalRepository.isInCurrentGoals(mockGoal)).thenReturn(true);

        // Act
        interactor.executeConfirmedDeletion("Test Goal");

        // Assert
        verify(goalRepository).deleteByName("Test Goal");
        verify(outputBoundary).prepareSuccessView(argThat(output ->
                output.getMessage().contains("Successfully deleted goal")));
    }
}