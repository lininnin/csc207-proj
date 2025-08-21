package entity;

import entity.Sophia.GoalInfo;
import entity.info.Info;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoalInfoTest {

    @Test
    void testConstructorValidInput() {
        Info goalInfo = new Info.Builder("Goal Name")
                .category("Goal Category")
                .description("Goal Description")
                .build();

        Info taskInfo = new Info.Builder("Task Name")
                .category("Task Category")
                .description("Task Description")
                .build();

        GoalInfo goal = new GoalInfo(goalInfo, taskInfo);

        assertEquals(goalInfo, goal.getInfo());
        assertEquals(taskInfo, goal.getTargetTaskInfo());
    }

    @Test
    void testConstructorNullGoalInfoThrows() {
        Info taskInfo = new Info.Builder("Task Name")
                .category("Task Category")
                .description("Task Description")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoalInfo(null, taskInfo));
        assertEquals("Goal info cannot be null.", exception.getMessage());
    }

    @Test
    void testConstructorNullTargetInfoThrows() {
        Info goalInfo = new Info.Builder("Goal Name")
                .category("Goal Category")
                .description("Goal Description")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new GoalInfo(goalInfo, null));
        assertEquals("Target task info cannot be null.", exception.getMessage());
    }

    @Test
    void testSetTargetTaskInfoValid() {
        Info goalInfo = new Info.Builder("Goal Name")
                .category("Goal Category")
                .description("Goal Description")
                .build();

        Info originalTask = new Info.Builder("Task A")
                .category("Work")
                .description("Original Task")
                .build();

        Info newTask = new Info.Builder("Task B")
                .category("Work")
                .description("Updated Task")
                .build();

        GoalInfo goal = new GoalInfo(goalInfo, originalTask);
        goal.setTargetTaskInfo(newTask);

        assertEquals(newTask, goal.getTargetTaskInfo());
    }

    @Test
    void testSetTargetTaskInfoNullThrows() {
        Info goalInfo = new Info.Builder("Goal Name")
                .category("Goal Category")
                .description("Goal Description")
                .build();

        Info taskInfo = new Info.Builder("Task Name")
                .category("Task Category")
                .description("Task Description")
                .build();

        GoalInfo goal = new GoalInfo(goalInfo, taskInfo);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                goal.setTargetTaskInfo(null));
        assertEquals("Target task info cannot be null.", exception.getMessage());
    }
}


