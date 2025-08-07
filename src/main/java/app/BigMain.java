package app;

import app.FeedbackPanel.CreateGenerateFeedback;
import app.FeedbackPanel.FeedbackPageBuilder;
import data_access.in_memory_repo.InMemoryFeedbackRepository;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;

public class BigMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1250, 800);

            // --- Navigation sidebar ---
            JPanel sideBar = new JPanel();
            sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
            sideBar.setBackground(new Color(60, 63, 65));
            sideBar.setPreferredSize(new Dimension(200, 700));
            JButton btnTasks = new JButton("📋 Tasks");
            JButton btnEvents = new JButton("📆 Events");
            JButton btnGoals = new JButton("🎯 Goals");
            JButton btnWellness = new JButton("🧠 Wellness Log");
            JButton btnAIAnalysis = new JButton("🤖 AI-Feedback & Analysis");
            JButton btnSettings = new JButton("⚙️ Settings");
            sideBar.add(btnTasks);
            sideBar.add(btnEvents);
            sideBar.add(btnGoals);
            sideBar.add(btnWellness);
            sideBar.add(btnAIAnalysis);
            sideBar.add(btnSettings);

            // --- Demo feedback Repo
            InMemoryFeedbackRepository repo = new InMemoryFeedbackRepository();
            repo.save(CreateGenerateFeedback.generateFeedbackEntry());

            // --- Main Content Area ---
            JPanel mainPanel = new JPanel(new CardLayout());
            JPanel eventPanel = new EventPageBuilder().build();
            JPanel wellnessPanel = new WellnessLogPageBuilder().build();
            JPanel feedbackPage = FeedbackPageBuilder.build(repo.loadAll());
            JPanel settingPage = new SettingsPageBuilder().build();

            mainPanel.add(makePlaceholderPanel("Tasks"), "Tasks");
            mainPanel.add(eventPanel, "Events");
            mainPanel.add(makePlaceholderPanel("Goals"), "Goals");
            mainPanel.add(wellnessPanel, "WellnessLog");
            mainPanel.add(feedbackPage, "FeedbackPage"); // Renamed for clarity
            mainPanel.add(settingPage, "Settings");

            // --- Navigation Button Logic ---
            btnTasks.addActionListener(e ->  ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Tasks"));
            btnEvents.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Events"));
            btnGoals.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Goals"));
            btnWellness.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "WellnessLog"));
            btnAIAnalysis.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "FeedbackPage"));
            btnSettings.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Settings"));

            // --- SplitPane for sidebar
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideBar, mainPanel);
            splitPane.setDividerLocation(200);
            splitPane.setDividerSize(3);
            splitPane.setResizeWeight(0.0);

            CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sideBar, mainPanel);

            // --- Layout All ---
            frame.setLayout(new BorderLayout());
            frame.add(collapsibleCenter, BorderLayout.WEST);
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
