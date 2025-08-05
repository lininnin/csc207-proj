package app;

import app.FeedbackPanel.CreateGenerateFeedback;
import app.FeedbackPanel.FeedbackPageBuilder;
import data_access.in_memory_repo.InMemoryFeedbackRepository;

import javax.swing.*;
import java.awt.*;

public class BigMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1250, 800);

            // --- Navigation sidebar ---
            JPanel navigator = new JPanel();
            navigator.setLayout(new BoxLayout(navigator, BoxLayout.Y_AXIS));
            JButton btnTasks = new JButton("📋 Tasks");
            JButton btnEvents = new JButton("📆 Events");
            JButton btnGoals = new JButton("🎯 Goals");
            JButton btnWellness = new JButton("🧠 Wellness Log");
            JButton btnAIAnalysis = new JButton("🤖 AI-Feedback & Analysis");
            JButton btnSettings = new JButton("⚙️ Settings");
            navigator.add(btnTasks);
            navigator.add(btnEvents);
            navigator.add(btnGoals);
            navigator.add(btnWellness);
            navigator.add(btnAIAnalysis);
            navigator.add(btnSettings);

            // --- Demo feedback Repo
            InMemoryFeedbackRepository repo = new InMemoryFeedbackRepository();
            repo.save(CreateGenerateFeedback.generateFeedbackEntry());

            // --- Main Content Area ---
            JPanel mainPanel = new JPanel(new CardLayout());
            JPanel eventPanel = new EventPageBuilder().build();
            JPanel wellnessPanel = new WellnessLogPageBuilder().build();
            JPanel feedbackPage = FeedbackPageBuilder.build(repo.loadAll());

            mainPanel.add(makePlaceholderPanel("Tasks"), "Tasks");
            mainPanel.add(eventPanel, "Events");
            mainPanel.add(makePlaceholderPanel("Goals"), "Goals");
            mainPanel.add(wellnessPanel, "WellnessLog");
            mainPanel.add(feedbackPage, "FeedbackPage"); // Renamed for clarity
            mainPanel.add(makePlaceholderPanel("Settings"), "Settings");

            // --- Navigation Button Logic ---
            btnTasks.addActionListener(e ->  ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Tasks"));
            btnEvents.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Events"));
            btnGoals.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Goals"));
            btnWellness.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "WellnessLog"));
            btnAIAnalysis.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "FeedbackPage"));
            btnSettings.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Settings"));

            // --- Layout All ---
            frame.setLayout(new BorderLayout());
            frame.add(navigator, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);

            // Show main content by default
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Events");

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static JPanel makePlaceholderPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 36));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
