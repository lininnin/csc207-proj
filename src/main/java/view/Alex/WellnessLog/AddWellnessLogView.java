package view.Alex.WellnessLog;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogController;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogState;
import interface_adapter.Alex.WellnessLog_related.new_wellness_log.AddWellnessLogViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Swing-based view for creating a new wellness log.
 */
public class AddWellnessLogView extends JPanel implements PropertyChangeListener {

    private final AddWellnessLogViewModel viewModel;
    private final AddWellnessLogController controller;

    private final JComboBox<MoodLabel> moodLabelComboBox = new JComboBox<>();
    private final JComboBox<Levels> stressComboBox = new JComboBox<>(Levels.values());
    private final JComboBox<Levels> energyComboBox = new JComboBox<>(Levels.values());
    private final JComboBox<Levels> fatigueComboBox = new JComboBox<>(Levels.values());
    private final JTextField userNoteField = new JTextField(20);
    private final JButton submitButton = new JButton();

    private final JLabel messageLabel = new JLabel();

    public AddWellnessLogView(AddWellnessLogViewModel viewModel,
                              AddWellnessLogController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Section Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("New wellness log");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, gbc);

        gbc.gridwidth = 1;

        // Mood label
        gbc.gridy++;
        add(new JLabel("Mood label:"), gbc);
        gbc.gridx = 1;
        add(moodLabelComboBox, gbc);

        // Energy
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Energy:"), gbc);
        gbc.gridx = 1;
        add(energyComboBox, gbc);

        // Stress
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Stress:"), gbc);
        gbc.gridx = 1;
        add(stressComboBox, gbc);

        // Fatigue
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("Fatigue:"), gbc);
        gbc.gridx = 1;
        add(fatigueComboBox, gbc);

        // User note
        gbc.gridy++;
        gbc.gridx = 0;
        add(new JLabel("User Note:"), gbc);
        gbc.gridx = 1;
        add(userNoteField, gbc);

        // Submit button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        submitButton.setText(viewModel.getSubmitButtonLabel());
        add(submitButton, gbc);

        // Message label
        gbc.gridy++;
        messageLabel.setForeground(Color.BLUE);
        add(messageLabel, gbc);

        // Submit listener
        submitButton.addActionListener(e -> {
            System.out.println("Submit button clicked!");
            MoodLabel mood = (MoodLabel) moodLabelComboBox.getSelectedItem();
            Levels stress = (Levels) stressComboBox.getSelectedItem();
            Levels energy = (Levels) energyComboBox.getSelectedItem();
            Levels fatigue = (Levels) fatigueComboBox.getSelectedItem();
            String note = userNoteField.getText();

            controller.execute(LocalDateTime.now(), stress, energy, fatigue, mood, note);
        });

        // 初始刷新
        refresh();
    }

    private void refresh() {
        AddWellnessLogState state = viewModel.getState();

        // 更新 mood label 下拉框选项
        List<MoodLabel> moodOptions = state.getAvailableMoodLabels();
        moodLabelComboBox.removeAllItems();
        for (MoodLabel label : moodOptions) {
            moodLabelComboBox.addItem(label);
        }

        messageLabel.setText(state.getErrorMessage() != null
                ? state.getErrorMessage()
                : state.getSuccessMessage() != null ? state.getSuccessMessage() : "");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AddWellnessLogViewModel.WELLNESS_LOG_PROPERTY.equals(evt.getPropertyName())) {
            refresh();
        }
    }
}

