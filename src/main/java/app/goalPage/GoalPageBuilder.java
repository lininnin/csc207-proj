package app.goalPage;
import entity.Angela.Task.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;                // Goal domain model
import entity.Sophia.goalFactory;         // Factory for creating goals

// Import view models
import entity.info.Info;
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
import data_access.GoalRepository;     // Goal repository interface

// Import views
import views.*;
import view.CollapsibleSidebarView;    // Collapsible sidebar component

// Import Java/Swing components
import javax.swing.*;
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
    private GoalRepository goalRepository;  // Handles goal persistence
    private goalFactory goalFactory;       // Creates goal objects

    // Form reference for goal creation
    private JPanel createGoalForm;

    private JComboBox<Task> targetTaskBox;


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
        // Initialize repository with data files
        goalRepository = new FileGoalRepository(
                new File("goals.txt"),         // Main goals storage
                new File("current_goals.txt"), // Current goals state
                new File("today_goal.txt"),    // Today's goals
                new goalFactory()             // Goal object factory
        );
        goalFactory = new goalFactory();
    }

    /**
     * Configures all use cases with their dependencies
     */
    private void initializeUseCases() {
        // Goal Creation Setup
        CreateGoalOutputBoundary createGoalPresenter = new CreateGoalPresenter();
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
        TodayGoalOutputBoundary todayGoalPresenter = new TodayGoalPresenter(todayGoalsViewModel);
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

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topCenterRow, BorderLayout.CENTER);
        centerPanel.add(availableGoalsContainer, BorderLayout.SOUTH);

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

        // Target Amount & Current Amount (horizontal)
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.X_AXIS));
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel targetAmountPanel = new JPanel();
        targetAmountPanel.setLayout(new BoxLayout(targetAmountPanel, BoxLayout.Y_AXIS));
        targetAmountPanel.add(createCompactLabel("Target Amount:"));
        JTextField targetAmountField = new JTextField("0.0");
        targetAmountField.setMaximumSize(new Dimension(150, 25));
        targetAmountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        targetAmountPanel.add(targetAmountField);
        targetAmountPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10)); // spacing

        JPanel currentAmountPanel = new JPanel();
        currentAmountPanel.setLayout(new BoxLayout(currentAmountPanel, BoxLayout.Y_AXIS));
        currentAmountPanel.add(createCompactLabel("Current Amount:"));
        JTextField currentAmountField = new JTextField("0.0");
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

        // Target Task (vertical)
        formPanel.add(createCompactLabel("Target Task:"));

        // Dummy tasks
        Info dummyInfo1 = new Info.Builder("Read Chapter 1")
                .description("Finish by Friday")
                .category("Reading")
                .build();
        BeginAndDueDates dummyDates1 = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(2));
        Task task1 = new Task("template-task-1", dummyInfo1, dummyDates1, false);

        Info dummyInfo2 = new Info.Builder("Exercise")
                .description("Do 30 minutes of cardio")
                .category("Fitness")
                .build();
        BeginAndDueDates dummyDates2 = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(5));
        Task task2 = new Task("template-task-2", dummyInfo2, dummyDates2, false);

        List<Task> availableTasks = new ArrayList<>();
        availableTasks.add(task1);
        availableTasks.add(task2);

        targetTaskBox = new JComboBox<>(availableTasks.toArray(new Task[0]));
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
            String goalName = ((JTextField) createGoalForm.getComponent(1)).getText();

            JPanel amountPanel = (JPanel) createGoalForm.getComponent(3);
            JPanel targetAmountPanel = (JPanel) amountPanel.getComponent(0);
            JTextField targetAmountField = (JTextField) targetAmountPanel.getComponent(1);
            double targetAmount = Double.parseDouble(targetAmountField.getText());

            JPanel currentAmountPanel = (JPanel) amountPanel.getComponent(1);
            JTextField currentAmountField = (JTextField) currentAmountPanel.getComponent(1);
            double currentAmount = Double.parseDouble(currentAmountField.getText());

            JPanel datePanel = (JPanel) createGoalForm.getComponent(5);
            JPanel startDatePanel = (JPanel) datePanel.getComponent(0);
            JTextField startDateField = (JTextField) startDatePanel.getComponent(1);
            LocalDate startDate = LocalDate.parse(startDateField.getText());

            JPanel endDatePanel = (JPanel) datePanel.getComponent(1);
            JTextField endDateField = (JTextField) endDatePanel.getComponent(1);
            LocalDate endDate = LocalDate.parse(endDateField.getText());

            JPanel timeFreqPanel = (JPanel) createGoalForm.getComponent(7);
            JPanel timePeriodPanel = (JPanel) timeFreqPanel.getComponent(0);
            JComboBox<?> timePeriodBox = (JComboBox<?>) timePeriodPanel.getComponent(1);
            Goal.TimePeriod timePeriod = Goal.TimePeriod.valueOf(timePeriodBox.getSelectedItem().toString());

            JPanel frequencyPanel = (JPanel) timeFreqPanel.getComponent(1);
            JTextField frequencyField = (JTextField) frequencyPanel.getComponent(1);
            int frequency = Integer.parseInt(frequencyField.getText());

            Task selectedTargetTask = (Task) targetTaskBox.getSelectedItem();

            createGoalController.execute(
                    goalName,
                    "",  // Default description
                    targetAmount,
                    currentAmount,
                    startDate,
                    endDate,
                    timePeriod,
                    frequency,
                    selectedTargetTask
            );

            availableGoalsController.execute("");
            JOptionPane.showMessageDialog(null, "Goal created successfully!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error: " + ex.getMessage(), "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Creates the available goals view component
     */
    private AvailableGoalsView createAvailableGoalsView() {
        return new AvailableGoalsView(availableGoalsViewModel, availableGoalsController);
    }
}