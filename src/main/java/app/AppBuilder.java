package app;

import data_access.CategoryRepository;
import data_access.TaskRepository;
import interface_adapter.ViewManagerModel;
import interface_adapter.controller.*;
import interface_adapter.presenter.*;
import interface_adapter.view_model.*;
import use_case.Angela.category.*;
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.edit.*;
import use_case.Angela.task.*;
import use_case.Angela.task.delete.*;
import use_case.Angela.task.edit_available.*;
import use_case.Angela.task.edit_todays.*;
import use_case.Angela.task.remove_from_today.*;
import use_case.Angela.task.unmark_complete.*;
import view.*;
import view.Task.TaskView;
import view.Task.TaskViewModel;

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
        CreateTaskPresenter createTaskPresenter = new CreateTaskPresenter(taskViewModel);
        CreateTaskInteractor createTaskInteractor =
                new CreateTaskInteractor(taskRepository, createTaskPresenter);
        CreateTaskController createTaskController =
                new CreateTaskController(createTaskInteractor);

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
        taskViewModel.setCreateController(createTaskController);
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
}