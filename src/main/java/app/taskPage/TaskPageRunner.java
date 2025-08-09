package app.taskPage;

import app.taskPage.TaskPageBuilder;

import javax.swing.*;

/**
 * Runner for the Task page following the same pattern as EventPageRunner.
 */
public class TaskPageRunner {
    /**
     * Call this method from your main program to launch the Task page.
     */
    public void run() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack - Task Manager");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            TaskPageBuilder builder = new TaskPageBuilder();
            JPanel mainPanel = builder.build();
            
            frame.setContentPane(mainPanel);
            frame.pack();  // Use pack() to size the frame based on content
            frame.setSize(1450, 750);  // Then set to our desired size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
