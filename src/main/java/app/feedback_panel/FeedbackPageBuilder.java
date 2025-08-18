package app.feedback_panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import constants.Constants;
import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryPresenter;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import use_case.feedback_history.FeedbackHistoryInputBoundary;
import use_case.feedback_history.FeedbackHistoryInteractor;
import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.repository.FeedbackRepository;
import view.feedback_panel.FeedbackEntryPanel;
import view.feedback_panel.FeedbackHistoryPanel;

/**
 * Builds the Feedback page (detail + history) with full CA wiring.
 * This lives in the app layer, so it's a fine place to compose dependencies.
 */
public final class FeedbackPageBuilder {

    private final FeedbackRepository repo;

    /** Prefer injecting the repo so tests can pass an in-memory one. */
    public FeedbackPageBuilder(FeedbackRepository repo) {
        this.repo = repo;
    }

    /** Build and return the composed feedback page panel. */
    public JPanel build() {
        // --- ViewModel
        final FeedbackHistoryViewModel historyVm = new FeedbackHistoryViewModel();

        // --- Presenter & Interactor
        final FeedbackHistoryOutputBoundary presenter = new FeedbackHistoryPresenter(historyVm);
        final FeedbackHistoryInputBoundary interactor = new FeedbackHistoryInteractor(repo, presenter);

        // --- Controller
        final FeedbackHistoryController controller = new FeedbackHistoryController(interactor);

        // --- Views
        final FeedbackEntryPanel entryPanel = new FeedbackEntryPanel();
        final FeedbackHistoryPanel historyPanel = new FeedbackHistoryPanel(
                historyVm,
                entryPanel::displayEntry
        );
        historyPanel.setPreferredSize(new Dimension(Constants.TWO_SIXTY, 0));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(Constants.EIGHT, Constants.EIGHT, Constants.EIGHT, Constants.EIGHT));

        // --- Compose page
        final JPanel page = new JPanel(new BorderLayout(Constants.SIXTEEN, 0));
        page.add(entryPanel, BorderLayout.CENTER);
        page.add(historyPanel, BorderLayout.EAST);

        // --- Initial load + select newest if available
        controller.loadFeedbackHistory();
        if (historyVm.getEntries() != null && !historyVm.getEntries().isEmpty()) {
            entryPanel.displayEntry(historyVm.getEntries().get(0));
        }

        return page;
    }
}
