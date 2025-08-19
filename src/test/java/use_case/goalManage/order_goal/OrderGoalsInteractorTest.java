package use_case.goalManage.order_goal;

import data_access.GoalRepository;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.goalManage.available_goals.available_goals.AvailableGoalsOutputBoundary;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class OrderGoalsInteractorTest {
    private GoalRepository goalRepository;
    private OrderGoalsOutputBoundary presenter;
    private AvailableGoalsOutputBoundary availableGoalsPresenter;
    private OrderGoalsInteractor interactor;

    @BeforeEach
    void setUp() {
        goalRepository = mock(GoalRepository.class);
        presenter = mock(OrderGoalsOutputBoundary.class);
        availableGoalsPresenter = mock(AvailableGoalsOutputBoundary.class);
        interactor = new OrderGoalsInteractor(goalRepository, presenter, availableGoalsPresenter);
    }

    @Test
    void execute_OrdersByNameCorrectly() {
        // Arrange
        Goal goalA = createTestGoal("A", LocalDate.now().plusDays(2), Goal.TimePeriod.WEEK);
        Goal goalB = createTestGoal("B", LocalDate.now().plusDays(1), Goal.TimePeriod.MONTH);
        List<Goal> goals = Arrays.asList(goalB, goalA);
        when(goalRepository.findAvailableGoals()).thenReturn(goals);

        OrderGoalsInputData inputData = new OrderGoalsInputData("name", false);

        // Act
        interactor.execute(inputData);

        // Assert
        verify(availableGoalsPresenter).presentAvailableGoals(argThat(output ->
                output.getAvailableGoals().get(0).getGoalInfo().getInfo().getName().equals("A")));
    }

    private Goal createTestGoal(String name, LocalDate dueDate, Goal.TimePeriod timePeriod) {
        GoalInfo goalInfo = new GoalInfo(new Info.Builder(name).build(), new Info.Builder("Task").build());
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);
        return new Goal(goalInfo, dates, timePeriod, 1);
    }
}