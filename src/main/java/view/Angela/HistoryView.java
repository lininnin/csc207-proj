package view.Angela;

import entity.alex.Event.Event;
import entity.alex.Event.EventInterf;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarState;
import interface_adapter.Angela.today_so_far.TodaySoFarViewModel;
import interface_adapter.Angela.view_history.ViewHistoryController;
import interface_adapter.Angela.view_history.ViewHistoryState;
import interface_adapter.Angela.view_history.ViewHistoryViewModel;
import interface_adapter.sophia.today_goal.TodayGoalsViewModel;
import interface_adapter.sophia.today_goal.TodaysGoalsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import view.alex.Event.TodaysEventsView;
import view.Angela.Task.TodaysTasksView;
import view.Sophia.TodayGoalView;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * History page view following the feedback page layout pattern.
 * Left side: Date list with view buttons
 * Right side: Full panels (TodaySoFar on top, Task/Event/Goal on bottom)
 */
public class HistoryView extends JPanel implements PropertyChangeListener {
    private final ViewHistoryController controller;
    private final ViewHistoryViewModel viewModel;
    
    // Date selection panel (left side)
    private JPanel dateListPanel;
    private JScrollPane dateScrollPane;
    private ButtonGroup dateButtonGroup;
    
    // Content panel (right side)
    private JPanel contentPanel;
    
    // Reusable view components
    private TodaySoFarView todaySoFarView;
    private TodaysTasksView todaysTasksView;
    private TodaysEventsView todaysEventsView;
    private TodayGoalView todayGoalView;
    
    // View models for the reusable components
    private final TodaySoFarViewModel todaySoFarViewModel;
    private final TodayTasksViewModel todayTasksViewModel;
    private final TodaysEventsViewModel todaysEventsViewModel;
    private final TodayGoalsViewModel todayGoalsViewModel;
    
    // Current selected date
    private LocalDate selectedDate;
    
    // Historical data panels
    private JPanel historicalTasksContent;
    private JPanel historicalEventsContent;
    private JPanel historicalGoalsContent;
    
    /**
     * Creates a read-only events view for displaying historical events.
     * This doesn't need controllers since the data is read-only.
     */
    private TodaysEventsView createReadOnlyEventsView() {
        // Create dummy view models for the controllers we don't need
        interface_adapter.alex.event_related.add_event.AddedEventViewModel dummyAddedVM = 
            new interface_adapter.alex.event_related.add_event.AddedEventViewModel();
        interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventViewModel dummyEditVM = 
            new interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventViewModel();
        
        // Return a TodaysEventsView with null controllers (won't be used for read-only view)
        return new TodaysEventsView(todaysEventsViewModel, null, dummyAddedVM, null, null, dummyEditVM);
    }
    
    public HistoryView(ViewHistoryController controller, 
                      ViewHistoryViewModel viewModel,
                      TodaySoFarViewModel todaySoFarViewModel,
                      TodayTasksViewModel todayTasksViewModel,
                      TodaysEventsViewModel todaysEventsViewModel,
                      TodayGoalsViewModel todayGoalsViewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.todaySoFarViewModel = todaySoFarViewModel;
        this.todayTasksViewModel = todayTasksViewModel;
        this.todaysEventsViewModel = todaysEventsViewModel;
        this.todayGoalsViewModel = todayGoalsViewModel;
        
        this.viewModel.addPropertyChangeListener(this);
        
        initializeUI();
        controller.loadAvailableDates();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(16, 0)); // 16px horizontal gap like feedback page
        setBackground(Color.WHITE);
        
        // Create date list panel (left side - similar to feedback history panel)
        dateListPanel = createDateListPanel();
        dateListPanel.setPreferredSize(new Dimension(260, 0)); // Fixed width like feedback panel
        
        // Create content panel (right side)
        contentPanel = createContentPanel();
        
