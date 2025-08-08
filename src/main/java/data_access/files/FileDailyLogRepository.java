package data_access.files;

import entity.Angela.DailyLog;
import org.json.JSONObject;
import org.json.JSONTokener;
import use_case.repository.DailyLogRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileDailyLogRepository implements DailyLogRepository {

    private static final String FILE_PATH = "daily_logs.json";

    @Override
    public void save(DailyLog dailyLog) {
        try {
            Path path = Paths.get(FILE_PATH);
            JSONObject root;

            if (Files.exists(path) && Files.size(path) > 0) {
                String content = Files.readString(path, StandardCharsets.UTF_8);
                root = new JSONObject(new JSONTokener(content));
            } else {
                root = new JSONObject();
            }

            JSONObject obj = new JSONObject();
            obj.put("date", dailyLog.getDate().toString());
            obj.put("tasks_scheduled_count",
                    dailyLog.getDailyTaskSummary().getScheduledTasks().size());
            obj.put("tasks_completed_count",
                    dailyLog.getDailyTaskSummary().getCompletedTasks().size());
            obj.put("events_count",
                    dailyLog.getDailyEventLog().getActualEvents().size());
            obj.put("wellness_entries_count",
                    dailyLog.getDailyWellnessLog().getEntries().size());

            root.put(dailyLog.getDate().toString(), obj);

            Files.writeString(
                    path,
                    root.toString(2),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to save DailyLog", e);
        }
    }

    @Override
    public DailyLog findByDate(LocalDate date) {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path) || Files.size(path) == 0) return null;

            String content = Files.readString(path, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(new JSONTokener(content));

            String key = date.toString();
            if (!root.has(key)) return null;

            // Recreate a DailyLog shell with just the date.
            return new DailyLog(date);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DailyLog", e);
        }
    }

    @Override
    public List<DailyLog> loadBetween(LocalDate from, LocalDate to) {
        List<DailyLog> list = new ArrayList<>();
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path) || Files.size(path) == 0) return list;

            String content = Files.readString(path, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(new JSONTokener(content));

            for (String key : root.keySet()) {
                LocalDate date = LocalDate.parse(key);
                if ((date.isEqual(from) || date.isAfter(from)) &&
                        (date.isEqual(to)   || date.isBefore(to))) {
                    list.add(new DailyLog(date)); // shell; no deep rehydration yet
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load daily logs", e);
        }
        return list;
    }
}
