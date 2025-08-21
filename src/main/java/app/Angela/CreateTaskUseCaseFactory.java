package app.Angela;

import interface_adapter.Angela.task.create.CreateTaskController;
import interface_adapter.Angela.task.create.CreateTaskPresenter;
import interface_adapter.Angela.task.create.CreateTaskViewModel;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.ViewManagerModel;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import use_case.Angela.task.create.CreateTaskCategoryDataAccessInterface;
import use_case.Angela.task.create.CreateTaskInputBoundary;
import use_case.Angela.task.create.CreateTaskInteractor;
import use_case.Angela.task.create.CreateTaskOutputBoundary;
import entity.info.InfoFactory;
import entity.Angela.Task.TaskAvailableFactory;

/**
 * Factory for creating the create_task use case components.
 * This class wires up all the dependencies following Clean Architecture.
 */
public class CreateTaskUseCaseFactory {

    /**
     * Creates a controller for the create task use case.
     *
     * @param dataAccess The data access interface for creating tasks
     * @param categoryDataAccess The data access interface for categories
     * @param createTaskViewModel The view model for create task view
     * @param availableTasksViewModel The view model for available tasks view
     * @param viewManagerModel The view manager model
     * @return A configured CreateTaskController
     */
    public static CreateTaskController create(
            CreateTaskDataAccessInterface dataAccess,
            CreateTaskCategoryDataAccessInterface categoryDataAccess,
            CreateTaskViewModel createTaskViewModel,
            AvailableTasksViewModel availableTasksViewModel,
            ViewManagerModel viewManagerModel) {

        // Create the output boundary (presenter)
        CreateTaskOutputBoundary createTaskPresenter = new CreateTaskPresenter(
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        // Create factories
        InfoFactory infoFactory = new InfoFactory();
        TaskAvailableFactory taskAvailableFactory = new TaskAvailableFactory();
        
        // Create the input boundary (interactor)
        CreateTaskInputBoundary createTaskInteractor = new CreateTaskInteractor(
                dataAccess,
                categoryDataAccess,
                createTaskPresenter,
                infoFactory,
                taskAvailableFactory
        );

        // Create and return the controller
        return new CreateTaskController(createTaskInteractor);
    }
}