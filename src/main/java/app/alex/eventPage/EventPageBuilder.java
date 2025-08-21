package app.alex.eventPage;

import app.AppDataAccessFactory;
import entity.CategoryFactory;
import entity.CommonCategoryFactory;
import entity.alex.EventAvailable.EventAvailableFactory;
import entity.alex.EventAvailable.EventAvailableFactoryInterf;
import entity.alex.Event.EventFactory;
import entity.alex.Event.EventFactoryInterf;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.BeginAndDueDates.BeginAndDueDatesFactoryInterf;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeleteEventController;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeleteEventPresenter;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeletedEventViewModel;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditEventController;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditEventPresenter;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditedEventViewModel;
import interface_adapter.alex.event_related.add_event.AddEventController;
import interface_adapter.alex.event_related.add_event.AddEventPresenter;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.create_event.CreateEventController;
import interface_adapter.alex.event_related.create_event.CreateEventPresenter;
import interface_adapter.alex.event_related.create_event.CreatedEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventController;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventPresenter;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventController;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventPresenter;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;

// Category management imports
import data_access.InMemoryCategoryDataAccessObject;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import interface_adapter.Angela.category.*;
import interface_adapter.Angela.category.create.*;
import interface_adapter.Angela.category.delete.*;
import interface_adapter.Angela.category.edit.*;
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.delete.DeleteCategoryCategoryDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryTaskDataAccessInterface;
import use_case.Angela.category.edit.*;
import view.Angela.Category.CategoryManagementDialog;

import java.util.ArrayList;

import use_case.alex.event_related.add_event.*;
import use_case.alex.event_related.create_event.*;
import use_case.alex.event_related.avaliable_events_module.delete_event.*;
import use_case.alex.event_related.avaliable_events_module.edit_event.*;
import use_case.alex.event_related.todays_events_module.delete_todays_event.*;
import use_case.alex.event_related.todays_events_module.edit_todays_event.*;

import data_access.alex.EventAvailableDataAccessObject;
import data_access.alex.TodaysEventDataAccessObject;
import entity.info.InfoFactory;

