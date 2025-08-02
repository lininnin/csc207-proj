package app.feedback_history;

import entity.Ina.FeedbackEntry;
import use_case.repository.InMemoryFeedbackRepository;

import javax.swing.*;
import java.time.LocalDate;

public class FeedbackHistoryRunner {
    public static void main(String[] args) {
        // Prepare demo data
        InMemoryFeedbackRepository repo = new InMemoryFeedbackRepository();
        repo.save(new FeedbackEntry(LocalDate.of(2024, 7, 22), "Analysis1", "{\"effect_summary\":[],\"notes\":\"Demo\"}", "1. Rec 1\n2. Rec 2"));
        repo.save(new FeedbackEntry(LocalDate.of(2024, 7, 15), "Analysis2", "{\"effect_summary\":[],\"notes\":\"Demo\"}", "1. Rec A\n2. Rec B"));

        // UI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Feedback History Test Host");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.setLocationRelativeTo(null);

            // Set feedback history panel as content pane
            frame.setContentPane(FeedbackHistoryBuilder.build(repo));
            frame.setVisible(true);
        });
    }
}