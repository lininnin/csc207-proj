package app.Angela;

import data_access.InMemoryHistoryDataAccess;
import entity.Angela.TodaySoFarSnapshot;
import entity.Angela.Task.Task;
import entity.Angela.Task.TaskFactory;
import entity.info.Info;
import entity.info.InfoFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import interface_adapter.Angela.view_history.ViewHistoryController;
import interface_adapter.Angela.view_history.ViewHistoryPresenter;
import interface_adapter.Angela.view_history.ViewHistoryViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarViewModel;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.sophia.today_goal.TodayGoalsViewModel;
import use_case.Angela.view_history.ViewHistoryDataAccessInterface;
import use_case.Angela.view_history.ViewHistoryInteractor;
import use_case.Angela.view_history.ViewHistoryInputBoundary;
import use_case.Angela.view_history.ViewHistoryOutputBoundary;
import view.Angela.HistoryView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Builder class for creating the History page with all its dependencies.
 * Follows Clean Architecture by wiring all layers together.
 * Creates separate view models for the reusable components.
 */
public class HistoryPageBuilder {
    private static ViewHistoryDataAccessInterface historyDataAccess;
    private static ViewHistoryViewModel viewModel;
    
    // View models for the reusable components
    private static TodaySoFarViewModel todaySoFarViewModel;
    private static TodayTasksViewModel todayTasksViewModel;
    private static TodaysEventsViewModel todaysEventsViewModel;
    private static TodayGoalsViewModel todayGoalsViewModel;
    
    /**
     * Creates and returns a fully wired History view panel.
     * @return The History view panel
     */
    public static JPanel createHistoryView() {
        // Initialize data access (singleton pattern for shared data)
        if (historyDataAccess == null) {
            historyDataAccess = new InMemoryHistoryDataAccess();
            // Create sample data for demo purposes
            createSampleHistoryData();
        }
        
        // Create view models
        if (viewModel == null) {
            viewModel = new ViewHistoryViewModel();
        }
        
        // Create view models for reusable components (separate instances for history)
        if (todaySoFarViewModel == null) {
            todaySoFarViewModel = new TodaySoFarViewModel();
        }
        if (todayTasksViewModel == null) {
            todayTasksViewModel = new TodayTasksViewModel();
        }
        if (todaysEventsViewModel == null) {
            todaysEventsViewModel = new TodaysEventsViewModel();
        }
        if (todayGoalsViewModel == null) {
            todayGoalsViewModel = new TodayGoalsViewModel();
        }
        
        // Create presenter
        ViewHistoryOutputBoundary presenter = new ViewHistoryPresenter(viewModel);
        
        // Create interactor
        ViewHistoryInputBoundary interactor = new ViewHistoryInteractor(historyDataAccess, presenter);
        
        // Create controller
        ViewHistoryController controller = new ViewHistoryController(interactor);
        
        // Create and return view with all view models
        return new HistoryView(
            controller, 
            viewModel,
            todaySoFarViewModel,
            todayTasksViewModel,
            todaysEventsViewModel,
            todayGoalsViewModel
        );
    }
    
    /**
     * Gets the shared history data access instance.
     * Used by other components to save snapshots.
     * @return The history data access instance
     */
    public static ViewHistoryDataAccessInterface getHistoryDataAccess() {
        if (historyDataAccess == null) {
            historyDataAccess = new InMemoryHistoryDataAccess();
        }
        return historyDataAccess;
    }
    
    /**
     * Resets the history data (useful for testing).
     */
    public static void resetHistory() {
        if (historyDataAccess instanceof InMemoryHistoryDataAccess) {
            ((InMemoryHistoryDataAccess) historyDataAccess).clear();
        }
        // Reset view models
        todaySoFarViewModel = null;
        todayTasksViewModel = null;
        todaysEventsViewModel = null;
        todayGoalsViewModel = null;
    }
    
