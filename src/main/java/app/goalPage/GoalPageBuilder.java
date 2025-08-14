package app.goalPage;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;                // Goal domain model
import entity.Sophia.GoalFactory;         // Factory for creating goals

// Import view models
import entity.info.Info;
import data_access.InMemoryTaskGateway;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.Sophia.available_goals.AvailableGoalsViewModel;
import interface_adapter.Sophia.create_goal.CreatedGoalViewModel;
import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalViewModel;
import interface_adapter.Sophia.today_goal.TodayGoalsViewModel;
import interface_adapter.Sophia.order_goal.OrderedGoalViewModel;

// Import controllers
import interface_adapter.Sophia.available_goals.AvailableGoalsController;
import interface_adapter.Sophia.create_goal.CreateGoalController;
import interface_adapter.Sophia.delete_goal.DeleteGoalController;
import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalController;
import interface_adapter.Sophia.today_goal.TodayGoalController;
import interface_adapter.Sophia.order_goal.OrderGoalController;

// Import presenters
import interface_adapter.Sophia.available_goals.AvailableGoalsPresenter;
import interface_adapter.Sophia.create_goal.CreateGoalPresenter;
import interface_adapter.Sophia.delete_goal.DeleteGoalPresenter;
import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalPresenter;
import interface_adapter.Sophia.today_goal.TodayGoalPresenter;
import interface_adapter.Sophia.order_goal.OrderGoalPresenter;

// Import use case interactors and boundaries
import use_case.goalManage.available_goals.*;
import use_case.goalManage.create_goal.*;
import use_case.goalManage.delete_goal.*;
import use_case.goalManage.edit_todays_goal.*;
import use_case.goalManage.order_goal.*;
import use_case.goalManage.today_goal.*;

// Import data access
import data_access.FileGoalRepository;  // File-based goal repository

// Import views
import views.*;
import view.CollapsibleSidebarView;    // Collapsible sidebar component
import view.Angela.TodaySoFarView;

// Import Java/Swing components
import javax.swing.*;
import javax.swing.DefaultListCellRenderer;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Main builder class that constructs the goal management page UI.
 * Implements the complete goal tracking interface with:
 * - Goal creation form
 * - Today's goals view
 * - Available goals list
 * - Goal management controls
 */
public class GoalPageBuilder {
    // Data access components
    private FileGoalRepository goalRepository;  // Handles goal persistence (implements both interfaces)
    private GoalFactory goalFactory;       // Creates goal objects
    private InMemoryTaskGateway taskGateway; // For accessing available tasks

    // Form reference for goal creation
    private JPanel createGoalForm;

    private JComboBox<TaskAvailable> targetTaskBox;


    // View Models (hold state for different UI sections)
    private CreatedGoalViewModel createdGoalViewModel;       // Tracks new goal creation state
    private AvailableGoalsViewModel availableGoalsViewModel; // Manages available goals list
    private EditTodaysGoalViewModel editTodaysGoalViewModel; // Tracks today's goals editing
    private TodayGoalsViewModel todayGoalsViewModel;         // Manages today's goals display
    private OrderedGoalViewModel orderedGoalViewModel;       // Tracks goal ordering state
    private AvailableGoalsView availableGoalsView;

    // Controllers (handle user actions)
    private CreateGoalController createGoalController;       // Handles goal creation
    private DeleteGoalController deleteGoalController;       // Manages goal deletion
    private EditTodaysGoalController editTodaysGoalController; // Edits today's goals
    private TodayGoalController todayGoalController;         // Manages today's goals
    private OrderGoalController orderGoalController;         // Handles goal ordering
    private AvailableGoalsController availableGoalsController; // Manages available goals
    
    // Presenters (needed for wiring TodaySoFarController)
    private CreateGoalPresenter createGoalPresenter;
    private TodayGoalPresenter todayGoalPresenter;

    /**
     * Main build method that constructs the complete goal page
     * @return JPanel containing the fully assembled UI
     */
    public JPanel build() {
        initializeViewModels();     // Set up view models
        initializeDataAccess();     // Configure data persistence
        initializeUseCases();       // Set up business logic components
        return createMainPanel();   // Build and return the complete UI
    }

