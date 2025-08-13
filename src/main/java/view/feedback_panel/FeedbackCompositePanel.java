package view.feedback_panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import constants.Constants;
import entity.feedback_entry.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;

public class FeedbackCompositePanel extends JPanel {

    public FeedbackCompositePanel(List<FeedbackEntry> entries) {
        setLayout(new BorderLayout(Constants.SIXTEEN, 0));

        final FeedbackEntryPanel entryPanel = new FeedbackEntryPanel();

        // Convert entries list to a ViewModel
        final FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
        viewModel.setEntries(entries);

        // History panel with callback to update the detail view
        final FeedbackHistoryPanel historyPanel = new FeedbackHistoryPanel(
                viewModel,
                entryPanel::displayEntry
        );
        historyPanel.setPreferredSize(new Dimension(Constants.TWO_SIXTY, 0));

        add(entryPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.EAST);

        // Show the first entry at startup, if any
        if (!entries.isEmpty()) {
            entryPanel.displayEntry(entries.get(0));
        }
    }
}
