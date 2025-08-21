package use_case.Angela.task.overdue;

import entity.Angela.Task.TaskInterf;
import entity.Category;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the overdue tasks use case.
 * Handles the business logic for retrieving and processing overdue tasks.
 */
public class OverdueTasksInteractor implements OverdueTasksInputBoundary {
    private final OverdueTasksDataAccessInterface taskDataAccess;
    private final OverdueTasksCategoryDataAccessInterface categoryDataAccess;
    private final OverdueTasksOutputBoundary presenter;
    
    public OverdueTasksInteractor(OverdueTasksDataAccessInterface taskDataAccess,
                                 OverdueTasksCategoryDataAccessInterface categoryDataAccess,
                                 OverdueTasksOutputBoundary presenter) {
        this.taskDataAccess = taskDataAccess;
        this.categoryDataAccess = categoryDataAccess;
        this.presenter = presenter;
    }
    
    @Override
    public void execute(OverdueTasksInputData inputData) {
        try {
            int daysBack = inputData.getDaysBack();
            List<TaskInterf> overdueTasks = taskDataAccess.getOverdueTasks(daysBack);
            
            // Convert to output data
            List<OverdueTasksOutputData.OverdueTaskData> overdueDataList = new ArrayList<>();
            LocalDate today = LocalDate.now();
            
            for (TaskInterf task : overdueTasks) {
                // Get category name
                String categoryName = "";
                String categoryId = task.getInfo().getCategory();
                if (categoryId != null && !categoryId.isEmpty()) {
                    Category category = categoryDataAccess.getCategoryById(categoryId);
                    if (category != null) {
                        categoryName = category.getName();
                    }
                }
                
                // Calculate days overdue
                LocalDate dueDate = task.getBeginAndDueDates().getDueDate();
                int daysOverdue = 0;
                if (dueDate != null) {
                    daysOverdue = (int) ChronoUnit.DAYS.between(dueDate, today);
                }
                
                // Get task priority as string
                String priorityStr = "";
                if (task.getPriority() != null) {
                    priorityStr = task.getPriority().toString();
                }
                
                OverdueTasksOutputData.OverdueTaskData taskData = 
                    new OverdueTasksOutputData.OverdueTaskData(
                        task.getId(),
                        task.getInfo().getName(),
                        task.getInfo().getDescription() != null ? task.getInfo().getDescription() : "",
                        categoryName,
                        priorityStr,
                        dueDate,
                        daysOverdue
                    );
                
                overdueDataList.add(taskData);
            }
            
            OverdueTasksOutputData outputData = new OverdueTasksOutputData(overdueDataList);
            presenter.presentOverdueTasks(outputData);
            
        } catch (Exception e) {
            presenter.prepareFailView("Failed to retrieve overdue tasks: " + e.getMessage());
        }
    }
}