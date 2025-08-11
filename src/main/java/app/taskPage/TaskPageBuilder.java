package app.taskPage;

import interface_adapter.Angela.task.create.*;
import interface_adapter.Angela.task.delete.*;
import interface_adapter.Angela.task.available.*;
import interface_adapter.Angela.task.edit_available.*;
import interface_adapter.Angela.task.add_to_today.*;
import interface_adapter.Angela.task.mark_complete.*;
import interface_adapter.Angela.task.edit_today.*;
import interface_adapter.Angela.task.today.*;
import interface_adapter.Angela.task.remove_from_today.*;
import interface_adapter.Angela.task.overdue.*;
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
import use_case.Angela.task.remove_from_today.*;
import use_case.Angela.task.overdue.*;
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.edit.*;
import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryGateway;
import view.Angela.Task.*;
import view.Angela.Category.*;
import view.Angela.TodaySoFarView;
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
    private final OverdueTasksViewModel overdueTasksViewModel = new OverdueTasksViewModel();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();

    // Views
    private CreateTaskView createTaskView;
    private AvailableTasksView availableTasksView;
    private AddToTodayView addToTodayView;
    private TodaysTasksView todaysTasksView;
    private TodaySoFarView todaySoFarView;
    private CategoryManagementDialog categoryDialog;
    
    // Controllers
    private OverdueTasksController overdueTasksController;

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
        // Set the AddTaskToTodayViewModel so dropdown refreshes when task names are edited
        editAvailableTaskPresenter.setAddTaskToTodayViewModel(addTaskToTodayViewModel);

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
        AddTaskToTodayPresenter addToTodayPresenter = new AddTaskToTodayPresenter(
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
        addToTodayView.setCategoryGateway(categoryGateway); // Set category gateway for dropdown display

        // Wire up Mark Task Complete Use Case
        MarkTaskCompletePresenter markCompletePresenter = new MarkTaskCompletePresenter(
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
        EditTodayTaskPresenter editTodayPresenter = new EditTodayTaskPresenter(
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

        // Wire up Remove from Today Use Case
        RemoveFromTodayPresenter removeFromTodayPresenter = new RemoveFromTodayPresenter(
                todayTasksViewModel
        );

        RemoveFromTodayInputBoundary removeFromTodayInteractor = new RemoveFromTodayInteractor(
                taskGateway, // InMemoryTaskGateway implements RemoveFromTodayDataAccessInterface
                removeFromTodayPresenter
        );

        RemoveFromTodayController removeFromTodayController = new RemoveFromTodayController(
                removeFromTodayInteractor
        );

        // Set the controller on the today's tasks view
        todaysTasksView.setRemoveFromTodayController(removeFromTodayController);

        // Wire up Overdue Tasks Use Case
        OverdueTasksOutputBoundary overdueTasksPresenter = new OverdueTasksPresenter(
                overdueTasksViewModel
        );

        OverdueTasksInputBoundary overdueTasksInteractor = new OverdueTasksInteractor(
                taskGateway, // InMemoryTaskGateway implements OverdueTasksDataAccessInterface
                categoryGateway,
                overdueTasksPresenter
        );

        overdueTasksController = new OverdueTasksController(
                overdueTasksInteractor
        );

        // Create Today So Far view
        todaySoFarView = new TodaySoFarView(overdueTasksViewModel);
        todaySoFarView.setOverdueTasksController(overdueTasksController);
        
        // Set overdue controller on presenters that need to refresh overdue tasks
        markCompletePresenter.setOverdueTasksController(overdueTasksController);
        editTodayPresenter.setOverdueTasksController(overdueTasksController);
        addToTodayPresenter.setOverdueTasksController(overdueTasksController);
        editAvailableTaskPresenter.setOverdueTasksController(overdueTasksController);
        deleteTaskPresenter.setOverdueTasksController(overdueTasksController);
        removeFromTodayPresenter.setOverdueTasksController(overdueTasksController);

        // Set up category management dialog opening
        createTaskView.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
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
                categoryPresenter.setOverdueTasksController(overdueTasksController);

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
        leftPanel.setPreferredSize(new Dimension(380, 450)); // Balanced width
        leftPanel.setMinimumSize(new Dimension(280, 450)); // Reasonable minimum

        // Top: New Available Task
        createTaskView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        createTaskView.setPreferredSize(new Dimension(380, 250));
        leftPanel.add(createTaskView);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing

        // Bottom: Add Today's Task
        addToTodayView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        addToTodayView.setPreferredSize(new Dimension(380, 200));
        leftPanel.add(addToTodayView);

        // --- Right side: Today's Tasks panel ---
        JPanel todaysPanel = new JPanel(new BorderLayout());
        todaysPanel.add(todaysTasksView, BorderLayout.CENTER);
        todaysPanel.setPreferredSize(new Dimension(720, 450)); // Balanced width
        todaysPanel.setMinimumSize(new Dimension(600, 450)); // Keep minimum to ensure visibility

        // --- Top section: Use JSplitPane for user-adjustable layout ---
        JSplitPane topSection = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topSection.setLeftComponent(leftPanel);
        topSection.setRightComponent(todaysPanel);
        
        // Set initial divider location (380px for left panel)
        topSection.setDividerLocation(380);
        
        // Allow continuous layout updates while dragging
        topSection.setContinuousLayout(true);
        
        // Add expand/collapse buttons
        topSection.setOneTouchExpandable(true);
        
        // Set divider size
        topSection.setDividerSize(8);
        
        topSection.setPreferredSize(new Dimension(1100, 450)); // Total width

        // --- Bottom section: Available Tasks ---
        JPanel bottomSection = new JPanel(new BorderLayout());
        bottomSection.add(availableTasksView, BorderLayout.CENTER);
        bottomSection.setPreferredSize(new Dimension(1100, 300)); // Match top section width
        bottomSection.setMinimumSize(new Dimension(800, 200)); // Ensure minimum size

        // --- Center Panel: Use vertical split pane for top and bottom ---
        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        centerSplitPane.setTopComponent(topSection);
        centerSplitPane.setBottomComponent(bottomSection);
        
        // Set initial divider location (450px for top section)
        centerSplitPane.setDividerLocation(450);
        
        // Allow continuous layout updates
        centerSplitPane.setContinuousLayout(true);
        
        // Add expand/collapse buttons
        centerSplitPane.setOneTouchExpandable(true);
        
        // Set divider size
        centerSplitPane.setDividerSize(8);
        
        // --- Right Panel (Today So Far) ---
        // Initial refresh of overdue tasks
        todaySoFarView.refreshOverdueTasks();

        // --- Final Frame Layout ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
        mainPanel.add(todaySoFarView, BorderLayout.EAST);

        return mainPanel;
    }
}