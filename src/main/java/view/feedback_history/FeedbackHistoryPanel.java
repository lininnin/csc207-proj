package view.feedback_history;

import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_history.FeedbackHistoryController;
import interface_adapter.feedback_history.FeedbackHistoryViewModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class FeedbackHistoryPanel extends JPanel {
    private final FeedbackHistoryController controller;
    private final FeedbackHistoryViewModel viewModel;
    private final JPanel listContainer = new JPanel();

    public FeedbackHistoryPanel(FeedbackHistoryController controller, FeedbackHistoryViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());
        JLabel header = new JLabel("Feedback History");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 20f));
        header.setBorder(new EmptyBorder(8, 12, 8, 12));
        add(header, BorderLayout.NORTH);

        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(new EmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        // Load entries on startup
        controller.loadFeedbackHistory();
        refresh();
    }

    public void refresh() {
        List<FeedbackEntry> entries = viewModel.getEntries();
        listContainer.removeAll();
        if (entries != null) {
            entries.stream()
                    .sorted(Comparator.comparing(FeedbackEntry::getDate).reversed())
                    .forEach(e -> listContainer.add(row(e)));
        }
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JComponent row(FeedbackEntry entry) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(6, 4, 6, 4)
        ));
        JLabel dateLabel = new JLabel(entry.getDate().toString());
        dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD));
        row.add(dateLabel, BorderLayout.WEST);

        JButton viewButton = new JButton("View");
        // TODO: Show details panel here
        row.add(viewButton, BorderLayout.EAST);
        return row;
    }
}
