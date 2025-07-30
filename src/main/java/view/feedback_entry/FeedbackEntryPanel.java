package view.feedback_entry;

import entity.Ina.FeedbackEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/**
 * View for a single feedback entry,
 * now includes the Bayesian correlation analysis.
 */
public class FeedbackEntryPanel extends JPanel {

    private final JLabel header = new JLabel();
    private final JTextArea analysisArea = new JTextArea();
    private final JTextArea recArea = new JTextArea();

    public FeedbackEntryPanel(FeedbackEntry entry) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 20, 10, 20));

        // Header
        header.setFont(header.getFont().deriveFont(Font.BOLD, 20f));
        add(header, BorderLayout.NORTH);

        // 1. Analysis and Recommendation
        JPanel top = new JPanel(new GridLayout(1, 2, 8, 0));
        top.setBorder(new EmptyBorder(8, 8, 8, 8));
        top.add(makeTextPanel("Analysis", analysisArea));
        top.add(makeTextPanel("Recommendations", recArea));

        // 2. Correlation Panel
        JPanel correlationPanel = null;
        String correlationJson = entry.getCorrelationData();
        if (correlationJson != null && !correlationJson.isBlank()) {
            correlationPanel = new CorrelationPanel(correlationJson);
        } else {
            correlationPanel = new JPanel();
            correlationPanel.add(new JLabel("No correlation data available."));
        }
        correlationPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Layout: Top half = Analysis/Rec, Bottom = Correlation
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, correlationPanel);
        split.setResizeWeight(0.45);
        add(split, BorderLayout.CENTER);

        setEntry(entry);
    }

    private void setEntry(FeedbackEntry entry) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        header.setText("Feedback on " + dateTimeFormatter.format(entry.getDate()));
        analysisArea.setText(entry.getAiAnalysis() == null ? "None": entry.getAiAnalysis());
        recArea.setText(entry.getRecommendations() == null ? "None": entry.getRecommendations());
    }

    private JPanel makeTextPanel(String title, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD,14f));
        label.setBorder(new EmptyBorder(0,0,4,0));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);
        return panel;
    }
}
