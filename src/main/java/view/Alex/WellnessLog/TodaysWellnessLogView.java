package view.Alex.WellnessLog;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.AvailableMoodLabelViewModel;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.add_moodLabel.AddMoodLabelController;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.delete_moodLabel.DeleteMoodLabelController;
import interface_adapter.alex.WellnessLog_related.moodLabel_related.edit_moodLabel.EditMoodLabelController;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogController;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogController;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TodaysWellnessLogView extends JPanel {

    private final TodaysWellnessLogViewModel viewModel;
    private final JPanel logDisplayPanel = new JPanel();
    private final DeleteWellnessLogController deleteController;
    private final EditWellnessLogController editController;
    private final AvailableMoodLabelViewModel moodLabelViewModel;
    private final AddMoodLabelController addMoodController;
    private final EditMoodLabelController editMoodController;
    private final DeleteMoodLabelController deleteMoodController;

    public TodaysWellnessLogView(TodaysWellnessLogViewModel viewModel,
                                 DeleteWellnessLogController deleteController,
                                 EditWellnessLogController editController,
                                 AvailableMoodLabelViewModel moodLabelViewModel,
                                 AddMoodLabelController addMoodController,
                                 EditMoodLabelController editMoodController,
                                 DeleteMoodLabelController deleteMoodController) {
        this.viewModel = viewModel;
        this.deleteController = deleteController;
        this.editController = editController;
        this.moodLabelViewModel = moodLabelViewModel;
        this.addMoodController = addMoodController;
        this.editMoodController = editMoodController;
        this.deleteMoodController = deleteMoodController;

        setLayout(new BorderLayout());

        JLabel title = new JLabel("Today's Wellness Log", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel header = new JPanel(new GridLayout(1, 7));
        header.add(new JLabel("Mood", SwingConstants.CENTER));
        header.add(new JLabel("Energy", SwingConstants.CENTER));
        header.add(new JLabel("Stress", SwingConstants.CENTER));
        header.add(new JLabel("Fatigue", SwingConstants.CENTER));
        header.add(new JLabel("User Note", SwingConstants.CENTER));
        header.add(new JLabel("Edit", SwingConstants.CENTER));
        header.add(new JLabel("Delete", SwingConstants.CENTER));
        add(header, BorderLayout.PAGE_START);

        logDisplayPanel.setLayout(new BoxLayout(logDisplayPanel, BoxLayout.Y_AXIS));
        logDisplayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logDisplayPanel.setPreferredSize(new Dimension(400, 400));

        JScrollPane scrollPane = new JScrollPane(logDisplayPanel);
        add(scrollPane, BorderLayout.CENTER);

        viewModel.addPropertyChangeListener(evt -> {
            if (TodaysWellnessLogViewModel.TODAYS_WELLNESS_LOG_PROPERTY.equals(evt.getPropertyName())) {
                refreshDisplay(viewModel.getState());
            }
        });

        refreshDisplay(viewModel.getState());
    }

    private void refreshDisplay(TodaysWellnessLogState state) {
        logDisplayPanel.removeAll();

        List<WellnessLogEntryInterf> entries = state.getEntries();

        if (entries.isEmpty()) {
            logDisplayPanel.add(new JLabel("No wellness log recorded today.", SwingConstants.CENTER));
        } else {
            for (int i = entries.size() - 1; i >= 0; i--) {
                WellnessLogEntryInterf entry = entries.get(i);

                JPanel row = new JPanel(new GridLayout(1, 7));
                row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                row.add(new JLabel(entry.getMoodLabel().getName(), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getEnergyLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getStressLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getFatigueLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(entry.getUserNote() != null ? entry.getUserNote() : "", SwingConstants.CENTER));

                JButton editButton = new JButton("edit");
                editButton.addActionListener(e -> {
                    JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));

                    JTextField stressField = new JTextField(String.valueOf(entry.getStressLevel().getValue()));
                    JTextField energyField = new JTextField(String.valueOf(entry.getEnergyLevel().getValue()));
                    JTextField fatigueField = new JTextField(String.valueOf(entry.getFatigueLevel().getValue()));
                    JTextArea noteArea = new JTextArea(entry.getUserNote() != null ? entry.getUserNote() : "", 3, 20);
                    JScrollPane noteScrollPane = new JScrollPane(noteArea);

                    JLabel moodDisplay = new JLabel(entry.getMoodLabel().getName());
                    JButton chooseMoodBtn = new JButton("Choose...");
                    final MoodLabelInterf[] selectedMood = {entry.getMoodLabel()};

                    chooseMoodBtn.addActionListener(evt -> {
                        AvailableMoodLabelView labelView = new AvailableMoodLabelView(
                                moodLabelViewModel,
                                addMoodController,
                                editMoodController,
                                deleteMoodController
                        );
                        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Mood Label", true);
                        dialog.setContentPane(labelView);
                        dialog.pack();
                        dialog.setLocationRelativeTo(this);
                        dialog.setVisible(true);

                        MoodLabelInterf chosen = labelView.getSelectedLabel();
                        if (chosen != null) {
                            selectedMood[0] = chosen;
                            moodDisplay.setText(chosen.getName());
                        }
                    });

                    panel.add(new JLabel("Stress (1-10):")); panel.add(stressField);
                    panel.add(new JLabel("Energy (1-10):")); panel.add(energyField);
                    panel.add(new JLabel("Fatigue (1-10):")); panel.add(fatigueField);
                    panel.add(new JLabel("Note:")); panel.add(noteScrollPane);
                    panel.add(new JLabel("Mood:")); panel.add(moodDisplay);
                    panel.add(new JLabel("")); panel.add(chooseMoodBtn);

                    int result = JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Edit Wellness Log",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            int stress = Integer.parseInt(stressField.getText().trim());
                            int energy = Integer.parseInt(energyField.getText().trim());
                            int fatigue = Integer.parseInt(fatigueField.getText().trim());
                            String note = noteArea.getText().trim();

                            if (stress < 1 || stress > 10 || energy < 1 || energy > 10 || fatigue < 1 || fatigue > 10) {
                                JOptionPane.showMessageDialog(this, "Levels must be between 1 and 10.");
                                return;
                            }

                            MoodLabelInterf mood = selectedMood[0];
                            editController.execute(
                                    entry.getId(), energy, stress, fatigue,
                                    mood.getName(), mood.getType(), note
                            );

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Please enter valid integers for levels.");
                        }
                    }
                });

                JButton deleteButton = new JButton("delete");
                deleteButton.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete this entry?",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        deleteController.delete(entry.getId());
                    }
                });

                row.add(editButton);
                row.add(deleteButton);
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                logDisplayPanel.add(row);
            }
        }

        logDisplayPanel.revalidate();
        logDisplayPanel.repaint();
    }
}
