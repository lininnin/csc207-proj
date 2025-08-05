package view.Angela;

import view.Angela.Task.OverdueTasksPanel;
import interface_adapter.Angela.task.overdue.OverdueTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import view.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * View for the Today So Far panel on the right side.
 * Currently implements only the Overdue Tasks section.
 * Other sections are placeholders for team integration.
 */
public class TodaySoFarView extends JPanel {
    private final OverdueTasksPanel overdueTasksPanel;
    private OverdueTasksController overdueTasksController;
    
    public TodaySoFarView(OverdueTasksViewModel overdueTasksViewModel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(300, 0));
        setMinimumSize(new Dimension(250, 0));
        
        // Title
        JLabel titleLabel = new JLabel("Today so far");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        
        // TODO: Goals Progress Section
        add(createPlaceholderSection("Goals", 
            "TODO: Implement goals progress\n" +
            "- Show current goals with period\n" +
            "- Display progress (e.g., 2/3)"));
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // TODO: Task Summary Section
        add(createPlaceholderSection("Task Completion rate:",
            "TODO: Calculate completion percentage\n" +
            "Progress: 0%"));
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // TODO: Completed Tasks Section
        add(createPlaceholderSection("Completed Tasks",
            "TODO: List completed tasks\n" +
            "with name and category"));
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // TODO: Wellness Log Section
        add(createPlaceholderSection("Wellness Log",
            "TODO: Show mood, stress,\n" +
            "energy, fatigue levels"));
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Overdue Tasks Section (Implemented)
        overdueTasksPanel = new OverdueTasksPanel(overdueTasksViewModel);
        overdueTasksPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        overdueTasksPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        add(overdueTasksPanel);
        
        // Add vertical glue to push content to top
        add(Box.createVerticalGlue());
    }
    
    /**
     * Creates a placeholder section for team integration.
     */
    private JPanel createPlaceholderSection(String title, String content) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontUtil.getBoldFont());
        panel.add(titleLabel);
        
        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(FontUtil.getSmallFont());
        contentArea.setForeground(Color.GRAY);
        contentArea.setEditable(false);
        contentArea.setBackground(new Color(245, 245, 245));
        panel.add(contentArea);
        
        return panel;
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }
    
    /**
     * Refreshes the overdue tasks. Should be called when tasks are updated.
     */
    public void refreshOverdueTasks() {
        overdueTasksPanel.refreshOverdueTasks(overdueTasksController);
    }
}