package view.Angela.Task;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import view.FontUtil;

/**
 * Placeholder view for Today's Tasks.
 * This will be properly implemented when you work on the today's task use cases.
 */
public class TodaysTasksView extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTable taskTable;

    public TodaysTasksView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Today's Tasks"));
        setBackground(new Color(240, 240, 255));

        // Create table
        String[] columnNames = {"Name", "Category", "Priority", "Status", "Due"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only for now
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setFont(FontUtil.getStandardFont());
        taskTable.getTableHeader().setFont(FontUtil.getBoldFont());
        taskTable.setRowHeight(25);

        // No sample data - start with empty table

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel completedLabel = new JLabel("Completed: 0/0");
        completedLabel.setFont(FontUtil.getStandardFont());
        statsPanel.add(completedLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        JLabel inProgressLabel = new JLabel("In Progress: 0");
        inProgressLabel.setFont(FontUtil.getStandardFont());
        statsPanel.add(inProgressLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        JLabel notStartedLabel = new JLabel("Not Started: 0");
        notStartedLabel.setFont(FontUtil.getStandardFont());
        statsPanel.add(notStartedLabel);
        add(statsPanel, BorderLayout.SOUTH);
    }
}