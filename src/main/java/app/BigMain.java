//package app;
//
//import app.feedback_panel.CreateGenerateFeedback;
//import app.feedback_panel.FeedbackPageBuilder;
//import app.WellnessPage.WellnessLogPageBuilder;
//import app.eventPage.EventPageBuilder;
//import app.settingsPage.SettingsPageBuilder;
//import app.taskPage.TaskPageBuilder;
//import data_access.files.FileFeedbackRepository;
//import use_case.repository.FeedbackRepository;
//import view.CollapsibleSidebarView;
//import view.FontUtil;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class BigMain {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            FeedbackRepository feedbackRepository = new FileFeedbackRepository();
//            feedbackRepository.save(CreateGenerateFeedback.generateFeedbackEntry());
//
//            JFrame frame = new JFrame("MindTrack");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1250, 800);
//
//            // --- Main Content Area ---
//            JPanel centrePanel = new JPanel(new CardLayout());
//            JPanel taskPanel = new TaskPageBuilder().build();
//            JPanel eventPanel = new EventPageBuilder().build();
//            JPanel wellnessPanel = new WellnessLogPageBuilder().build();
//            JPanel feedbackPage = FeedbackPageBuilder.build(feedbackRepository.loadAll());
//            JPanel settingPage = new SettingsPageBuilder().build();
//
//            centrePanel.add(taskPanel, "Tasks");
//            centrePanel.add(eventPanel, "Events");
//            centrePanel.add(makePlaceholderPanel("Goals"), "Goals");
//            centrePanel.add(wellnessPanel, "WellnessLog");
//            centrePanel.add(feedbackPage, "FeedbackPage");
//            centrePanel.add(settingPage, "Settings");
//
////            // --- Navigation sidebar ---
//            JPanel sideBar = new JPanel();
//            sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
//            sideBar.setBackground(new Color(60, 63, 65));
//            sideBar.setPreferredSize(new Dimension(200, 700));
//
////            JButton btnTasks = new JButton("📋 Tasks");
////            JButton btnEvents = new JButton("📆 Events");
////            JButton btnGoals = new JButton("🎯 Goals");
////            JButton btnWellness = new JButton("🧠 Wellness Log");
////            JButton btnAIAnalysis = new JButton("🤖 AI-Feedback & Analysis");
////            JButton btnSettings = new JButton("⚙️ Settings");
////            sideBar.add(btnTasks);
////            sideBar.add(btnEvents);
////            sideBar.add(btnGoals);
////            sideBar.add(btnWellness);
////            sideBar.add(btnAIAnalysis);
////            sideBar.add(btnSettings);
//
//            String[] menuItems = {
//                    "📋 Tasks", "📆 Events", "🎯 Goals",
//                    "🧠 Wellness Log", "🤖 AI-Feedback & Analysis", "⚙️ Settings"
//            };
//
//            for (String item : menuItems) {
//                JButton btn = new JButton(item);
//                btn.setMaximumSize(new Dimension(200, 40));
//                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
//                btn.setForeground(Color.WHITE);
//                btn.setOpaque(true);
//                btn.setBorderPainted(false);
//                btn.setFont(FontUtil.getStandardFont());
//
//                if (item.contains("Tasks")) {
//                    btn.setBackground(new Color(45, 47, 49)); // Highlight current page
//                } else {
//                    btn.setBackground(new Color(60, 63, 65));
//                }
//
//                // ActionListener for navigation
//                btn.addActionListener(e -> {
//                    CardLayout cl = (CardLayout) centrePanel.getLayout();
//                    // Map menu text to Card name
//                    switch (item) {
//                        case "📋 Tasks" -> cl.show(centrePanel, "Tasks");
//                        case "📆 Events" -> cl.show(centrePanel, "Events");
//                        case "🎯 Goals" -> cl.show(centrePanel, "Goals");
//                        case "🧠 Wellness Log" -> cl.show(centrePanel, "WellnessLog");
//                        case "🤖 AI-Feedback & Analysis" -> cl.show(centrePanel, "FeedbackPage");
//                        case "⚙️ Settings" -> cl.show(centrePanel, "Settings");
//                    }
//                });
//
//                sideBar.add(btn);
//            }
//
//
//            CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sideBar, centrePanel);
//
////            // --- Navigation Button Logic ---
////            btnTasks.addActionListener(e ->  ((CardLayout) centrePanel.getLayout()).show(centrePanel, "Tasks"));
////            btnEvents.addActionListener(e -> ((CardLayout) centrePanel.getLayout()).show(centrePanel, "Events"));
////            btnGoals.addActionListener(e -> ((CardLayout) centrePanel.getLayout()).show(centrePanel, "Goals"));
////            btnWellness.addActionListener(e -> ((CardLayout) centrePanel.getLayout()).show(centrePanel, "WellnessLog"));
////            btnAIAnalysis.addActionListener(e -> ((CardLayout) centrePanel.getLayout()).show(centrePanel, "FeedbackPage"));
////            btnSettings.addActionListener(e -> ((CardLayout) centrePanel.getLayout()).show(centrePanel, "Settings"));
//
////            // --- SplitPane for sidebar
////            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideBar, centrePanel);
////            splitPane.setDividerLocation(200);
////            splitPane.setDividerSize(3);
////            splitPane.setResizeWeight(0.0);
//
//
//            // --- Layout All ---
//            frame.setLayout(new BorderLayout());
//            frame.add(collapsibleCenter, BorderLayout.CENTER);
////            frame.add(centrePanel, BorderLayout.CENTER);
//
//            // Main content by default
//            ((CardLayout) centrePanel.getLayout()).show(centrePanel, "Tasks");
//
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//
//    private static JPanel makePlaceholderPanel(String title) {
//        JPanel panel = new JPanel(new BorderLayout());
//        JLabel label = new JLabel(title, SwingConstants.CENTER);
//        label.setFont(new Font("SansSerif", Font.BOLD, 36));
//        panel.add(label, BorderLayout.CENTER);
//        return panel;
//    }
//}
