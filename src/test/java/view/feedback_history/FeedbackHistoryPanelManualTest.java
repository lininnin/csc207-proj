package view.feedback_history;

import entity.feedback_entry.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import view.feedback_panel.FeedbackHistoryPanel;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FeedbackHistoryPanelManualTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create a dummy main panel for the runner
            JPanel mainPanel = new JPanel(new CardLayout());

            // Use a real view model and controller (minimal, as this is a manual test)
            FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
            FeedbackEntry entry1 = new FeedbackEntry(LocalDate.of(2024, 7, 22), "Analysis 1", "Corr 1", "Rec 1");
            FeedbackEntry entry2 = new FeedbackEntry(LocalDate.of(2024, 7, 15), "Analysis 2", "Corr 2", "Rec 2");
            viewModel.setEntries(List.of(entry1, entry2));

            FeedbackHistoryPanel panel = new FeedbackHistoryPanel(
                    viewModel,
                    entry -> System.out.println("Clicked: " + entry.getDate())
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
