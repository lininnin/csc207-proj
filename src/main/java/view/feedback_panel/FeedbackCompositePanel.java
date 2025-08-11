package view.feedback_panel;

import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FeedbackCompositePanel extends JPanel {

    public FeedbackCompositePanel(List<FeedbackEntry> entries) {
        setLayout(new BorderLayout(16, 0));

        FeedbackEntryPanel entryPanel = new FeedbackEntryPanel();

        // Convert entries list to a ViewModel
        FeedbackHistoryViewModel viewModel = new FeedbackHistoryViewModel();
        viewModel.setEntries(entries);

        // History panel with callback to update the detail view
        FeedbackHistoryPanel historyPanel = new FeedbackHistoryPanel(
                viewModel,
                entryPanel::displayEntry
        );
        historyPanel.setPreferredSize(new Dimension(260, 0));

        add(entryPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.EAST);

        // Show the first entry at startup, if any
        if (!entries.isEmpty()) {
            entryPanel.displayEntry(entries.get(0));
        }
    }
}
