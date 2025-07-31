package app;

import interface_adapter.Angela.task.create.*;
import interface_adapter.Angela.task.delete.*;
import interface_adapter.Angela.task.available.*;
import interface_adapter.Angela.category.*;
import interface_adapter.Angela.category.create.*;
import interface_adapter.Angela.category.delete.*;
import interface_adapter.Angela.category.edit.*;
import interface_adapter.ViewManagerModel;
import use_case.Angela.task.TaskGateway;
import use_case.Angela.category.CategoryGateway;
import use_case.Angela.task.create.*;
import use_case.Angela.task.delete.*;
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.edit.*;
import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryGateway;
import view.Angela.Task.*;
import view.Angela.Category.*;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Builder for the Task page with category management.
 */
public class TaskPageBuilder {

    // Data Access
    private final TaskGateway taskGateway = new InMemoryTaskGateway();
    private final CategoryGateway categoryGateway = new InMemoryCategoryGateway();

    // View Models
    private final CreateTaskViewModel createTaskViewModel = new CreateTaskViewModel();
    private final AvailableTasksViewModel availableTasksViewModel = new AvailableTasksViewModel();
    private final DeleteTaskViewModel deleteTaskViewModel = new DeleteTaskViewModel();
    private final CategoryManagementViewModel categoryManagementViewModel = new CategoryManagementViewModel();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    // Views
    private CreateTaskView createTaskView;
    private AvailableTasksView availableTasksView;
    private CategoryManagementDialog categoryDialog;

    // Frame reference for dialog parent
    private JFrame parentFrame;

