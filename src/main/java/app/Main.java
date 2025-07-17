package app;

import interface_adapter.persistence.InMemoryDailyLogRepository;
import interface_adapter.persistence.InMemoryTaskRepository;
import interface_adapter.controller.CreateTaskController;
import interface_adapter.controller.MarkTaskCompleteController;
import interface_adapter.presenter.CreateTaskPresenter;
import interface_adapter.presenter.MarkTaskCompletePresenter;
import use_case.create_task.CreateTaskUseCase;
import use_case.mark_task_complete.MarkTaskCompleteUseCase;
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

        // Create controllers (interface adapters)
        CreateTaskController createTaskController = new CreateTaskController(createTaskUseCase);
        MarkTaskCompleteController markCompleteController = new MarkTaskCompleteController(markCompleteUseCase);

        // Create view
        TaskView taskView = new TaskView(taskViewModel, createTaskController, markCompleteController);

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
        taskViewModel.setTodaysTasks(taskRepository.findTodaysTasks());
        taskViewModel.setCompletedTasks(taskRepository.findTodaysCompletedTasks());
        taskViewModel.setOverdueTasks(taskRepository.findOverdueTasks());
        isLoading = false;
    }
}