package view.Angela;

import entity.Alex.Event.Event;
import entity.Alex.Event.EventInterf;
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
import view.Alex.Event.TodaysEventsView;
import view.Angela.Task.TodaysTasksView;
import view.sophia.TodayGoalView;

import javax.swing.BorderFactory;
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
        todaySoFarView = new TodaySoFarView(null, todaySoFarViewModel);
        JScrollPane todaySoFarScroll = new JScrollPane(todaySoFarView);
        todaySoFarScroll.setBorder(BorderFactory.createTitledBorder("Today So Far"));
        mainSplit.setTopComponent(todaySoFarScroll);
        
        // Bottom: Three panels side by side (Task, Event, Goal)
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // 3 columns with 10px gap
        bottomPanel.setBackground(Color.WHITE);
        
        // Create scrollable panels for each component
        todaysTasksView = new TodaysTasksView(todayTasksViewModel);
        JScrollPane taskScroll = new JScrollPane(todaysTasksView);
        taskScroll.setBorder(BorderFactory.createTitledBorder("Today's Tasks"));
        bottomPanel.add(taskScroll);
        
        // For history view, we don't need edit/delete controllers since it's read-only
        // Create a simple panel to display events instead of using TodaysEventsView
        todaysEventsView = createReadOnlyEventsView();
        JScrollPane eventScroll = new JScrollPane(todaysEventsView);
        eventScroll.setBorder(BorderFactory.createTitledBorder("Today's Events"));
        bottomPanel.add(eventScroll);
        
        todayGoalView = new TodayGoalView(todayGoalsViewModel, null);
        JScrollPane goalScroll = new JScrollPane(todayGoalView);
        goalScroll.setBorder(BorderFactory.createTitledBorder("Current Goals"));
        bottomPanel.add(goalScroll);
        
        mainSplit.setBottomComponent(bottomPanel);
        
        panel.add(mainSplit, BorderLayout.CENTER);
        
        // Initially show empty state
        showEmptyState();
        
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
        if (todayTasksViewModel != null) {
            todayTasksViewModel.setState(new TodayTasksState());
        }
        if (todaysEventsViewModel != null) {
            todaysEventsViewModel.setState(new TodaysEventsState());
        }
        if (todayGoalsViewModel != null) {
            todayGoalsViewModel.setState(new TodaysGoalsState());
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
        // wellness entries - TODO: convert format
        todaySoFarViewModel.setState(soFarState);
        
        // Update Today's Tasks view model with historical tasks
        TodayTasksState tasksState = new TodayTasksState();
        // TodayTasksState doesn't store tasks directly, it just triggers refresh
        tasksState.setRefreshNeeded(true);
        todayTasksViewModel.setState(tasksState);
        
        // Update Today's Events view model with historical events
        final TodaysEventsState eventsState = new TodaysEventsState();
        // Convert Info objects to EventInterf objects
        final List<EventInterf> events = new ArrayList<>();
        for (Info info : state.getTodaysEvents()) {
            // Create a simple Event wrapper with today as the due date (historical data)
            Event event = new Event.Builder(info)
                .beginAndDueDates(new BeginAndDueDates(LocalDate.now(), LocalDate.now()))
                .oneTime(false)
                .build();
            events.add(event);
        }
        eventsState.setTodaysEvents(events);
        todaysEventsViewModel.setState(eventsState);
        
        // Update Today's Goals view model
        final TodaysGoalsState goalsState = new TodaysGoalsState();
        // Convert goal progress to Goal format if needed
        // For now, set empty list as we don't have Goal entities in the state
        goalsState.setTodayGoals(new ArrayList<>());
        todayGoalsViewModel.setState(goalsState);
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