package app;

import app.EventPageBuilder;
import app.WellnessLogPageBuilder;
import app.feedback_history.FeedbackHistoryBuilder;
import entity.Ina.FeedbackEntry;
import use_case.repository.InMemoryFeedbackRepository;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class BigMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);

            // --- Navigation side panel
            JPanel navi = new JPanel();
            navi.setLayout(new BoxLayout(navi, BoxLayout.Y_AXIS));
            JButton btnTasks = new JButton("📋 Tasks");
            JButton btnEvents = new JButton("📆 Events");
            JButton btnGoals = new JButton("🎯 Goals");
            JButton btnWellness = new JButton("🧠 Wellness Log");
            // JButton btnCharts = new JButton("📊 Charts");
            JButton btnAIAnalysis = new JButton("🤖 AI-Feedback & Analysis");
            JButton btnSettings = new JButton("⚙️ Settings");

            // --- Feedback repo with demo data ---
            InMemoryFeedbackRepository repo = new InMemoryFeedbackRepository();
            repo.save(new FeedbackEntry(LocalDate.of(2024, 7, 22), "Analysis1", "{\"effect_summary\":[],\"notes\":\"Demo\"}", "1. Rec 1\n2. Rec 2"));
            repo.save(new FeedbackEntry(LocalDate.of(2024, 7, 15), "Analysis2", "{\"effect_summary\":[],\"notes\":\"Demo\"}", "1. Rec A\n2. Rec B"));

            // --- Main Content Area (CardLayout) ---
            JPanel mainPanel = new JPanel(new CardLayout());
            JPanel eventPanel = new EventPageBuilder().build();
            JPanel wellnessPanel = new WellnessLogPageBuilder().build();
            mainPanel.add(eventPanel, "Events");
            mainPanel.add(wellnessPanel, "WellnessLog");

            // --- Feedback sidebar panel ---
            JPanel feedbackSidebar = FeedbackHistoryBuilder.build(repo);
            feedbackSidebar.setMinimumSize(new Dimension(300, 800));
            feedbackSidebar.setPreferredSize(new Dimension(400, 800));

            // --- Split Pane: left = mainPanel, right = feedbackSidebar ---
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mainPanel, feedbackSidebar);
            splitPane.setDividerLocation(900); // Start with sidebar hidden
            splitPane.setResizeWeight(1.0);    // Main content expands
            feedbackSidebar.setVisible(false);
            splitPane.setDividerSize(0); // No thick line

            // --- Navigation Button Logic ---
            btnTasks.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Tasks"));
            btnEvents.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Events"));
            btnGoals.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Goals"));
            btnWellness.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "WellnessLog"));
            btnAIAnalysis.addActionListener(e -> {feedbackSidebar.setVisible(true);splitPane.setDividerLocation(800); // Show sidebar
                 });
            btnSettings.addActionListener(e -> ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Settings"));

            // Close button to sidebar (top right)
            JButton closeBtn = new JButton("X");
            closeBtn.setFocusable(false);
            closeBtn.addActionListener(e -> {
                feedbackSidebar.setVisible(false);
                splitPane.setDividerLocation(1.0); // Hide sidebar (main takes all)
            });
            JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            closePanel.add(closeBtn);
            feedbackSidebar.add(closePanel, BorderLayout.NORTH);

            // --- Layout All ---
            frame.setLayout(new BorderLayout());
            frame.add(navi, BorderLayout.WEST);
            frame.add(splitPane, BorderLayout.CENTER);

            // Show main content by default
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, "Events");
            feedbackSidebar.setVisible(false);
            splitPane.setDividerLocation(1.0); // Hide sidebar at start

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
