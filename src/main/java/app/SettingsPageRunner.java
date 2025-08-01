package app;

import javax.swing.*;

public class SettingsPageRunner {
    /**
     * Call this method from your main program to launch the Settings page.
     */
    public void run() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack - Settings Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);

            SettingsPageBuilder builder = new SettingsPageBuilder();  // ✅ 修改名称
            JPanel mainPanel = builder.build();

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

