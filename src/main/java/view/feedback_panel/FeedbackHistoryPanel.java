package view.feedback_panel;

import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class FeedbackHistoryPanel extends JPanel {
    public FeedbackHistoryPanel(
            FeedbackHistoryViewModel viewModel,
            Consumer<FeedbackEntry> onViewEntry
    ) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel header = new JLabel("Feedback History");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 20f));
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(header);

        List<FeedbackEntry> entries = viewModel.getEntries();
        if (entries == null) entries = List.of();

        for (FeedbackEntry entry : entries) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel dateLabel = new JLabel(entry.getDate().toString());
            dateLabel.setPreferredSize(new Dimension(100, 22));

            JButton viewBtn = new JButton("View");
            viewBtn.addActionListener(e -> onViewEntry.accept(entry));

            row.add(dateLabel);
            row.add(viewBtn);
            add(row);
        }

        // If there are no entries, show a message
        if (entries.isEmpty()) {
            JLabel emptyLabel = new JLabel("No feedback entry on record.");
            emptyLabel.setForeground(Color.GRAY);
            add(emptyLabel);
        }
    }
}