    /**
     * Initializes all view models with fresh instances
     */
    private void initializeViewModels() {
        createdGoalViewModel = new CreatedGoalViewModel();
        availableGoalsViewModel = new AvailableGoalsViewModel();
        editTodaysGoalViewModel = new EditTodaysGoalViewModel();
        todayGoalsViewModel = new TodayGoalsViewModel();
        orderedGoalViewModel = new OrderedGoalViewModel();
    }

    /**
     * Sets up data access layer with file-based persistence
     */
    private void initializeDataAccess() {
        // Use shared goal repository
        goalRepository = app.SharedDataAccess.getInstance().getGoalRepository();
        goalFactory = new GoalFactory();
        
        // Use shared task gateway to access available tasks
        taskGateway = app.SharedDataAccess.getInstance().getTaskGateway();
    }

    /**
     * Configures all use cases with their dependencies
     */
    private void initializeUseCases() {
        // Goal Creation Setup
        createGoalPresenter = new CreateGoalPresenter();
        CreateGoalInputBoundary createGoalInteractor = new CreateGoalInteractor(
                goalRepository, createGoalPresenter, goalFactory);
        createGoalController = new CreateGoalController(createGoalInteractor);

        // Goal Deletion Setup
        DeleteGoalPresenter deleteGoalPresenter = new DeleteGoalPresenter(
                availableGoalsViewModel, todayGoalsViewModel
        );

        DeleteGoalInteractor deleteGoalInteractor = new DeleteGoalInteractor(goalRepository, deleteGoalPresenter);
        DeleteGoalController deleteGoalController = new DeleteGoalController(deleteGoalInteractor);

        deleteGoalController.setPresenter(deleteGoalPresenter);


        // Today's Goals Editing Setup
        EditTodaysGoalOutputBoundary editTodaysGoalPresenter = new EditTodaysGoalPresenter();
        EditTodaysGoalInputBoundary editTodaysGoalInteractor = new EditTodaysGoalInteractor(
                goalRepository, editTodaysGoalPresenter);
        editTodaysGoalController = new EditTodaysGoalController(editTodaysGoalInteractor);

        // Today's Goals Management Setup
        todayGoalPresenter = new TodayGoalPresenter(todayGoalsViewModel);
        TodayGoalInputBoundary todayGoalInteractor = new TodayGoalInteractor(
                goalRepository, todayGoalPresenter);
        todayGoalController = new TodayGoalController((TodayGoalInteractor) todayGoalInteractor);

        // Goal Ordering Setup
        OrderGoalsOutputBoundary orderGoalPresenter = new OrderGoalPresenter();
        OrderGoalsInputBoundary orderGoalInteractor = new OrderGoalsInteractor(
                goalRepository, orderGoalPresenter);
        orderGoalController = new OrderGoalController(orderGoalInteractor);

        // Available Goals Management Setup
        AvailableGoalsOutputBoundary availableGoalsPresenter = new AvailableGoalsPresenter(availableGoalsViewModel);
        AvailableGoalsInputBoundary availableGoalsInteractor = new AvailableGoalsInteractor(
                goalRepository, availableGoalsPresenter);
        availableGoalsController = new AvailableGoalsController(
                availableGoalsInteractor,
                todayGoalInteractor,
                deleteGoalInteractor
        );
    }

