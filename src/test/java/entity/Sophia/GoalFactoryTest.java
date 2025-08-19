package entity.Sophia;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import java.time.LocalDate;

class GoalFactoryTest {

    @Test
    void testCreateGoal() {
        // Create necessary dependencies using the Builder pattern for Info
        Info goalGeneralInfo = new Info.Builder("Study for exam").description("Study for the final exam in history.").build();
        Info targetTaskInfo = new Info.Builder("Complete chapter reading").build();
        GoalInfo goalInfo = new GoalInfo(goalGeneralInfo, targetTaskInfo);
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusWeeks(2));
        GoalFactory factory = new GoalFactory();

        // Use the factory to create a Goal instance
        Goal newGoal = factory.createGoal(
                goalInfo,
                dates,
                GoalInterface.TimePeriod.MONTH,
                10
        );

        // Assert that the created Goal is not null and its attributes are correctly set
        assertNotNull(newGoal);
        assertEquals(goalInfo, newGoal.getGoalInfo());
        assertEquals(dates, newGoal.getBeginAndDueDates());
        assertEquals(GoalInterface.TimePeriod.MONTH, newGoal.getTimePeriod());
        assertEquals(10, newGoal.getFrequency());
        assertEquals(0, newGoal.getCurrentProgress());
        assertFalse(newGoal.isCompleted());
    }
}
