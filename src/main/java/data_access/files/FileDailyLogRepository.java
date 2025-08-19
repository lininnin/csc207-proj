package data_access.files;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import entity.Angela.DailyLog;
import use_case.repository.DailyLogRepository;

public class FileDailyLogRepository implements DailyLogRepository {

    private static final String FILE_PATH = "daily_logs.json";

    @Override
    public void save(DailyLog dailyLog) {
        try {
            final Path path = Paths.get(FILE_PATH);
            final JSONObject root;

            if (Files.exists(path) && Files.size(path) > 0) {
                final String content = Files.readString(path, StandardCharsets.UTF_8);
                root = new JSONObject(new JSONTokener(content));
            }
            else {
                root = new JSONObject();
            }

            final JSONObject obj = getObj(dailyLog);

            root.put(dailyLog.getDate().toString(), obj);

            Files.writeString(
                    path,
                    root.toString(2),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to save DailyLog", exception);
        }
    }

    @NotNull
    private static JSONObject getObj(DailyLog dailyLog) {
        final JSONObject obj = new JSONObject();
        obj.put("date", dailyLog.getDate().toString());
        obj.put("tasks_scheduled_count",
                dailyLog.getDailyTaskSummary().getScheduledTasks().size());
        obj.put("tasks_completed_count",
                dailyLog.getDailyTaskSummary().getCompletedTasks().size());
        obj.put("events_count",
                dailyLog.getDailyEventLog().getActualEvents().size());
        obj.put("wellness_entries_count",
                dailyLog.getDailyWellnessLog().getEntries().size());
        return obj;
    }

    @Override
    public DailyLog findByDate(LocalDate date) {
        try {
            final Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path) || Files.size(path) == 0) {
                return null;
            }

            final String content = Files.readString(path, StandardCharsets.UTF_8);
            final JSONObject root = new JSONObject(new JSONTokener(content));

            final String key = date.toString();
            if (!root.has(key)) {
                return null;
            }

            // Recreate a DailyLog shell with just the date.
            return new DailyLog(date);
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to load DailyLog", exception);
        }
    }

    @Override
    public List<DailyLog> loadBetween(LocalDate from, LocalDate to) {
        final List<DailyLog> list = new ArrayList<>();
        try {
            final Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path) || Files.size(path) == 0) {
                return list;
            }

            final String content = Files.readString(path, StandardCharsets.UTF_8);
            final JSONObject root = new JSONObject(new JSONTokener(content));

            for (String key : root.keySet()) {
                final LocalDate date = LocalDate.parse(key);
                if ((date.isEqual(from) || date.isAfter(from))
                        && (date.isEqual(to) || date.isBefore(to))) {
                    list.add(new DailyLog(date));
                    // shell; no deep rehydration yet
                }
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to load daily logs", exception);
        }
        return list;
    }
}
