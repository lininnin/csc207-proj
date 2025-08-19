package app.goalPage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Main runner class for the Goal Management page of the MindTrack application.
 * Handles the creation, configuration and display of the goal management interface.
 */
public class GoalPageRunner {
    /** Default width of the application window in pixels. */
    private final int width = 1200;

    /** Default height of the application window in pixels. */
    private final int height = 700;

    /**
     * Initializes and displays the Goal Management page.
     * This method schedules the GUI creation on the Event Dispatch Thread.
     */
    public void run() {
        SwingUtilities.invokeLater(this::showGoalManagementFrame);
    }

    /**
     * Creates and configures the main application frame for goal management.
     * Sets up the window properties, content pane, and makes the frame visible.
     */
    private void showGoalManagementFrame() {
        final JFrame frame = new JFrame("MindTrack - Goal Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        final GoalPageBuilder builder = new GoalPageBuilder();
        final JPanel mainPanel = builder.build();

        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Entry point for standalone execution of the Goal Management page.
     * Usage: {@code java app.goalPage.GoalPageRunner}
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        new GoalPageRunner().run();
    }
}
