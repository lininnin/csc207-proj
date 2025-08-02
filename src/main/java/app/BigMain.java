package app;

import app.feedback_history.FeedbackHistoryBuilder;
import entity.Ina.FeedbackEntry;
import data_access.in_memory_repo.InMemoryFeedbackRepository;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class BigMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);

            // --- Navigation sidebar ---
            JPanel navigator = new JPanel();
            navigator.setLayout(new BoxLayout(navigator, BoxLayout.Y_AXIS));
            JButton btnTasks = new JButton("📋 Tasks");
            JButton btnEvents = new JButton("📆 Events");
            JButton btnGoals = new JButton("🎯 Goals");
            JButton btnWellness = new JButton("🧠 Wellness Log");
            // JButton btnCharts = new JButton("📊 Charts");
            JButton btnAIAnalysis = new JButton("🤖 Wellness Analysis History");
            JButton btnSettings = new JButton("⚙️ Settings");
            navigator.add(btnTasks);
            navigator.add(btnEvents);
            navigator.add(btnGoals);
            navigator.add(btnWellness);
            navigator.add(btnAIAnalysis);
            navigator.add(btnSettings);

            // --- Demo feedback Repo
            InMemoryFeedbackRepository repo = new InMemoryFeedbackRepository();
            repo.save(new FeedbackEntry(LocalDate.of(2024, 7, 22), "Analysis1",
                    "{\"effect_summary\":[],\"notes\":\"Demo\"}", "1. Rec 1\n2. Rec 2"));
            repo.save(new FeedbackEntry(LocalDate.of(2024, 7, 15), "Analysis2",
                    "{\"effect_summary\":[],\"notes\":\"Demo\"}", "1. Rec A\n2. Rec B"));

            // --- Main Content Area ---
            JPanel mainPanel = new JPanel(new CardLayout());
            JPanel eventPanel = new EventPageBuilder().build();
            JPanel wellnessPanel = new WellnessLogPageBuilder().build();
            mainPanel.add(makePlaceholderPanel("Tasks"), "Tasks");
            mainPanel.add(eventPanel, "Events");
            mainPanel.add(makePlaceholderPanel("Goals"), "Goals");
            mainPanel.add(wellnessPanel, "WellnessLog");
            mainPanel.add(makePlaceholderPanel("Settings"), "Settings");

            // --- Feedback sidebar panel ---
            JPanel feedbackSidebar = FeedbackHistoryBuilder.build(repo);
            feedbackSidebar.setPreferredSize(new Dimension(400, 800));
            CollapsibleSidebarView collapsibleSidebarView = new CollapsibleSidebarView(feedbackSidebar, mainPanel);

            // --- Split Pane: left = mainPanel, right = feedbackSidebar ---
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, feedbackSidebar);
            splitPane.setDividerLocation(900); // Start with sidebar hidden
            splitPane.setResizeWeight(1.0);    // Main content expands
            feedbackSidebar.setVisible(false);
            splitPane.setDividerSize(0); // No thick line


            // close button to sidebar (top right)
            JButton closeBtn = new JButton("X");
            closeBtn.setFocusable(false);
            closeBtn.addActionListener(e -> {
                feedbackSidebar.setVisible(false);
                splitPane.setDividerLocation(1.0); // Hide sidebar (main takes all)
            });
            JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            closePanel.add(closeBtn);
            feedbackSidebar.add(closePanel, BorderLayout.NORTH);

            // --- Navigation Button Logic ---
            btnTasks.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Tasks"));
            btnEvents.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Events"));
            btnGoals.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Goals"));
            btnWellness.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "WellnessLog"));
            btnAIAnalysis.addActionListener(e -> {
                splitPane.setRightComponent(feedbackSidebar);
                splitPane.setDividerLocation(0.75);
                feedbackSidebar.setVisible(true);
            });
            btnSettings.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Settings"));

            // --- Layout All ---
            frame.setLayout(new BorderLayout());
            frame.add(navigator, BorderLayout.WEST);
            frame.add(splitPane, BorderLayout.CENTER);

            CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(navigator, mainPanel);
            frame.add(collapsibleCenter);

            // Show main content by default
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Events");
            feedbackSidebar.setVisible(false);
            splitPane.setDividerLocation(1.0); // Hide sidebar at start

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