    private JButton createAddButtonForAvailableGoals() {
        JButton addButton = new JButton("Add Selected Goal to Today");
        addButton.addActionListener(e -> {
            try {
                // ASSUMPTION: The AvailableGoalsView has a public method getSelectedGoal()
                Goal selectedGoal = availableGoalsView.getSelectedGoal();
                if (selectedGoal != null) {
                    // ASSUMPTION: The AvailableGoalsController has a public method
                    // addSelectedGoalToToday() that handles the use case logic.
                    // This method will need to be added to the controller class.
                    availableGoalsController.addSelectedGoalToToday(selectedGoal);
                    JOptionPane.showMessageDialog(null, "Goal added to today's list!");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Please select a goal to add.", "No Goal Selected",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error adding goal: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        return addButton;
    }



    /**
     * Constructs the main application panel with all UI components
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Build component sections
        JPanel sidebarPanel = createSidebarPanel();    // Left navigation
        JPanel centerPanel = createCenterPanel();      // Main content area

        // Combine components with collapsible sidebar
        CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sidebarPanel, centerPanel);
        mainPanel.add(collapsibleCenter, BorderLayout.CENTER);

        // Load initial data
        availableGoalsController.execute("");  // Load available goals
        todayGoalController.execute();         // Load today's goals

        return mainPanel;
    }

    /**
     * Creates the left navigation sidebar
     */
    private JPanel createSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(60, 63, 65));
        sidebarPanel.setPreferredSize(new Dimension(200, 700));

        // Sidebar menu items
        String[] buttonLabels = {
                "📋 Tasks", "📆 Events", "🎯 Goals",
                "🧠 Wellness Log", "📊 Charts",
                "🤖 AI-Feedback", "⚙️ Settings"
        };

        // Add all navigation buttons
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBackground(new Color(70, 73, 75));
            button.setForeground(Color.YELLOW);
            button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createVerticalStrut(10));
        }

        return sidebarPanel;
    }

    /**
     * Creates the main content center panel containing:
     * - Goal creation form
     * - Today's goals view
     * - Available goals list
     * - Today So Far panel
     */
    private JPanel createCenterPanel() {
        // Goal Creation Form
        JPanel createGoalForm = createGoalFormPanel();
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.add(createGoalForm, BorderLayout.CENTER);
        formContainer.add(createGoalButton(), BorderLayout.SOUTH);

        // Today's Goals Editor View
        EditTodayGoalView editTodayGoalView = new EditTodayGoalView(
                editTodaysGoalViewModel,
                deleteGoalController,
                editTodaysGoalController,
                todayGoalController,
                orderGoalController
        );

        // Today's Goals Display
        TodayGoalView todayGoalView = new TodayGoalView(todayGoalsViewModel, todayGoalController);
        JPanel todayGoalsContainer = new JPanel(new BorderLayout());
        todayGoalsContainer.setBorder(BorderFactory.createTitledBorder("Today's Goals"));
        todayGoalsContainer.add(todayGoalView, BorderLayout.CENTER);

        // Available Goals View
        AvailableGoalsView availableGoalsView = createAvailableGoalsView();
        JPanel availableGoalsContainer = new JPanel(new BorderLayout());
        availableGoalsContainer.add(availableGoalsView, BorderLayout.CENTER);
        availableGoalsContainer.setPreferredSize(new Dimension(800, 300));
        availableGoalsContainer.setBackground(Color.GRAY);

        // Combine components with split pane
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                formContainer, editTodayGoalView);
        verticalSplit.setResizeWeight(0.5);
        verticalSplit.setDividerSize(2);
        verticalSplit.setEnabled(false);

        JPanel topCenterRow = new JPanel(new GridLayout(1, 2));
        topCenterRow.add(verticalSplit);
        topCenterRow.add(todayGoalsContainer);

        JPanel centerContent = new JPanel(new BorderLayout());
        centerContent.add(topCenterRow, BorderLayout.CENTER);
        centerContent.add(availableGoalsContainer, BorderLayout.SOUTH);
        
        // Create Today So Far panel
        TodaySoFarView todaySoFarView = createTodaySoFarPanel();
        
        // Wrap Today So Far in a panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(todaySoFarView, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(380, 0));
        rightPanel.setMinimumSize(new Dimension(320, 0));
        
