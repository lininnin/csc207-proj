package app;

import interface_adapter.Angela.task.create.*;
import interface_adapter.Angela.task.delete.*;
import interface_adapter.Angela.task.available.*;
import interface_adapter.Angela.task.edit_available.*;
import interface_adapter.Angela.task.add_to_today.*;
import interface_adapter.Angela.task.mark_complete.*;
import interface_adapter.Angela.task.edit_today.*;
import interface_adapter.Angela.task.today.*;
import interface_adapter.Angela.category.*;
import interface_adapter.Angela.category.create.*;
import interface_adapter.Angela.category.delete.*;
import interface_adapter.Angela.category.edit.*;
import interface_adapter.ViewManagerModel;
import use_case.Angela.task.create.*;
import use_case.Angela.task.delete.*;
import use_case.Angela.task.edit_available.*;
import use_case.Angela.task.add_to_today.*;
import use_case.Angela.task.mark_complete.*;
import use_case.Angela.task.edit_today.*;
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.edit.*;
import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryGateway;
import view.Angela.Task.*;
import view.Angela.Category.*;
import view.CollapsibleSidebarView;
import view.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Builder for the Task page following the MindTrack GUI template.
 */
public class TaskPageBuilder {

    // Data Access
    private final InMemoryTaskGateway taskGateway = new InMemoryTaskGateway();
    private final InMemoryCategoryGateway categoryGateway = new InMemoryCategoryGateway();

    // View Models
    private final CreateTaskViewModel createTaskViewModel = new CreateTaskViewModel();
    private final AvailableTasksViewModel availableTasksViewModel = new AvailableTasksViewModel();
    private final DeleteTaskViewModel deleteTaskViewModel = new DeleteTaskViewModel();
    private final EditAvailableTaskViewModel editAvailableTaskViewModel = new EditAvailableTaskViewModel();
    private final AddTaskToTodayViewModel addTaskToTodayViewModel = new AddTaskToTodayViewModel();
    private final TodayTasksViewModel todayTasksViewModel = new TodayTasksViewModel();
    private final EditTodayTaskViewModel editTodayTaskViewModel = new EditTodayTaskViewModel();
    private final CategoryManagementViewModel categoryManagementViewModel = new CategoryManagementViewModel();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    // Views
    private CreateTaskView createTaskView;
    private AvailableTasksView availableTasksView;
    private AddToTodayView addToTodayView;
    private TodaysTasksView todaysTasksView;
    private CategoryManagementDialog categoryDialog;

