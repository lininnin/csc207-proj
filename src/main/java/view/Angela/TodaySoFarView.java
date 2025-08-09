package view.Angela;

import view.Angela.Task.OverdueTasksPanel;
import interface_adapter.Angela.task.overdue.OverdueTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarState;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.Angela.today_so_far.TodaySoFarOutputData;
import view.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;

/**
 * View for the Today So Far panel on the right side.
 * Currently implements only the Overdue Tasks section.
 * Other sections are placeholders for team integration.
 */
public class TodaySoFarView extends JPanel implements PropertyChangeListener {
    private OverdueTasksPanel overdueTasksPanel;
    private OverdueTasksController overdueTasksController;
    private TodaySoFarController todaySoFarController;
    private TodaySoFarViewModel todaySoFarViewModel;
    private JSplitPane mainSplitPane;
    private JPanel contentPanel;
    
    // Section panels for dynamic updates
    private JPanel goalsPanel;
    private JPanel completedItemsPanel;
    private JPanel wellnessPanel;
    private JLabel completionRateLabel;
    
    // Section heading colors
    private static final Color GOALS_COLOR = new Color(30, 58, 138);       // Dark blue
    private static final Color COMPLETED_COLOR = new Color(5, 150, 105);   // Green
    private static final Color WELLNESS_COLOR = new Color(124, 58, 237);   // Purple
    private static final Color OVERDUE_COLOR = new Color(220, 38, 38);     // Red
    
    public TodaySoFarView(OverdueTasksViewModel overdueTasksViewModel, TodaySoFarViewModel todaySoFarViewModel) {
        this.todaySoFarViewModel = todaySoFarViewModel;
        if (todaySoFarViewModel != null) {
            todaySoFarViewModel.addPropertyChangeListener(this);
        }
        
        initialize(overdueTasksViewModel);
    }
    
    // Constructor for backward compatibility
    public TodaySoFarView(OverdueTasksViewModel overdueTasksViewModel) {
        initialize(overdueTasksViewModel);
    }
    
    private void initialize(OverdueTasksViewModel overdueTasksViewModel) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        // Don't set preferred size here as it's handled by the wrapper
        setMinimumSize(new Dimension(320, 400));

        // Create main content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setOpaque(true);

        // Title Panel to ensure visibility
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JLabel titleLabel = new JLabel("Today So Far");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Goals Progress Section
        JPanel goalsContainer = createGoalsSection();
        goalsContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(goalsContainer);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Completed Tasks Section (includes completion rate)
        JPanel completedContainer = createCompletedTasksSection();
        completedContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(completedContainer);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Wellness Log Section
        JPanel wellnessContainer = createWellnessSection();
        wellnessContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(wellnessContainer);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Overdue Tasks Section (Improved layout)
        if (overdueTasksViewModel != null) {
            overdueTasksPanel = createImprovedOverdueSection(overdueTasksViewModel);
            overdueTasksPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(overdueTasksPanel);
        }
        
        // Add vertical glue to push content to top
        contentPanel.add(Box.createVerticalGlue());
        
        // Add content panel to scroll pane for vertical scrollability
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
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
     * Creates the Goals section with name, period, and progress.
     */
    private JPanel createGoalsSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Section title
        JLabel titleLabel = new JLabel("Goals");
        titleLabel.setFont(FontUtil.getLargeFont());
        titleLabel.setForeground(GOALS_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        
        // Initialize goals panel as member variable
        goalsPanel = new JPanel();
        goalsPanel.setLayout(new BorderLayout());
        goalsPanel.setBackground(Color.WHITE);
        goalsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Add initial placeholder content
        JPanel tablePanel = new JPanel(new GridLayout(0, 3, 5, 5));
        tablePanel.setBackground(Color.WHITE);
        
        // Headers
        JLabel nameHeader = new JLabel("Goal Name");
        nameHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(nameHeader);
        
        JLabel periodHeader = new JLabel("Period");
        periodHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(periodHeader);
        
        JLabel progressHeader = new JLabel("Progress");
        progressHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(progressHeader);
        
        // Placeholder
        JLabel placeholder = new JLabel("No goals yet");
        placeholder.setFont(FontUtil.getStandardFont());
        placeholder.setForeground(Color.LIGHT_GRAY);
        tablePanel.add(placeholder);
        tablePanel.add(new JLabel(""));
        tablePanel.add(new JLabel(""));
        
        goalsPanel.add(tablePanel, BorderLayout.CENTER);
        panel.add(goalsPanel);
        
        return panel;
    }
    
