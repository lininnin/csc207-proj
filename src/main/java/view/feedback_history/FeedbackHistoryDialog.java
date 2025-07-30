package view.feedback_history;

import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for browsing and viewing historical feedback entries.
 */
public class FeedbackHistoryDialog extends JDialog {
    private final FeedbackHistoryPanel historyPanel;

    public FeedbackHistoryDialog(
            Frame owner,
            FeedbackHistoryController controller,
            FeedbackHistoryViewModel viewModel
    ) {
        super(owner, "Feedback History", true);

        // 1. Create historyPanel (with controller and view model)
        this.historyPanel = new FeedbackHistoryPanel(controller, viewModel);

        // 2. OPTIONAL: Add a "Close" button at the bottom
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> setVisible(false));
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(closeButton);

        // 3. Compose dialog
        setLayout(new BorderLayout());
        add(historyPanel, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        setSize(700, 500);
        setLocationRelativeTo(owner);

        // 4. Load entries when opened (View triggers controller)
        controller.loadFeedbackHistory();
        historyPanel.refresh(); // update list on startup
    }

    public void refresh() {
        historyPanel.refresh();
    }
}
