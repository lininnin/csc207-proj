package view.Angela.Task;

import interface_adapter.Angela.task.overdue.OverdueTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import use_case.Angela.task.overdue.OverdueTasksOutputData.OverdueTaskData;
import view.FontUtil;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;

/**
 * Panel for displaying overdue tasks.
 */
public class OverdueTasksPanel extends JPanel implements PropertyChangeListener {
    private final OverdueTasksViewModel viewModel;
    private final DefaultTableModel tableModel;
    private final JTable overdueTable;
    private final JLabel titleLabel;
    private final JLabel emptyLabel;
    private final JScrollPane scrollPane;
    
    public OverdueTasksPanel(OverdueTasksViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create title panel for centered title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        
        // Title - centered
        titleLabel = new JLabel("Task Overdue (Last 7 Days)");
        titleLabel.setFont(FontUtil.getLargeFont());
        titleLabel.setForeground(Color.RED.darker());
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235)); // Light grey
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(10));
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"Task Name", "Category", "Due Date", "Days Overdue"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };
        
        overdueTable = new JTable(tableModel);
        overdueTable.setFont(FontUtil.getStandardFont());
        overdueTable.getTableHeader().setFont(FontUtil.getBoldFont());
        overdueTable.setRowHeight(30);
        overdueTable.setShowGrid(true);
        overdueTable.setGridColor(Color.LIGHT_GRAY);
        
        // Set column widths with better proportions
        overdueTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Task Name
        overdueTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // Category
        overdueTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Due Date - increased width
        overdueTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Days Overdue
        
        // Enable auto-resize to fit panel width
        overdueTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Custom renderer for red text
        DefaultTableCellRenderer redRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.RED.darker());
                if (column == 3 && value != null) { // Days Overdue column
                    setText(value + " days");
                }
                return c;
            }
        };
        
        // Apply red renderer to all columns
        for (int i = 0; i < overdueTable.getColumnCount(); i++) {
            overdueTable.getColumnModel().getColumn(i).setCellRenderer(redRenderer);
        }
        
        scrollPane = new JScrollPane(overdueTable);
        scrollPane.setPreferredSize(new Dimension(320, 180));
        scrollPane.setMinimumSize(new Dimension(280, 120));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Ensure horizontal scrollbar appears if needed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Empty state label
        emptyLabel = new JLabel("No overdue tasks");
        emptyLabel.setFont(FontUtil.getStandardFont());
        emptyLabel.setForeground(Color.GRAY);
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Initially show empty state
        add(emptyLabel, BorderLayout.CENTER);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (OverdueTasksViewModel.OVERDUE_TASKS_STATE_PROPERTY.equals(evt.getPropertyName())) {
            OverdueTasksState state = (OverdueTasksState) evt.getNewValue();
            updateOverdueTasks(state);
        }
    }
    
    private void updateOverdueTasks(OverdueTasksState state) {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        if (state.getError() != null) {
            // Show error state
            remove(scrollPane);
            emptyLabel.setText("Error: " + state.getError());
            emptyLabel.setForeground(Color.RED);
            add(emptyLabel, BorderLayout.CENTER);
        } else if (state.getOverdueTasks().isEmpty()) {
            // Show empty state
            remove(scrollPane);
            emptyLabel.setText("No overdue tasks");
            emptyLabel.setForeground(Color.GRAY);
            add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Show table with overdue tasks
            remove(emptyLabel);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            for (OverdueTaskData task : state.getOverdueTasks()) {
                Object[] row = {
                    task.getTaskName(),
                    task.getCategoryName().isEmpty() ? "-" : task.getCategoryName(),
                    task.getDueDate() != null ? task.getDueDate().format(formatter) : "-",
                    task.getDaysOverdue()
                };
                tableModel.addRow(row);
            }
            
            add(scrollPane, BorderLayout.CENTER);
        }
        
        // Update title with count
        if (state.getTotalOverdueTasks() > 0) {
            titleLabel.setText(String.format("Task Overdue (Last 7 Days) - %d tasks", state.getTotalOverdueTasks()));
        } else {
            titleLabel.setText("Task Overdue (Last 7 Days)");
        }
        
        revalidate();
        repaint();
    }
    
    /**
     * Refreshes the overdue tasks by triggering the controller.
     * Should be called when tasks are updated elsewhere.
     */
    public void refreshOverdueTasks(OverdueTasksController controller) {
        if (controller != null) {
            controller.execute(7); // Last 7 days
        }
    }
}