    public JPanel build() {
        // Create Views
        createTaskView = new CreateTaskView(createTaskViewModel, categoryGateway);
        availableTasksView = new AvailableTasksView(availableTasksViewModel, deleteTaskViewModel);

        // Wire up Create Task Use Case
        CreateTaskOutputBoundary createTaskPresenter = new CreateTaskPresenter(
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        CreateTaskInputBoundary createTaskInteractor = new CreateTaskInteractor(
                taskGateway,
                categoryGateway,
                createTaskPresenter
        );

        CreateTaskController createTaskController = new CreateTaskController(createTaskInteractor);
        createTaskView.setCreateTaskController(createTaskController);

        // Wire up Delete Task Use Case
        DeleteTaskOutputBoundary deleteTaskPresenter = new DeleteTaskPresenter(
                availableTasksViewModel,
                deleteTaskViewModel
        );

        DeleteTaskInputBoundary deleteTaskInteractor = new DeleteTaskInteractor(
                taskGateway,
                deleteTaskPresenter
        );

        DeleteTaskController deleteTaskController = new DeleteTaskController(deleteTaskInteractor);
        availableTasksView.setDeleteTaskController(deleteTaskController);

        // Wire up Category Management Use Cases
        CategoryManagementPresenter categoryPresenter = new CategoryManagementPresenter(
                categoryManagementViewModel
        );

        // Create Category
        CreateCategoryInputBoundary createCategoryInteractor = new CreateCategoryInteractor(
                categoryGateway,
                categoryPresenter
        );
        CreateCategoryController createCategoryController = new CreateCategoryController(
                createCategoryInteractor
        );

        // Delete Category
        DeleteCategoryInputBoundary deleteCategoryInteractor = new DeleteCategoryInteractor(
                categoryGateway,
                categoryPresenter
        );
        DeleteCategoryController deleteCategoryController = new DeleteCategoryController(
                deleteCategoryInteractor
        );

        // Edit Category
        EditCategoryInputBoundary editCategoryInteractor = new EditCategoryInteractor(
                categoryGateway,
                categoryPresenter
        );
        EditCategoryController editCategoryController = new EditCategoryController(
                editCategoryInteractor
        );

        // Set up category management dialog opening
        createTaskView.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("openCategoryManagement".equals(evt.getPropertyName())) {
                    openCategoryDialog();
                }
            }
        });

        // Set the task gateway so the view can fetch tasks
        availableTasksView.setTaskGateway(taskGateway);

        return buildLayout();
    }

    public void setParentFrame(JFrame frame) {
        this.parentFrame = frame;
    }

    private void openCategoryDialog() {
        if (parentFrame == null) {
            // Find the parent frame
            Container parent = createTaskView.getParent();
            while (parent != null && !(parent instanceof JFrame)) {
                parent = parent.getParent();
            }
            parentFrame = (JFrame) parent;
        }

        if (categoryDialog == null) {
            categoryDialog = new CategoryManagementDialog(
                    parentFrame,
                    categoryGateway,
                    categoryManagementViewModel
            );

            // Wire up controllers
            CategoryManagementPresenter categoryPresenter = new CategoryManagementPresenter(
                    categoryManagementViewModel
            );

            CreateCategoryInputBoundary createCategoryInteractor = new CreateCategoryInteractor(
                    categoryGateway,
                    categoryPresenter
            );
            CreateCategoryController createCategoryController = new CreateCategoryController(
                    createCategoryInteractor
            );

            DeleteCategoryInputBoundary deleteCategoryInteractor = new DeleteCategoryInteractor(
                    categoryGateway,
                    categoryPresenter
            );
            DeleteCategoryController deleteCategoryController = new DeleteCategoryController(
                    deleteCategoryInteractor
            );

            EditCategoryInputBoundary editCategoryInteractor = new EditCategoryInteractor(
                    categoryGateway,
                    categoryPresenter
            );
            EditCategoryController editCategoryController = new EditCategoryController(
                    editCategoryInteractor
            );

            categoryDialog.setControllers(
                    createCategoryController,
                    deleteCategoryController,
                    editCategoryController
            );

            // Listen for category updates
            categoryDialog.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("categoriesUpdated".equals(evt.getPropertyName())) {
                        createTaskView.refreshCategories();
                    }
                }
            });
        }

        categoryDialog.setVisible(true);
    }

    private JPanel buildLayout() {
        // Top section with create task form
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(createTaskView, BorderLayout.CENTER);
        topPanel.setPreferredSize(new Dimension(800, 300));

        // Middle section for today's tasks (placeholder for now)
        JPanel todaysTasksPanel = new JPanel();
        todaysTasksPanel.setBorder(BorderFactory.createTitledBorder("Today's Tasks"));
        todaysTasksPanel.setPreferredSize(new Dimension(800, 150));
        todaysTasksPanel.add(new JLabel("Today's tasks will appear here"));

        // Bottom section for available tasks
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(availableTasksView, BorderLayout.CENTER);
        bottomPanel.setPreferredSize(new Dimension(800, 250));

        // Combine all sections
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(topPanel);
        centerPanel.add(todaysTasksPanel);
        centerPanel.add(bottomPanel);

        // Sidebar
        JPanel sidebarPanel = createSidebar();

        // Wrap with collapsible sidebar
        CollapsibleSidebarView collapsibleView = new CollapsibleSidebarView(sidebarPanel, centerPanel);

        // Right panel for details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(new JLabel("Task Details", SwingConstants.CENTER), BorderLayout.NORTH);

        // Final layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(collapsibleView, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        return mainPanel;
    }

    private JPanel createSidebar() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(60, 63, 65));
        sidebarPanel.setPreferredSize(new Dimension(200, 700));

        String[] menuItems = {
                "📋 Tasks", "📆 Events", "🎯 Goals",
                "🧠 Wellness Log", "📊 Charts", "🤖 AI-Feedback & Analysis", "⚙️ Settings"
        };

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);

            if (item.contains("Tasks")) {
                btn.setBackground(new Color(45, 47, 49));
            } else {
                btn.setBackground(new Color(60, 63, 65));
            }

            sidebar.add(btn);
        }

        return sidebarPanel;
    }
}