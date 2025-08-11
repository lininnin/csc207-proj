package app.Angela;

import interface_adapter.Angela.task.add_to_today.AddTaskToTodayController;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayPresenter;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import use_case.Angela.task.add_to_today.AddTaskToTodayInputBoundary;
import use_case.Angela.task.add_to_today.AddTaskToTodayInteractor;
import use_case.Angela.task.add_to_today.AddTaskToTodayOutputBoundary;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;

/**
 * Factory for creating the Add to Today use case components.
 */
public class AddTaskToTodayUseCaseFactory {

    /**
     * Creates a controller for the Add to Today use case.
     *
     * @param dataAccess the data access interface
     * @param addTaskToTodayViewModel the view model for add to today
     * @param todayTasksViewModel the view model for today's tasks
     * @return the controller
     */
    public static AddTaskToTodayController create(
            AddToTodayDataAccessInterface dataAccess,
            AddTaskToTodayViewModel addTaskToTodayViewModel,
            TodayTasksViewModel todayTasksViewModel) {

        AddTaskToTodayOutputBoundary presenter = new AddTaskToTodayPresenter(
                addTaskToTodayViewModel,
                todayTasksViewModel
        );

        AddTaskToTodayInputBoundary interactor = new AddTaskToTodayInteractor(
                dataAccess,
                presenter
        );

        return new AddTaskToTodayController(interactor);
    }
}