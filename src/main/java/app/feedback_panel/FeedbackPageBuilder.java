package app.feedback_panel;

import java.util.List;

import javax.swing.JPanel;

import entity.Ina.FeedbackEntry;
import view.feedback_panel.FeedbackCompositePanel;

// style checked
public final class FeedbackPageBuilder {
    private FeedbackPageBuilder() {

    }
    /**
     * Builds the whole feedback page (detail+history, all wired up).
     * @param entries List of feedback entries (newest first)
     * @return A panel that consists of entry panel + history panel
     */

    public static JPanel build(final List<FeedbackEntry> entries) {
        // final list - reference as fixed during call
        return new FeedbackCompositePanel(entries);
    }
}
