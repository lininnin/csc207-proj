package view.Angela.Task;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

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
        taskTable.setRowHeight(25);

        // No sample data - start with empty table

        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom stats panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statsPanel.add(new JLabel("Completed: 0/0"));
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(new JLabel("In Progress: 0"));
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(new JLabel("Not Started: 0"));
        add(statsPanel, BorderLayout.SOUTH);
    }
}