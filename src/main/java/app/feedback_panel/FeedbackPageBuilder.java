package app.feedback_panel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import constants.Constants;
import interface_adapter.feedback_entry.FeedbackEntryController;
import interface_adapter.feedback_entry.FeedbackEntryPresenter;
import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryPresenter;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import use_case.feedback_entry.FeedbackEntryInputBoundary;
import use_case.feedback_entry.FeedbackEntryInteractor;
import use_case.feedback_entry.FeedbackEntryOutputBoundary;
import use_case.feedback_history.FeedbackHistoryInputBoundary;
import use_case.feedback_history.FeedbackHistoryInteractor;
import use_case.feedback_history.FeedbackHistoryOutputBoundary;
import use_case.repository.FeedbackRepository;
import view.feedback_panel.FeedbackHistoryPanel;
import view.feedback_panel.feedback_entry.FeedbackEntryPanel;

public final class FeedbackPageBuilder {
    private final FeedbackRepository repo;

    public FeedbackPageBuilder(FeedbackRepository repo) {
        this.repo = repo;
    }

    /**
     * Build the feedback page.
     * @return the JPanel containing the newest feedback entry and history
     */
    public JPanel build() {
        // ViewModels
        final FeedbackHistoryViewModel historyVm = new FeedbackHistoryViewModel();

        // Presenters
        final FeedbackHistoryOutputBoundary historyPresenter = new FeedbackHistoryPresenter(historyVm);

        // Interactors
        final FeedbackHistoryInputBoundary historyInteractor =
                new FeedbackHistoryInteractor(repo, historyPresenter);

        // Controllers
        final FeedbackHistoryController historyController =
                new FeedbackHistoryController(historyInteractor);

        // Detail view + presenter + interactor + controller
        final FeedbackEntryPanel entryPanel = new FeedbackEntryPanel();
        final FeedbackEntryOutputBoundary entryPresenter = new FeedbackEntryPresenter(entryPanel);
        final FeedbackEntryInputBoundary entryInteractor =
                new FeedbackEntryInteractor(repo, entryPresenter);

        final FeedbackEntryController entryController = new FeedbackEntryController(entryInteractor);

        // Views
        final FeedbackHistoryPanel historyPanel = new FeedbackHistoryPanel(
                historyVm,
                entry -> entryController.onViewPressed(entry.getDate())
        );

        historyPanel.setPreferredSize(new Dimension(Constants.TWO_SIXTY, 0));

        // Compose
        final JPanel page = new JPanel(new BorderLayout(Constants.SIXTEEN, 0));
        page.add(entryPanel, BorderLayout.CENTER);
        page.add(historyPanel, BorderLayout.EAST);

        // Initial load (through controller/use case)
        historyController.loadFeedbackHistory();

        // Optionally select newest (through controller)
        if (historyVm.getEntries() != null && !historyVm.getEntries().isEmpty()) {
            entryController.onViewPressed(historyVm.getEntries().get(0).getDate());
        }

        return page;
    }
}
