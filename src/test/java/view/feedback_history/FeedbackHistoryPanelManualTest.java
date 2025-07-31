package view.feedback_history;

import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class FeedbackHistoryPanelManualTest {
    static class StubController extends FeedbackHistoryController {
        public StubController() { super(null); }
        @Override public void loadFeedbackHistory() {/* Placeholder */}
    }

    public static void main(String[] args) {
        System.out.println("MAIN STARTED");
        SwingUtilities.invokeLater(() -> {
            FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
            FeedbackEntry entry1 = new FeedbackEntry(LocalDate.of(2024, 7, 22), "Analysis 1", "Corr 1", "Rec 1");
            FeedbackEntry entry2 = new FeedbackEntry(LocalDate.of(2024, 7, 15), "Analysis 2", "Corr 2", "Rec 2");
            viewModel.setEntries(List.of(entry1, entry2));

            FeedbackHistoryPanel panel = new FeedbackHistoryPanel(new StubController(), viewModel);

            JFrame frame = new JFrame("Manual Test: Feedback History Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(panel);
            frame.setSize(300, 1200);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
