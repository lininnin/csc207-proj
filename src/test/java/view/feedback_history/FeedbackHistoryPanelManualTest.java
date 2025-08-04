package view.feedback_history;

import app.feedback_entry.FeedbackEntryRunner;
import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FeedbackHistoryPanelManualTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a dummy main panel for the runner
            JPanel mainPanel = new JPanel(new CardLayout());
            FeedbackEntryRunner feedbackEntryRunner = new FeedbackEntryRunner(mainPanel) {
                @Override
                public void showFeedbackEntry(FeedbackEntry entry) {
                    // In a real app, swap panel; here just print for demo
                    System.out.println("Show feedback for date: " + entry.getDate());
                }
            };

            // Use a real view model and controller (minimal, as this is a manual test)
            FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
            FeedbackEntry entry1 = new FeedbackEntry(LocalDate.of(2024, 7, 22), "Analysis 1", "Corr 1", "Rec 1");
            FeedbackEntry entry2 = new FeedbackEntry(LocalDate.of(2024, 7, 15), "Analysis 2", "Corr 2", "Rec 2");
            viewModel.setEntries(List.of(entry1, entry2));

            // You can use a no-op controller if you don't need loading logic for manual test
            FeedbackHistoryController controller = new FeedbackHistoryController(null);

            FeedbackHistoryPanel panel = new FeedbackHistoryPanel(
                    controller,
                    viewModel,
                    feedbackEntryRunner
            );

            JFrame frame = new JFrame("Manual Test: Feedback History Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(panel);
            frame.setSize(350, 200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
