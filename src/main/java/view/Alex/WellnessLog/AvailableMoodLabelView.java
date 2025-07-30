package view.Alex.WellnessLog;

import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelState.MoodLabelEntry;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelController;
import interface_adapter.Alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class AvailableMoodLabelView extends JPanel implements PropertyChangeListener {

    private final AvailableMoodLabelViewModel viewModel;
    private final AddMoodLabelController addController;
    private final JPanel labelDisplayPanel;
    private final EditMoodLabelController editController;

    public AvailableMoodLabelView(AvailableMoodLabelViewModel viewModel,
                                  AddMoodLabelController addController,
                                  EditMoodLabelController editController) {

        this.editController = editController;
        this.viewModel = viewModel;
        this.addController = addController;

        this.setLayout(new BorderLayout());
        this.viewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel("Available Mood Labels", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        this.add(title, BorderLayout.NORTH);

        // Header row
        JPanel header = new JPanel(new GridLayout(1, 4));
        header.add(new JLabel("Name", SwingConstants.CENTER));
        header.add(new JLabel("Type", SwingConstants.CENTER));
        header.add(new JLabel("Edit", SwingConstants.CENTER));
        header.add(new JLabel("Delete", SwingConstants.CENTER));
        this.add(header, BorderLayout.PAGE_START);

        labelDisplayPanel = new JPanel();
        labelDisplayPanel.setLayout(new BoxLayout(labelDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(labelDisplayPanel);
        this.add(scrollPane, BorderLayout.CENTER);

        refreshDisplay();
    }

    private void refreshDisplay() {
        labelDisplayPanel.removeAll();

        List<MoodLabelEntry> allLabels = viewModel.getState().getMoodLabels();

        if (allLabels.isEmpty()) {
            JLabel empty = new JLabel("No mood labels available.", SwingConstants.CENTER);
            labelDisplayPanel.add(empty);
        } else {
            for (MoodLabelEntry entry : allLabels) {
                JPanel row = new JPanel(new GridLayout(1, 4));
                row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                if (entry.isEditing()) {
                    JTextField nameField = new JTextField(entry.getName());
                    row.add(nameField);

                    row.add(new JLabel(entry.getType(), SwingConstants.CENTER));

                    row.add(makeButton("save", e -> {
                        String newName = nameField.getText().trim();
                        if (!newName.isEmpty()) {
                            if (entry.isNew()) {
                                entry.setNew(false);
                                addController.addMoodLabel(newName, entry.getType());
                            } else {
                                editController.execute(entry.getName(), newName, entry.getType());
                                entry.setName(newName);
                            }
                            entry.setEditing(false);
                            viewModel.fireLabelListChanged();
                        }
                    }));


                    row.add(makeButton("cancel", e -> {
                        entry.setEditing(false);
                        viewModel.fireLabelListChanged();
                    }));
                } else {
                    row.add(new JLabel(entry.getName(), SwingConstants.CENTER));
                    row.add(new JLabel(entry.getType(), SwingConstants.CENTER));

                    row.add(makeButton("edit", e -> {
                        entry.setEditing(true);
                        viewModel.fireLabelListChanged();
                    }));

                    row.add(makeButton("delete", e -> {
                        // TODO: delete use case
                    }));
                }

                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                labelDisplayPanel.add(row);
            }
        }

        // Bottom "new" button
        JButton newBtn = new JButton("New Label");
        newBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        newBtn.setBackground(new Color(204, 255, 204));
        newBtn.addActionListener(e -> {
            String[] options = {"Positive", "Negative"};
            String type = (String) JOptionPane.showInputDialog(this,
                    "Select type:", "Mood Type",
                    JOptionPane.PLAIN_MESSAGE, null,
                    options, options[0]);

            if (type != null) {
                MoodLabelEntry newEntry = new MoodLabelEntry("", type);
                newEntry.setEditing(true);
                newEntry.setNew(true);
                viewModel.getState().getMoodLabels().add(newEntry);
                viewModel.fireLabelListChanged();
            }
        });

        JPanel newBtnPanel = new JPanel();
        newBtnPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        newBtnPanel.add(newBtn);
        labelDisplayPanel.add(newBtnPanel);

        labelDisplayPanel.revalidate();
        labelDisplayPanel.repaint();
    }

    private JButton makeButton(String label, AbstractAction action) {
        JButton btn = new JButton(action);
        btn.setText(label);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(new Color(255, 204, 153));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return btn;
    }

    private JButton makeButton(String label, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBackground(new Color(255, 204, 153));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.addActionListener(listener);
        return btn;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AvailableMoodLabelViewModel.MOOD_LABEL_LIST_PROPERTY.equals(evt.getPropertyName())) {
            refreshDisplay();
        }
    }
}