    /**
     * Creates the Completed Tasks/Events section with Type, Name, Category columns.
     */
    private JPanel createCompletedTasksSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Section title
        JLabel titleLabel = new JLabel("Completed Tasks & Events");
        titleLabel.setFont(FontUtil.getLargeFont());
        titleLabel.setForeground(COMPLETED_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        
        // Task Completion Rate
        completionRateLabel = new JLabel("Completion Rate: 0%");
        completionRateLabel.setFont(FontUtil.getStandardFont());
        completionRateLabel.setForeground(Color.DARK_GRAY);
        completionRateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(completionRateLabel);
        panel.add(Box.createVerticalStrut(10));
        
        // Initialize completed items panel as member variable
        completedItemsPanel = new JPanel();
        completedItemsPanel.setLayout(new BorderLayout());
        completedItemsPanel.setBackground(Color.WHITE);
        completedItemsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Add initial placeholder content
        JPanel tablePanel = new JPanel(new GridLayout(0, 3, 5, 5));
        tablePanel.setBackground(Color.WHITE);
        
        // Headers
        JLabel typeHeader = new JLabel("Type");
        typeHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(typeHeader);
        
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(nameHeader);
        
        JLabel categoryHeader = new JLabel("Category");
        categoryHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(categoryHeader);
        
        // Placeholder
        JLabel placeholder = new JLabel("No completed items");
        placeholder.setFont(FontUtil.getStandardFont());
        placeholder.setForeground(Color.LIGHT_GRAY);
        tablePanel.add(placeholder);
        tablePanel.add(new JLabel(""));
        tablePanel.add(new JLabel(""));
        
        completedItemsPanel.add(tablePanel, BorderLayout.CENTER);
        panel.add(completedItemsPanel);
        
        return panel;
    }
    
    /**
     * Creates the Wellness section with Mood, Stress, Energy, Fatigue, Time columns.
     */
    private JPanel createWellnessSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        // Section title
        JLabel titleLabel = new JLabel("Wellness Log");
        titleLabel.setFont(FontUtil.getLargeFont());
        titleLabel.setForeground(WELLNESS_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(229, 231, 235));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        
        // Initialize wellness panel as member variable
        wellnessPanel = new JPanel();
        wellnessPanel.setLayout(new BorderLayout());
        wellnessPanel.setBackground(Color.WHITE);
        wellnessPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // Add initial placeholder content
        JPanel tablePanel = new JPanel(new GridLayout(0, 5, 5, 5));
        tablePanel.setBackground(Color.WHITE);
        
        // Headers
        String[] headers = {"Mood", "Stress", "Energy", "Fatigue", "Time"};
        for (String header : headers) {
            JLabel headerLabel = new JLabel(header);
            headerLabel.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
            tablePanel.add(headerLabel);
        }
        
        // Placeholder
        JLabel placeholder = new JLabel("No wellness entries");
        placeholder.setFont(FontUtil.getStandardFont());
        placeholder.setForeground(Color.LIGHT_GRAY);
        tablePanel.add(placeholder);
        for (int i = 1; i < 5; i++) {
            tablePanel.add(new JLabel(""));
        }
        
        wellnessPanel.add(tablePanel, BorderLayout.CENTER);
        panel.add(wellnessPanel);
        
