package sampleGoalPage;

import entity.BeginAndDueDates;
import entity.Goal;
import entity.Info;
import entity.Task;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.awt.*;

public class Main2 {

    public static void main(String[] args) {
        // Setup domain objects
        Info info = new Info("GOAL", "UI Test Goal", "Testing");
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(7));
        Task task = new Task(info, dates, Task.Priority.MEDIUM);
        Goal goal = new Goal(info, dates, task, Goal.TimePeriod.WEEK, 1);

        SwingUtilities.invokeLater(() -> {
            GoalTrackerUI ui = new GoalTrackerUI(task, goal);
            ui.setVisible(true);
        });
    }

    // Inner static class for UI window
    public static class GoalTrackerUI extends JFrame {
        private final JLabel progressLabel;
        private final JButton completeTaskButton;

        private final Task task;
        private final Goal goal;

        public GoalTrackerUI(Task task, Goal goal) {
            this.task = task;
            this.goal = goal;

            setTitle("Goal Tracker");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new FlowLayout());

            progressLabel = new JLabel(goal.getGoalStatus());
            completeTaskButton = new JButton("Complete Task");

            add(progressLabel);
            add(completeTaskButton);

            // Listener updates label when task completes
            task.addTaskCompleteListener((completedTask, completionTime) -> {
                SwingUtilities.invokeLater(() -> {
                    progressLabel.setText(goal.getGoalStatus());
                    if (goal.isGoalAchieved()) {
                        JOptionPane.showMessageDialog(this, "🎯 Goal achieved!");
                    }
                });
            });

            completeTaskButton.addActionListener(e -> {
                if (!task.isComplete()) {
                    task.completeTask(LocalDateTime.now());
                } else {
                    JOptionPane.showMessageDialog(this, "Task already completed.");
                }
            });
        }
    }
}
