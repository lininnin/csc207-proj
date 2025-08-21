package interface_adapter.Angela.category;

import interface_adapter.Angela.category.create.CreateCategoryPresenter;
import interface_adapter.Angela.category.delete.DeleteCategoryPresenter;
import interface_adapter.Angela.category.edit.EditCategoryPresenter;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.edit.*;

/**
 * Facade presenter that delegates to specific presenters for each use case.
 * Maintains backward compatibility while following Single Responsibility Principle.
 */
public class CategoryManagementPresenter implements
        CreateCategoryOutputBoundary,
        DeleteCategoryOutputBoundary,
        EditCategoryOutputBoundary {

    private final CreateCategoryPresenter createPresenter;
    private final DeleteCategoryPresenter deletePresenter;
    private final EditCategoryPresenter editPresenter;

    public CategoryManagementPresenter(CategoryManagementViewModel viewModel) {
        this.createPresenter = new CreateCategoryPresenter(viewModel);
        this.deletePresenter = new DeleteCategoryPresenter(viewModel);
        this.editPresenter = new EditCategoryPresenter(viewModel);
    }
    
    public void setAvailableTasksViewModel(AvailableTasksViewModel availableTasksViewModel) {
        this.deletePresenter.setAvailableTasksViewModel(availableTasksViewModel);
        this.editPresenter.setAvailableTasksViewModel(availableTasksViewModel);
    }
    
    public void setTodayTasksViewModel(TodayTasksViewModel todayTasksViewModel) {
        this.deletePresenter.setTodayTasksViewModel(todayTasksViewModel);
        this.editPresenter.setTodayTasksViewModel(todayTasksViewModel);
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.deletePresenter.setOverdueTasksController(controller);
        this.editPresenter.setOverdueTasksController(controller);
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.deletePresenter.setTodaySoFarController(controller);
        this.editPresenter.setTodaySoFarController(controller);
    }
    
    public void setAvailableEventViewModel(AvailableEventViewModel availableEventViewModel) {
        this.deletePresenter.setAvailableEventViewModel(availableEventViewModel);
        this.editPresenter.setAvailableEventViewModel(availableEventViewModel);
    }
    
    public void setTodaysEventsViewModel(TodaysEventsViewModel todaysEventsViewModel) {
        this.deletePresenter.setTodaysEventsViewModel(todaysEventsViewModel);
        this.editPresenter.setTodaysEventsViewModel(todaysEventsViewModel);
    }

    // Create Category delegation
    @Override
    public void prepareSuccessView(CreateCategoryOutputData outputData) {
        createPresenter.prepareSuccessView(outputData);
    }

    @Override
    public void prepareFailView(String error) {
        createPresenter.prepareFailView(error);
    }

    // Delete Category delegation  
    @Override
    public void prepareSuccessView(DeleteCategoryOutputData outputData) {
        deletePresenter.prepareSuccessView(outputData);
    }

    // Edit Category delegation
    @Override
    public void prepareSuccessView(EditCategoryOutputData outputData) {
        editPresenter.prepareSuccessView(outputData);
    }
}