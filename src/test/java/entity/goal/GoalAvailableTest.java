package entity.goal;

import entity.Sophia.Goal;
import entity.Sophia.GoalAvaliable;
import entity.Sophia.GoalInfo;
import entity.Sophia.GoalInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

class GoalAvailableTest {

    private GoalInfo goalInfo;

    @BeforeEach
    void setUp() {
        // Setup mock or simple Info object for testing
        Info generalInfo = new Info.Builder("Test Goal").build();
        Info targetInfo = new Info.Builder("Test Task").build();
        goalInfo = new GoalInfo(generalInfo, targetInfo);
    }

    @Test
    void testFilterAvailableGoals() {
        // Create an available goal
        Goal availableGoal = new Goal(
                goalInfo,
                new BeginAndDueDates(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)),
                GoalInterface.TimePeriod.WEEK,
                7
        );

        // Create a completed goal (should not be available)
        Goal completedGoal = new Goal(
                goalInfo,
                new BeginAndDueDates(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)),
                GoalInterface.TimePeriod.WEEK,
                1
        );
        completedGoal.setCompleted(true);

        // Create a future goal (not available yet)
        Goal futureGoal = new Goal(
                goalInfo,
                new BeginAndDueDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(7)),
                GoalInterface.TimePeriod.WEEK,
                7
        );

        // Create a past goal (not available anymore)
        Goal pastGoal = new Goal(
                goalInfo,
                new BeginAndDueDates(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1)),
                GoalInterface.TimePeriod.WEEK,
                7
        );

        // Create the list of all goals to be filtered
        List<Goal> allGoals = new ArrayList<>();
        allGoals.add(availableGoal);
        allGoals.add(completedGoal);
        allGoals.add(futureGoal);
        allGoals.add(pastGoal);

        // Filter the list
        List<Goal> availableGoals = GoalAvaliable.filterAvailableGoals(allGoals);

        // Assert that only the available goal is in the filtered list
        assertEquals(1, availableGoals.size());
        assertEquals(availableGoal, availableGoals.get(0));
    }

    @Test
    void testFilterAvailableGoals_WithNoAvailableGoals() {
        Goal completedGoal = new Goal(goalInfo, new BeginAndDueDates(LocalDate.now(), LocalDate.now()), GoalInterface.TimePeriod.WEEK, 1);
        completedGoal.setCompleted(true);

        List<Goal> allGoals = List.of(completedGoal);
        List<Goal> availableGoals = GoalAvaliable.filterAvailableGoals(allGoals);

        assertTrue(availableGoals.isEmpty());
    }

    @Test
    void testFilterAvailableGoals_WithEmptyList() {
        List<Goal> emptyList = Collections.emptyList();
        List<Goal> result = GoalAvaliable.filterAvailableGoals(emptyList);
        assertTrue(result.isEmpty());
    }
}
