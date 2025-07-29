package view.ina;

import entity.Ina.FeedbackEntry;
import use_case.repository.FeedbackRepository;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

/**
 * Dialog for browsing and viewing historical feedback entries.
 * Clicking “View” swaps to the entry’s detail panel, with “Back to History” button.
 */
public class FeedbackHistoryDialog extends JDialog {

    public FeedbackHistoryDialog(Frame owner, FeedbackRepository repo) {
        super(owner, "Feedback History", true);

        List<FeedbackEntry> history = repo.loadAll().stream()
                .sorted(Comparator.comparing(FeedbackEntry::getDate).reversed())
                .toList();

        // The main history list panel (for return)
        FeedbackHistoryPanel[] panelRef = new FeedbackHistoryPanel[1];

        // Detail-view callback
        FeedbackHistoryPanel.Viewer viewer = entry -> {
            FeedbackEntry full = repo.loadByDate(entry.getDate());
            FeedbackEntryPanel detailPanel = new FeedbackEntryPanel(full);

            JButton backButton = new JButton("Back to History");
            backButton.addActionListener(e -> setContentPane(panelRef[0]));

            JPanel detailWrapper = new JPanel(new BorderLayout());
            detailWrapper.add(detailPanel, BorderLayout.CENTER);
            detailWrapper.add(backButton, BorderLayout.SOUTH);

            setContentPane(detailWrapper);
            revalidate();
            repaint();
        };

        FeedbackHistoryPanel historyPanel = new FeedbackHistoryPanel(history, viewer);
        panelRef[0] = historyPanel;

        setContentPane(historyPanel);
        setSize(700, 500);
        setLocationRelativeTo(owner);
    }
}
