package view.Alex.WellnessLog;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.delete_wellnessLog.DeleteWellnessLogController;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog.EditWellnessLogController;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogViewModel;
import interface_adapter.Alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log.TodaysWellnessLogState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TodaysWellnessLogView extends JPanel {

    private final TodaysWellnessLogViewModel viewModel;
    private final JPanel logDisplayPanel = new JPanel();
    private final DeleteWellnessLogController deleteController;
    private final EditWellnessLogController editController;
    private final List<MoodLabel> moodOptions;

    public TodaysWellnessLogView(TodaysWellnessLogViewModel viewModel,
                                 DeleteWellnessLogController deleteController,
                                 EditWellnessLogController editController,
                                 List<MoodLabel> moodOptions) {
        this.viewModel = viewModel;
        this.deleteController = deleteController;
        this.editController = editController;
        this.moodOptions = moodOptions;

        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Today's wellness Log", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        // Header row
        JPanel header = new JPanel(new GridLayout(1, 7));
        header.add(new JLabel("Mood", SwingConstants.CENTER));
        header.add(new JLabel("Energy", SwingConstants.CENTER));
        header.add(new JLabel("Stress", SwingConstants.CENTER));
        header.add(new JLabel("Fatigue", SwingConstants.CENTER));
        header.add(new JLabel("User Note", SwingConstants.CENTER));
        header.add(new JLabel("Edit", SwingConstants.CENTER));
        header.add(new JLabel("Delete", SwingConstants.CENTER));
        add(header, BorderLayout.PAGE_START);

        // Log display panel with vertical layout
        logDisplayPanel.setLayout(new BoxLayout(logDisplayPanel, BoxLayout.Y_AXIS));
        logDisplayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Set preferred size to trigger scroll when needed
        logDisplayPanel.setPreferredSize(new Dimension(400, 400));

        JScrollPane scrollPane = new JScrollPane(logDisplayPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER); // ✅ 可滚动区域放中间

        // Register for state updates
        viewModel.addPropertyChangeListener(evt -> {
            if (TodaysWellnessLogViewModel.TODAYS_WELLNESS_LOG_PROPERTY.equals(evt.getPropertyName())) {
                refreshDisplay(viewModel.getState());
            }
        });

        // Initial render
        refreshDisplay(viewModel.getState());
    }

    private void refreshDisplay(TodaysWellnessLogState state) {
        logDisplayPanel.removeAll();

        List<WellnessLogEntry> entries = state.getEntries();

        if (entries.isEmpty()) {
            JLabel empty = new JLabel("No wellness log recorded today.", SwingConstants.CENTER);
            logDisplayPanel.add(empty);
        } else {
            for (int i = entries.size() - 1; i >= 0; i--) { // 最新的显示在最上面
                WellnessLogEntry entry = entries.get(i);

                JPanel row = new JPanel(new GridLayout(1, 7));
                row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                row.add(new JLabel(entry.getMoodLabel().getName(), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getEnergyLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getStressLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getFatigueLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(entry.getUserNote() != null ? entry.getUserNote() : "", SwingConstants.CENTER));

                // 单独按钮列
                JButton editButton = new JButton("edit");
                editButton.addActionListener(e -> {
                    JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));

                    JTextField stressField = new JTextField(String.valueOf(entry.getStressLevel().getValue()));
                    JTextField energyField = new JTextField(String.valueOf(entry.getEnergyLevel().getValue()));
                    JTextField fatigueField = new JTextField(String.valueOf(entry.getFatigueLevel().getValue()));
                    JTextArea noteArea = new JTextArea(entry.getUserNote() != null ? entry.getUserNote() : "", 3, 20);
                    noteArea.setLineWrap(true);
                    noteArea.setWrapStyleWord(true);
                    JScrollPane noteScrollPane = new JScrollPane(noteArea);

                    JComboBox<MoodLabel> moodComboBox = new JComboBox<>();
                    for (MoodLabel mood : moodOptions) {
                        moodComboBox.addItem(mood);
                    }
                    // 预设当前 mood
                    moodComboBox.setSelectedItem(
                            moodOptions.stream()
                                    .filter(m -> m.getName().equals(entry.getMoodLabel().getName()))
                                    .findFirst()
                                    .orElse(null)
                    );

                    panel.add(new JLabel("Stress (1-10):"));
                    panel.add(stressField);
                    panel.add(new JLabel("Energy (1-10):"));
                    panel.add(energyField);
                    panel.add(new JLabel("Fatigue (1-10):"));
                    panel.add(fatigueField);
                    panel.add(new JLabel("Note:"));
                    panel.add(noteScrollPane);
                    panel.add(new JLabel("Mood:"));
                    panel.add(moodComboBox);

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
                            MoodLabel selectedMood = (MoodLabel) moodComboBox.getSelectedItem();

                            if (stress < 1 || stress > 10 || energy < 1 || energy > 10 || fatigue < 1 || fatigue > 10) {
                                JOptionPane.showMessageDialog(this, "Levels must be between 1 and 10.");
                                return;
                            }

                            editController.execute(
                                    entry.getId(),
                                    energy,
                                    stress,
                                    fatigue,
                                    selectedMood.getName(),
                                    selectedMood.getType(),
                                    note
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


