package use_case.goalManage.available_goals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;

import use_case.goalManage.available_goals.AvailableGoalsOutputBoundary;
import use_case.goalManage.available_goals.AvailableGoalsOutputData;
import data_access.GoalRepository;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;

/**
 * Test class for AvailableGoalsInteractor.
 * Tests the behavior of the interactor for managing available goals.
 */
class AvailableGoalInteractorTest {

    private GoalRepository goalRepository;
    private use_case.goalManage.available_goals.AvailableGoalsOutputBoundary presenter;
    private AvailableGoalsInteractor interactor;

    @BeforeEach
    void setUp() {
        goalRepository = mock(GoalRepository.class);
        presenter = mock(use_case.goalManage.available_goals.AvailableGoalsOutputBoundary.class);
        interactor = new AvailableGoalsInteractor(goalRepository, presenter);
    }

    @Test
    void execute_WhenCalled_PresentsAllAvailableGoals() {
        // Arrange
        Goal goal1 = createTestGoal("Goal A", Goal.TimePeriod.MONTH);
        Goal goal2 = createTestGoal("Goal B", Goal.TimePeriod.WEEK);
        List<Goal> mockGoals = Arrays.asList(goal1, goal2);

        when(goalRepository.findAvailableGoals()).thenReturn(mockGoals);

        // Act
        interactor.execute();

        // Assert
        verify(presenter).presentAvailableGoals(argThat(outputData ->
                outputData.getAvailableGoals().size() == 2 &&
                        outputData.getAvailableGoals().containsAll(mockGoals)
        ));
    }

    @Test
    void execute_WhenNoGoalsAvailable_PresentsEmptyList() {
        // Arrange
        when(goalRepository.findAvailableGoals()).thenReturn(Collections.emptyList());

        // Act
        interactor.execute();

        // Assert
        verify(presenter).presentAvailableGoals(argThat(outputData ->
                outputData.getAvailableGoals().isEmpty()
        ));
    }

    @Test
    void executeRefreshCommand_WhenCalled_ReloadsAndPresentsGoals() {
        // Arrange
        Goal testGoal = createTestGoal("Test Goal", Goal.TimePeriod.MONTH);
        when(goalRepository.findAvailableGoals()).thenReturn(Collections.singletonList(testGoal));

        // Act
        interactor.execute("refresh");

        // Assert
        verify(goalRepository).findAvailableGoals();
        verify(presenter).presentAvailableGoals(any(use_case.goalManage.available_goals.AvailableGoalsOutputData.class));
    }

    @Test
    void executeInvalidCommand_WhenCalled_DoesNotInteractWithDependencies() {
        // Act
        interactor.execute("invalid_command");

        // Assert
        verifyNoInteractions(goalRepository);
        verifyNoInteractions(presenter);
    }

    private Goal createTestGoal(String name, Goal.TimePeriod timePeriod) {
        // Create valid Info objects for both goal and target task
        Info goalInfo = new Info.Builder(name).build();
        Info taskInfo = new Info.Builder(name + " Task").build(); // Create valid task info

        GoalInfo goalInfoObj = new GoalInfo(goalInfo, taskInfo); // Now both parameters are valid

        return new Goal(
                goalInfoObj,
                new BeginAndDueDates(null, null),
                timePeriod,
                1
        );
    }
}
