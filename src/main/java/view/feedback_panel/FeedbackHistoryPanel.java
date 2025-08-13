package view.feedback_panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.Constants;
import entity.feedback_entry.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;

public class FeedbackHistoryPanel extends JPanel {
    public FeedbackHistoryPanel(
            FeedbackHistoryViewModel viewModel,
            Consumer<FeedbackEntry> onViewEntry
    ) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(Constants.TWELVE, Constants.TWELVE,
                Constants.TWELVE, Constants.TWELVE));

        final JLabel header = new JLabel("Feedback History");
        header.setFont(header.getFont().deriveFont(Font.BOLD, Constants.F20));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, Constants.SIXTEEN, 0));
        add(header);

        List<FeedbackEntry> entries = viewModel.getEntries();
        if (entries == null) {
            entries = List.of();
        }

        for (FeedbackEntry entry : entries) {
            final JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, Constants.TWELVE, 0));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setMaximumSize(new Dimension(Constants.FIVE_FIFTY, Constants.FORTY));

            final JLabel dateLabel = new JLabel(entry.getDate().toString());
            dateLabel.setPreferredSize(new Dimension(Constants.HUNDRED, Constants.TWENTY_TWO));

            final JButton viewBtn = new JButton("View");
            viewBtn.setBackground(new Color(Constants.TWO_FIFTY_FIVE,
                    Constants.TWO_FIFTY_FIVE, Constants.TWO_FIFTY_FIVE));
            viewBtn.addActionListener(view -> onViewEntry.accept(entry));

            row.add(dateLabel);
            row.add(viewBtn);
            add(row);
        }

        // If there are no entries, show a message
        if (entries.isEmpty()) {
            final JLabel emptyLabel = new JLabel("No feedback entry on record.");
            emptyLabel.setForeground(Color.GRAY);
            add(emptyLabel);
        }
    }
}
