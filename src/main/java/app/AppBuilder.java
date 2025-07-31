package app;

import data_access.CategoryRepository;
import data_access.TaskRepository;
import entity.info.InfoFactory;
import interface_adapter.Sophia.create_goal.CreateGoalController;
import interface_adapter.Sophia.create_goal.CreateGoalPresenter;
import interface_adapter.Sophia.create_goal.CreatedGoalViewModel; // update this after new pr are merged
import interface_adapter.Sophia.delete_goal.DeleteGoalController;
import interface_adapter.Sophia.delete_goal.DeleteGoalPresenter;
import interface_adapter.Sophia.delete_goal.DeletedGoalViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.controller.*;
import interface_adapter.orderGoals.OrderGoalsController;
import interface_adapter.orderGoals.OrderGoalsPresenter;
import interface_adapter.presenter.*;
import interface_adapter.view_model.*;
import use_case.Angela.category.*;
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.edit.*;
import use_case.Angela.task.*;
import use_case.Angela.task.delete.*;
import use_case.Angela.task.edit_available.*;
import use_case.orderGoal.OrderGoalsInputBoundary;
import use_case.orderGoal.OrderGoalsOutputBoundary;
import view.ViewManager;
import view.CategoryDialogView;
import view.Task.TaskView;

import javax.swing.*;
import java.awt.*;

/**
 * Builds and wires together all components of the application.
 * Extended to include task and category management components.
 */
