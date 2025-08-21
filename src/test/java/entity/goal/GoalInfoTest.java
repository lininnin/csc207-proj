package entity.goal;

import entity.Sophia.GoalInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import entity.info.Info;

class GoalInfoTest {

    @Test
    void testGoalInfoConstructor_ValidArguments() {
        // Correctly create Info objects using the Builder pattern
        Info goalInfo = new Info.Builder("Goal Info Name").description("Description for goal info.").build();
        Info targetTaskInfo = new Info.Builder("Target Task Name").category("Work").build();

        GoalInfo newGoalInfo = new GoalInfo(goalInfo, targetTaskInfo);

        assertNotNull(newGoalInfo);
        assertEquals(goalInfo, newGoalInfo.getInfo());
        assertEquals(targetTaskInfo, newGoalInfo.getTargetTaskInfo());
    }

    @Test
    void testGoalInfoConstructor_NullArguments() {
        // Create valid Info objects for testing null cases
        Info validInfo = new Info.Builder("Valid Info").build();

        // Test with null for the first argument
        assertThrows(IllegalArgumentException.class, () -> new GoalInfo(null, validInfo));

        // Test with null for the second argument
        assertThrows(IllegalArgumentException.class, () -> new GoalInfo(validInfo, null));
    }

    @Test
    void testGetters() {
        Info goalInfo = new Info.Builder("Goal Info").build();
        Info targetTaskInfo = new Info.Builder("Target Task").build();
        GoalInfo newGoalInfo = new GoalInfo(goalInfo, targetTaskInfo);

        assertEquals(goalInfo, newGoalInfo.getInfo());
        assertEquals(targetTaskInfo, newGoalInfo.getTargetTaskInfo());
    }

    @Test
    void testSetTargetTaskInfo_ValidArgument() {
        Info goalInfo = new Info.Builder("Goal Info").build();
        Info targetTaskInfo = new Info.Builder("Initial Target Task").build();
        GoalInfo newGoalInfo = new GoalInfo(goalInfo, targetTaskInfo);

        Info updatedTargetTaskInfo = new Info.Builder("Updated Target Task").category("Personal").build();
        newGoalInfo.setTargetTaskInfo(updatedTargetTaskInfo);

        assertEquals(updatedTargetTaskInfo, newGoalInfo.getTargetTaskInfo());
    }

    @Test
    void testSetTargetTaskInfo_NullArgument() {
        Info goalInfo = new Info.Builder("Goal Info").build();
        Info targetTaskInfo = new Info.Builder("Initial Target Task").build();
        GoalInfo newGoalInfo = new GoalInfo(goalInfo, targetTaskInfo);

        assertThrows(IllegalArgumentException.class, () -> newGoalInfo.setTargetTaskInfo(null));
    }
}
