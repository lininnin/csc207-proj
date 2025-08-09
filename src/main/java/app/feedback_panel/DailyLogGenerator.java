package app.feedback_panel;

import data_access.files.FileDailyLogRepository;
import entity.Alex.WellnessLogEntry.Levels;
import entity.Angela.DailyLog;
import entity.Angela.Task.Task;
import entity.Alex.WellnessLogEntry.WellnessLogEntry;
import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabel.Type;
import entity.Alex.Event.Event;
import entity.info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;
import use_case.repository.DailyLogRepository;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyLogGenerator {
    public static List<DailyLog> generateFakeLogs() {
        DailyLogRepository dailyLogRepository = new FileDailyLogRepository();
        List<DailyLog> logs = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        for (int i = 1; i < 4; i++) createTestDailyLog4Tasks(monday, i, logs);
        for (int j = 3; j < 8; j++)createTestDailyLog3Tasks(monday, j, logs);
        logs.forEach(dailyLogRepository::save);
        return logs;
    }

    private static void createTestDailyLog4Tasks(LocalDate monday, int i, List<DailyLog> logs) {
        LocalDate date = monday.minusDays(i);
        DailyLog log = new DailyLog(date);


        // Fake Task 1
        Info taskInfo = new Info.Builder("Task " + (i +1))
                .description("Studying " + (i +1))
                .category("General")
                .build();
        BeginAndDueDates dates = new BeginAndDueDates(date, date.plusDays(1));
        Task task = new Task("template-" + i, taskInfo, dates, true);
        log.addTask(task);

        //Fake task 2
        Info taskInfo2 = new Info.Builder("Task 2" + (i +1))
                .description("Learning " + (i +1))
                .category("Academics")
                .build();
        BeginAndDueDates date2 = new BeginAndDueDates(date, date.plusDays(1));
        Task task2 = new Task("template-" + i, taskInfo2, date2, true);
        log.addTask(task2);
        log.markTaskCompleted(task2, LocalDateTime.now());

        //Fake task 3
        Info taskInfo3 = new Info.Builder("Task 2" + (i +1))
                .description("Exercising " + (i +1))
                .category("Academics")
                .build();
        BeginAndDueDates date3 = new BeginAndDueDates(date, date.plusDays(1));
        Task task3 = new Task("template-" + i, taskInfo3, date3, true);
        log.addTask(task3);
        log.markTaskCompleted(task3, LocalDateTime.now());

        //Fake task 4
        Info taskInfo4 = new Info.Builder("Task 2" + (i +1))
                .description("Miniature building " + (i +1))
                .category("Life")
                .build();
        BeginAndDueDates date4 = new BeginAndDueDates(date, date.plusDays(1));
        Task task4 = new Task("template-" + i, taskInfo4, date4, true);
        log.addTask(task4);
        log.markTaskCompleted(task4, LocalDateTime.now());

        // Fake Event
        Info eventInfo = new Info.Builder("Event " + (i +1))
                .category("Life")
                .build();
        Event event = new Event.Builder(eventInfo)
                .beginAndDueDates(new BeginAndDueDates(date, date.plusDays(1)))
                .oneTime(true)
                .build();
        log.getDailyEventLog().addEntry(event);


        // Fake Wellness Log Entry
        MoodLabel mood = new MoodLabel.Builder(i % 2 == 0 ? "Happy" : "Stressed")
                .type(i % 2 == 0 ? Type.Positive : Type.Negative)
                .build();

        Levels[] lvls = Levels.values();
        Random random = new Random();
        Levels randomWellnessLvl =  lvls[random.nextInt(lvls.length)];
        for (int j=0; j < 5; j++) {
            WellnessLogEntry entry = new WellnessLogEntry.Builder()
                    .time(LocalDateTime.of(date, LocalDateTime.now().toLocalTime()))
                    .moodLabel(mood)
                    .energyLevel(randomWellnessLvl)
                    .stressLevel(randomWellnessLvl)
                    .fatigueLevel(randomWellnessLvl)
                    .userNote("Feeling " + (j % 2 == 0 ? "feeling okay but tired" : "feeling tired but okay"))
                    .build();
            log.getDailyWellnessLog().addEntry(entry);
        }
        logs.add(log);
    }
    private static void createTestDailyLog3Tasks(LocalDate monday, int i, List<DailyLog> logs) {
        LocalDate date = monday.minusDays(i);
        DailyLog log = new DailyLog(date);

        // Fake Task 1
        Info taskInfo = new Info.Builder("Task " + (i +1))
                .description("Studying " + (i +1))
                .category("General")
                .build();
        BeginAndDueDates dates = new BeginAndDueDates(date, date.plusDays(1));
        Task task = new Task("template-" + i, taskInfo, dates, true);
        log.addTask(task);

        //Fake task 2
        Info taskInfo2 = new Info.Builder("Task 2" + (i +1))
                .description("Learning " + (i +1))
                .category("Academics")
                .build();
        BeginAndDueDates date2 = new BeginAndDueDates(date, date.plusDays(1));
        Task task2 = new Task("template-" + i, taskInfo2, date2, true);
        log.addTask(task2);
        log.markTaskCompleted(task2, LocalDateTime.now());

        //Fake task 3
        Info taskInfo3 = new Info.Builder("Task 2" + (i +1))
                .description("Exercising " + (i +1))
                .category("Academics")
                .build();
        BeginAndDueDates date3 = new BeginAndDueDates(date, date.plusDays(1));
        Task task3 = new Task("template-" + i, taskInfo3, date3, true);
        log.addTask(task3);
        log.markTaskCompleted(task3, LocalDateTime.now());

        // Fake Event
        Info eventInfo = new Info.Builder("Event " + (i +1))
                .category("Life")
                .build();
        Event event = new Event.Builder(eventInfo)
                .beginAndDueDates(new BeginAndDueDates(date, date.plusDays(1)))
                .oneTime(true)
                .build();
        log.getDailyEventLog().addEntry(event);


        // Fake Wellness Log Entry
        MoodLabel mood = new MoodLabel.Builder(i % 2 == 0 ? "Happy" : "Stressed")
                .type(i % 2 == 0 ? Type.Positive : Type.Negative)
                .build();

        Levels[] lvls = Levels.values();
        Random random = new Random();
        Levels randomWellnessLvl =  lvls[random.nextInt(lvls.length)];
        for (int j=0; j < 5; j++) {
            WellnessLogEntry entry = new WellnessLogEntry.Builder()
                    .time(LocalDateTime.of(date, LocalDateTime.now().toLocalTime()))
                    .moodLabel(mood)
                    .energyLevel(randomWellnessLvl)
                    .stressLevel(randomWellnessLvl)
                    .fatigueLevel(randomWellnessLvl)
                    .userNote("Feeling " + (j % 2 == 0 ? "feeling okay but tired" : "feeling tired but okay"))
                    .build();
            log.getDailyWellnessLog().addEntry(entry);
        }
        logs.add(log);
    }
    public static void main(String[] args) {
        List<DailyLog> logs = generateFakeLogs();
        for (DailyLog log : logs) {
            System.out.println("Date: " + log.getDate());
            System.out.println("  Tasks: " + log.getDailyTaskSummary().getScheduledTasks().size());
            System.out.println("  Completed tasks: " + log.getDailyTaskSummary().getCompletedTasks().size());
            System.out.println("  Events: " + log.getDailyEventLog().getActualEvents().size());
            System.out.println("  Wellness: " + log.getDailyWellnessLog().getEntries().size());
        }
    }
}