public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Builds the complete application with all views and use cases wired together.
     *
     * @return The main application frame
     */
    public JFrame build() {
        // Create repositories
        CategoryGateway categoryRepository = new CategoryRepository();
        TaskGateway taskRepository = new TaskRepository();

        // Create view models
        CategoryViewModel categoryViewModel = new CategoryViewModel();
        TaskViewModel taskViewModel = new TaskViewModel();

        // Create views
        CategoryDialogView categoryView = new CategoryDialogView(categoryViewModel);
        TaskView taskView = new TaskView(taskViewModel);

        // Wire category use cases
        wireCategoryUseCases(categoryRepository, categoryViewModel);

        // Wire task use cases
        wireTaskUseCases(taskRepository, categoryRepository, taskViewModel);

        // Add views to card panel
        cardPanel.add(taskView, taskView.viewName);
        cardPanel.add(categoryView, categoryView.viewName);

        // Set up view manager
        viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        // Create main frame
        JFrame application = new JFrame("MindTrack - Task Management");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setLayout(new BorderLayout());

        // Add navigation panel
        application.add(createNavigationPanel(), BorderLayout.WEST);

        // Add main content
        application.add(cardPanel, BorderLayout.CENTER);

        // Set initial view
        viewManagerModel.setActiveView(taskView.viewName);
        viewManagerModel.firePropertyChanged();

        application.setSize(1200, 800);
        application.setLocationRelativeTo(null);

        return application;
    }

    /**
     * Wires all category-related use cases.
     */
    private void wireCategoryUseCases(CategoryGateway categoryRepository,
                                      CategoryViewModel categoryViewModel) {
        // Create Category
        CreateCategoryOutputBoundary createCategoryPresenter =
                new CreateCategoryPresenter(categoryViewModel, viewManagerModel);
        CreateCategoryInputBoundary createCategoryInteractor =
                new CreateCategoryInteractor(categoryRepository, createCategoryPresenter);
        CreateCategoryController createCategoryController =
                new CreateCategoryController(createCategoryInteractor);

        // Delete Category
        DeleteCategoryOutputBoundary deleteCategoryPresenter =
                new DeleteCategoryPresenter(categoryViewModel);
        DeleteCategoryInputBoundary deleteCategoryInteractor =
                new DeleteCategoryInteractor(categoryRepository, deleteCategoryPresenter);
        DeleteCategoryController deleteCategoryController =
                new DeleteCategoryController(deleteCategoryInteractor);

        // Edit Category
        EditCategoryOutputBoundary editCategoryPresenter =
                new EditCategoryPresenter(categoryViewModel);
        EditCategoryInputBoundary editCategoryInteractor =
                new EditCategoryInteractor(categoryRepository, editCategoryPresenter);
        EditCategoryController editCategoryController =
                new EditCategoryController(editCategoryInteractor);

        // Set controllers in view model
        categoryViewModel.setCreateController(createCategoryController);
        categoryViewModel.setDeleteController(deleteCategoryController);
        categoryViewModel.setEditController(editCategoryController);
    }

    /**
     * Wires all task-related use cases.
     */
    private void wireTaskUseCases(TaskGateway taskRepository,
                                  CategoryGateway categoryRepository,
                                  TaskViewModel taskViewModel) {
        // Create Task (already exists)
        // Assuming CreateTaskPresenter and CreateTaskInteractor exist from previous implementation
        // CreateTaskPresenter createTaskPresenter = new CreateTaskPresenter(taskViewModel);
        // CreateTaskInteractor createTaskInteractor =
        //     new CreateTaskInteractor(taskRepository, createTaskPresenter);
        // CreateTaskController createTaskController =
        //     new CreateTaskController(createTaskInteractor);

        // Delete Task
        DeleteTaskOutputBoundary deleteTaskPresenter =
                new DeleteTaskPresenter(taskViewModel, viewManagerModel);
        DeleteTaskInputBoundary deleteTaskInteractor =
                new DeleteTaskInteractor(taskRepository, deleteTaskPresenter);
        DeleteTaskController deleteTaskController =
                new DeleteTaskController(deleteTaskInteractor);

        // Edit Available Task
        EditAvailableTaskOutputBoundary editAvailablePresenter =
                new EditAvailableTaskPresenter(taskViewModel);
        EditAvailableTaskInputBoundary editAvailableInteractor =
                new EditAvailableTaskInteractor(taskRepository, categoryRepository, editAvailablePresenter);
        EditAvailableTaskController editAvailableController =
                new EditAvailableTaskController(editAvailableInteractor);

        // Add more use cases similarly...

        // Set controllers in view model
        // taskViewModel.setCreateController(createTaskController);
        taskViewModel.setDeleteController(deleteTaskController);
        taskViewModel.setEditAvailableController(editAvailableController);
    }

    /**
     * Creates the navigation panel.
     */
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(52, 73, 94));
        navPanel.setPreferredSize(new Dimension(200, 0));

        // Add navigation buttons
        String[] navItems = {"Tasks", "Events", "Goals", "Wellness Log",
                "History", "Charts", "AI Feedback", "Settings"};

        for (String item : navItems) {
            JButton button = createNavButton(item);
            navPanel.add(button);
            navPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        return navPanel;
    }

    /**
     * Creates a navigation button.
     */
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> {
            // Navigate to appropriate view
            String viewName = text.toLowerCase().replace(" ", "_");
            viewManagerModel.setActiveView(viewName);
            viewManagerModel.firePropertyChanged();
        });
        return button;
    }

    public static class GoalPageBuilder {

        public JPanel build() {
            // --- ViewModels ---
            CreatedGoalViewModel createdGoalViewModel = new CreatedGoalViewModel();
            DeletedGoalViewModel deletedGoalViewModel = new DeletedGoalViewModel();
            EditedGoalViewModel editedGoalViewModel = new EditedGoalViewModel();
            AddedGoalViewModel addedGoalViewModel = new AddedGoalViewModel();
            TodaysGoalsViewModel todaysGoalsViewModel = new TodaysGoalsViewModel();
            OrderGoalsViewModel orderGoalsViewModel = new OrderGoalsViewModel();

            // --- Data Access & Factory ---
            GoalRepository goalRepository = new GoalRepository();
            InfoFactory infoFactory = new InfoFactory();

            // --- Use Case Wiring ---
            // Create Goal
            CreateGoalOutputBoundary createGoalPresenter = new CreateGoalPresenter(
                    createdGoalViewModel,
                    goalRepository
            );
            CreateGoalInputBoundary createGoalInteractor = new CreateGoalInteractor(
                    goalRepository,
                    createGoalPresenter,
                    infoFactory
            );
            CreateGoalController createGoalController = new CreateGoalController(createGoalInteractor);

            // Delete Goal
            DeleteGoalOutputBoundary deleteGoalPresenter = new DeleteGoalPresenter(
                    deletedGoalViewModel,
                    goalRepository
            );
            DeleteGoalInputBoundary deleteGoalInteractor = new DeleteGoalInteractor(
                    goalRepository,
                    deleteGoalPresenter
            );
            DeleteGoalController deleteGoalController = new DeleteGoalController(deleteGoalInteractor);

            // Edit Goal
            EditGoalOutputBoundary editGoalPresenter = new EditGoalPresenter(
                    editedGoalViewModel,
                    goalRepository
            );
            EditGoalInputBoundary editGoalInteractor = new EditGoalInteractor(
                    goalRepository,
                    editGoalPresenter
            );
            EditGoalController editGoalController = new EditGoalController(editGoalInteractor);

            // Add Goal to Today
            AddGoalToTodayOutputBoundary addGoalPresenter = new AddGoalToTodayPresenter(
                    addedGoalViewModel,
                    todaysGoalsViewModel,
                    goalRepository
            );
            AddGoalToTodayInputBoundary addGoalInteractor = new AddGoalToTodayInteractor(
                    goalRepository,
                    addGoalPresenter
            );
            AddGoalToTodayController addGoalController = new AddGoalToTodayController(addGoalInteractor);

            // Order Goals
            OrderGoalsOutputBoundary orderGoalsPresenter = new OrderGoalsPresenter(orderGoalsViewModel);
            OrderGoalsInputBoundary orderGoalsInteractor = new OrderGoalsInteractor(
                    goalRepository,
                    orderGoalsPresenter
            );
            OrderGoalsController orderGoalsController = new OrderGoalsController(orderGoalsInteractor);

            // --- Views --- (Simplified placeholder views)
            JPanel createGoalView = new JPanel();
            createGoalView.add(new JLabel("Create Goal Form"));

            JPanel todaysGoalsView = new JPanel();
            todaysGoalsView.add(new JLabel("Today's Goals List"));

            JPanel availableGoalsView = new JPanel();
            availableGoalsView.add(new JLabel("Available Goals List"));

            JPanel orderGoalsView = new JPanel();
            orderGoalsView.add(new JLabel("Order/Sort Controls"));

            // --- Layout ---
            JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

            // Left Panel (Create/View Goals)
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(createGoalView, BorderLayout.NORTH);
            leftPanel.add(availableGoalsView, BorderLayout.CENTER);

            // Right Panel (Today's Goals)
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.add(todaysGoalsView, BorderLayout.CENTER);
            rightPanel.add(orderGoalsView, BorderLayout.SOUTH);

            mainSplit.setLeftComponent(leftPanel);
            mainSplit.setRightComponent(rightPanel);
            mainSplit.setDividerLocation(600);

            // Sidebar
            JPanel sidebarPanel = new JPanel();
            sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
            sidebarPanel.setBackground(new Color(60, 63, 65));
            sidebarPanel.add(new JButton("📋 Tasks"));
            sidebarPanel.add(new JButton("📆 Events"));
            sidebarPanel.add(new JButton("🎯 Goals"));
            sidebarPanel.add(new JButton("🧠 Wellness Log"));
            sidebarPanel.add(new JButton("📊 Charts"));
            sidebarPanel.add(new JButton("🤖 AI-Feedback"));
            sidebarPanel.add(new JButton("⚙️ Settings"));

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(sidebarPanel, BorderLayout.WEST);
            mainPanel.add(mainSplit, BorderLayout.CENTER);

            return mainPanel;
        }
    }
}