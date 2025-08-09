// view/feedback_entry/FeedbackEntryPanel.java
package view.feedback_panel;

import entity.Ina.FeedbackEntry;
import interface_adapter.feedback_entry.FeedbackEntryView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class FeedbackEntryPanel extends JPanel implements FeedbackEntryView {
    private final JLabel header = new JLabel();
    private final JTextArea analysisArea = new JTextArea();
    private final JTextArea recArea = new JTextArea();
    private final JSplitPane split;

    public FeedbackEntryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 20, 10, 20));
        header.setFont(header.getFont().deriveFont(Font.BOLD, 20f));
        add(header, BorderLayout.NORTH);

        JPanel analysisRecPanel = new JPanel(new GridLayout(1, 2, 14, 0));
        analysisRecPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        analysisRecPanel.add(makeTextPanel("Analysis", analysisArea));
        analysisRecPanel.add(makeTextPanel("Recommendations", recArea));
        analysisRecPanel.setPreferredSize(new Dimension(400, 450));

        JPanel correlationPanel = new JPanel();
        correlationPanel.add(new JLabel("No correlation data available."));
        correlationPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, analysisRecPanel, correlationPanel);
        split.setResizeWeight(0.45);
        add(split, BorderLayout.CENTER);
    }

    @Override
    public void displayEntry(FeedbackEntry entry) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        header.setText("Feedback on " + dateTimeFormatter.format(entry.getDate()));
        analysisArea.setText(entry.getAiAnalysis() == null ? "None" : entry.getAiAnalysis());
        recArea.setText(entry.getRecommendations() == null ? "None" : entry.getRecommendations());

        // Replace correlation panel dynamically
        JPanel correlationPanel;
        String correlationJson = entry.getCorrelationData();
        if (correlationJson != null && !correlationJson.isBlank()) {
            correlationPanel = new CorrelationPanel(correlationJson);
        } else {
            correlationPanel = new JPanel();
            correlationPanel.add(new JLabel("No correlation data available."));
        }
        correlationPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        split.setBottomComponent(correlationPanel);

        revalidate();
        repaint();
    }

    private JPanel makeTextPanel(String title, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout());

        // Title
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 16f));
        label.setBorder(new EmptyBorder(0, 0, 4, 0));

        // Text area
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(area.getFont().deriveFont(14f));
        area.setMargin(new Insets(6, 6, 6, 6));

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        return panel;
    }

}
