package app.taskPage;

import app.AppDataAccessFactory;
import app.TodaySoFarComponentsFactory;
import interface_adapter.Angela.task.create.*;
import interface_adapter.Angela.task.delete.*;
import interface_adapter.Angela.task.available.*;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.edit_available.*;
import interface_adapter.Angela.task.add_to_today.*;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayState;
import interface_adapter.Angela.task.mark_complete.*;
import interface_adapter.Angela.task.edit_today.*;
import interface_adapter.Angela.task.today.*;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.remove_from_today.*;
import interface_adapter.Angela.task.overdue.*;
import interface_adapter.Angela.category.*;
import interface_adapter.Angela.category.create.*;
import interface_adapter.Angela.category.delete.*;
import interface_adapter.Angela.category.edit.*;
import interface_adapter.Angela.today_so_far.*;
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
import use_case.Angela.today_so_far.*;
import data_access.InMemoryTaskGateway;
import data_access.InMemoryCategoryDataAccessObject;
import data_access.InMemoryTodaySoFarDataAccess;
import view.Angela.Task.*;
import view.Angela.Category.*;
import view.Angela.TodaySoFarView;
import view.FontUtil;

import entity.info.Info;
import entity.CategoryFactory;
import entity.CommonCategoryFactory;
import entity.Alex.Event.Event;
import entity.Alex.WellnessLogEntry.WellnessLogEntryFactory;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelFactory;
import entity.Alex.MoodLabel.Type;
import entity.BeginAndDueDates.BeginAndDueDates;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Builder for the Task page following the MindTrack GUI template.
 * Uses dependency injection instead of singleton pattern.
 */
public class TaskPageBuilder {

    // Data Access - Injected via constructor
    private final InMemoryTaskGateway taskGateway;
    private final InMemoryCategoryDataAccessObject categoryDataAccess;
    private final AppDataAccessFactory dataAccessFactory;
    private final TodaySoFarComponentsFactory todaySoFarFactory;

    // View Models
    private final CreateTaskViewModel createTaskViewModel = new CreateTaskViewModel();
    private final AvailableTasksViewModel availableTasksViewModel = new AvailableTasksViewModel();
    private final DeleteTaskViewModel deleteTaskViewModel = new DeleteTaskViewModel();
    private final EditAvailableTaskViewModel editAvailableTaskViewModel = new EditAvailableTaskViewModel();
    private final AddTaskToTodayViewModel addTaskToTodayViewModel = new AddTaskToTodayViewModel();
    private final TodayTasksViewModel todayTasksViewModel = new TodayTasksViewModel();
    private final EditTodayTaskViewModel editTodayTaskViewModel = new EditTodayTaskViewModel();
    private final CategoryManagementViewModel categoryManagementViewModel; // Shared across pages
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
    private TodaySoFarController todaySoFarController;
    
    /**
     * Creates a new TaskPageBuilder with injected dependencies.
     * @param dataAccessFactory The factory for creating data access objects
     */
    public TaskPageBuilder(AppDataAccessFactory dataAccessFactory) {
        this.dataAccessFactory = dataAccessFactory;
        this.taskGateway = dataAccessFactory.getTaskGateway();
        this.categoryDataAccess = dataAccessFactory.getCategoryDataAccess();
        this.categoryManagementViewModel = dataAccessFactory.getCategoryManagementViewModel();
        this.todaySoFarFactory = new TodaySoFarComponentsFactory(dataAccessFactory);
    }
    
    /**
     * Creates a new TaskPageBuilder with default data access objects.
     * For backward compatibility.
     */
    public TaskPageBuilder() {
        this(new AppDataAccessFactory());
    }
    
    /**
     * Refreshes all task views to show updated categories and tasks.
     * Call this when the task page becomes visible.
     */
    public void refreshViews() {
        if (createTaskView != null) {
            createTaskView.refreshCategories();
        }
        
        // Trigger refresh of available tasks view through ViewModel
        if (availableTasksViewModel != null) {
            AvailableTasksState availableState = availableTasksViewModel.getState();
            availableState.setRefreshNeeded(true);
            availableTasksViewModel.setState(availableState);
            availableTasksViewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
        }
        
        // Trigger refresh of the Add to Today dropdown
        if (addTaskToTodayViewModel != null) {
            AddTaskToTodayState addState = addTaskToTodayViewModel.getState();
            addState.setRefreshNeeded(true);
            addTaskToTodayViewModel.setState(addState);
            addTaskToTodayViewModel.firePropertyChanged();
        }
        
        // Trigger refresh of today's tasks view through ViewModel
        if (todayTasksViewModel != null) {
            TodayTasksState todayState = todayTasksViewModel.getState();
            if (todayState == null) {
                todayState = new TodayTasksState();
            }
            todayState.setRefreshNeeded(true);
            todayTasksViewModel.setState(todayState);
            todayTasksViewModel.firePropertyChanged();
        }
        
        // Refresh Today So Far panel
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
    }

