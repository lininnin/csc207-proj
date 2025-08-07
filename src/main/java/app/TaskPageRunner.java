package app;

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
            frame.setSize(1200, 700);

            TaskPageBuilder builder = new TaskPageBuilder();
            JPanel mainPanel = builder.build();

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}