    /**
     * Creates sample history data for demo purposes.
     */
    private static void createSampleHistoryData() {
        TaskFactory taskFactory = new TaskFactory();
        InfoFactory infoFactory = new InfoFactory();
        BeginAndDueDatesFactory datesFactory = new BeginAndDueDatesFactory();
        
        // Define varied task sets for different days
        String[][] workTasks = {
            {"Review code", "Complete presentation", "Update documentation"},
            {"Client meeting", "Project planning", "Email responses"},
            {"Bug fixing", "Team standup", "Design review"},
            {"Write tests", "Deploy to staging", "Performance tuning"},
            {"Research task", "Create wireframes", "Database updates"},
            {"Sprint planning", "User interviews", "API integration"},
            {"Code refactoring", "Security review", "System monitoring"},
            {"Release preparation", "Training session", "Vendor meeting"}
        };
        
        String[][] healthTasks = {
            {"Morning run", "Yoga class", "Meal prep"},
            {"Gym workout", "Walk in park", "Hydration tracking"},
            {"Stretching", "Meditation", "Healthy breakfast"},
            {"Swimming", "Strength training", "Sleep optimization"},
            {"Cycling", "Mindfulness", "Vitamin supplements"},
            {"Pilates", "Deep breathing", "Nutrition planning"},
            {"Cardio workout", "Relaxation", "Health checkup"},
            {"Dancing", "Journaling", "Meal planning"}
        };
        
        String[][] personalTasks = {
            {"Read chapter", "Call family", "Organize closet"},
            {"Learn new skill", "Pay bills", "Clean house"},
            {"Practice guitar", "Grocery shopping", "Laundry"},
            {"Write journal", "Plan vacation", "Fix bike"},
            {"Photography", "Cook dinner", "Garden work"},
            {"Study language", "Social meetup", "Car maintenance"},
            {"Art project", "Financial review", "Home repairs"},
            {"Music practice", "Friend visit", "Decluttering"}
        };
        
        String[][] weatherEvents = {
            {"Sunny morning", "Clear skies", "Warm afternoon"},
            {"Light rain", "Cloudy day", "Cool breeze"},
            {"Partly cloudy", "Sunny intervals", "Mild temperature"},
            {"Overcast", "Drizzle", "Humid conditions"},
            {"Bright sunshine", "Blue skies", "Perfect weather"},
            {"Morning fog", "Clearing up", "Pleasant evening"},
            {"Windy day", "Scattered clouds", "Crisp air"},
            {"Beautiful sunset", "Clear night", "Starry sky"}
        };
        
        String[][] workEvents = {
            {"Product launch", "Team celebration", "New hire onboarding"},
            {"Quarterly review", "Training workshop", "Client demo"},
            {"System upgrade", "Emergency fix", "Successful deployment"},
            {"Conference call", "Brainstorming session", "Project milestone"},
            {"Knowledge sharing", "Innovation meeting", "Process improvement"},
            {"Customer feedback", "Technology presentation", "Team building"},
            {"Strategic planning", "Performance review", "System integration"},
            {"Market analysis", "Competitor research", "Growth planning"}
        };
        
        // Create sample data for the past week
        for (int i = 7; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            int dayIndex = i % 8; // Cycle through different task sets
            
            // Create varied tasks for each day
            Info taskInfo1 = new Info.Builder(workTasks[dayIndex][0])
                .category("Work")
                .description("Work-related task for " + date.getDayOfWeek())
                .build();
            Task task1 = (Task) taskFactory.create(UUID.randomUUID().toString(), taskInfo1, 
                datesFactory.create(date, date.plusDays(1)), false);
            
            Info taskInfo2 = new Info.Builder(healthTasks[dayIndex][1])
                .category("Health")
                .description("Health and wellness activity")
                .build();
            Task task2 = (Task) taskFactory.create(UUID.randomUUID().toString(), taskInfo2,
                datesFactory.create(date, date), false);
            
            Info taskInfo3 = new Info.Builder(personalTasks[dayIndex][2])
                .category("Personal")
                .description("Personal development or chore")
                .build();
            Task task3 = (Task) taskFactory.create(UUID.randomUUID().toString(), taskInfo3,
                datesFactory.create(date, date), false);
            
            // Vary completion patterns more realistically
            if (i <= 2) { // Recent days - higher completion
                task1.markComplete();
                task2.markComplete();
                if (i == 0) task3.markComplete(); // Today - all done
            } else if (i <= 4) { // Mid-week - moderate completion
                task1.markComplete();
                if (i % 2 == 0) task2.markComplete();
            } else { // Older days - varied completion
                if (i % 3 == 0) task1.markComplete();
                if (i % 2 == 1) task2.markComplete();
                if (i == 7) task3.markComplete();
            }
            
            List<Task> todaysTasks = Arrays.asList(task1, task2, task3);
            List<Task> completedTasks = todaysTasks.stream()
                .filter(Task::isCompleted)
                .toList();
            
            // Create varied events for each day
            Info event1 = new Info.Builder(weatherEvents[dayIndex][0])
                .category("Weather")
                .description("Weather condition for " + date)
                .build();
            
            Info event2 = new Info.Builder(workEvents[dayIndex][1])
                .category("Work")
                .description("Work event or milestone")
                .build();
            
            List<Info> events = Arrays.asList(event1, event2);
            
            // Create realistic goal progress that changes over time
            TodaySoFarSnapshot.GoalProgress goal1 = new TodaySoFarSnapshot.GoalProgress(
                "workout-goal",
                "Complete 5 workouts this week",
                "Weekly",
                Math.max(0, 7 - i), // Progress increases over time
                5
            );
            
            TodaySoFarSnapshot.GoalProgress goal2 = new TodaySoFarSnapshot.GoalProgress(
                "reading-goal",
                "Read 20 pages daily",
                "Daily",
                i < 4 ? 20 : (i % 3 == 0 ? 15 : 0), // Varied daily reading
                20
            );
            
            List<TodaySoFarSnapshot.GoalProgress> goalProgress = Arrays.asList(goal1, goal2);
            
            // Calculate completion rate
            int completionRate = todaysTasks.isEmpty() ? 0 : (completedTasks.size() * 100) / todaysTasks.size();
            
            // Create and save snapshot
            TodaySoFarSnapshot snapshot = new TodaySoFarSnapshot(
                date,
                todaysTasks,
                completedTasks,
                completionRate,
                Collections.emptyList(), // No overdue tasks in sample
                events,
                goalProgress,
                Collections.emptyList()  // No wellness entries for Angela's scope
            );
            
            historyDataAccess.saveSnapshot(snapshot);
        }
        
        System.out.println("Sample history data created for the past 8 days with varied content");
    }
}