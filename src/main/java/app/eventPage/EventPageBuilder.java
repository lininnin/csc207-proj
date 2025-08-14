package app.eventPage;

import entity.Alex.DailyEventLog.DailyEventLogFactory;
import entity.Alex.DailyEventLog.DailyEventLogFactoryInterf;
import entity.Alex.EventAvailable.EventAvailableFactory;
import entity.Alex.EventAvailable.EventAvailableFactoryInterf;
import entity.Alex.Event.EventFactory;
import entity.Alex.Event.EventFactoryInterf;
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
import data_access.InMemoryCategoryGateway;
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
import java.util.Collections;

import use_case.alex.event_related.add_event.*;
import use_case.alex.event_related.create_event.*;
import use_case.alex.event_related.avaliable_events_module.delete_event.*;
import use_case.alex.event_related.avaliable_events_module.edit_event.*;
import use_case.alex.event_related.todays_events_module.delete_todays_event.*;
import use_case.alex.event_related.todays_events_module.edit_todays_event.*;

import data_access.EventAvailableDataAccessObject;
import data_access.TodaysEventDataAccessObject;
import entity.info.InfoFactory;

import view.Alex.Event.*;
import view.Angela.TodaySoFarView;
import interface_adapter.Angela.task.overdue.OverdueTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.task.overdue.OverdueTasksPresenter;
import interface_adapter.Angela.today_so_far.TodaySoFarViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.Angela.today_so_far.TodaySoFarPresenter;
import use_case.Angela.task.overdue.OverdueTasksInputBoundary;
import use_case.Angela.task.overdue.OverdueTasksInteractor;
import use_case.Angela.task.overdue.OverdueTasksOutputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInputBoundary;
import use_case.Angela.today_so_far.TodaySoFarInteractor;
import data_access.InMemoryTaskGateway;
import data_access.InMemoryTodaySoFarDataAccess;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class EventPageBuilder {
    
    // Category management fields - Use shared instance
    private final InMemoryCategoryGateway categoryGateway = app.SharedDataAccess.getInstance().getCategoryGateway();
    private final CategoryManagementViewModel categoryManagementViewModel = new CategoryManagementViewModel();
    private CategoryManagementDialog categoryDialog;
    private CreateEventView createEventView;
    private AvailableEventViewModel availableEventViewModel;
    private TodaysEventsViewModel todaysEventsViewModel;
    
    // Today So Far panel fields
    private TodaySoFarController todaySoFarController;
    private OverdueTasksController overdueTasksController;


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
        DailyEventLogFactoryInterf dailyEventLogFactory = new DailyEventLogFactory(); // 假设你实现了这个类
        TodaysEventDataAccessObject todaysEventDAO = new TodaysEventDataAccessObject(dailyEventLogFactory);

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
        AddEventView addEventView = new AddEventView(addEventViewModel, addEventController);
        TodaysEventsView todaysEventsView = new TodaysEventsView(
                todaysEventsViewModel, addEventController, addEventViewModel,
                deleteTodaysEventController, editTodaysEventController, editTodaysEventViewModel
        );
        todaysEventsView.setCategoryGateway(categoryGateway);

        // 初始化 addEventViewModel 的下拉框数据
        List<String> names = commonDao.getAllEvents().stream().map(e -> e.getName()).toList();
        AddedEventState state = addEventViewModel.getState();
        state.setAvailableNames(names);
        addEventViewModel.setState(state);

        createEventView = new CreateEventView(createdEventViewModel, addEventViewModel, commonDao, categoryGateway);
        createEventView.setCreateEventController(createEventController);
        
        // Set up category management dialog opening
        createEventView.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("openCategoryManagement".equals(evt.getPropertyName())) {
                    openCategoryDialog(commonDao, todaysEventDAO);
                }
            }
        });

        // Set up category management dialog opening
        createEventView.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("openCategoryManagement".equals(evt.getPropertyName())) {
                    openCategoryDialog(commonDao, todaysEventDAO);
                }
            }
        });

        AvailableEventView availableEventView = new AvailableEventView(
                availableEventViewModel, deleteEventController, deletedEventViewModel,
                createdEventViewModel, editEventController, editedEventViewModel
        );
        availableEventView.setCategoryGateway(categoryGateway);

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
        // Create ViewModels for Today So Far
        OverdueTasksViewModel overdueTasksViewModel = new OverdueTasksViewModel();
        TodaySoFarViewModel todaySoFarViewModel = new TodaySoFarViewModel();
        
        // Use shared task gateway for Today So Far integration
        InMemoryTaskGateway taskGateway = app.SharedDataAccess.getInstance().getTaskGateway();
        
        // Wire up Overdue Tasks Use Case
        OverdueTasksOutputBoundary overdueTasksPresenter = new OverdueTasksPresenter(overdueTasksViewModel);
        OverdueTasksInputBoundary overdueTasksInteractor = new OverdueTasksInteractor(
                taskGateway, categoryGateway, overdueTasksPresenter);
        overdueTasksController = new OverdueTasksController(overdueTasksInteractor);
        
        // Use shared Goal repository
        data_access.FileGoalRepository goalRepository = 
            app.SharedDataAccess.getInstance().getGoalRepository();
        
        // Use shared Wellness DAO for Today So Far integration
        data_access.TodaysWellnessLogDataAccessObject wellnessLogDAO = 
            app.SharedDataAccess.getInstance().getWellnessDataAccess();
        
        // Wire up Today So Far Use Case with all data sources
        InMemoryTodaySoFarDataAccess todaySoFarDataAccess = new InMemoryTodaySoFarDataAccess(
                taskGateway, todaysEventDAO, wellnessLogDAO, goalRepository);
        
        TodaySoFarPresenter todaySoFarPresenter = new TodaySoFarPresenter(todaySoFarViewModel);
        TodaySoFarInputBoundary todaySoFarInteractor = new TodaySoFarInteractor(
                todaySoFarDataAccess, todaySoFarPresenter, categoryGateway);
        todaySoFarController = new TodaySoFarController(todaySoFarInteractor);
        
        // Create Today So Far view
        TodaySoFarView todaySoFarView = new TodaySoFarView(overdueTasksViewModel, todaySoFarViewModel);
        todaySoFarView.setOverdueTasksController(overdueTasksController);
        todaySoFarView.setTodaySoFarController(todaySoFarController);
        
        // Trigger initial data load
        todaySoFarController.refresh();
        overdueTasksController.execute(7);
        
        // Set Today So Far controller on event presenters that need to refresh
        if (addEventPresenter instanceof AddEventPresenter) {
            ((AddEventPresenter) addEventPresenter).setTodaySoFarController(todaySoFarController);
        }
        if (delTodayPresenter instanceof DeleteTodaysEventPresenter) {
            ((DeleteTodaysEventPresenter) delTodayPresenter).setTodaySoFarController(todaySoFarController);
        }
        if (editTodayPresenter instanceof EditTodaysEventPresenter) {
            ((EditTodaysEventPresenter) editTodayPresenter).setTodaySoFarController(todaySoFarController);
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
                        categoryGateway,
                        categoryManagementViewModel
                );
                
                // Wire up controllers
                CategoryManagementPresenter categoryPresenter = new CategoryManagementPresenter(
                        categoryManagementViewModel
                );
                // Wire up event ViewModels for auto-refresh when categories change
                categoryPresenter.setAvailableEventViewModel(availableEventViewModel);
                categoryPresenter.setTodaysEventsViewModel(todaysEventsViewModel);
                
                CreateCategoryInputBoundary createCategoryInteractor = new CreateCategoryInteractor(
                        categoryGateway,
                        categoryPresenter
                );
                CreateCategoryController createCategoryController = new CreateCategoryController(
                        createCategoryInteractor
                );
                
                // Create adapters following ISP - separate interfaces for each concern
                // Category operations adapter
                DeleteCategoryCategoryDataAccessInterface categoryCategoryAdapter = new DeleteCategoryCategoryDataAccessInterface() {
                    @Override
                    public entity.Category getCategoryById(String categoryId) {
                        return categoryGateway.getCategoryById(categoryId);
                    }
                    
                    @Override
                    public int getCategoryCount() {
                        return categoryGateway.getCategoryCount();
                    }
                    
                    @Override
                    public boolean exists(entity.Category category) {
                        return categoryGateway.getCategoryById(category.getId()) != null;
                    }
                    
                    @Override
                    public boolean deleteCategory(entity.Category category) {
                        return categoryGateway.deleteCategory(category.getId());
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
                        categoryGateway,
                        eventCategoryAdapter,
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
                        createEventView.refreshCategories();
                    }
                });
            }
            
            categoryDialog.setVisible(true);
        }
    }
}