        return panel;
    }
    
    /**
     * Creates an improved Overdue Tasks section with better layout.
     */
    private OverdueTasksPanel createImprovedOverdueSection(OverdueTasksViewModel overdueTasksViewModel) {
        OverdueTasksPanel panel = new OverdueTasksPanel(overdueTasksViewModel);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Set proper sizing for the overdue panel
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        panel.setPreferredSize(new Dimension(350, 200));
        panel.setBackground(Color.WHITE);
        return panel;
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
        // Trigger initial refresh when controller is set
        if (controller != null) {
            refreshOverdueTasks();
        }
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
        // Trigger initial refresh when controller is set
        if (controller != null) {
            controller.refresh();
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TodaySoFarViewModel.TODAY_SO_FAR_STATE_PROPERTY.equals(evt.getPropertyName())) {
            TodaySoFarState state = (TodaySoFarState) evt.getNewValue();
            updateDisplay(state);
        }
    }
    
    private void updateDisplay(TodaySoFarState state) {
        if (state == null) return;
        
        // Update Goals section
        updateGoalsPanel(state.getGoals());
        
        // Update Completed Items section
        updateCompletedItemsPanel(state.getCompletedItems());
        completionRateLabel.setText("Completion Rate: " + state.getCompletionRate() + "%");
        
        // Update Wellness section
        updateWellnessPanel(state.getWellnessEntries());
        
        // Force the entire panel to refresh
        contentPanel.revalidate();
        contentPanel.repaint();
        revalidate();
        repaint();
    }
    
    private void updateGoalsPanel(java.util.List<TodaySoFarOutputData.GoalProgress> goals) {
        goalsPanel.removeAll();
        
        // Create table with headers
        JPanel tablePanel = new JPanel(new GridLayout(0, 3, 5, 5));
        tablePanel.setBackground(Color.WHITE);
        
        // Headers
        JLabel nameHeader = new JLabel("Goal Name");
        nameHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(nameHeader);
        
        JLabel periodHeader = new JLabel("Period");
        periodHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(periodHeader);
        
        JLabel progressHeader = new JLabel("Progress");
        progressHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(progressHeader);
        
        if (goals == null || goals.isEmpty()) {
            // Placeholder
            JLabel placeholder = new JLabel("No goals yet");
            placeholder.setFont(FontUtil.getStandardFont());
            placeholder.setForeground(Color.LIGHT_GRAY);
            tablePanel.add(placeholder);
            tablePanel.add(new JLabel(""));
            tablePanel.add(new JLabel(""));
        } else {
            // Add goal data
            for (TodaySoFarOutputData.GoalProgress goal : goals) {
                JLabel nameLabel = new JLabel(goal.getName());
                nameLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(nameLabel);
                
                JLabel periodLabel = new JLabel(goal.getPeriod());
                periodLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(periodLabel);
                
                JLabel progressLabel = new JLabel(goal.getProgress());
                progressLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(progressLabel);
            }
        }
        
        goalsPanel.add(tablePanel, BorderLayout.CENTER);
        goalsPanel.revalidate();
        goalsPanel.repaint();
    }
    
    private void updateCompletedItemsPanel(java.util.List<TodaySoFarOutputData.CompletedItem> items) {
        completedItemsPanel.removeAll();
        
        // Create table with headers
        JPanel tablePanel = new JPanel(new GridLayout(0, 3, 5, 5));
        tablePanel.setBackground(Color.WHITE);
        
        // Headers
        JLabel typeHeader = new JLabel("Type");
        typeHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(typeHeader);
        
        JLabel nameHeader = new JLabel("Name");
        nameHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(nameHeader);
        
        JLabel categoryHeader = new JLabel("Category");
        categoryHeader.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
        tablePanel.add(categoryHeader);
        
        if (items == null || items.isEmpty()) {
            // Placeholder
            JLabel placeholder = new JLabel("No completed items");
            placeholder.setFont(FontUtil.getStandardFont());
            placeholder.setForeground(Color.LIGHT_GRAY);
            tablePanel.add(placeholder);
            tablePanel.add(new JLabel(""));
            tablePanel.add(new JLabel(""));
        } else {
            // Add completed items data
            for (TodaySoFarOutputData.CompletedItem item : items) {
                JLabel typeLabel = new JLabel(item.getType());
                typeLabel.setFont(FontUtil.getStandardFont());
                if ("Task".equals(item.getType())) {
                    typeLabel.setForeground(COMPLETED_COLOR);
                } else {
                    typeLabel.setForeground(GOALS_COLOR);
                }
                tablePanel.add(typeLabel);
                
                JLabel nameLabel = new JLabel(item.getName());
                nameLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(nameLabel);
                
                JLabel categoryLabel = new JLabel(item.getCategory());
                categoryLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(categoryLabel);
            }
        }
        
        completedItemsPanel.add(tablePanel, BorderLayout.CENTER);
        completedItemsPanel.revalidate();
        completedItemsPanel.repaint();
    }
    
    private void updateWellnessPanel(java.util.List<TodaySoFarOutputData.WellnessEntry> entries) {
        wellnessPanel.removeAll();
        
        // Create table with headers
        JPanel tablePanel = new JPanel(new GridLayout(0, 5, 5, 5));
        tablePanel.setBackground(Color.WHITE);
        
        // Headers
        String[] headers = {"Mood", "Stress", "Energy", "Fatigue", "Time"};
        for (String header : headers) {
            JLabel headerLabel = new JLabel(header);
            headerLabel.setFont(FontUtil.getStandardFont().deriveFont(Font.BOLD));
            tablePanel.add(headerLabel);
        }
        
        if (entries == null || entries.isEmpty()) {
            // Placeholder
            JLabel placeholder = new JLabel("No wellness entries");
            placeholder.setFont(FontUtil.getStandardFont());
            placeholder.setForeground(Color.LIGHT_GRAY);
            tablePanel.add(placeholder);
            for (int i = 1; i < 5; i++) {
                tablePanel.add(new JLabel(""));
            }
        } else {
            // Add wellness data
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            for (TodaySoFarOutputData.WellnessEntry entry : entries) {
                JLabel moodLabel = new JLabel(entry.getMood());
                moodLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(moodLabel);
                
                JLabel stressLabel = new JLabel(String.valueOf(entry.getStress()));
                stressLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(stressLabel);
                
                JLabel energyLabel = new JLabel(String.valueOf(entry.getEnergy()));
                energyLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(energyLabel);
                
                JLabel fatigueLabel = new JLabel(String.valueOf(entry.getFatigue()));
                fatigueLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(fatigueLabel);
                
                JLabel timeLabel = new JLabel(entry.getTime().format(timeFormatter));
                timeLabel.setFont(FontUtil.getStandardFont());
                tablePanel.add(timeLabel);
            }
        }
        
        wellnessPanel.add(tablePanel, BorderLayout.CENTER);
        wellnessPanel.revalidate();
        wellnessPanel.repaint();
    }
    
    /**
     * Refreshes the overdue tasks. Should be called when tasks are updated.
     */
    public void refreshOverdueTasks() {
        if (overdueTasksController != null) {
            overdueTasksPanel.refreshOverdueTasks(overdueTasksController);
        }
    }
}