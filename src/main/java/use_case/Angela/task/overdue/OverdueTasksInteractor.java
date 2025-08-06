package use_case.Angela.task.overdue;

import entity.Angela.Task.Task;
import entity.Category;
import use_case.Angela.category.CategoryGateway;
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
    private final CategoryGateway categoryGateway;
    private final OverdueTasksOutputBoundary presenter;
    
    public OverdueTasksInteractor(OverdueTasksDataAccessInterface taskDataAccess,
                                 CategoryGateway categoryGateway,
                                 OverdueTasksOutputBoundary presenter) {
        this.taskDataAccess = taskDataAccess;
        this.categoryGateway = categoryGateway;
        this.presenter = presenter;
    }
    
    @Override
    public void execute(OverdueTasksInputData inputData) {
        System.out.println("DEBUG [OverdueTasksInteractor]: execute() called with daysBack = " + inputData.getDaysBack());
        try {
            int daysBack = inputData.getDaysBack();
            List<Task> overdueTasks = taskDataAccess.getOverdueTasks(daysBack);
            System.out.println("DEBUG [OverdueTasksInteractor]: Found " + overdueTasks.size() + " overdue tasks");
            
            // Convert to output data
            List<OverdueTasksOutputData.OverdueTaskData> overdueDataList = new ArrayList<>();
            LocalDate today = LocalDate.now();
            
            for (Task task : overdueTasks) {
                System.out.println("DEBUG [OverdueTasksInteractor]: Processing overdue task - ID: " + task.getId() + 
                                  ", Name: " + task.getInfo().getName() + 
                                  ", Due Date: " + task.getDates().getDueDate());
                // Get category name
                String categoryName = "";
                String categoryId = task.getInfo().getCategory();
                if (categoryId != null && !categoryId.isEmpty()) {
                    Category category = categoryGateway.getCategoryById(categoryId);
                    if (category != null) {
                        categoryName = category.getName();
                    }
                }
                
                // Calculate days overdue
                LocalDate dueDate = task.getDates().getDueDate();
                int daysOverdue = 0;
                if (dueDate != null) {
                    daysOverdue = (int) ChronoUnit.DAYS.between(dueDate, today);
                }
                
                OverdueTasksOutputData.OverdueTaskData taskData = 
                    new OverdueTasksOutputData.OverdueTaskData(
                        task.getId(),
                        task.getInfo().getName(),
                        categoryName,
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