        // Create horizontal split pane for resizable layout
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerContent, rightPanel);
        mainSplitPane.setDividerLocation(870);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(0.7);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(mainSplitPane, BorderLayout.CENTER);

        return centerPanel;
    }

    /**
     * Creates the goal creation form with all input fields
     */
    private JPanel createGoalFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Goal"));

        int verticalGap = 4;

        // Goal Name (vertical)
        JTextField goalNameField = createLabeledTextField(formPanel, "Goal Name:", "", verticalGap);

        // Goal Description (vertical)
        JTextField goalDescriptionField = createLabeledTextField(formPanel, "Description (Optional):", "", verticalGap);

        // Target Amount & Current Amount (horizontal)
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.X_AXIS));
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel targetAmountPanel = new JPanel();
        targetAmountPanel.setLayout(new BoxLayout(targetAmountPanel, BoxLayout.Y_AXIS));
        targetAmountPanel.add(createCompactLabel("Target Amount:"));
        JTextField targetAmountField = new JTextField("0"); // Changed default to "0"
        targetAmountField.setMaximumSize(new Dimension(150, 25));
        targetAmountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        targetAmountPanel.add(targetAmountField);
        targetAmountPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10)); // spacing

        JPanel currentAmountPanel = new JPanel();
        currentAmountPanel.setLayout(new BoxLayout(currentAmountPanel, BoxLayout.Y_AXIS));
        currentAmountPanel.add(createCompactLabel("Current Amount:"));
        JTextField currentAmountField = new JTextField("0"); // Changed default to "0"
        currentAmountField.setMaximumSize(new Dimension(150, 25));
        currentAmountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentAmountPanel.add(currentAmountField);

        amountPanel.add(targetAmountPanel);
        amountPanel.add(currentAmountPanel);
        formPanel.add(amountPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        // Start Date & End Date (horizontal)
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel startDatePanel = new JPanel();
        startDatePanel.setLayout(new BoxLayout(startDatePanel, BoxLayout.Y_AXIS));
        startDatePanel.add(createCompactLabel("Start Date (YYYY-MM-DD):"));
        JTextField startDateField = new JTextField(LocalDate.now().toString());
        startDateField.setMaximumSize(new Dimension(150, 25));
        startDateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        startDatePanel.add(startDateField);
        startDatePanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10)); // spacing

        JPanel endDatePanel = new JPanel();
        endDatePanel.setLayout(new BoxLayout(endDatePanel, BoxLayout.Y_AXIS));
        endDatePanel.add(createCompactLabel("End Date (YYYY-MM-DD):"));
        JTextField endDateField = new JTextField(LocalDate.now().plusDays(7).toString());
        endDateField.setMaximumSize(new Dimension(150, 25));
        endDateField.setAlignmentX(Component.LEFT_ALIGNMENT);
        endDatePanel.add(endDateField);

        datePanel.add(startDatePanel);
        datePanel.add(endDatePanel);
        formPanel.add(datePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        // Time Period & Frequency (horizontal)
        JPanel timeFreqPanel = new JPanel();
        timeFreqPanel.setLayout(new BoxLayout(timeFreqPanel, BoxLayout.X_AXIS));
        timeFreqPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel timePeriodPanel = new JPanel();
        timePeriodPanel.setLayout(new BoxLayout(timePeriodPanel, BoxLayout.Y_AXIS));
        timePeriodPanel.add(createCompactLabel("Time Period:"));
        JComboBox<String> timePeriodBox = new JComboBox<>(new String[]{"WEEK", "MONTH"});
        timePeriodBox.setMaximumSize(new Dimension(150, 25));
        timePeriodBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        timePeriodPanel.add(timePeriodBox);
        timePeriodPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10)); // spacing

        JPanel frequencyPanel = new JPanel();
        frequencyPanel.setLayout(new BoxLayout(frequencyPanel, BoxLayout.Y_AXIS));
        frequencyPanel.add(createCompactLabel("Frequency:"));
        JTextField frequencyField = new JTextField("1");
        frequencyField.setMaximumSize(new Dimension(150, 25));
        frequencyField.setAlignmentX(Component.LEFT_ALIGNMENT);
        frequencyPanel.add(frequencyField);

        timeFreqPanel.add(timePeriodPanel);
        timeFreqPanel.add(frequencyPanel);
        formPanel.add(timeFreqPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        // Target Task (vertical) with refresh button
        JPanel targetTaskPanel = new JPanel();
        targetTaskPanel.setLayout(new BoxLayout(targetTaskPanel, BoxLayout.X_AXIS));
        targetTaskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel targetTaskLabel = createCompactLabel("Target Task:");
        targetTaskLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        targetTaskPanel.add(targetTaskLabel);
        
        JButton refreshTasksButton = new JButton("Refresh");
        refreshTasksButton.setMaximumSize(new Dimension(80, 25));
        refreshTasksButton.addActionListener(e -> refreshTargetTaskDropdown());
        targetTaskPanel.add(refreshTasksButton);
        
        formPanel.add(targetTaskPanel);

        // Get real available tasks from the task gateway
        List<TaskAvailable> availableTasks = taskGateway.getAvailableTaskTemplates();
        
        // Create a custom renderer to display task names in the combo box
        targetTaskBox = new JComboBox<>(availableTasks.toArray(new TaskAvailable[0]));
        targetTaskBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TaskAvailable) {
                    TaskAvailable task = (TaskAvailable) value;
                    setText(task.getInfo().getName());
                }
                return this;
            }
        });
        targetTaskBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        targetTaskBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        formPanel.add(targetTaskBox);
        formPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

        this.createGoalForm = formPanel;
        return formPanel;
    }


    private JLabel createCompactLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0)); // small vertical padding
        return label;
    }

    private JTextField createLabeledTextField(JPanel panel, String label, String defaultText, int gap) {
        JLabel jLabel = createCompactLabel(label);
        panel.add(jLabel);

        JTextField textField = new JTextField(defaultText);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(textField);

        panel.add(Box.createRigidArea(new Dimension(0, gap)));
        return textField;
    }



    private JTextField createLabeledTextField(JPanel panel, String label, String defaultText) {
        JLabel jLabel = new JLabel(label);
        jLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(jLabel);

        JTextField textField = new JTextField(defaultText);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(textField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));  // spacing after each field

        return textField;
    }


    /**
     * Creates the goal creation submit button
     */
    private JButton createGoalButton() {
        JButton createButton = new JButton("Create Goal");
        createButton.addActionListener(e -> handleCreateGoal());
        return createButton;
    }

    /**
     * Handles goal creation form submission
     */
    private void handleCreateGoal() {
        try {
            // Retrieve and validate input from the form
            String goalName = GoalInputValidator.validateString(
                    ((JTextField) createGoalForm.getComponent(1)).getText(), "Goal Name");

            // The description field is now at component index 4
            String description = ((JTextField) createGoalForm.getComponent(4)).getText();

            // The amount panel has shifted to component index 6
            JPanel amountPanel = (JPanel) createGoalForm.getComponent(6);
            JPanel targetAmountPanel = (JPanel) amountPanel.getComponent(0);
            double targetAmount = GoalInputValidator.validatePositiveInteger(
                    ((JTextField) targetAmountPanel.getComponent(1)).getText(), "Target Amount");

            JPanel currentAmountPanel = (JPanel) amountPanel.getComponent(1);
            double currentAmount = GoalInputValidator.validatePositiveInteger(
                    ((JTextField) currentAmountPanel.getComponent(1)).getText(), "Current Amount");

            // The date panel has shifted to component index 8
            JPanel datePanel = (JPanel) createGoalForm.getComponent(8);
            JPanel startDatePanel = (JPanel) datePanel.getComponent(0);
            LocalDate startDate = GoalInputValidator.validateDate(
                    ((JTextField) startDatePanel.getComponent(1)).getText(), "Start Date");

            JPanel endDatePanel = (JPanel) datePanel.getComponent(1);
            LocalDate endDate = GoalInputValidator.validateDate(
                    ((JTextField) endDatePanel.getComponent(1)).getText(), "End Date");

            // Validate that the start date is not after the end date
            GoalInputValidator.validateDateRange(startDate, endDate);

            // The time/frequency panel has shifted to component index 10
            JPanel timeFreqPanel = (JPanel) createGoalForm.getComponent(10);
            JPanel timePeriodPanel = (JPanel) timeFreqPanel.getComponent(0);
            JComboBox<?> timePeriodBox = (JComboBox<?>) timePeriodPanel.getComponent(1);
            Goal.TimePeriod timePeriod = Goal.TimePeriod.valueOf(timePeriodBox.getSelectedItem().toString());

            // The frequency field is no longer used, so it is commented out.
            // JPanel frequencyPanel = (JPanel) timeFreqPanel.getComponent(1);
            // int frequency = GoalInputValidator.validatePositiveInteger(
            //         ((JTextField) frequencyPanel.getComponent(1)).getText(), "Frequency");

            TaskAvailable selectedTargetTaskAvailable = (TaskAvailable) targetTaskBox.getSelectedItem();
            
            // Convert TaskAvailable to Task for goal creation
            Task targetTask = null;
            if (selectedTargetTaskAvailable != null) {
                // Create a BeginAndDueDates object for the Task
                // Use today as begin date and the goal's end date as due date
                BeginAndDueDates taskDates = new BeginAndDueDates(LocalDate.now(), endDate);
                
                // Create a Task from TaskAvailable for the goal
                targetTask = new Task(
                    selectedTargetTaskAvailable.getId(),  // Use template ID
                    selectedTargetTaskAvailable.getInfo(),
                    taskDates,
                    selectedTargetTaskAvailable.isOneTime()
                );
            }

            // The targetAmount value is now used for both the target amount and the frequency.
            createGoalController.execute(
                    goalName,
                    description,
                    targetAmount,
                    currentAmount,
                    startDate,
                    endDate,
                    timePeriod,
                    frequency,
                    targetTask
            );

            availableGoalsController.execute("");
            JOptionPane.showMessageDialog(null, "Goal created successfully!");

        } catch (IllegalArgumentException ex) {
            // Catch validation-specific exceptions
            JOptionPane.showMessageDialog(null,
                    "Validation Error: " + ex.getMessage(), "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            // Catch any other unexpected exceptions
            JOptionPane.showMessageDialog(null,
                    "An unexpected error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Creates the available goals view component
     */
    private AvailableGoalsView createAvailableGoalsView() {
        return new AvailableGoalsView(availableGoalsViewModel, availableGoalsController);
    }
    
    /**
     * Refreshes the target task dropdown with the latest available tasks
     */
    private void refreshTargetTaskDropdown() {
        if (targetTaskBox != null) {
            TaskAvailable selectedTask = (TaskAvailable) targetTaskBox.getSelectedItem();
            
            // Get fresh list of available tasks
            List<TaskAvailable> availableTasks = taskGateway.getAvailableTaskTemplates();
            
            // Update the combo box
            targetTaskBox.removeAllItems();
            for (TaskAvailable task : availableTasks) {
                targetTaskBox.addItem(task);
            }
            
            // Try to restore previous selection if it still exists
            if (selectedTask != null) {
                for (int i = 0; i < targetTaskBox.getItemCount(); i++) {
                    TaskAvailable task = targetTaskBox.getItemAt(i);
                    if (task.getId().equals(selectedTask.getId())) {
                        targetTaskBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Creates the Today So Far panel with all data sources connected
     */
    private TodaySoFarView createTodaySoFarPanel() {
        // Get shared Today So Far components
        app.SharedTodaySoFarComponents sharedTodaySoFar = app.SharedTodaySoFarComponents.getInstance();
        
        // Create Today So Far view using shared components
        TodaySoFarView todaySoFarView = sharedTodaySoFar.createTodaySoFarView();
        
        // Get the TodaySoFarController to wire to presenters
        TodaySoFarController todaySoFarController = sharedTodaySoFar.getTodaySoFarController();
        
        // Wire TodaySoFarController to presenters that need to refresh the panel
        if (createGoalPresenter != null) {
            createGoalPresenter.setTodaySoFarController(todaySoFarController);
        }
        if (todayGoalPresenter != null) {
            todayGoalPresenter.setTodaySoFarController(todaySoFarController);
        }
        
        // Trigger initial data load
        sharedTodaySoFar.refresh();
        
        return todaySoFarView;
    }
}