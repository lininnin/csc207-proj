package app.feedback_panel;

import data_access.files.FileFeedbackRepository;
import entity.Ina.FeedbackEntry;
import use_case.repository.FeedbackRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FeedbackPageRunner {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Feedback Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);

            // Load real data from your cache
            FeedbackRepository repo = new FileFeedbackRepository();
            List<FeedbackEntry> entries = repo.loadAll(); // assume newest-first

            frame.setLayout(new BorderLayout());
            frame.add(FeedbackPageBuilder.build(entries), BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