    public JPanel build() {
        // CRITICAL: Connect the gateways so category deletion can update tasks
        categoryGateway.setTaskGateway(taskGateway);
        
        // Create Views
        createTaskView = new CreateTaskView(createTaskViewModel, categoryGateway);
        availableTasksView = new AvailableTasksView(availableTasksViewModel, deleteTaskViewModel);
        addToTodayView = new AddToTodayView(addTaskToTodayViewModel);
        todaysTasksView = new TodaysTasksView(todayTasksViewModel);

        // Wire up Create Task Use Case
        CreateTaskPresenter createTaskPresenter = new CreateTaskPresenter(
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );
        // Connect AddTaskToTodayViewModel so dropdown refreshes when new tasks are created
        createTaskPresenter.setAddTaskToTodayViewModel(addTaskToTodayViewModel);

        CreateTaskInputBoundary createTaskInteractor = new CreateTaskInteractor(
                taskGateway, // InMemoryTaskGateway implements CreateTaskDataAccessInterface
                categoryGateway,
                createTaskPresenter
        );

        CreateTaskController createTaskController = new CreateTaskController(createTaskInteractor);
        createTaskView.setCreateTaskController(createTaskController);

        // Wire up Delete Task Use Case
        DeleteTaskPresenter deleteTaskPresenter = new DeleteTaskPresenter(
                availableTasksViewModel,
                deleteTaskViewModel
        );
        // Set the TodayTasksViewModel so deletes trigger refresh
        deleteTaskPresenter.setTodayTasksViewModel(todayTasksViewModel);

        DeleteTaskInputBoundary deleteTaskInteractor = new DeleteTaskInteractor(
                taskGateway,
                deleteTaskPresenter
        );

        DeleteTaskController deleteTaskController = new DeleteTaskController(deleteTaskInteractor);
        availableTasksView.setDeleteTaskController(deleteTaskController);

        // Wire up Edit Available Task Use Case
        EditAvailableTaskPresenter editAvailableTaskPresenter = new EditAvailableTaskPresenter(
                editAvailableTaskViewModel,
                availableTasksViewModel
        );
        // Set the TodayTasksViewModel so edits trigger refresh
        editAvailableTaskPresenter.setTodayTasksViewModel(todayTasksViewModel);

        EditAvailableTaskInputBoundary editAvailableTaskInteractor = new EditAvailableTaskInteractor(
                taskGateway, // InMemoryTaskGateway implements EditAvailableTaskDataAccessInterface
                categoryGateway,
                editAvailableTaskPresenter
        );

        EditAvailableTaskController editAvailableTaskController = new EditAvailableTaskController(
                editAvailableTaskInteractor
        );
        
        availableTasksView.setEditAvailableTaskController(editAvailableTaskController);
        availableTasksView.setEditAvailableTaskViewModel(editAvailableTaskViewModel);
        availableTasksView.setEditTaskDataAccess(taskGateway); // InMemoryTaskGateway implements EditAvailableTaskDataAccessInterface

        // Wire up Add to Today Use Case
        AddTaskToTodayOutputBoundary addToTodayPresenter = new AddTaskToTodayPresenter(
                addTaskToTodayViewModel,
                todayTasksViewModel
        );

        AddTaskToTodayInputBoundary addToTodayInteractor = new AddTaskToTodayInteractor(
                taskGateway, // InMemoryTaskGateway implements AddToTodayDataAccessInterface
                addToTodayPresenter
        );

        AddTaskToTodayController addToTodayController = new AddTaskToTodayController(
                addToTodayInteractor
        );

        addToTodayView.setAddTaskToTodayController(addToTodayController);
        System.out.println("DEBUG: TaskPageBuilder - Setting dataAccess on addToTodayView with taskGateway: " + taskGateway);
        addToTodayView.setDataAccess(taskGateway);

        // Wire up Mark Task Complete Use Case
        MarkTaskCompleteOutputBoundary markCompletePresenter = new MarkTaskCompletePresenter(
                todayTasksViewModel
        );

        MarkTaskCompleteInputBoundary markCompleteInteractor = new MarkTaskCompleteInteractor(
                taskGateway, // InMemoryTaskGateway implements MarkTaskCompleteDataAccessInterface
                markCompletePresenter
        );

        MarkTaskCompleteController markCompleteController = new MarkTaskCompleteController(
                markCompleteInteractor
        );

        // Set the controller on the today's tasks view
        todaysTasksView.setMarkTaskCompleteController(markCompleteController);

        // Wire up Edit Today Task Use Case
        EditTodayTaskOutputBoundary editTodayPresenter = new EditTodayTaskPresenter(
                editTodayTaskViewModel,
                todayTasksViewModel
        );

        EditTodayTaskInputBoundary editTodayInteractor = new EditTodayTaskInteractor(
                taskGateway, // InMemoryTaskGateway implements EditTodayTaskDataAccessInterface
                editTodayPresenter
        );

        EditTodayTaskController editTodayController = new EditTodayTaskController(
                editTodayInteractor
        );

        // Set the controller and view model on the today's tasks view
        todaysTasksView.setEditTodayTaskController(editTodayController);
        todaysTasksView.setEditTodayTaskViewModel(editTodayTaskViewModel);

        // Set up category management dialog opening
        createTaskView.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("openCategoryManagement".equals(evt.getPropertyName())) {
                    openCategoryDialog();
                }
            }
        });

        // Set the gateways so the views can fetch data
        availableTasksView.setTaskGateway(taskGateway);
        availableTasksView.setCategoryGateway(categoryGateway);
        todaysTasksView.setTaskGateway(taskGateway);
        todaysTasksView.setCategoryGateway(categoryGateway);

        return buildLayout();
    }

    private void openCategoryDialog() {
        Container parent = createTaskView.getParent();
        while (parent != null && !(parent instanceof JFrame)) {
            parent = parent.getParent();
        }
        JFrame parentFrame = (JFrame) parent;

        if (parentFrame != null) {
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
                categoryPresenter.setAvailableTasksViewModel(availableTasksViewModel);
                categoryPresenter.setTodayTasksViewModel(todayTasksViewModel);

                CreateCategoryInputBoundary createCategoryInteractor = new CreateCategoryInteractor(
                        categoryGateway,
                        categoryPresenter
                );
                CreateCategoryController createCategoryController = new CreateCategoryController(
                        createCategoryInteractor
                );

                DeleteCategoryInputBoundary deleteCategoryInteractor = new DeleteCategoryInteractor(
                        categoryGateway, // InMemoryCategoryGateway implements DeleteCategoryDataAccessInterface
                        categoryPresenter
                );
                DeleteCategoryController deleteCategoryController = new DeleteCategoryController(
                        deleteCategoryInteractor
                );

                EditCategoryInputBoundary editCategoryInteractor = new EditCategoryInteractor(
                        categoryGateway,  // InMemoryCategoryGateway implements EditCategoryDataAccessInterface
                        taskGateway,      // InMemoryTaskGateway implements EditCategoryTaskDataAccessInterface
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

                categoryDialog.setCategoryChangeListener(new CategoryManagementDialog.CategoryChangeListener() {
                    @Override
                    public void onCategoryChanged() {
                        createTaskView.refreshCategories();
                    }
                });
            }

            categoryDialog.setVisible(true);
        }
    }

    private JPanel buildLayout() {
        // --- Left side: Vertically stacked Create Task and Add to Today ---
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(450, 450)); // Wider panel

        // Top: New Available Task
        createTaskView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        createTaskView.setPreferredSize(new Dimension(450, 250));
        leftPanel.add(createTaskView);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing

        // Bottom: Add Today's Task
        addToTodayView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        addToTodayView.setPreferredSize(new Dimension(450, 200));
        leftPanel.add(addToTodayView);

        // --- Right side: Today's Tasks panel ---
        JPanel todaysPanel = new JPanel(new BorderLayout());
        todaysPanel.add(todaysTasksView, BorderLayout.CENTER);
        todaysPanel.setPreferredSize(new Dimension(550, 450));

        // --- Top section: Left and Right panels side by side ---
        JPanel topSection = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left panel takes 45% of width
        gbc.gridx = 0;
        gbc.weightx = 0.45;
        gbc.insets = new Insets(5, 5, 5, 5);
        topSection.add(leftPanel, gbc);

        // Today's panel takes 55% of width
        gbc.gridx = 1;
        gbc.weightx = 0.55;
        topSection.add(todaysPanel, gbc);

        topSection.setPreferredSize(new Dimension(900, 450));

        // --- Bottom section: Available Tasks ---
        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.add(availableTasksView, BorderLayout.CENTER);
        bottomSection.setPreferredSize(new Dimension(900, 300));

        // --- Center Panel: Combine top and bottom ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topSection, BorderLayout.NORTH);
        centerPanel.add(bottomSection, BorderLayout.CENTER);

        // --- Sidebar Panel ---
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(60, 63, 65));
        sidebarPanel.setPreferredSize(new Dimension(200, 700));

        String[] menuItems = {
                "📋 Tasks", "📆 Events", "🎯 Goals",
                "🧠 Wellness Log", "📊 Charts",
                "🤖 AI-Feedback & Analysis", "⚙️ Settings"
        };

        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setMaximumSize(new Dimension(200, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setFont(FontUtil.getStandardFont());

            if (item.contains("Tasks")) {
                btn.setBackground(new Color(45, 47, 49)); // Highlight current page
            } else {
                btn.setBackground(new Color(60, 63, 65));
            }

            sidebarPanel.add(btn);
        }

        // --- Wrap sidebar + centerPanel ---
        CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sidebarPanel, centerPanel);

        // --- Right Panel (Details) ---
        JPanel taskDetailsPanel = new JPanel(new BorderLayout());
        taskDetailsPanel.setPreferredSize(new Dimension(300, 0));
        taskDetailsPanel.setBackground(Color.WHITE);

        // Task details section
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel detailsTitle = new JLabel("Today so far");
        detailsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        detailsPanel.add(detailsTitle);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Goals section
        JPanel goalsPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JLabel goalsLabel = new JLabel("Goals");
        goalsLabel.setFont(FontUtil.getStandardFont());
        goalsPanel.add(goalsLabel);
        JLabel periodLabel = new JLabel("period");
        periodLabel.setFont(FontUtil.getStandardFont());
        goalsPanel.add(periodLabel);
        JLabel gymLabel = new JLabel("Go to gym");
        gymLabel.setFont(FontUtil.getStandardFont());
        goalsPanel.add(gymLabel);
        JLabel weeklyLabel = new JLabel("weekly");
        weeklyLabel.setFont(FontUtil.getStandardFont());
        goalsPanel.add(weeklyLabel);
        JLabel readLabel = new JLabel("Read books");
        readLabel.setFont(FontUtil.getStandardFont());
        goalsPanel.add(readLabel);
        JLabel monthlyLabel = new JLabel("monthly");
        monthlyLabel.setFont(FontUtil.getStandardFont());
        goalsPanel.add(monthlyLabel);
        detailsPanel.add(goalsPanel);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Task Completion Rate
        JLabel completionLabel = new JLabel("Task Completion rate:");
        completionLabel.setFont(FontUtil.getStandardFont());
        detailsPanel.add(completionLabel);

        JPanel progressPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JLabel progressLabel = new JLabel("Progress");
        progressLabel.setFont(FontUtil.getStandardFont());
        progressPanel.add(progressLabel);
        JLabel percentLabel = new JLabel("0%");
        percentLabel.setFont(FontUtil.getStandardFont());
        progressPanel.add(percentLabel);
        progressPanel.add(new JLabel(""));
        progressPanel.add(new JLabel(""));
        detailsPanel.add(progressPanel);

        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Task Overdue section
        JLabel overdueTitle = new JLabel("Task Overdue");
        overdueTitle.setFont(FontUtil.getLargeFont());
        detailsPanel.add(overdueTitle);

        JLabel overdueName = new JLabel("Name    due date");
        overdueName.setFont(FontUtil.getStandardFont());
        detailsPanel.add(overdueName);

        taskDetailsPanel.add(detailsPanel, BorderLayout.NORTH);

        // --- Final Frame Layout ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(collapsibleCenter, BorderLayout.CENTER);
        mainPanel.add(taskDetailsPanel, BorderLayout.EAST);

        return mainPanel;
    }
}