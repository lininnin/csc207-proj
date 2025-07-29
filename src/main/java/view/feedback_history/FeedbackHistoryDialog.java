package view.feedback_history;

import entity.Ina.FeedbackEntry;
import view.feedback_entry.FeedbackEntryPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Dialog for browsing and viewing historical feedback entries.
 * Clicking “View” swaps to the entry’s detail panel, with “Back to History” button.
 */
public class FeedbackHistoryDialog extends JDialog {

    public FeedbackHistoryDialog(Frame owner, List<FeedbackEntry> history, FeedbackHistoryPanel.Viewer viewer) {
        super(owner, "Feedback History", true);

        FeedbackHistoryPanel[] panelRef = new FeedbackHistoryPanel[1];

        FeedbackHistoryPanel historyPanel = new FeedbackHistoryPanel(history, entry -> {
            FeedbackEntryPanel detailPanel = new FeedbackEntryPanel(entry);

            JButton backButton = new JButton("Back to History");
            backButton.addActionListener(e -> setContentPane(panelRef[0]));

            JPanel detailWrapper = new JPanel(new BorderLayout());
            detailWrapper.add(detailPanel, BorderLayout.CENTER);
            detailWrapper.add(backButton, BorderLayout.SOUTH);

            setContentPane(detailWrapper);
            revalidate();
            repaint();
        });
        panelRef[0] = historyPanel;

        setContentPane(historyPanel);
        setSize(700, 500);
        setLocationRelativeTo(owner);
    }
}
