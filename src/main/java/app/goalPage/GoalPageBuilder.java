package app.goalPage;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.Sophia.GoalFactory;

// Import view models
import data_access.InMemoryTaskGateway;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.sophia.available_goals.AvailableGoalsViewModel;
import interface_adapter.sophia.create_goal.CreatedGoalViewModel;
import interface_adapter.sophia.edit_todays_goal.EditTodaysGoalViewModel;
import interface_adapter.sophia.today_goal.TodayGoalsViewModel;
import interface_adapter.sophia.order_goal.OrderedGoalViewModel;

// Import controllers
import interface_adapter.sophia.available_goals.AvailableGoalsController;
import interface_adapter.sophia.create_goal.CreateGoalController;
import interface_adapter.sophia.delete_goal.DeleteGoalController;
import interface_adapter.sophia.edit_todays_goal.EditTodaysGoalController;
import interface_adapter.sophia.today_goal.TodayGoalController;
import interface_adapter.sophia.order_goal.OrderGoalController;

// Import presenters
import interface_adapter.sophia.available_goals.AvailableGoalsPresenter;
import interface_adapter.sophia.create_goal.CreateGoalPresenter;
import interface_adapter.sophia.delete_goal.DeleteGoalPresenter;
import interface_adapter.sophia.edit_todays_goal.EditTodaysGoalPresenter;
import interface_adapter.sophia.today_goal.TodayGoalPresenter;
import interface_adapter.sophia.order_goal.OrderGoalPresenter;

// Import use case interactors and boundaries
import use_case.goalManage.available_goals.*;
import use_case.goalManage.create_goal.*;
import use_case.goalManage.delete_goal.*;
import use_case.goalManage.edit_todays_goal.*;
import use_case.goalManage.order_goal.*;
import use_case.goalManage.today_goal.*;

// Import data access
import data_access.FileGoalRepository;

// Import views
import view.sophia.AvailableGoalsView;
import view.sophia.EditTodayGoalView;
import view.sophia.TodayGoalView;
import view.Angela.TodaySoFarView;

