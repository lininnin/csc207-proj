package app;

import interface_adapter.persistence.InMemoryDailyLogRepository;
import interface_adapter.persistence.InMemoryTaskRepository;
import entity.Task;
import interface_adapter.controller.*;
import interface_adapter.presenter.*;
import use_case.create_task.*;
import use_case.mark_task_complete.*;
import use_case.add_task_to_today.*;
import view.TaskView;
import view.TaskViewModel;

import javax.swing.*;
import java.awt.*;

/**
 * Main application class for MindTrack.
 * Sets up the application using Clean Architecture principles.
 */
public class Main {
    public static void main(String[] args) {
        // Use Swing's event dispatch thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }

            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // Create repositories (data layer)
        InMemoryTaskRepository taskRepository = new InMemoryTaskRepository();
        InMemoryDailyLogRepository dailyLogRepository = new InMemoryDailyLogRepository();

        // Create view model
        TaskViewModel taskViewModel = new TaskViewModel();

        // Create presenters (interface adapters)
        CreateTaskPresenter createTaskPresenter = new CreateTaskPresenter(taskViewModel);
        MarkTaskCompletePresenter markCompletePresenter = new MarkTaskCompletePresenter(taskViewModel);
        AddTaskToTodayPresenter addTaskToTodayPresenter = new AddTaskToTodayPresenter(taskViewModel);

        // Create use cases
        CreateTaskUseCase createTaskUseCase = new CreateTaskUseCase(
                taskRepository,
                createTaskPresenter
        );

        MarkTaskCompleteUseCase markCompleteUseCase = new MarkTaskCompleteUseCase(
                taskRepository,
                dailyLogRepository,
                markCompletePresenter
        );

        AddTaskToTodayUseCase addTaskToTodayUseCase = new AddTaskToTodayUseCase(
                taskRepository,
                dailyLogRepository,
                addTaskToTodayPresenter
        );

        // Create controllers (interface adapters)
        CreateTaskController createTaskController = new CreateTaskController(createTaskUseCase);
        MarkTaskCompleteController markCompleteController = new MarkTaskCompleteController(markCompleteUseCase);
        AddTaskToTodayController addTaskToTodayController = new AddTaskToTodayController(addTaskToTodayUseCase);

        // Create view
        TaskView taskView = new TaskView(taskViewModel, createTaskController, markCompleteController);
        taskView.setAddTaskToTodayController(addTaskToTodayController);

        // Create a data loader to populate view model
        TaskDataLoader dataLoader = new TaskDataLoader(taskRepository, taskViewModel);
        taskView.setDataReloader(dataLoader::loadInitialData);
        dataLoader.loadInitialData();

        // Create main window
        JFrame frame = new JFrame("MindTrack - Task Management");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Add components
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(taskView, BorderLayout.CENTER);

        // Set window properties
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(41, 128, 185));

        JLabel titleLabel = new JLabel("MindTrack - Daily Task Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }
}

/**
 * Helper class to load data into the view model.
 */
class TaskDataLoader {
    private final InMemoryTaskRepository taskRepository;
    private final TaskViewModel taskViewModel;
    private boolean isLoading = false;

    TaskDataLoader(InMemoryTaskRepository taskRepository, TaskViewModel taskViewModel) {
        this.taskRepository = taskRepository;
        this.taskViewModel = taskViewModel;
    }

    void loadInitialData() {
        loadData();
    }

    private void loadData() {
        if (isLoading) {
            return; // Prevent recursive calls
        }

        isLoading = true;
        taskViewModel.setAvailableTasks(taskRepository.findAvailableTasks());
        taskViewModel.setTodaysTasks(taskRepository.findTodaysTasks());
        taskViewModel.setCompletedTasks(taskRepository.findTodaysCompletedTasks());
        taskViewModel.setOverdueTasks(taskRepository.findOverdueTasks());

        // Calculate completion rate
        int scheduled = taskRepository.findTodaysTasks().size() + taskRepository.findTodaysCompletedTasks().size();
        int completed = taskRepository.findTodaysCompletedTasks().size();
        double rate = scheduled > 0 ? (double) completed / scheduled : 0.0;
        taskViewModel.setCompletionRate(rate);

        isLoading = false;
    }
}