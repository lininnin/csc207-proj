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
    
    // Section heading colors
    private static final Color GOALS_COLOR = new Color(30, 58, 138);       // Dark blue
    private static final Color COMPLETED_COLOR = new Color(5, 150, 105);   // Green
    private static final Color WELLNESS_COLOR = new Color(124, 58, 237);   // Purple
    
    public TodaySoFarView(OverdueTasksViewModel overdueTasksViewModel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(300, 0));
        setMinimumSize(new Dimension(250, 0));

        // Title and refresh button panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        titlePanel.add(Box.createHorizontalGlue());

        JLabel titleLabel = new JLabel("Today so far");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        titlePanel.add(Box.createHorizontalStrut(20));

        // TEMPORARY: Manual refresh button for testing
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(FontUtil.getSmallFont());
        refreshButton.addActionListener(e -> {
            System.out.println("DEBUG: Manual refresh button clicked");
            refreshOverdueTasks();
        });
        titlePanel.add(refreshButton);

        titlePanel.add(Box.createHorizontalGlue());

        add(titlePanel);
        add(Box.createVerticalStrut(15));
        
        // Goals Progress Section
        add(createSection("Goals", GOALS_COLOR, "No goals yet"));
        add(Box.createVerticalStrut(15));
        
        // Completed Tasks Section (includes completion rate)
        add(createCompletedTasksSection());
        add(Box.createVerticalStrut(15));
        
        // Wellness Log Section
        add(createSection("Wellness Log", WELLNESS_COLOR, "No wellness entries yet"));
        add(Box.createVerticalStrut(15));
        
        // Overdue Tasks Section (Implemented)
        overdueTasksPanel = new OverdueTasksPanel(overdueTasksViewModel);
        overdueTasksPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        overdueTasksPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        overdueTasksPanel.setBackground(Color.WHITE);
        add(overdueTasksPanel);
        
        // Add vertical glue to push content to top
        add(Box.createVerticalGlue());
    }
    
    /**
     * Creates a consistent section with centered title and content.
     */
    private JPanel createSection(String title, Color titleColor, String placeholderText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Section title - centered
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontUtil.getLargeFont());
        titleLabel.setForeground(titleColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235)); // Light grey
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        
        // Placeholder content
        JLabel contentLabel = new JLabel(placeholderText);
        contentLabel.setFont(FontUtil.getStandardFont());
        contentLabel.setForeground(Color.LIGHT_GRAY);
        contentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(contentLabel);
        
        return panel;
    }
    
    /**
     * Creates the Completed Tasks section with completion rate.
     */
    private JPanel createCompletedTasksSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Section title - centered
        JLabel titleLabel = new JLabel("Completed Tasks");
        titleLabel.setFont(FontUtil.getLargeFont());
        titleLabel.setForeground(COMPLETED_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235)); // Light grey
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        
        // Task Completion Rate
        JLabel completionRateLabel = new JLabel("Task Completion Rate: 0%");
        completionRateLabel.setFont(FontUtil.getStandardFont());
        completionRateLabel.setForeground(Color.DARK_GRAY);
        completionRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(completionRateLabel);
        panel.add(Box.createVerticalStrut(5));
        
        // Placeholder for completed tasks list
        JLabel contentLabel = new JLabel("No completed tasks yet");
        contentLabel.setFont(FontUtil.getStandardFont());
        contentLabel.setForeground(Color.LIGHT_GRAY);
        contentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(contentLabel);
        
        return panel;
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
        // Trigger initial refresh when controller is set
        if (controller != null) {
            System.out.println("DEBUG [TodaySoFarView]: Controller set, triggering initial refresh");
            refreshOverdueTasks();
        }
    }
    
    /**
     * Refreshes the overdue tasks. Should be called when tasks are updated.
     */
    public void refreshOverdueTasks() {
        System.out.println("DEBUG [TodaySoFarView]: refreshOverdueTasks() called");
        if (overdueTasksController != null) {
            System.out.println("DEBUG [TodaySoFarView]: Controller is available, refreshing panel");
            overdueTasksPanel.refreshOverdueTasks(overdueTasksController);
        } else {
            System.out.println("DEBUG [TodaySoFarView]: Controller is null, cannot refresh");
        }
    }
}