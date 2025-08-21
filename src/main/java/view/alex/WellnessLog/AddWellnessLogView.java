package view.alex.WellnessLog;

import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.MoodLabel.MoodLabelFactoryInterf;
import entity.alex.WellnessLogEntry.Levels;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelController;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelController;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelController;
import interface_adapter.alex.WellnessLog_related.new_wellness_log.AddWellnessLogController;
import interface_adapter.alex.WellnessLog_related.new_wellness_log.AddWellnessLogState;
import interface_adapter.alex.WellnessLog_related.new_wellness_log.AddWellnessLogViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDateTime;

/**
 * Swing-based view for creating a new wellness log.
 * Now decoupled from the concrete MoodLabel class by using the MoodLabelInterf interface.
 */
public class AddWellnessLogView extends JPanel implements PropertyChangeListener {

    private final AddWellnessLogViewModel viewModel;
    private final AddWellnessLogController controller;

    private MoodLabelInterf selectedMoodLabel = null;
    private final JLabel selectedMoodLabelLabel = new JLabel("Selected: None");
    private final JComboBox<Levels> stressComboBox = new JComboBox<>(Levels.values());
    private final JComboBox<Levels> energyComboBox = new JComboBox<>(Levels.values());
    private final JComboBox<Levels> fatigueComboBox = new JComboBox<>(Levels.values());
    private final JTextField userNoteField = new JTextField(20);
    private final JButton submitButton = new JButton();
    private final AvailableMoodLabelViewModel availableMoodLabelViewModel;
    private final AddMoodLabelController addLabelController;
    private final EditMoodLabelController editLabelController;
    private final DeleteMoodLabelController deleteLabelController;
    private final MoodLabelFactoryInterf moodLabelFactory;

    private final JLabel messageLabel = new JLabel();

    public AddWellnessLogView(
            AddWellnessLogViewModel viewModel,
            AddWellnessLogController controller,
            AvailableMoodLabelViewModel availableMoodLabelViewModel,
            AddMoodLabelController addLabelController,
            EditMoodLabelController editLabelController,
            DeleteMoodLabelController deleteLabelController,
            MoodLabelFactoryInterf moodLabelFactory
    ) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.availableMoodLabelViewModel = availableMoodLabelViewModel;
        this.addLabelController = addLabelController;
        this.editLabelController = editLabelController;
        this.deleteLabelController = deleteLabelController;
        this.moodLabelFactory = moodLabelFactory;

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
        JPanel moodLabelPanel = new JPanel(new BorderLayout());

        JButton chooseBtn = new JButton("Choose...");
        chooseBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        chooseBtn.addActionListener(e -> {
            AvailableMoodLabelView labelView = new AvailableMoodLabelView(
                    availableMoodLabelViewModel,
                    addLabelController,
                    editLabelController,
                    deleteLabelController,
                    moodLabelFactory
            );
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Mood Label", true);
            dialog.setContentPane(labelView);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

            MoodLabelInterf selected = labelView.getSelectedLabel();
            if (selected != null) {
                selectedMoodLabel = selected;
                selectedMoodLabelLabel.setText("Selected: " + selected.getName());
            }
        });

        moodLabelPanel.add(selectedMoodLabelLabel, BorderLayout.CENTER);
        moodLabelPanel.add(chooseBtn, BorderLayout.EAST);
        add(moodLabelPanel, gbc);

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
            MoodLabelInterf mood = selectedMoodLabel;

            Levels stress = (Levels) stressComboBox.getSelectedItem();
            Levels energy = (Levels) energyComboBox.getSelectedItem();
            Levels fatigue = (Levels) fatigueComboBox.getSelectedItem();
            String note = userNoteField.getText();

            controller.execute(LocalDateTime.now(), stress, energy, fatigue, mood, note);
        });

        refresh();
    }

    private void refresh() {
        AddWellnessLogState state = viewModel.getState();
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