    public JPanel build() {
        // Note: Category and task gateways are now independent following SRP
        // Task updates during category deletion are handled by the interactor
        
        // Create Views
        createTaskView = new CreateTaskView(createTaskViewModel, categoryDataAccess);
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
                categoryDataAccess,
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
        // Set the AddTaskToTodayViewModel so the dropdown refreshes when tasks are deleted
        deleteTaskPresenter.setAddTaskToTodayViewModel(addTaskToTodayViewModel);

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
                categoryDataAccess,
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
        addToTodayView.setCategoryGateway(categoryDataAccess); // Set category gateway for dropdown display

        // Wire up Mark Task Complete Use Case
        MarkTaskCompletePresenter markCompletePresenter = new MarkTaskCompletePresenter(
                todayTasksViewModel
        );

        MarkTaskCompleteInputBoundary markCompleteInteractor = new MarkTaskCompleteInteractor(
                taskGateway, // InMemoryTaskGateway implements MarkTaskCompleteDataAccessInterface
                markCompletePresenter,
                dataAccessFactory.getGoalRepository() // Pass goal repository for updating goal progress
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

        // Get Today So Far components from factory
        overdueTasksController = todaySoFarFactory.getOverdueTasksController();
        todaySoFarController = todaySoFarFactory.getTodaySoFarController();
        
        // Create Today So Far view using factory
        todaySoFarView = todaySoFarFactory.createTodaySoFarView();
        
        // Trigger initial data load (no sample data - user will input real data)
        todaySoFarFactory.refresh();
        
        // Set overdue controller on presenters that need to refresh overdue tasks
        markCompletePresenter.setOverdueTasksController(overdueTasksController);
        editTodayPresenter.setOverdueTasksController(overdueTasksController);
        addToTodayPresenter.setOverdueTasksController(overdueTasksController);
        editAvailableTaskPresenter.setOverdueTasksController(overdueTasksController);
        deleteTaskPresenter.setOverdueTasksController(overdueTasksController);
        removeFromTodayPresenter.setOverdueTasksController(overdueTasksController);
        
        // Set Today So Far controller on presenters that need to refresh Today So Far panel
        editAvailableTaskPresenter.setTodaySoFarController(todaySoFarController);
        markCompletePresenter.setTodaySoFarController(todaySoFarController);
        addToTodayPresenter.setTodaySoFarController(todaySoFarController);
        removeFromTodayPresenter.setTodaySoFarController(todaySoFarController);
        editTodayPresenter.setTodaySoFarController(todaySoFarController);
        deleteTaskPresenter.setTodaySoFarController(todaySoFarController);

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
        availableTasksView.setCategoryGateway(categoryDataAccess);
        todaysTasksView.setTaskGateway(taskGateway);
        todaysTasksView.setCategoryGateway(categoryDataAccess);

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
                        categoryDataAccess,
                        categoryManagementViewModel
                );

                // Wire up controllers
                CategoryManagementPresenter categoryPresenter = new CategoryManagementPresenter(
                        categoryManagementViewModel
                );
                categoryPresenter.setAvailableTasksViewModel(availableTasksViewModel);
                categoryPresenter.setTodayTasksViewModel(todayTasksViewModel);
                categoryPresenter.setOverdueTasksController(overdueTasksController);
                categoryPresenter.setTodaySoFarController(todaySoFarController);

                CategoryFactory categoryFactory = new CommonCategoryFactory();
                CreateCategoryInputBoundary createCategoryInteractor = new CreateCategoryInteractor(
                        categoryDataAccess,
                        categoryPresenter,
                        categoryFactory
                );
                CreateCategoryController createCategoryController = new CreateCategoryController(
                        createCategoryInteractor
                );

                DeleteCategoryInputBoundary deleteCategoryInteractor = new DeleteCategoryInteractor(
                        categoryDataAccess, // InMemoryCategoryGateway implements DeleteCategoryCategoryDataAccessInterface
                        taskGateway,     // InMemoryTaskGateway implements DeleteCategoryTaskDataAccessInterface
                        null,            // No event data access needed in task context
                        categoryPresenter
                );
                DeleteCategoryController deleteCategoryController = new DeleteCategoryController(
                        deleteCategoryInteractor
                );

                EditCategoryInputBoundary editCategoryInteractor = new EditCategoryInteractor(
                        categoryDataAccess,  // InMemoryCategoryDataAccessObject implements EditCategoryDataAccessInterface
                        taskGateway,      // InMemoryTaskGateway implements EditCategoryTaskDataAccessInterface
                        categoryPresenter,
                        categoryFactory
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

            // Reload categories to ensure latest from Event page
            categoryDialog.loadCategories();
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

        // --- Final Frame Layout with Resizable Today So Far ---
        // Wrap Today So Far in a panel to control sizing
        JPanel todaySoFarWrapper = new JPanel(new BorderLayout());
        todaySoFarWrapper.add(todaySoFarView, BorderLayout.CENTER);
        todaySoFarWrapper.setPreferredSize(new Dimension(380, 750));
        todaySoFarWrapper.setMinimumSize(new Dimension(320, 400));
        
        // Create a horizontal split pane for resizable layout
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                                                   centerSplitPane, 
                                                   todaySoFarWrapper);
        mainSplitPane.setDividerLocation(1070);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(0.75);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);
        mainPanel.setPreferredSize(new Dimension(1450, 750));

        return mainPanel;
    }
}