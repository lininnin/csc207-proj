package view.Alex.WellnessLog;

import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class AvailableMoodLabelView extends JPanel implements PropertyChangeListener {

    private final AvailableMoodLabelViewModel viewModel;
    private final JPanel contentPanel;

    public AvailableMoodLabelView(AvailableMoodLabelViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Mood Label Selector", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(title, BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        renderMoodLabels();
    }

    private void renderMoodLabels() {
        contentPanel.removeAll();

        // Section: Positive
        JLabel posLabel = new JLabel("Positive");
        posLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        posLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLUE));
        contentPanel.add(posLabel);
        renderSection(viewModel.getState().getPositiveLabels(), "Positive");

        // Section: Negative
        JLabel negLabel = new JLabel("Negative");
        negLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        negLabel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLUE));
        contentPanel.add(negLabel);
        renderSection(viewModel.getState().getNegativeLabels(), "Negative");

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void renderSection(List<MoodLabelEntry> entries, String type) {
        for (MoodLabelEntry entry : entries) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
//            if (entry.isEditing()) {
//                JTextField field = new JTextField(entry.getName(), 10);
//                row.add(field);
//                row.add(makeButton("cancel", e -> {
//                    entry.setEditing(false);
//                    renderMoodLabels();
//                }));
//                row.add(makeButton("save", e -> {
//                    entry.setName(field.getText().trim());
//                    entry.setEditing(false);
//                    if (entry.isNew()) {
//                        entry.setNew(false);
//                        // TODO: 调用 AddUseCase
//                    } else {
//                        // TODO: 调用 EditUseCase
//                    }
//                    viewModel.fireLabelListChanged();
//                }));
//            } else {
//                row.add(new JLabel(entry.getName()));
//                row.add(makeButton("select", e -> {
//                    // TODO: 实现 Select 操作
//                }));
//                row.add(makeButton("edit", e -> {
//                    entry.setEditing(true);
//                    renderMoodLabels();
//                }));
//                row.add(makeButton("delete", e -> {
//                    // TODO: 调用 DeleteUseCase
//                }));
//            }
            contentPanel.add(row);
        }

        // new 按钮
//        JButton newBtn = makeButton("new", e -> {
//            MoodLabelEntry newEntry = new MoodLabelEntry("", type);
//            newEntry.setEditing(true);
//            newEntry.setNew(true);
//            viewModel.getState().getMoodLabels().add(newEntry);
//            viewModel.fireLabelListChanged();
//        });
//        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        btnPanel.add(newBtn);
//        contentPanel.add(btnPanel);
    }

    private JButton makeButton(String label, AbstractAction action) {
        JButton btn = new JButton(action);
        btn.setText(label);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(new Color(255, 204, 153)); // match image style
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return btn;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(AvailableMoodLabelViewModel.MOOD_LABEL_LIST_PROPERTY)) {
            renderMoodLabels();
        }
    }
}
