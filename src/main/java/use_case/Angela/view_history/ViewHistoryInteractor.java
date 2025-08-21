package use_case.Angela.view_history;

import entity.Angela.TodaySoFarSnapshot;
import entity.Angela.Task.Task;
import entity.info.Info;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interactor for the view history use case.
 * Handles the business logic for retrieving and exporting historical data.
 */
public class ViewHistoryInteractor implements ViewHistoryInputBoundary {
    private final ViewHistoryDataAccessInterface dataAccess;
    private final ViewHistoryOutputBoundary outputBoundary;
    
    public ViewHistoryInteractor(ViewHistoryDataAccessInterface dataAccess,
                                 ViewHistoryOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(ViewHistoryInputData inputData) {
        LocalDate date = inputData.getDate();
        
        // Validate date is not in the future
        if (date.isAfter(LocalDate.now())) {
            outputBoundary.prepareFailView("Cannot view history for future dates");
            return;
        }
        
        // Retrieve the snapshot for the requested date
        TodaySoFarSnapshot snapshot = dataAccess.getSnapshot(date);
        
        if (snapshot == null) {
            outputBoundary.prepareFailView("No history data available for " + 
                date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            return;
        }
        
        // Sort events and wellness entries by creation time as requested by teammate
        List<Info> sortedEvents = snapshot.getTodaysEvents().stream()
            .sorted(Comparator.comparing(Info::getCreatedDate))
            .collect(Collectors.toList());
        
        // Create a new snapshot with sorted data
        TodaySoFarSnapshot sortedSnapshot = new TodaySoFarSnapshot(
            snapshot.getDate(),
            snapshot.getTodaysTasks(),
            snapshot.getCompletedTasks(),
            snapshot.getTaskCompletionRate(),
            snapshot.getOverdueTasks(),
            sortedEvents,
            snapshot.getGoalProgress(),
            snapshot.getWellnessEntries()
        );
        
        // Prepare the output data
        ViewHistoryOutputData outputData = new ViewHistoryOutputData(sortedSnapshot);
        outputBoundary.prepareSuccessView(outputData);
    }
    
    @Override
    public void loadAvailableDates() {
        List<LocalDate> availableDates = dataAccess.getAvailableDates();
        outputBoundary.presentAvailableDates(availableDates);
    }
    
    @Override
    public void exportHistory(ViewHistoryInputData inputData) {
        LocalDate date = inputData.getDate();
        TodaySoFarSnapshot snapshot = dataAccess.getSnapshot(date);
        
        if (snapshot == null) {
            outputBoundary.presentExportFailure("No data available for export");
            return;
        }
        
        // Generate filename
        String filename = "history_" + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".txt";
        String filePath = System.getProperty("user.home") + "/Downloads/" + filename;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("=== MindTrack History Report ===\n");
            writer.write("Date: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE) + "\n");
            writer.write("Generated: " + LocalDate.now() + "\n\n");
            
            // Write task summary
            writer.write("=== Today's Tasks ===\n");
            writer.write("Completion Rate: " + snapshot.getTaskCompletionRate() + "%\n");
            writer.write("Completed Tasks: " + snapshot.getCompletedTasks().size() + "\n");
            writer.write("Total Tasks: " + snapshot.getTodaysTasks().size() + "\n\n");
            
            // Write completed tasks
            if (!snapshot.getCompletedTasks().isEmpty()) {
                writer.write("Completed:\n");
                for (Task task : snapshot.getCompletedTasks()) {
                    writer.write("  ✓ " + task.getInfo().getName());
                    if (task.getInfo().getCategory() != null && !task.getInfo().getCategory().isEmpty()) {
                        writer.write(" [" + task.getInfo().getCategory() + "]");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }
            
            // Write overdue tasks
            if (!snapshot.getOverdueTasks().isEmpty()) {
                writer.write("Overdue:\n");
                for (Task task : snapshot.getOverdueTasks()) {
                    writer.write("  ⚠ " + task.getInfo().getName() + "\n");
                }
                writer.write("\n");
            }
            
            // Write events
            if (!snapshot.getTodaysEvents().isEmpty()) {
                writer.write("=== Today's Events ===\n");
                for (Info event : snapshot.getTodaysEvents()) {
                    writer.write("  • " + event.getName());
                    if (event.getCategory() != null && !event.getCategory().isEmpty()) {
                        writer.write(" [" + event.getCategory() + "]");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }
            
            // Write goal progress
            if (!snapshot.getGoalProgress().isEmpty()) {
                writer.write("=== Goal Progress ===\n");
                for (TodaySoFarSnapshot.GoalProgress goal : snapshot.getGoalProgress()) {
                    writer.write("  • " + goal.getGoalName() + " (" + goal.getPeriod() + "): " + 
                                goal.getProgressString() + "\n");
                }
                writer.write("\n");
            }
            
            // Write wellness entries
            if (!snapshot.getWellnessEntries().isEmpty()) {
                writer.write("=== Wellness Log ===\n");
                for (var entry : snapshot.getWellnessEntries()) {
                    writer.write("  Time: " + entry.getTime() + "\n");
                    if (entry.getMoodLabel() != null) {
                        writer.write("    Mood: " + entry.getMoodLabel().getName() + "\n");
                    }
                    if (entry.getStressLevel() != null) {
                        writer.write("    Stress: " + entry.getStressLevel().getValue() + "/10\n");
                    }
                    if (entry.getEnergyLevel() != null) {
                        writer.write("    Energy: " + entry.getEnergyLevel().getValue() + "/10\n");
                    }
                    if (entry.getFatigueLevel() != null) {
                        writer.write("    Fatigue: " + entry.getFatigueLevel().getValue() + "/10\n");
                    }
                    writer.write("\n");
                }
            }
            
            outputBoundary.presentExportSuccess(filePath);
            
        } catch (IOException e) {
            outputBoundary.presentExportFailure("Failed to export history: " + e.getMessage());
        }
    }
}