package app.feedback_panel;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import constants.Constants;
import data_access.files.FileFeedbackRepository;
import entity.feedback_entry.FeedbackEntry;
import use_case.repository.FeedbackRepository;

/**
 * Runner that launches the Feedback page UI for the app.
 * Loads feedback entries from the file-based repository and displays them.
 */
public class FeedbackPageRunner {

    /**
     * Application entry point for launching the standalone Feedback page.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FeedbackPageRunner::createAndShowUi);
    }

    /** Creates the frame and shows the feedback page. */
    private static void createAndShowUi() {
        final JFrame frame = new JFrame("Feedback Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Constants.NINE_HUNDRED, Constants.SIX_HUNDRED);
        frame.setLocationRelativeTo(null);

        // Load real data from your cache (newest-first assumed by repo)
        final FeedbackRepository repo = new FileFeedbackRepository();
        final List<FeedbackEntry> entries = repo.loadAll();

        frame.setLayout(new BorderLayout());
        frame.add(FeedbackPageBuilder.build(entries), BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