// Import Java/Swing components
import javax.swing.*;
import javax.swing.DefaultListCellRenderer;
import java.awt.*;
import java.time.LocalDate;
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
    private FileGoalRepository goalRepository;
    private GoalFactory goalFactory;
    private InMemoryTaskGateway taskGateway;

    // Form reference for goal creation
    private JPanel createGoalForm;
    private JComboBox<TaskAvailable> targetTaskBox;

    // View Models (hold state for different UI sections)
    private CreatedGoalViewModel createdGoalViewModel;
    private AvailableGoalsViewModel availableGoalsViewModel;
    private EditTodaysGoalViewModel editTodaysGoalViewModel;
    private TodayGoalsViewModel todayGoalsViewModel;
    private OrderedGoalViewModel orderedGoalViewModel;
    private AvailableGoalsView availableGoalsView;

    // Controllers (handle user actions)
    private CreateGoalController createGoalController;
    private DeleteGoalController deleteGoalController;
    private EditTodaysGoalController editTodaysGoalController;
    private TodayGoalController todayGoalController;
    private OrderGoalController orderGoalController;
    private AvailableGoalsController availableGoalsController;

    // Presenters (needed for wiring TodaySoFarController)
    private CreateGoalPresenter createGoalPresenter;
    private TodayGoalPresenter todayGoalPresenter;

    /**
     * Main build method that constructs the complete goal page
     * @return JPanel containing the fully assembled UI
     */
    public JPanel build() {
        initializeViewModels();
        initializeDataAccess();
        initializeUseCases();
        return createMainPanel();
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
        goalRepository = app.SharedDataAccess.getInstance().getGoalRepository();
        goalFactory = new GoalFactory();
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
        deleteGoalController = new DeleteGoalController(deleteGoalInteractor);
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
        AvailableGoalsOutputBoundary availableGoalsPresenter = new AvailableGoalsPresenter(availableGoalsViewModel);
        OrderGoalsInputBoundary orderGoalInteractor = new OrderGoalsInteractor(
                goalRepository, orderGoalPresenter, availableGoalsPresenter);
        orderGoalController = new OrderGoalController(orderGoalInteractor);

        // Available Goals Management Setup
        AvailableGoalsInputBoundary availableGoalsInteractor = new AvailableGoalsInteractor(
                goalRepository, availableGoalsPresenter);
        availableGoalsController = new AvailableGoalsController(
                availableGoalsInteractor,
                todayGoalInteractor,
                deleteGoalInteractor,
                orderGoalController
        );
    }

    /**
     * Constructs the main application panel with all UI components
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel centerPanel = createCenterPanel();

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Load initial data
        availableGoalsController.execute("");
        todayGoalController.execute();

        // Apply the last known order on initial load
        orderGoalController.restoreLastOrder();

        return mainPanel;
    }

    /**
     * Creates the main content center panel containing:
     * - Goal creation form
     * - Today's goals view
     * - Available goals list
     * - Today So Far panel
     */
    private JPanel createCenterPanel() {
        JPanel createGoalForm = createGoalFormPanel();
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.add(createGoalForm, BorderLayout.CENTER);
        formContainer.add(createGoalButton(), BorderLayout.SOUTH);

        EditTodayGoalView editTodayGoalView = new EditTodayGoalView(
                editTodaysGoalViewModel,
                deleteGoalController,
                editTodaysGoalController,
                todayGoalController,
                orderGoalController
        );

        TodayGoalView todayGoalView = new TodayGoalView(todayGoalsViewModel, todayGoalController);
        JPanel todayGoalsContainer = new JPanel(new BorderLayout());
        todayGoalsContainer.setBorder(BorderFactory.createTitledBorder("Today's Goals"));
        todayGoalsContainer.add(todayGoalView, BorderLayout.CENTER);

        availableGoalsView = createAvailableGoalsView();
        JPanel availableGoalsContainer = new JPanel(new BorderLayout());
        availableGoalsContainer.add(availableGoalsView, BorderLayout.CENTER);
        availableGoalsContainer.setPreferredSize(new Dimension(800, 300));
        availableGoalsContainer.setBackground(Color.GRAY);

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

        TodaySoFarView todaySoFarView = createTodaySoFarPanel();

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(todaySoFarView, BorderLayout.CENTER);
        rightPanel.setMinimumSize(new Dimension(250, 0));

        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerContent, rightPanel);
        mainSplitPane.setDividerLocation(800);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(0.75);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(mainSplitPane, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createGoalFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Create New Goal"));

        int verticalGap = 4;
        JTextField goalNameField = createLabeledTextField(formPanel, "Goal Name:", "", verticalGap);
        JTextField goalDescriptionField = createLabeledTextField(formPanel, "Description (Optional):", "", verticalGap);

        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.X_AXIS));
        amountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel targetAmountPanel = new JPanel();
        targetAmountPanel.setLayout(new BoxLayout(targetAmountPanel, BoxLayout.Y_AXIS));
        targetAmountPanel.add(createCompactLabel("Target Amount:"));
        JTextField targetAmountField = new JTextField("0");
        targetAmountField.setMaximumSize(new Dimension(150, 25));
        targetAmountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        targetAmountPanel.add(targetAmountField);
        targetAmountPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));

        JPanel currentAmountPanel = new JPanel();
        currentAmountPanel.setLayout(new BoxLayout(currentAmountPanel, BoxLayout.Y_AXIS));
        currentAmountPanel.add(createCompactLabel("Current Amount:"));
        JTextField currentAmountField = new JTextField("0");
        currentAmountField.setMaximumSize(new Dimension(150, 25));
        currentAmountField.setAlignmentX(Component.LEFT_ALIGNMENT);
        currentAmountPanel.add(currentAmountField);
        amountPanel.add(targetAmountPanel);
        amountPanel.add(currentAmountPanel);
        formPanel.add(amountPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, verticalGap)));

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
        startDatePanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));

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
        timePeriodPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));

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

        List<TaskAvailable> availableTasks = taskGateway.getAvailableTaskTemplates();

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
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
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
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return textField;
    }

    private JButton createGoalButton() {
        JButton createButton = new JButton("Create Goal");
        createButton.addActionListener(e -> handleCreateGoal());
        return createButton;
    }

    private void handleCreateGoal() {
        try {
            String goalName = GoalInputValidator.validateString(
                    ((JTextField) createGoalForm.getComponent(1)).getText(), "Goal Name");
            String description = ((JTextField) createGoalForm.getComponent(4)).getText();
            JPanel amountPanel = (JPanel) createGoalForm.getComponent(6);
            JPanel targetAmountPanel = (JPanel) amountPanel.getComponent(0);
            double targetAmount = GoalInputValidator.validatePositiveInteger(
                    ((JTextField) targetAmountPanel.getComponent(1)).getText(), "Target Amount");
            JPanel currentAmountPanel = (JPanel) amountPanel.getComponent(1);
            double currentAmount = GoalInputValidator.validatePositiveInteger(
                    ((JTextField) currentAmountPanel.getComponent(1)).getText(), "Current Amount");
            JPanel datePanel = (JPanel) createGoalForm.getComponent(8);
            JPanel startDatePanel = (JPanel) datePanel.getComponent(0);
            LocalDate startDate = GoalInputValidator.validateDate(
                    ((JTextField) startDatePanel.getComponent(1)).getText(), "Start Date");
            JPanel endDatePanel = (JPanel) datePanel.getComponent(1);
            LocalDate endDate = GoalInputValidator.validateDate(
                    ((JTextField) endDatePanel.getComponent(1)).getText(), "End Date");
            GoalInputValidator.validateDateRange(startDate, endDate);
            JPanel timeFreqPanel = (JPanel) createGoalForm.getComponent(10);
            JPanel timePeriodPanel = (JPanel) timeFreqPanel.getComponent(0);
            JComboBox<?> timePeriodBox = (JComboBox<?>) timePeriodPanel.getComponent(1);
            Goal.TimePeriod timePeriod = Goal.TimePeriod.valueOf(timePeriodBox.getSelectedItem().toString());
            JPanel frequencyPanel = (JPanel) timeFreqPanel.getComponent(1);
            int frequency = GoalInputValidator.validatePositiveInteger(
                    ((JTextField) frequencyPanel.getComponent(1)).getText(), "Frequency");
            TaskAvailable selectedTargetTaskAvailable = (TaskAvailable) targetTaskBox.getSelectedItem();
            Task targetTask = null;
            if (selectedTargetTaskAvailable != null) {
                BeginAndDueDates taskDates = new BeginAndDueDates(LocalDate.now(), endDate);
                targetTask = new Task(
                        selectedTargetTaskAvailable.getId(),
                        selectedTargetTaskAvailable.getInfo(),
                        taskDates,
                        selectedTargetTaskAvailable.isOneTime()
                );
            }
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
            JOptionPane.showMessageDialog(null,
                    "Validation Error: " + ex.getMessage(), "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "An unexpected error occurred: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private AvailableGoalsView createAvailableGoalsView() {
        return new AvailableGoalsView(availableGoalsViewModel, availableGoalsController);
    }

    private void refreshTargetTaskDropdown() {
        if (targetTaskBox != null) {
            TaskAvailable selectedTask = (TaskAvailable) targetTaskBox.getSelectedItem();
            List<TaskAvailable> availableTasks = taskGateway.getAvailableTaskTemplates();
            targetTaskBox.removeAllItems();
            for (TaskAvailable task : availableTasks) {
                targetTaskBox.addItem(task);
            }
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

    private TodaySoFarView createTodaySoFarPanel() {
        app.SharedTodaySoFarComponents sharedTodaySoFar = app.SharedTodaySoFarComponents.getInstance();
        TodaySoFarView todaySoFarView = sharedTodaySoFar.createTodaySoFarView();
        TodaySoFarController todaySoFarController = sharedTodaySoFar.getTodaySoFarController();
        if (createGoalPresenter != null) {
            createGoalPresenter.setTodaySoFarController(todaySoFarController);
        }
        if (todayGoalPresenter != null) {
            todayGoalPresenter.setTodaySoFarController(todaySoFarController);
        }
        sharedTodaySoFar.refresh();
        return todaySoFarView;
    }

    private JButton createOrderGoalButton(OrderGoalController orderGoalController) {
        JButton orderButton = new JButton("Reorder Today's Goals");
        orderButton.addActionListener(e -> showOrderDialog(orderGoalController));
        return orderButton;
    }

    private void showOrderDialog(OrderGoalController orderGoalController) {
        String[] options = {"name", "deadline"};
        String selectedCriterion = (String) JOptionPane.showInputDialog(
                null,
                "Choose sorting criteria:",
                "Order Today's Goals",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (selectedCriterion != null) {
            int reverseOption = JOptionPane.showConfirmDialog(
                    null,
                    "Order in reverse?",
                    "Reverse Order",
                    JOptionPane.YES_NO_OPTION
            );
            boolean reverse = (reverseOption == JOptionPane.YES_OPTION);
            handleOrderGoal(orderGoalController, selectedCriterion, reverse);
        }
    }

    private void handleOrderGoal(OrderGoalController orderGoalController, String orderBy, boolean reverse) {
        try {
            orderGoalController.execute(orderBy, reverse);
            JOptionPane.showMessageDialog(null, "Today's goals have been reordered based on " + orderBy + ".");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error reordering goals: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}