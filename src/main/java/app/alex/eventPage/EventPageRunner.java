package app.alex.eventPage;

import javax.swing.*;

public class EventPageRunner {
    /**
     * Call this method from your main program to launch the Event page.
     */
    public void run() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack - Event Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);

            EventPageBuilder builder = new EventPageBuilder();
            JPanel mainPanel = builder.build();

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
