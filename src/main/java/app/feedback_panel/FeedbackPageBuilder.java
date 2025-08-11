package app.feedback_panel;

import entity.Ina.FeedbackEntry;
import view.feedback_panel.FeedbackCompositePanel;

import javax.swing.*;
import java.util.List;

public class FeedbackPageBuilder {
    private FeedbackPageBuilder() {} // Utility class

    /**
     * Builds the whole feedback page (detail+history, all wired up).
     * @param entries List of feedback entries (newest first)
     */
    public static JPanel build(List<FeedbackEntry> entries) {
        return new FeedbackCompositePanel(entries);
    }
}
