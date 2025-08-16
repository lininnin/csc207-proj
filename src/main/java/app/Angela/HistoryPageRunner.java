package app.Angela;

import entity.Angela.TodaySoFarSnapshot;
import entity.Angela.Task.Task;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import use_case.Angela.view_history.ViewHistoryDataAccessInterface;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Main runner class for testing the History page.
 */
public class HistoryPageRunner {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create sample data
            createSampleHistoryData();
            
            // Create and show the frame
            JFrame frame = new JFrame("MindTrack - History Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            
            // Add History view
            JPanel historyView = HistoryPageBuilder.createHistoryView();
            frame.add(historyView);
            
            // Add menu bar with test options
            JMenuBar menuBar = createMenuBar();
            frame.setJMenuBar(menuBar);
            
            // Center the frame
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    private static void createSampleHistoryData() {
        ViewHistoryDataAccessInterface dataAccess = HistoryPageBuilder.getHistoryDataAccess();
        
        // Create sample data for the past week
        for (int i = 7; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            
            // Create sample tasks
            Info taskInfo1 = new Info.Builder("Complete project report")
                .category("Work")
                .description("Finish the quarterly report")
                .build();
            Task task1 = new Task(UUID.randomUUID().toString(), taskInfo1, 
                new BeginAndDueDates(date, date.plusDays(2)), false);
            task1.markComplete(); // Mark as completed
            
            Info taskInfo2 = new Info.Builder("Team meeting")
                .category("Work")
                .description("Weekly sync with team")
                .build();
            Task task2 = new Task(UUID.randomUUID().toString(), taskInfo2,
                new BeginAndDueDates(date, date), false);
            if (i % 2 == 0) {
                task2.markComplete(); // Completed every other day
            }
            
            Info taskInfo3 = new Info.Builder("Exercise")
                .category("Health")
                .description("30 minutes workout")
                .build();
            Task task3 = new Task(UUID.randomUUID().toString(), taskInfo3,
                new BeginAndDueDates(date, date), false);
            if (i < 5) {
                task3.markComplete(); // Completed last 5 days
            }
            
            List<Task> todaysTasks = Arrays.asList(task1, task2, task3);
            List<Task> completedTasks = todaysTasks.stream()
                .filter(Task::isCompleted)
                .toList();
            
            // Create sample events
            Info event1 = new Info.Builder("Sunny weather")
                .category("Weather")
                .description("Nice sunny day")
                .build();
            
            Info event2 = new Info.Builder("Client call")
                .category("Work")
                .description("Important client discussion")
                .build();
            
            List<Info> events = Arrays.asList(event1, event2);
            
            // Create sample goal progress
            TodaySoFarSnapshot.GoalProgress goal1 = new TodaySoFarSnapshot.GoalProgress(
                UUID.randomUUID().toString(),
                "Complete 5 workouts",
                "Weekly",
                Math.min(i, 5),
                5
            );
            
            TodaySoFarSnapshot.GoalProgress goal2 = new TodaySoFarSnapshot.GoalProgress(
                UUID.randomUUID().toString(),
                "Read 2 books",
                "Monthly",
                i / 4,
                2
            );
            
            List<TodaySoFarSnapshot.GoalProgress> goalProgress = Arrays.asList(goal1, goal2);
            
            // Calculate completion rate
            int completionRate = (completedTasks.size() * 100) / todaysTasks.size();
            
            // Create and save snapshot
            TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                date,
                todaysTasks,
                completedTasks,
                completionRate,
                List.of(), // No overdue tasks in sample
                events,
                goalProgress,
                List.of()  // No wellness entries in sample
            );
            
            dataAccess.saveSnapshot(snapshot);
        }
        
        System.out.println("Sample history data created for the past 8 days");
    }
    
    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        menuBar.add(fileMenu);
        
        // Test menu
        JMenu testMenu = new JMenu("Test");
        
        JMenuItem saveTodayItem = new JMenuItem("Save Today's Snapshot");
        saveTodayItem.addActionListener(e -> {
            DailySnapshotService.getInstance().saveCurrentDaySnapshot();
            JOptionPane.showMessageDialog(null, "Today's snapshot saved!");
        });
        testMenu.add(saveTodayItem);
        
        JMenuItem clearHistoryItem = new JMenuItem("Clear All History");
        clearHistoryItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to clear all history?",
                "Confirm Clear", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                HistoryPageBuilder.resetHistory();
                JOptionPane.showMessageDialog(null, "History cleared!");
            }
        });
        testMenu.add(clearHistoryItem);
        
        JMenuItem regenerateItem = new JMenuItem("Regenerate Sample Data");
        regenerateItem.addActionListener(e -> {
            HistoryPageBuilder.resetHistory();
            createSampleHistoryData();
            JOptionPane.showMessageDialog(null, "Sample data regenerated!");
        });
        testMenu.add(regenerateItem);
        
        menuBar.add(testMenu);
        
        return menuBar;
    }
}