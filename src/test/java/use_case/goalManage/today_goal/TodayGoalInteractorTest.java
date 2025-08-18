package use_case.goalManage.today_goal;

import data_access.GoalRepository;
import entity.Sophia.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TodayGoalInteractorTest {
    private GoalRepository goalRepository;
    private TodayGoalOutputBoundary outputBoundary;
    private TodayGoalInteractor interactor;

    @BeforeEach
    void setUp() {
        goalRepository = mock(GoalRepository.class);
        outputBoundary = mock(TodayGoalOutputBoundary.class);
        interactor = new TodayGoalInteractor(goalRepository, outputBoundary);
    }

    @Test
    void execute_ShowsTodayGoals() {
        // Arrange
        Goal mockGoal = mock(Goal.class);
        when(goalRepository.getTodayGoals()).thenReturn(List.of(mockGoal));

        // Act
        interactor.execute();

        // Assert
        verify(outputBoundary).prepareSuccessView(argThat(output ->
                output.getTodayGoals().size() == 1));
    }

    @Test
    void addToToday_AddsNewGoal() {
        // Arrange
        when(goalRepository.getTodayGoals()).thenReturn(Collections.emptyList());

        // Act
        interactor.addToToday(new TodayGoalInputData("New Goal", 0));

        // Assert
        verify(goalRepository).addGoalToToday("New Goal");
    }

    @Test
    void updateProgress_UpdatesGoalCorrectly() {
        // Arrange
        Goal mockGoal = mock(Goal.class);
        when(goalRepository.findByName("Test Goal")).thenReturn(Optional.of(mockGoal));

        // Act
        interactor.updateProgress(new TodayGoalInputData("Test Goal", 5.0));

        // Assert
        verify(mockGoal).setCurrentProgress(5);
        verify(goalRepository).save(mockGoal);
    }
}