import view.alex.Event.*;
import view.Angela.TodaySoFarView;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.Angela.today_so_far.TodaySoFarPresenter;
import use_case.Angela.task.overdue.OverdueTasksInputBoundary;
import use_case.Angela.task.overdue.OverdueTasksInteractor;
import use_case.Angela.task.overdue.OverdueTasksOutputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInteractor;
import data_access.InMemoryTaskDataAccessObject;
import data_access.InMemoryTodaySoFarDataAccess;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class EventPageBuilder {
    
    // Data Access - Injected via constructor
    private final AppDataAccessFactory dataAccessFactory;
    private final InMemoryCategoryDataAccessObject categoryDataAccess;
    private final CategoryManagementViewModel categoryManagementViewModel; // Shared across pages
    private CategoryManagementDialog categoryDialog;
    private CreateEventView createEventView;
    private AddEventView addEventView;
    private AvailableEventView availableEventView;
    private TodaysEventsView todaysEventsView;
    private AvailableEventViewModel availableEventViewModel;
    private TodaysEventsViewModel todaysEventsViewModel;
    
    // Today So Far panel fields
    private TodaySoFarController todaySoFarController;
    private OverdueTasksController overdueTasksController;
    
    /**
     * Creates a new EventPageBuilder with injected dependencies.
     * @param dataAccessFactory The factory for creating data access objects
     */
    public EventPageBuilder(AppDataAccessFactory dataAccessFactory) {
        this.dataAccessFactory = AppDataAccessFactory.getInstance(); // Use singleton
        this.categoryDataAccess = this.dataAccessFactory.getCategoryDataAccess();
        this.categoryManagementViewModel = this.dataAccessFactory.getCategoryManagementViewModel();
    }
    
    /**
     * Creates a new EventPageBuilder with default data access objects.
     * For backward compatibility.
     */
    public EventPageBuilder() {
        this(AppDataAccessFactory.getInstance());
    }
    
    /**
     * Refreshes all event views to show updated categories.
     * Call this when the event page becomes visible.
     */
    public void refreshViews() {
        if (createEventView != null) {
            createEventView.refreshCategories();
        }
        if (addEventView != null) {
            addEventView.forceRefresh();
        }
        if (availableEventView != null) {
            availableEventView.forceRefresh();
        }
        if (todaysEventsView != null) {
            todaysEventsView.forceRefresh();
        }
    }


    public JPanel build() {
        // --- ViewModels ---
        CreatedEventViewModel createdEventViewModel = new CreatedEventViewModel();
        this.availableEventViewModel = new AvailableEventViewModel();
        DeletedEventViewModel deletedEventViewModel = new DeletedEventViewModel();
        EditedEventViewModel editedEventViewModel = new EditedEventViewModel();
        AddedEventViewModel addEventViewModel = new AddedEventViewModel();
        this.todaysEventsViewModel = new TodaysEventsViewModel();
        DeleteTodaysEventViewModel deleteTodaysEventViewModel = new DeleteTodaysEventViewModel();
        EditTodaysEventViewModel editTodaysEventViewModel = new EditTodaysEventViewModel();

        // --- Data Access & Factory ---
        EventAvailableFactoryInterf eventAvailableFactory = new EventAvailableFactory();
        EventAvailableDataAccessObject commonDao = new EventAvailableDataAccessObject(eventAvailableFactory);
        // Use shared event data access so Today So Far panel can see the events
        TodaysEventDataAccessObject todaysEventDAO = dataAccessFactory.getEventDataAccess();

        InfoFactory infoFactory = new InfoFactory();

        // --- Use Case Wiring ---
        CreateEventOutputBoundary createEventPresenter = new CreateEventPresenter(createdEventViewModel, availableEventViewModel, commonDao);
        CreateEventInputBoundary createEventInteractor = new CreateEventInteractor(commonDao, createEventPresenter, infoFactory);
        CreateEventController createEventController = new CreateEventController(createEventInteractor);

        DeleteEventOutputBoundary deleteEventPresenter = new DeleteEventPresenter(deletedEventViewModel, availableEventViewModel, addEventViewModel);
        DeleteEventInputBoundary deleteEventInteractor = new DeleteEventInteractor(commonDao, deleteEventPresenter);
        DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);

        EditEventPresenter editEventPresenter = new EditEventPresenter(editedEventViewModel, availableEventViewModel);
        editEventPresenter.setTodaysEventsViewModel(todaysEventsViewModel); // Wire up today's events view model
        EditEventInputBoundary editEventInteractor = new EditEventInteractor(commonDao, editEventPresenter);
        EditEventController editEventController = new EditEventController(editEventInteractor);

        AddEventOutputBoundary addEventPresenter = new AddEventPresenter(addEventViewModel, todaysEventsViewModel, todaysEventDAO);
        EventFactoryInterf eventFactory = new EventFactory();
        BeginAndDueDatesFactoryInterf datesFactory = new BeginAndDueDatesFactory();
        AddEventInputBoundary addEventInteractor = new AddEventInteractor(todaysEventDAO, commonDao, addEventPresenter, eventFactory, datesFactory);
        AddEventController addEventController = new AddEventController(addEventInteractor);

        DeleteTodaysEventOutputBoundary delTodayPresenter = new DeleteTodaysEventPresenter(deleteTodaysEventViewModel, todaysEventsViewModel, addEventViewModel);
        DeleteTodaysEventInputBoundary delTodayInteractor = new DeleteTodaysEventInteractor(todaysEventDAO, delTodayPresenter);
        DeleteTodaysEventController deleteTodaysEventController = new DeleteTodaysEventController(delTodayInteractor);

        EditTodaysEventOutputBoundary editTodayPresenter = new EditTodaysEventPresenter(editTodaysEventViewModel, todaysEventsViewModel);
        EditTodaysEventInputBoundary editTodayInteractor = new EditTodaysEventInteractor(todaysEventDAO, editTodayPresenter);
        EditTodaysEventController editTodaysEventController = new EditTodaysEventController(editTodayInteractor);

        // --- Views ---
        addEventView = new AddEventView(addEventViewModel, addEventController);
        todaysEventsView = new TodaysEventsView(
                todaysEventsViewModel, addEventController, addEventViewModel,
                deleteTodaysEventController, editTodaysEventController, editTodaysEventViewModel
        );
        todaysEventsView.setCategoryGateway(categoryDataAccess);

        // 初始化 addEventViewModel 的下拉框数据
        List<String> names = commonDao.getAllEvents().stream().map(e -> e.getName()).toList();
        AddedEventState state = addEventViewModel.getState();
        state.setAvailableNames(names);
        addEventViewModel.setState(state);

        createEventView = new CreateEventView(createdEventViewModel, addEventViewModel, commonDao, categoryDataAccess);
        createEventView.setCreateEventController(createEventController);
        
        // Set up category management dialog opening and event view refresh
        createEventView.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("openCategoryManagement".equals(evt.getPropertyName())) {
                    openCategoryDialog(commonDao, todaysEventDAO);
                } else if ("refreshEventViews".equals(evt.getPropertyName())) {
                    // Refresh event views to show updated categories from Task page
                    if (availableEventView != null) {
                        availableEventView.forceRefresh();
                    }
                    if (todaysEventsView != null) {
                        todaysEventsView.forceRefresh();
                    }
                }
            }
        });

        availableEventView = new AvailableEventView(
                availableEventViewModel, deleteEventController, deletedEventViewModel,
                createdEventViewModel, editEventController, editedEventViewModel
        );
        availableEventView.setCategoryGateway(categoryDataAccess);

        // --- Layout Panels ---
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createEventView, addEventView);
        verticalSplit.setResizeWeight(0.5);
        verticalSplit.setDividerSize(2);
        verticalSplit.setEnabled(false);

        JPanel upperRightPanel = new JPanel(new BorderLayout());
        upperRightPanel.add(todaysEventsView, BorderLayout.CENTER);
        upperRightPanel.setBackground(new Color(240, 240, 255));

        JPanel topCenterRow = new JPanel(new GridLayout(1, 2));
        topCenterRow.add(verticalSplit);
        topCenterRow.add(upperRightPanel);

        JPanel bottomBox = new JPanel(new BorderLayout());
        bottomBox.add(availableEventView, BorderLayout.CENTER);
        bottomBox.setPreferredSize(new Dimension(800, 300));
        bottomBox.setBackground(Color.GRAY);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topCenterRow, BorderLayout.CENTER);
        centerPanel.add(bottomBox, BorderLayout.SOUTH);

        // --- Set up Today So Far Panel ---
        // Get shared Today So Far components
        app.SharedTodaySoFarComponents sharedTodaySoFar = app.SharedTodaySoFarComponents.getInstance();
        overdueTasksController = sharedTodaySoFar.getOverdueTasksController();
        todaySoFarController = sharedTodaySoFar.getTodaySoFarController();
        
        // Create Today So Far view using shared components
        TodaySoFarView todaySoFarView = sharedTodaySoFar.createTodaySoFarView();
        
        // Trigger initial data load
        sharedTodaySoFar.refresh();
        
        // Set Today So Far controller on event presenters that need to refresh
        if (createEventPresenter instanceof CreateEventPresenter) {
            ((CreateEventPresenter) createEventPresenter).setTodaySoFarController(todaySoFarController);
        }
        if (addEventPresenter instanceof AddEventPresenter) {
            ((AddEventPresenter) addEventPresenter).setTodaySoFarController(todaySoFarController);
        }
        if (delTodayPresenter instanceof DeleteTodaysEventPresenter) {
            ((DeleteTodaysEventPresenter) delTodayPresenter).setTodaySoFarController(todaySoFarController);
        }
        if (editTodayPresenter instanceof EditTodaysEventPresenter) {
            ((EditTodaysEventPresenter) editTodayPresenter).setTodaySoFarController(todaySoFarController);
        }
        // Also set for edit available event presenter
        editEventPresenter.setTodaySoFarController(todaySoFarController);
        // Also set for delete available event presenter
        if (deleteEventPresenter instanceof DeleteEventPresenter) {
            ((DeleteEventPresenter) deleteEventPresenter).setTodaySoFarController(todaySoFarController);
        }

        // Wrap Today So Far in a panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(todaySoFarView, BorderLayout.CENTER);
        rightPanel.setPreferredSize(new Dimension(380, 0));
        rightPanel.setMinimumSize(new Dimension(320, 0));

        // Create horizontal split pane for resizable layout
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerPanel, rightPanel);
        mainSplitPane.setDividerLocation(870);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(0.7);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);

        return mainPanel;
    }
  
    private void openCategoryDialog(EventAvailableDataAccessObject commonDao, TodaysEventDataAccessObject todaysEventDAO) {
        Container parent = createEventView.getParent();
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
                // Wire up event ViewModels for auto-refresh when categories change
                categoryPresenter.setAvailableEventViewModel(availableEventViewModel);
                categoryPresenter.setTodaysEventsViewModel(todaysEventsViewModel);
                // Wire up Today So Far controller for auto-refresh when categories change
                categoryPresenter.setTodaySoFarController(todaySoFarController);
                // Wire up overdue tasks controller for auto-refresh when categories change
                categoryPresenter.setOverdueTasksController(overdueTasksController);
                
                CategoryFactory categoryFactory = new CommonCategoryFactory();
                CreateCategoryInputBoundary createCategoryInteractor = new CreateCategoryInteractor(
                        categoryDataAccess,
                        categoryPresenter,
                        categoryFactory
                );
                CreateCategoryController createCategoryController = new CreateCategoryController(
                        createCategoryInteractor
                );
                
                // Create adapters following ISP - separate interfaces for each concern
                // Category operations adapter
                DeleteCategoryCategoryDataAccessInterface categoryCategoryAdapter = new DeleteCategoryCategoryDataAccessInterface() {
                    @Override
                    public entity.Category getCategoryById(String categoryId) {
                        return categoryDataAccess.getCategoryById(categoryId);
                    }
                    
                    @Override
                    public int getCategoryCount() {
                        return categoryDataAccess.getCategoryCount();
                    }
                    
                    @Override
                    public boolean exists(entity.Category category) {
                        return categoryDataAccess.getCategoryById(category.getId()) != null;
                    }
                    
                    @Override
                    public boolean deleteCategory(entity.Category category) {
                        return categoryDataAccess.deleteCategory(category.getId());
                    }

                };
                
                // Task operations adapter (no tasks in event context)
                DeleteCategoryTaskDataAccessInterface categoryTaskAdapter = new DeleteCategoryTaskDataAccessInterface() {
                    @Override
                    public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
                        // No tasks in event context
                        return new ArrayList<>();
                    }
                    
                    @Override
                    public List<Task> findTodaysTasksByCategory(String categoryId) {
                        // No tasks in event context
                        return new ArrayList<>();
                    }
                    
                    @Override
                    public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
                        // No tasks in event context
                        return true;
                    }
                    
                    @Override
                    public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
                        // No tasks in event context
                        return true;
                    }

                    @Override
                    public List<TaskAvailable> findAvailableTasksWithEmptyCategory() {
                        // No tasks in event context
                        return new ArrayList<>();
                    }

                    @Override
                    public List<Task> findTodaysTasksWithEmptyCategory() {
                        // No tasks in event context
                        return new ArrayList<>();
                    }
                    
                    @Override
                    public boolean updateTasksCategoryToNull(String categoryId) {
                        // No tasks in event context
                        return true;
                    }
                };
                
                // Also create an event adapter for clearing event categories
                DeleteCategoryEventDataAccessInterface eventDeleteAdapter = new DeleteCategoryEventDataAccessInterface() {
                    @Override
                    public List<entity.info.Info> findAvailableEventsByCategory(String categoryId) {
                        return commonDao.findAvailableEventsByCategory(categoryId);
                    }
                    
                    @Override
                    public List<entity.info.Info> findTodaysEventsByCategory(String categoryId) {
                        return todaysEventDAO.findTodaysEventsByCategory(categoryId);
                    }
                    
                    @Override
                    public boolean clearAvailableEventCategory(String eventId) {
                        return commonDao.clearAvailableEventCategory(eventId);
                    }
                    
                    @Override
                    public boolean clearTodaysEventCategory(String eventId) {
                        return todaysEventDAO.clearTodaysEventCategory(eventId);
                    }

                    @Override
                    public List<entity.info.Info> findAvailableEventsWithEmptyCategory() {
                        return commonDao.findAvailableEventsWithEmptyCategory();
                    }

                    @Override
                    public List<entity.info.Info> findTodaysEventsWithEmptyCategory() {
                        return todaysEventDAO.findTodaysEventsWithEmptyCategory();
                    }
                    
                    @Override
                    public boolean updateEventsCategoryToNull(String categoryId) {
                        try {
                            return commonDao.updateEventsCategoryToNull(categoryId) && 
                                   todaysEventDAO.updateEventsCategoryToNull(categoryId);
                        } catch (Exception e) {
                            return false;
                        }
                    }
                };
                
                // Combined adapter no longer needed - we use segregated interfaces directly
                
                DeleteCategoryInteractor deleteCategoryInteractor = new DeleteCategoryInteractor(
                        categoryCategoryAdapter,  // Category operations
                        categoryTaskAdapter,      // Task operations (empty in event context)
                        eventDeleteAdapter,       // Event operations
                        categoryPresenter
                );
                DeleteCategoryController deleteCategoryController = new DeleteCategoryController(
                        deleteCategoryInteractor
                );
                
                // Create a no-op adapter for event context (events don't need updates since they share category objects)
                EditCategoryTaskDataAccessInterface eventCategoryAdapter = new EditCategoryTaskDataAccessInterface() {
                    @Override
                    public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
                        // Events don't need special handling - they share category objects
                        return new ArrayList<>();
                    }
                    
                    @Override
                    public List<Task> findTodaysTasksByCategory(String categoryId) {
                        // Events don't need special handling - they share category objects
                        return new ArrayList<>();
                    }
                    
                    @Override
                    public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
                        // Events don't need special handling - they share category objects
                        return true;
                    }
                    
                    @Override
                    public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
                        // Events don't need special handling - they share category objects
                        return true;
                    }
                };
                
                EditCategoryInputBoundary editCategoryInteractor = new EditCategoryInteractor(
                        categoryDataAccess,
                        eventCategoryAdapter,
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
                        createEventView.refreshCategories();
                    }
                });
            }
            
            // Reload categories in the dialog to ensure latest from Task page
            categoryDialog.loadCategories();
            categoryDialog.setVisible(true);
        }
    }
}
