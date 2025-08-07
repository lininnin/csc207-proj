package app;

import entity.Sophia.Goal;
import entity.Sophia.goalInterface;
import interface_adapter.Sophia.available_goals.AvailableGoalsController;
import interface_adapter.Sophia.available_goals.AvailableGoalsPresenter;
import interface_adapter.Sophia.available_goals.AvailableGoalsViewModel;
import interface_adapter.Sophia.create_goal.CreateGoalController;
import interface_adapter.Sophia.create_goal.CreateGoalPresenter;
import interface_adapter.Sophia.create_goal.CreatedGoalViewModel;
import interface_adapter.Sophia.delete_goal.DeleteGoalController;
import interface_adapter.Sophia.delete_goal.DeleteGoalPresenter;
import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalController;
import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalPresenter;
import interface_adapter.Sophia.edit_todays_goal.EditTodaysGoalViewModel;
import interface_adapter.Sophia.order_goal.OrderGoalController;
import interface_adapter.Sophia.order_goal.OrderGoalPresenter;
import interface_adapter.Sophia.order_goal.OrderedGoalViewModel;
import interface_adapter.Sophia.today_goal.TodayGoalController;
import interface_adapter.Sophia.today_goal.TodayGoalPresenter;
import interface_adapter.Sophia.today_goal.TodayGoalsViewModel;

import use_case.goalManage.available_goals.*;
import use_case.goalManage.create_goal.*;
import use_case.goalManage.delete_goal.*;
import use_case.goalManage.edit_todays_goal.*;
import use_case.goalManage.order_goal.*;
import use_case.goalManage.today_goal.*;

import data_access.FileGoalRepository;
import data_access.GoalRepository;
import entity.Sophia.goalFactory;

import views.*;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;

public class GoalPageBuilder {

