package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jetbrains.annotations.NotNull;

import app.WellnessPage.WellnessLogPageBuilder;
import app.eventPage.EventPageBuilder;
import app.feedback_panel.FeedbackPageBuilder;
import app.scheduler.WeeklyFeedbackScheduler;
import app.settingsPage.SettingsPageBuilder;
import app.taskPage.TaskPageBuilder;
import constants.Constants;
import data_access.files.FileFeedbackRepository;
import data_access.in_memory_repo.InMemoryDailyLogRepository;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import interface_adapter.generate_feedback.GenerateFeedbackPresenter;
import interface_adapter.gpt.OpenAiApiAdapter;
import use_case.generate_feedback.GPTService;
import use_case.generate_feedback.GenerateFeedbackInputBoundary;
import use_case.generate_feedback.GenerateFeedbackInteractor;
import use_case.generate_feedback.GenerateFeedbackOutputBoundary;
import use_case.repository.DailyLogRepository;
import use_case.repository.FeedbackRepository;
import view.CollapsibleSidebarView;
import view.FontUtil;

public class BigMain {

    /* Launch MinkTrack application UI.
     * @param args command line arguments not used in application
     */
    public static void main(String[] args) {
        final FeedbackRepository feedbackRepository = new FileFeedbackRepository();
        SwingUtilities.invokeLater(() -> buildGui(feedbackRepository));
    }

    private static void buildGui(FeedbackRepository feedbackRepository) {
        final WeeklyFeedbackScheduler scheduler = getWeeklyFeedbackScheduler(feedbackRepository);
        scheduler.start();

        final JFrame frame = new JFrame("MindTrack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.TWELVE_FIFTY, Constants.EIGHT_HUNDRED);

        // --- Main Content Area ---
        final JPanel centrePanel = new JPanel(new CardLayout());
        final JPanel taskPanel = new TaskPageBuilder().build();
        final JPanel eventPanel = new EventPageBuilder().build();
        final JPanel goalPanel = new GoalPageBuilder().build();
        final JPanel wellnessPanel = new WellnessLogPageBuilder().build();
        final JPanel feedbackPage = FeedbackPageBuilder.build(feedbackRepository.loadAll());
        final JPanel settingPage = new SettingsPageBuilder().build();

        centrePanel.add(taskPanel, "Tasks");
        centrePanel.add(eventPanel, "Events");
        centrePanel.add(goalPanel, "Goals");
        centrePanel.add(wellnessPanel, "WellnessLog");
        centrePanel.add(feedbackPage, "FeedbackPage");
        centrePanel.add(settingPage, "Settings");

        // --- Navigation sidebar ---
        final JPanel sideBar = new JPanel();
        setBar(sideBar);

        final String[] menuItems = {"📋 Tasks", "📆 Events", "🎯 Goals",
                                    "🧠 Wellness Log", "🤖 AI-Feedback & Analysis", "⚙️ Settings"};

        for (String item : menuItems) {
            final JButton btn = configureButton(item, centrePanel);

            sideBar.add(btn);
        }

        final CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sideBar, centrePanel);

        // --- Layout All ---
        frame.setLayout(new BorderLayout());
        frame.add(collapsibleCenter, BorderLayout.CENTER);

        // Main content by default
        ((CardLayout) centrePanel.getLayout()).show(centrePanel, "Tasks");

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void setBar(JPanel sideBar) {
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(new Color(Constants.SIXTY, Constants.SIXTY_THREE, Constants.SIXTY_FIVE));
        sideBar.setPreferredSize(new Dimension(Constants.TWO_HUNDRED, Constants.SEV_HUNDRED));
    }

    @NotNull
    private static JButton configureButton(String item, JPanel centrePanel) {
        final JButton btn = new JButton(item);
        btn.setMaximumSize(new Dimension(Constants.TWO_HUNDRED, Constants.FOURTY));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFont(FontUtil.getStandardFont());

        if (item.contains("Tasks")) {
            btn.setBackground(new Color(Constants.FOURTY_FIVE, Constants.FOURTY_SEV, Constants.FOURTY_NINE));
            // Highlight current page
        }
        else {
            btn.setBackground(new Color(Constants.SIXTY, Constants.SIXTY_THREE, Constants.SIXTY_FIVE));
        }

        // ActionListener for navigation
        btn.addActionListener(view -> menuSelection(item, centrePanel));
        return btn;
    }

    @NotNull
    private static WeeklyFeedbackScheduler getWeeklyFeedbackScheduler(FeedbackRepository feedbackRepository) {
        final DailyLogRepository dailyLogRepository = new InMemoryDailyLogRepository();
        final GPTService analyzer = new OpenAiApiAdapter();
        final FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
        final GenerateFeedbackOutputBoundary presenter = new GenerateFeedbackPresenter(viewModel);

        final GenerateFeedbackInputBoundary feedbackInputBoundary = new GenerateFeedbackInteractor(
                dailyLogRepository,
                feedbackRepository,
                analyzer,
                presenter
        );
        return new WeeklyFeedbackScheduler(feedbackInputBoundary);
    }

    private static void menuSelection(String item, JPanel centrePanel) {
        final CardLayout cl = (CardLayout) centrePanel.getLayout();
        switch (item) {
            case "📋 Tasks" -> cl.show(centrePanel, "Tasks");
            case "📆 Events" -> cl.show(centrePanel, "Events");
            case "🎯 Goals" -> cl.show(centrePanel, "Goals");
            case "🧠 Wellness Log" -> cl.show(centrePanel, "WellnessLog");
            case "🤖 AI-Feedback & Analysis" -> cl.show(centrePanel, "FeedbackPage");
            case "⚙️ Settings" -> cl.show(centrePanel, "Settings");
            default -> {
                // no action needed — all cases are covered
            }
        }
    }
}
