package view.feedback_panel.feedback_entry;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import constants.Constants;
import entity.feedback_entry.FeedbackEntryInterf;
import interface_adapter.feedback_entry.FeedbackEntryView;

/*
 * Panel displaying a feedback entry, with correlation panel, an analysis + recommendation panel
 */
public class FeedbackEntryPanel extends JPanel implements FeedbackEntryView {
    // style checked
    private final JLabel header = new JLabel();
    private final JTextArea analysisArea = new JTextArea();
    private final JTextArea recArea = new JTextArea();
    private final JSplitPane split;

    public FeedbackEntryPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(Constants.TEN, Constants.TWENTY, Constants.TEN, Constants.TWENTY));
        header.setFont(header.getFont().deriveFont(Font.BOLD, Constants.F20));
        add(header, BorderLayout.NORTH);

        final JPanel analysisRecPanel = new JPanel(new GridLayout(1, 2, 14, 0));
        analysisRecPanel.setBorder(new EmptyBorder(Constants.EIGHT, Constants.EIGHT, Constants.EIGHT, Constants.EIGHT));
        analysisRecPanel.add(makeTextPanel("Analysis", analysisArea));
        analysisRecPanel.add(makeTextPanel("Recommendations", recArea));
        analysisRecPanel.setPreferredSize(new Dimension(Constants.FOUR_HUNDRED, Constants.FOUR_HUNDRED_FIFTY));

        final JPanel correlationPanel = new JPanel();
        correlationPanel.add(new JLabel("No correlation data available."));
        correlationPanel.setBorder(new EmptyBorder(Constants.EIGHT, Constants.EIGHT, Constants.EIGHT, Constants.EIGHT));

        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                analysisRecPanel,
                correlationPanel);
        split.setResizeWeight(Constants.HALF);
        add(split, BorderLayout.CENTER);
    }

    @Override
    public void displayEntry(final FeedbackEntryInterf entry) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        header.setText("Feedback on " + dateTimeFormatter
                .format(entry.getDate()));

        if (entry.getAiAnalysis() == null) {
            analysisArea.setText("None");
        }
        else {
            analysisArea.setText(entry.getAiAnalysis());
        }

        if (entry.getRecommendations() == null) {
            recArea.setText("None");
        }
        else {
            recArea.setText(entry.getRecommendations());
        }

        // Replace correlation panel dynamically
        final JPanel correlationPanel;
        final String correlationJson = entry.getCorrelationData();

        if (correlationJson != null && !correlationJson.isBlank()) {
            correlationPanel = new CorrelationPanel(correlationJson);
        }
        else {
            correlationPanel = new JPanel();
            correlationPanel.add(new JLabel("No correlation data available."));
        }
        correlationPanel.setBorder(new EmptyBorder(Constants.EIGHT, Constants.EIGHT, Constants.EIGHT, Constants.EIGHT));
        split.setBottomComponent(correlationPanel);

        revalidate();
        repaint();
    }

    private JPanel makeTextPanel(final String title, final JTextArea area) {
        // Title
        final JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD, Constants.F16));
        label.setBorder(new EmptyBorder(0, 0, Constants.FOUR, 0));

        // Text area
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(area.getFont().deriveFont(Constants.F14));
        area.setMargin(new Insets(Constants.SIX, Constants.SIX, Constants.SIX, Constants.SIX));

        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        return panel;
    }

}
