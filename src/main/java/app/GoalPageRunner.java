package app;

import javax.swing.*;

public class GoalPageRunner {  // Fixed: Class name should be PascalCase
    /**
     * Call this method from your main program to launch the Goal Management page.
     */
    public void run() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack - Goal Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);

            GoalPageBuilder builder = new GoalPageBuilder();  // Fixed: Direct reference
            JPanel mainPanel = builder.build();

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        new GoalPageRunner().run();
    }
}
