package use_case.Sophia.goal;

import data_access.GoalRepository;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.Sophia.GoalFactory;
import entity.Sophia.GoalInfo;
import entity.info.Info;
import entity.Angela.Task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import use_case.goalManage.create_goal.CreateGoalInputData;
import use_case.goalManage.create_goal.CreateGoalInteractor;
import use_case.goalManage.create_goal.CreateGoalOutputBoundary;
import use_case.goalManage.create_goal.CreateGoalOutputData;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class create {

    private GoalRepository goalRepository;
    private CreateGoalOutputBoundary presenter;
    private GoalFactory goalFactory;
    private CreateGoalInteractor interactor;

    @BeforeEach
    void setUp() {
        goalRepository = mock(GoalRepository.class);
        presenter = mock(CreateGoalOutputBoundary.class);
        goalFactory = mock(GoalFactory.class);
        interactor = new CreateGoalInteractor(goalRepository, presenter, goalFactory);
    }

    @ParameterizedTest
    @EnumSource(value = Goal.TimePeriod.class, names = {"WEEK", "MONTH"})
    void execute_WithValidParameters_CreatesGoal(Goal.TimePeriod timePeriod) {
        // Arrange
        CreateGoalInputData inputData = new CreateGoalInputData(
                "Test Goal",
                "Test Description",
                100.0,
                0.0,
                LocalDate.now(),
                timePeriod == Goal.TimePeriod.WEEK ? LocalDate.now().plusWeeks(1) : LocalDate.now().plusMonths(1),
                timePeriod,
                1,
                null
        );

        Goal mockGoal = mock(Goal.class);
        GoalInfo mockGoalInfo = mock(GoalInfo.class);
        Info mockInfo = new Info.Builder("Test Goal").build();

        when(mockGoal.getGoalInfo()).thenReturn(mockGoalInfo);
        when(mockGoalInfo.getInfo()).thenReturn(mockInfo);
        when(goalFactory.createGoal(any(), any(), any(), anyInt())).thenReturn(mockGoal);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(goalFactory).createGoal(any(GoalInfo.class), any(BeginAndDueDates.class),
                eq(timePeriod), eq(1));
        verify(goalRepository).save(mockGoal);

        ArgumentCaptor<CreateGoalOutputData> outputCaptor = ArgumentCaptor.forClass(CreateGoalOutputData.class);
        verify(presenter).presentSuccess(outputCaptor.capture());
        assertEquals("Test Goal", outputCaptor.getValue().getGoalName());
    }

    @Test
    void execute_WithTaskTarget_UsesTaskInfo() {
        // Arrange
        Info taskInfo = new Info.Builder("Task").build();
        Task task = new Task(
                "template-id",
                taskInfo,
                new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                false
        );

        CreateGoalInputData inputData = new CreateGoalInputData(
                "Test Goal",
                null,
                100.0,
                0.0,
                LocalDate.now(),
                LocalDate.now().plusWeeks(1),
                Goal.TimePeriod.WEEK,
                1,
                task
        );

        Goal mockGoal = mock(Goal.class);
        GoalInfo mockGoalInfo = mock(GoalInfo.class);
        when(mockGoal.getGoalInfo()).thenReturn(mockGoalInfo);
        when(mockGoalInfo.getInfo()).thenReturn(new Info.Builder("Test Goal").build());
        when(goalFactory.createGoal(any(), any(), any(), anyInt())).thenReturn(mockGoal);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(goalFactory).createGoal(
                argThat(goalInfo -> goalInfo.getTargetTaskInfo().equals(taskInfo)),
                any(),
                any(),
                anyInt()
        );
    }

    @Test
    void execute_WithCurrentAmount_SetsProgress() {
        // Arrange
        CreateGoalInputData inputData = new CreateGoalInputData(
                "Test Goal",
                null,
                100.0,
                50.0,
                LocalDate.now(),
                LocalDate.now().plusWeeks(1),
                Goal.TimePeriod.WEEK,
                1,
                null
        );

        Goal mockGoal = mock(Goal.class);
        GoalInfo mockGoalInfo = mock(GoalInfo.class);
        when(mockGoal.getGoalInfo()).thenReturn(mockGoalInfo);
        when(mockGoalInfo.getInfo()).thenReturn(new Info.Builder("Test Goal").build());
        when(goalFactory.createGoal(any(), any(), any(), anyInt())).thenReturn(mockGoal);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(mockGoal).setCurrentProgress(50);
    }

    @Test
    void execute_WithInvalidParameters_PresentsError() {
        // Arrange
        CreateGoalInputData inputData = new CreateGoalInputData(
                null,  // Invalid name
                null,
                100.0,
                0.0,
                LocalDate.now(),
                LocalDate.now().plusWeeks(1),
                Goal.TimePeriod.WEEK,
                1,
                null
        );

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter).presentError(contains("Invalid goal parameters"));
    }

    @Test
    void execute_WhenRepositoryFails_PresentsError() {
        // Arrange
        CreateGoalInputData inputData = new CreateGoalInputData(
                "Test Goal",
                null,
                100.0,
                0.0,
                LocalDate.now(),
                LocalDate.now().plusWeeks(1),
                Goal.TimePeriod.WEEK,
                1,
                null
        );

        Goal mockGoal = mock(Goal.class);
        GoalInfo mockGoalInfo = mock(GoalInfo.class);
        when(mockGoal.getGoalInfo()).thenReturn(mockGoalInfo);
        when(mockGoalInfo.getInfo()).thenReturn(new Info.Builder("Test Goal").build());
        when(goalFactory.createGoal(any(), any(), any(), anyInt())).thenReturn(mockGoal);
        doThrow(new RuntimeException("DB error")).when(goalRepository).save(any());

        // Act
        interactor.execute(inputData);

        // Assert
        verify(presenter).presentError(contains("Failed to create goal"));
    }
}