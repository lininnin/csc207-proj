package view.feedback_entry;

import entity.Ina.FeedbackEntry;

import javax.swing.*;
import java.time.LocalDate;

public class FeedbackEntryPanelManualTest {
    public static void main(String[] args) {
        // JSON matches your documented expected structure!
        String correlationJson =
                "{ \"effect_summary\": [" +
                        " { \"variable\": \"stress\", \"direction\": \"negative\", \"confidence\": 0.78 }," +
                        " { \"variable\": \"energy\", \"direction\": \"positive\", \"confidence\": 0.60 }," +
                        " { \"variable\": \"fatigue\", \"direction\": \"none\", \"confidence\": 0.50 }" +
                        " ]," +
                        " \"notes\": \"Fatigue was mostly neutral.\" }";

        FeedbackEntry entry = new FeedbackEntry(
                LocalDate.of(2024, 7, 22),
                "Example analysis: User showed improved consistency.",
                correlationJson,
                "1. Keep tracking tasks. \n" +
                        "2. Try a walk. \n" +
                        "3. Review priorities."
        );

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Manual Test: Feedback Entry Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new FeedbackEntryPanel(entry));
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
