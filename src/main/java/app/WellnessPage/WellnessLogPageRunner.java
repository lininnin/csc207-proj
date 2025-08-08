package app.WellnessPage;

import javax.swing.*;

public class WellnessLogPageRunner {
    /**
     * Call this method from your main program to launch the Wellness Log page.
     */
    public void run() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack - Wellness Log");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);

            WellnessLogPageBuilder builder = new WellnessLogPageBuilder();
            JPanel mainPanel = builder.build();

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