        // Add panels to main layout
        add(contentPanel, BorderLayout.CENTER);
        add(dateListPanel, BorderLayout.EAST);
    }
    
    private JPanel createDateListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        // Header
        JLabel header = new JLabel("History Dates");
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        panel.add(header, BorderLayout.NORTH);
        
        // Date list container
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);
        
        dateButtonGroup = new ButtonGroup();
        
        // Scroll pane for dates
        dateScrollPane = new JScrollPane(listContainer);
        dateScrollPane.setBorder(BorderFactory.createEmptyBorder());
        dateScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(dateScrollPane, BorderLayout.CENTER);
        
        // Export button at bottom
        JButton exportButton = new JButton("Export Selected");
        exportButton.addActionListener(e -> {
            if (selectedDate != null) {
                controller.exportHistory(selectedDate);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a date first");
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(exportButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Main split: Top (TodaySoFar) and Bottom (Task/Event/Goal)
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setDividerLocation(400); // Give more space to TodaySoFar
        mainSplit.setResizeWeight(0.6); // 60% to top, 40% to bottom
        mainSplit.setBorder(BorderFactory.createEmptyBorder());
        
        // Top: Today So Far panel (reuse existing component)
        // Create a dummy OverdueTasksViewModel for history view (not used but required by constructor)
        interface_adapter.Angela.task.overdue.OverdueTasksViewModel dummyOverdueVM = 
            new interface_adapter.Angela.task.overdue.OverdueTasksViewModel();
        todaySoFarView = new TodaySoFarView(dummyOverdueVM, todaySoFarViewModel);
        JScrollPane todaySoFarScroll = new JScrollPane(todaySoFarView);
        todaySoFarScroll.setBorder(BorderFactory.createTitledBorder("Today So Far"));
        mainSplit.setTopComponent(todaySoFarScroll);
        
        // Bottom: Three panels side by side (Task, Event, Goal)
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // 3 columns with 10px gap
        bottomPanel.setBackground(Color.WHITE);
        
        // Create simple historical data display panels instead of using complex views
        JPanel historicalTasksPanel = createHistoricalTasksPanel();
        JScrollPane taskScroll = new JScrollPane(historicalTasksPanel);
        taskScroll.setBorder(BorderFactory.createTitledBorder("Historical Tasks"));
        bottomPanel.add(taskScroll);
        
        JPanel historicalEventsPanel = createHistoricalEventsPanel();
        JScrollPane eventScroll = new JScrollPane(historicalEventsPanel);
        eventScroll.setBorder(BorderFactory.createTitledBorder("Historical Events"));
        bottomPanel.add(eventScroll);
        
        JPanel historicalGoalsPanel = createHistoricalGoalsPanel();
        JScrollPane goalScroll = new JScrollPane(historicalGoalsPanel);
        goalScroll.setBorder(BorderFactory.createTitledBorder("Historical Goals"));
        bottomPanel.add(goalScroll);
        
        mainSplit.setBottomComponent(bottomPanel);
        
        panel.add(mainSplit, BorderLayout.CENTER);
        
        // Initially show empty state
        showEmptyState();
        
        return panel;
    }
    
    private JPanel createHistoricalTasksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        historicalTasksContent = new JPanel();
        historicalTasksContent.setLayout(new BoxLayout(historicalTasksContent, BoxLayout.Y_AXIS));
        historicalTasksContent.setBackground(Color.WHITE);
        
        JLabel emptyLabel = new JLabel("Select a date to view tasks", SwingConstants.CENTER);
        emptyLabel.setForeground(Color.GRAY);
        historicalTasksContent.add(emptyLabel);
        
        panel.add(historicalTasksContent, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createHistoricalEventsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        historicalEventsContent = new JPanel();
        historicalEventsContent.setLayout(new BoxLayout(historicalEventsContent, BoxLayout.Y_AXIS));
        historicalEventsContent.setBackground(Color.WHITE);
        
        JLabel emptyLabel = new JLabel("Select a date to view events", SwingConstants.CENTER);
        emptyLabel.setForeground(Color.GRAY);
        historicalEventsContent.add(emptyLabel);
        
        panel.add(historicalEventsContent, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createHistoricalGoalsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        historicalGoalsContent = new JPanel();
        historicalGoalsContent.setLayout(new BoxLayout(historicalGoalsContent, BoxLayout.Y_AXIS));
        historicalGoalsContent.setBackground(Color.WHITE);
        
        JLabel emptyLabel = new JLabel("Select a date to view goals", SwingConstants.CENTER);
        emptyLabel.setForeground(Color.GRAY);
        historicalGoalsContent.add(emptyLabel);
        
        panel.add(historicalGoalsContent, BorderLayout.CENTER);
        return panel;
    }
    
    private void updateDateList(List<LocalDate> dates) {
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(Color.WHITE);
        
        dateButtonGroup = new ButtonGroup();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        
        for (LocalDate date : dates) {
            JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
            dateRow.setBackground(Color.WHITE);
            dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Date label
            JLabel dateLabel = new JLabel(date.format(formatter));
            dateLabel.setPreferredSize(new Dimension(120, 22));
            if (date.equals(LocalDate.now())) {
                dateLabel.setText(dateLabel.getText() + " (Today)");
                dateLabel.setFont(dateLabel.getFont().deriveFont(Font.BOLD));
            }
            
            // View button (radio button style for single selection)
            JRadioButton viewButton = new JRadioButton("View");
            viewButton.setBackground(Color.WHITE);
            viewButton.addActionListener(e -> {
                selectedDate = date;
                controller.loadHistory(date);
            });
            dateButtonGroup.add(viewButton);
            
            dateRow.add(dateLabel);
            dateRow.add(viewButton);
            listContainer.add(dateRow);
        }
        
        // Update scroll pane content
        dateScrollPane.setViewportView(listContainer);
        dateScrollPane.revalidate();
        dateScrollPane.repaint();
    }
    
    private void showEmptyState() {
        // Clear all views to show empty state
        if (todaySoFarViewModel != null) {
            todaySoFarViewModel.setState(new TodaySoFarState());
        }
        
        // Clear historical data panels
        if (historicalTasksContent != null) {
            historicalTasksContent.removeAll();
            JLabel emptyLabel = new JLabel("Select a date to view tasks", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            historicalTasksContent.add(emptyLabel);
            historicalTasksContent.revalidate();
            historicalTasksContent.repaint();
        }
        
        if (historicalEventsContent != null) {
            historicalEventsContent.removeAll();
            JLabel emptyLabel = new JLabel("Select a date to view events", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            historicalEventsContent.add(emptyLabel);
            historicalEventsContent.revalidate();
            historicalEventsContent.repaint();
        }
        
        if (historicalGoalsContent != null) {
            historicalGoalsContent.removeAll();
            JLabel emptyLabel = new JLabel("Select a date to view goals", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            historicalGoalsContent.add(emptyLabel);
            historicalGoalsContent.revalidate();
            historicalGoalsContent.repaint();
        }
    }
    
    private void updateHistoricalData(ViewHistoryState state) {
        
        if (!state.hasData()) {
            showEmptyState();
            return;
        }
        
        
        // Update TodaySoFar view model with historical data
        TodaySoFarState soFarState = new TodaySoFarState();
        // Convert historical data to TodaySoFar format using TodaySoFarOutputData inner classes
        List<use_case.Angela.today_so_far.TodaySoFarOutputData.GoalProgress> goalProgressList = state.getGoalProgress().stream()
            .map(gp -> new use_case.Angela.today_so_far.TodaySoFarOutputData.GoalProgress(
                gp.getGoalName(), 
                gp.getPeriod(), 
                gp.getProgressString()))
            .toList();
        
        List<use_case.Angela.today_so_far.TodaySoFarOutputData.CompletedItem> completedItems = state.getCompletedTasks().stream()
            .map(task -> new use_case.Angela.today_so_far.TodaySoFarOutputData.CompletedItem(
                "Task",
                task.getInfo().getName(),
                task.getInfo().getCategory()))
            .toList();
        
        soFarState.setGoals(goalProgressList);
        soFarState.setCompletedItems(completedItems);
        soFarState.setCompletionRate(state.getTaskCompletionRate());
        soFarState.setWellnessEntries(new ArrayList<>()); // Empty for Angela's scope
        todaySoFarViewModel.setState(soFarState);
        todaySoFarViewModel.firePropertyChanged();
        
        
        // Update historical tasks panel
        updateHistoricalTasksPanel(state.getTodaysTasks(), state.getCompletedTasks());
        
        // Update historical events panel
        updateHistoricalEventsPanel(state.getTodaysEvents());
        
        // Update historical goals panel
        updateHistoricalGoalsPanel(state.getGoalProgress());
    }
    
    private void updateHistoricalTasksPanel(List<entity.Angela.Task.Task> allTasks, List<entity.Angela.Task.Task> completedTasks) {
        historicalTasksContent.removeAll();
        
        if (allTasks.isEmpty()) {
            JLabel emptyLabel = new JLabel("No tasks for this date", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            historicalTasksContent.add(emptyLabel);
        } else {
            for (entity.Angela.Task.Task task : allTasks) {
                JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                taskPanel.setBackground(Color.WHITE);
                taskPanel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
                
                // Completion status
                String status = task.isCompleted() ? "✓" : "○";
                JLabel statusLabel = new JLabel(status);
                statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                statusLabel.setForeground(task.isCompleted() ? new Color(34, 139, 34) : Color.GRAY);
                statusLabel.setPreferredSize(new Dimension(20, 20));
                
                // Task name
                JLabel nameLabel = new JLabel(task.getInfo().getName());
                nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                if (task.isCompleted()) {
                    nameLabel.setForeground(Color.GRAY);
                }
                
                // Category
                JLabel categoryLabel = new JLabel("(" + task.getInfo().getCategory() + ")");
                categoryLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
                categoryLabel.setForeground(Color.BLUE);
                
                taskPanel.add(statusLabel);
                taskPanel.add(nameLabel);
                taskPanel.add(categoryLabel);
                historicalTasksContent.add(taskPanel);
            }
        }
        
        historicalTasksContent.revalidate();
        historicalTasksContent.repaint();
    }
    
    private void updateHistoricalEventsPanel(List<Info> events) {
        historicalEventsContent.removeAll();
        
        if (events.isEmpty()) {
            JLabel emptyLabel = new JLabel("No events for this date", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            historicalEventsContent.add(emptyLabel);
        } else {
            for (Info event : events) {
                JPanel eventPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                eventPanel.setBackground(Color.WHITE);
                eventPanel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
                
                JLabel eventLabel = new JLabel("• " + event.getName());
                eventLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
                
                JLabel categoryLabel = new JLabel("(" + event.getCategory() + ")");
                categoryLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
                categoryLabel.setForeground(Color.BLUE);
                
                eventPanel.add(eventLabel);
                eventPanel.add(categoryLabel);
                historicalEventsContent.add(eventPanel);
            }
        }
        
        historicalEventsContent.revalidate();
        historicalEventsContent.repaint();
    }
    
    private void updateHistoricalGoalsPanel(List<entity.Angela.TodaySoFarSnapshot.GoalProgress> goals) {
        historicalGoalsContent.removeAll();
        
        if (goals.isEmpty()) {
            JLabel emptyLabel = new JLabel("No goals for this date", SwingConstants.CENTER);
            emptyLabel.setForeground(Color.GRAY);
            historicalGoalsContent.add(emptyLabel);
        } else {
            for (entity.Angela.TodaySoFarSnapshot.GoalProgress goal : goals) {
                JPanel goalPanel = new JPanel(new BorderLayout());
                goalPanel.setBackground(Color.WHITE);
                goalPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                
                JLabel nameLabel = new JLabel(goal.getGoalName());
                nameLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
                
                JLabel progressLabel = new JLabel(goal.getProgressString());
                progressLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
                progressLabel.setForeground(new Color(34, 139, 34));
                
                JLabel periodLabel = new JLabel("(" + goal.getPeriod() + ")");
                periodLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
                periodLabel.setForeground(Color.GRAY);
                
                JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                topPanel.setBackground(Color.WHITE);
                topPanel.add(nameLabel);
                topPanel.add(Box.createHorizontalStrut(8));
                topPanel.add(periodLabel);
                
                goalPanel.add(topPanel, BorderLayout.NORTH);
                goalPanel.add(progressLabel, BorderLayout.CENTER);
                
                historicalGoalsContent.add(goalPanel);
            }
        }
        
        historicalGoalsContent.revalidate();
        historicalGoalsContent.repaint();
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            ViewHistoryState state = viewModel.getState();
            
            // Update date list
            if (state.getAvailableDates() != null && !state.getAvailableDates().isEmpty()) {
                updateDateList(state.getAvailableDates());
            }
            
            // Update content panels with historical data
            updateHistoricalData(state);
            
            // Show export message if any
            if (state.getExportMessage() != null) {
                JOptionPane.showMessageDialog(this, state.getExportMessage());
            }
        }
    }
}