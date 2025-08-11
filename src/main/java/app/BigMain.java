package app;

import app.feedback_panel.CreateGeneratedFeedback;
import app.feedback_panel.FeedbackPageBuilder;
import app.WellnessPage.WellnessLogPageBuilder;
import app.eventPage.EventPageBuilder;
import app.scheduler.WeeklyFeedbackScheduler;
import app.settingsPage.SettingsPageBuilder;
import app.taskPage.TaskPageBuilder;
import data_access.files.FileFeedbackRepository;
import data_access.in_memory_repo.InMemoryDailyLogRepository;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import interface_adapter.generate_feedback.GenerateFeedbackPresenter;
import interface_adapter.gpt.OpenAIAPIAdapter;
import org.jetbrains.annotations.NotNull;
import use_case.generate_feedback.*;
import use_case.repository.DailyLogRepository;
import use_case.repository.FeedbackRepository;
import view.CollapsibleSidebarView;
import view.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class BigMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Feedback generation every week
            FeedbackRepository feedbackRepository = new FileFeedbackRepository();
            feedbackRepository.save(CreateGeneratedFeedback.generateFeedbackEntry());
            WeeklyFeedbackScheduler scheduler = getWeeklyFeedbackScheduler(feedbackRepository);
            scheduler.start();

            JFrame frame = new JFrame("MindTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1250, 800);

            // --- Main Content Area ---
            JPanel centrePanel = new JPanel(new CardLayout());
            JPanel taskPanel = new TaskPageBuilder().build();
            JPanel eventPanel = new EventPageBuilder().build();
            JPanel goalPanel = new GoalPageBuilder().build();
            JPanel wellnessPanel = new WellnessLogPageBuilder().build();
            JPanel feedbackPage = FeedbackPageBuilder.build(feedbackRepository.loadAll());
            JPanel settingPage = new SettingsPageBuilder().build();

            centrePanel.add(taskPanel, "Tasks");
            centrePanel.add(eventPanel, "Events");
            centrePanel.add(goalPanel, "Goals");
            centrePanel.add(wellnessPanel, "WellnessLog");
            centrePanel.add(feedbackPage, "FeedbackPage");
            centrePanel.add(settingPage, "Settings");

            // --- Navigation sidebar ---
            JPanel sideBar = new JPanel();
            sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
            sideBar.setBackground(new Color(60, 63, 65));
            sideBar.setPreferredSize(new Dimension(200, 700));

            String[] menuItems = {
                    "📋 Tasks", "📆 Events", "🎯 Goals",
                    "🧠 Wellness Log", "🤖 AI-Feedback & Analysis", "⚙️ Settings"
            };

            for (String item : menuItems) {
                JButton btn = configureButton(item, centrePanel);

                sideBar.add(btn);
            }

            CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sideBar, centrePanel);

            // --- Layout All ---
            frame.setLayout(new BorderLayout());
            frame.add(collapsibleCenter, BorderLayout.CENTER);

            // Main content by default
            ((CardLayout) centrePanel.getLayout()).show(centrePanel, "Tasks");

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    @NotNull
    private static JButton configureButton(String item, JPanel centrePanel) {
        JButton btn = new JButton(item);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(FontUtil.getStandardFont());

        if (item.contains("Tasks")) {
            btn.setBackground(new Color(45, 47, 49)); // Highlight current page
        } else {
            btn.setBackground(new Color(60, 63, 65));
        }

        // ActionListener for navigation
        btn.addActionListener(e -> {
            CardLayout cl = (CardLayout) centrePanel.getLayout();
            // Map menu text to Card name
            switch (item) {
                case "📋 Tasks" -> cl.show(centrePanel, "Tasks");
                case "📆 Events" -> cl.show(centrePanel, "Events");
                case "🎯 Goals" -> cl.show(centrePanel, "Goals");
                case "🧠 Wellness Log" -> cl.show(centrePanel, "WellnessLog");
                case "🤖 AI-Feedback & Analysis" -> cl.show(centrePanel, "FeedbackPage");
                case "⚙️ Settings" -> cl.show(centrePanel, "Settings");
            }
        });
        return btn;
    }

    @NotNull
    private static WeeklyFeedbackScheduler getWeeklyFeedbackScheduler(FeedbackRepository feedbackRepository) {
        DailyLogRepository dailyLogRepository = new InMemoryDailyLogRepository();
        GPTService analyzer = new OpenAIAPIAdapter();
        FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
        GenerateFeedbackOutputBoundary presenter = new GenerateFeedbackPresenter(viewModel);

        GenerateFeedbackInputBoundary feedbackInputBoundary = new GenerateFeedbackInteractor(
                dailyLogRepository,
                feedbackRepository,
                analyzer,
                presenter
        );
        WeeklyFeedbackScheduler scheduler = new WeeklyFeedbackScheduler(feedbackInputBoundary);
        return scheduler;
    }

}
