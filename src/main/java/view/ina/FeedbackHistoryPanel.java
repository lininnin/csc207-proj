package view.ina;

import entity.Ina.FeedbackEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class FeedbackHistoryPanel extends JPanel {

    public interface Viewer { void show(FeedbackEntry feedbackEntry); }

    private final JPanel listContainer = new JPanel();
    private final DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;
    private final Viewer viewer;

    public FeedbackHistoryPanel(List<FeedbackEntry> entries, Viewer viewer) {
        this.viewer = viewer;

        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Feedback History");
        header.setFont(header.getFont().deriveFont(Font.BOLD, 20f));
        header.setBorder(new EmptyBorder(8, 12, 8, 12));
        add(header, BorderLayout.NORTH);


        // List
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Scroller
        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        load(entries);
    }

    public void load(List<FeedbackEntry> feedbackEntries) {
        listContainer.removeAll();
        feedbackEntries.stream()
                .sorted(Comparator.comparing(FeedbackEntry::getDate).reversed())
                .forEach(e -> listContainer.add(row(e)));
        listContainer.revalidate();
        listContainer.repaint();
    }

    public void addEntry(FeedbackEntry entry) {
        listContainer.add(row(entry), 0); // newest on top
        listContainer.revalidate();
        listContainer.repaint();
    }

    private JComponent row(FeedbackEntry entry) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(6, 4, 6, 4)
        ));

        JLabel dateLabel = new JLabel(entry.getDate().format(df));
        dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD));
        row.add(dateLabel, BorderLayout.WEST);

        JButton viewButton = new JButton("View");
        viewButton.addActionListener(evt -> viewer.show(entry));
        row.add(viewButton, BorderLayout.EAST);

        return row;
    }
}