    public JPanel build() {
        // --- ViewModels ---
        CreatedGoalViewModel createdGoalViewModel = new CreatedGoalViewModel();
        AvailableGoalsViewModel availableGoalsViewModel = new AvailableGoalsViewModel();
        EditTodaysGoalViewModel editTodaysGoalViewModel = new EditTodaysGoalViewModel();
        TodayGoalsViewModel todayGoalsViewModel = new TodayGoalsViewModel();
        OrderedGoalViewModel orderedGoalViewModel = new OrderedGoalViewModel();

        // --- Data Access & Factory ---
        GoalRepository goalRepository = new FileGoalRepository();
        goalFactory goalFactory = new goalFactory();

        // --- Use Case Wiring ---
        CreateGoalOutputBoundary createGoalPresenter = new CreateGoalPresenter();
        CreateGoalInputBoundary createGoalInteractor = new CreateGoalInteractor(goalRepository, createGoalPresenter, goalFactory);
        CreateGoalController createGoalController = new CreateGoalController(createGoalInteractor);

        DeleteGoalOutputBoundary deleteGoalPresenter = new DeleteGoalPresenter();
        DeleteGoalInputBoundary deleteGoalInteractor = new DeleteGoalInteractor(goalRepository, deleteGoalPresenter);
        DeleteGoalController deleteGoalController = new DeleteGoalController(deleteGoalInteractor);

        EditTodaysGoalOutputBoundary editTodaysGoalPresenter = new EditTodaysGoalPresenter();
        EditTodaysGoalInputBoundary editTodaysGoalInteractor = new EditTodaysGoalInteractor(goalRepository, editTodaysGoalPresenter);
        EditTodaysGoalController editTodaysGoalController = new EditTodaysGoalController(editTodaysGoalInteractor);

        TodayGoalOutputBoundary todayGoalPresenter = new TodayGoalPresenter(todayGoalsViewModel);
        TodayGoalInputBoundary todayGoalInteractor = new TodayGoalInteractor(goalRepository, todayGoalPresenter);
        TodayGoalController todayGoalController = new TodayGoalController(todayGoalInteractor);

        OrderGoalsOutputBoundary orderGoalPresenter = new OrderGoalPresenter();
        OrderGoalsInputBoundary orderGoalInteractor = new OrderGoalsInteractor(goalRepository, orderGoalPresenter);
        OrderGoalController orderGoalController = new OrderGoalController(orderGoalInteractor);

        AvailableGoalsOutputBoundary availableGoalsPresenter = new AvailableGoalsPresenter(availableGoalsViewModel);
        AvailableGoalsInputBoundary availableGoalsInteractor = new AvailableGoalsInteractor(goalRepository, availableGoalsPresenter);
        AvailableGoalsController availableGoalsController = new AvailableGoalsController(
                availableGoalsInteractor,
                todayGoalInteractor,
                deleteGoalInteractor
        );

        // --- Create Goal Form ---
        JPanel createGoalForm = new JPanel(new GridLayout(8, 2, 5, 5));
        createGoalForm.setBorder(BorderFactory.createTitledBorder("Create New Goal"));

        // Form fields
        createGoalForm.add(new JLabel("Goal Name:"));
        JTextField goalNameField = new JTextField();
        createGoalForm.add(goalNameField);

        createGoalForm.add(new JLabel("Description:"));
        JTextField descriptionField = new JTextField();
        createGoalForm.add(descriptionField);

        createGoalForm.add(new JLabel("Target Amount:"));
        JTextField targetAmountField = new JTextField("0.0");
        createGoalForm.add(targetAmountField);

        createGoalForm.add(new JLabel("Current Amount:"));
        JTextField currentAmountField = new JTextField("0.0");
        createGoalForm.add(currentAmountField);

        createGoalForm.add(new JLabel("Start Date (YYYY-MM-DD):"));
        JTextField startDateField = new JTextField(LocalDate.now().toString());
        createGoalForm.add(startDateField);

        createGoalForm.add(new JLabel("End Date (YYYY-MM-DD):"));
        JTextField endDateField = new JTextField(LocalDate.now().plusWeeks(1).toString());
        createGoalForm.add(endDateField);

        createGoalForm.add(new JLabel("Time Period:"));
        JComboBox<Goal.TimePeriod> timePeriodCombo = new JComboBox<>(Goal.TimePeriod.values());
        timePeriodCombo.setSelectedItem(Goal.TimePeriod.WEEK);
        createGoalForm.add(timePeriodCombo);

        createGoalForm.add(new JLabel("Frequency:"));
        JTextField frequencyField = new JTextField("1");
        createGoalForm.add(frequencyField);

        // Create Goal Button
        JButton createButton = new JButton("Create Goal");
        createButton.addActionListener(e -> {
            try {
                createGoalController.execute(
                        goalNameField.getText(),
                        descriptionField.getText(),
                        Double.parseDouble(targetAmountField.getText()),
                        Double.parseDouble(currentAmountField.getText()),
                        LocalDate.parse(startDateField.getText()),
                        LocalDate.parse(endDateField.getText()),
                        (Goal.TimePeriod) timePeriodCombo.getSelectedItem(),
                        Integer.parseInt(frequencyField.getText())
                );
                availableGoalsController.execute("");
                JOptionPane.showMessageDialog(null, "Goal created successfully!");

                // Clear form
                goalNameField.setText("");
                descriptionField.setText("");
                targetAmountField.setText("0.0");
                currentAmountField.setText("0.0");
                startDateField.setText(LocalDate.now().toString());
                endDateField.setText(LocalDate.now().plusWeeks(1).toString());
                timePeriodCombo.setSelectedItem(Goal.TimePeriod.WEEK);
                frequencyField.setText("1");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error: " + ex.getMessage(), "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.add(createGoalForm, BorderLayout.CENTER);
        formContainer.add(createButton, BorderLayout.SOUTH);

        // --- Other Views ---
        EditTodayGoalView editTodayGoalView = new EditTodayGoalView(
                editTodaysGoalViewModel,
                deleteGoalController,
                editTodaysGoalController,
                todayGoalController,
                orderGoalController
        );

        // --- Today's Goals Section ---
        TodayGoalView todayGoalView = new TodayGoalView(todayGoalsViewModel, todayGoalController);

        // Create a container panel for Today's Goals
        JPanel todayGoalsContainer = new JPanel(new BorderLayout());
        todayGoalsContainer.setBorder(BorderFactory.createTitledBorder("Today's Goals"));
        todayGoalsContainer.add(todayGoalView, BorderLayout.CENTER);

        // Add this to your upper right panel
        JPanel upperRightPanel = new JPanel(new BorderLayout());
        upperRightPanel.add(todayGoalsContainer, BorderLayout.CENTER);
        upperRightPanel.setBackground(new Color(240, 240, 255));

        // --- Available Goals View ---
        AvailableGoalsView availableGoalsView = new AvailableGoalsView(availableGoalsViewModel, availableGoalsController) {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                super.propertyChange(evt); // Maintain original behavior

                // Add custom renderer when state updates
                if ("state".equals(evt.getPropertyName())) {
                    Component[] components = getComponents();
                    for (Component comp : components) {
                        if (comp instanceof JScrollPane) {
                            JScrollPane scrollPane = (JScrollPane) comp;
                            JViewport viewport = scrollPane.getViewport();
                            if (viewport.getView() instanceof JList) {
                                @SuppressWarnings("unchecked")
                                JList<Goal> goalList = (JList<Goal>) viewport.getView();

                                // Create main panel with list and buttons
                                JPanel mainPanel = new JPanel(new BorderLayout());

                                // Create title panel
                                JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                                titlePanel.add(new JLabel("Available Goals"));
                                mainPanel.add(titlePanel, BorderLayout.NORTH);

                                // Add the list
                                mainPanel.add(new JScrollPane(goalList), BorderLayout.CENTER);

                                // Create button panel matching the UI
                                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                                // Add "Add to Today" button
                                JButton addToTodayButton = new JButton("Add to Today");
                                addToTodayButton.addActionListener(e -> {
                                    Goal selected = goalList.getSelectedValue();
                                    if (selected != null) {
                                        todayGoalController.addToToday(selected.getGoalInfo().getInfo().getName());
                                        availableGoalsController.execute(""); // Refresh
                                    }
                                });

                                // Add "Delete" button
                                JButton deleteButton = new JButton("Delete");
                                deleteButton.addActionListener(e -> {
                                    Goal selected = goalList.getSelectedValue();
                                    if (selected != null) {
                                        int confirm = JOptionPane.showConfirmDialog(
                                                this,
                                                "Delete '" + selected.getGoalInfo().getInfo().getName() + "'?",
                                                "Confirm Deletion",
                                                JOptionPane.YES_NO_OPTION
                                        );
                                        if (confirm == JOptionPane.YES_OPTION) {
                                            deleteGoalController.execute(
                                                    selected.getGoalInfo().getInfo().getName(),
                                                    true
                                            );
                                            availableGoalsController.execute(""); // Refresh
                                        }
                                    }
                                });

                                // Add "Refresh" button
                                JButton refreshButton = new JButton("Refresh");
                                refreshButton.addActionListener(e -> {
                                    availableGoalsController.execute("");
                                });

                                buttonPanel.add(addToTodayButton);
                                buttonPanel.add(deleteButton);
                                buttonPanel.add(refreshButton);

                                mainPanel.add(buttonPanel, BorderLayout.SOUTH);

                                // Replace the original scroll pane with our new panel
                                scrollPane.setViewportView(mainPanel);

                                // Set the cell renderer to match UI format
                                goalList.setCellRenderer(new DefaultListCellRenderer() {
                                    @Override
                                    public Component getListCellRendererComponent(
                                            JList<?> list, Object value, int index,
                                            boolean isSelected, boolean cellHasFocus) {
                                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                                        if (value instanceof Goal) {
                                            Goal goal = (Goal) value;
                                            setText(String.format("%s - %d/%d",
                                                    goal.getGoalInfo().getInfo().getName(),
                                                    goal.getCurrentProgress(),
                                                    goal.getFrequency()));
                                        }
                                        return this;
                                    }
                                });
                            }
                        }
                    }
                }
            }
        };

        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formContainer, editTodayGoalView);
        verticalSplit.setResizeWeight(0.5);
        verticalSplit.setDividerSize(2);
        verticalSplit.setEnabled(false);

        JPanel topCenterRow = new JPanel(new GridLayout(1, 2));
        topCenterRow.add(verticalSplit);
        topCenterRow.add(upperRightPanel);

        JPanel availableGoalsContainer = new JPanel(new BorderLayout());
        availableGoalsContainer.add(availableGoalsView, BorderLayout.CENTER);
        availableGoalsContainer.setPreferredSize(new Dimension(800, 300));
        availableGoalsContainer.setBackground(Color.GRAY);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topCenterRow, BorderLayout.CENTER);
        centerPanel.add(availableGoalsContainer, BorderLayout.SOUTH);

        // --- Ordered Goals View ---
        OrderedGoalView orderedGoalView = new OrderedGoalView(orderedGoalViewModel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(orderedGoalView, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(300, 0));
        rightPanel.setBackground(Color.WHITE);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(60, 63, 65));
        sidebarPanel.setPreferredSize(new Dimension(200, 700));

        String[] buttonLabels = {
                "📋 Tasks", "📆 Events", "🎯 Goals",
                "🧠 Wellness Log", "📊 Charts",
                "🤖 AI-Feedback", "⚙️ Settings"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setBackground(new Color(70, 73, 75));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createVerticalStrut(10));
        }

        CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sidebarPanel, centerPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(collapsibleCenter, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Initial load
        availableGoalsController.execute("");
        todayGoalController.execute();

        return mainPanel;
    }
}