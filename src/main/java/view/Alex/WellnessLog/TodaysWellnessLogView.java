package view.Alex.WellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import interface_adapter.Alex.WellnessLog_related.todays_wellness_log.todays_wellnesslog_module.TodaysWellnessLogViewModel;
import interface_adapter.Alex.WellnessLog_related.todays_wellness_log.todays_wellnesslog_module.TodaysWellnessLogState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TodaysWellnessLogView extends JPanel {

    private final TodaysWellnessLogViewModel viewModel;
    private final JPanel logDisplayPanel = new JPanel();

    public TodaysWellnessLogView(TodaysWellnessLogViewModel viewModel) {
        this.viewModel = viewModel;

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

                JPanel row = new JPanel(new GridLayout(1, 6));
                row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                row.add(new JLabel(entry.getMoodLabel().getName(), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getEnergyLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getStressLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(String.valueOf(entry.getFatigueLevel().getValue()), SwingConstants.CENTER));
                row.add(new JLabel(entry.getUserNote() != null ? entry.getUserNote() : "", SwingConstants.CENTER));

                JPanel actionPanel = new JPanel(new FlowLayout());
                JButton editButton = new JButton("edit");
                JButton deleteButton = new JButton("delete");
                actionPanel.add(editButton);
                actionPanel.add(deleteButton);
                row.add(actionPanel);

                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
                logDisplayPanel.add(row);
            }
        }

        logDisplayPanel.revalidate();
        logDisplayPanel.repaint();
    }
}


