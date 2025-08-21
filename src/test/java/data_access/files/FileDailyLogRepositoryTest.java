package data_access.files;

import entity.Angela.DailyLog;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDailyLogRepositoryTest {

    private static final Path FILE = Paths.get("daily_logs.json");
    private FileDailyLogRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // start from a clean slate
        Files.deleteIfExists(FILE);
        repo = new FileDailyLogRepository();
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(FILE);
    }

    @Test
    void save_createsOrMergesJson_andPersistsZeroCountsForEmptyLog() throws Exception {
        LocalDate d1 = LocalDate.of(2025, 8, 20);
        repo.save(new DailyLog(d1)); // empty shell -> all counts 0 by construction

        assertTrue(Files.exists(FILE), "daily_logs.json should be created");

        // Read and verify JSON structure
        String content = Files.readString(FILE, StandardCharsets.UTF_8);
        JSONObject root = new JSONObject(new JSONTokener(content));
        assertTrue(root.has(d1.toString()), "JSON should contain the date key");

        JSONObject obj = root.getJSONObject(d1.toString());
        assertEquals(d1.toString(), obj.getString("date"));
        assertEquals(0, obj.getInt("tasks_scheduled_count"));
        assertEquals(0, obj.getInt("tasks_completed_count"));
        assertEquals(0, obj.getInt("events_count"));
        assertEquals(0, obj.getInt("wellness_entries_count"));

        // Save another date -> should MERGE (not overwrite)
        LocalDate d2 = LocalDate.of(2025, 8, 21);
        repo.save(new DailyLog(d2));

        String content2 = Files.readString(FILE, StandardCharsets.UTF_8);
        JSONObject root2 = new JSONObject(new JSONTokener(content2));
        assertTrue(root2.has(d1.toString()));
        assertTrue(root2.has(d2.toString()));
    }

    @Test
    void findByDate_roundTripsFromSavedDate_andReturnsNullWhenMissing() {
        LocalDate d1 = LocalDate.of(2025, 8, 20);
        LocalDate d2 = LocalDate.of(2025, 8, 21);

        // file absent -> null
        assertNull(repo.findByDate(d1));

        // after saving d1 -> should be found
        repo.save(new DailyLog(d1));
        DailyLog loaded = repo.findByDate(d1);
        assertNotNull(loaded, "Saved date should be retrievable");
        assertEquals(d1, loaded.getDate());
        // missing date -> still null
        assertNull(repo.findByDate(d2));
    }

    @Test
    void loadBetween_inclusiveRangeFiltersByKeys() {
        LocalDate d1 = LocalDate.of(2025, 8, 18); // Mon
        LocalDate d2 = LocalDate.of(2025, 8, 19);
        LocalDate d3 = LocalDate.of(2025, 8, 20);
        LocalDate d4 = LocalDate.of(2025, 8, 21);

        // Persist four logs
        repo.save(new DailyLog(d1));
        repo.save(new DailyLog(d2));
        repo.save(new DailyLog(d3));
        repo.save(new DailyLog(d4));

        // Query [d2..d3] inclusive
        List<DailyLog> between = repo.loadBetween(d2, d3);
        assertEquals(2, between.size(), "Should return exactly d2 and d3");
        assertTrue(between.stream().anyMatch(dl -> dl.getDate().equals(d2)));
        assertTrue(between.stream().anyMatch(dl -> dl.getDate().equals(d3)));

        // Single-day range [d1..d1]
        List<DailyLog> single = repo.loadBetween(d1, d1);
        assertEquals(1, single.size());
        assertEquals(d1, single.get(0).getDate());
    }

    @Test
    void loadBetween_returnsEmpty_whenFileMissingOrEmpty() throws Exception {
        // Missing file
        assertFalse(Files.exists(FILE));
        assertTrue(repo.loadBetween(LocalDate.now().minusDays(1), LocalDate.now()).isEmpty());

        // Empty file (size 0)
        Files.write(FILE, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        assertTrue(repo.loadBetween(LocalDate.now().minusDays(1), LocalDate.now()).isEmpty());
    }

    @Test
    void findByDate_returnsNull_whenFileEmptyOrKeyAbsent() throws Exception {
        // Empty file
        Files.write(FILE, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        assertNull(repo.findByDate(LocalDate.of(2025, 8, 20)));

        // Write a different key
        JSONObject root = new JSONObject();
        root.put("2025-08-19", new JSONObject().put("date", "2025-08-19"));
        Files.writeString(FILE, root.toString(2), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

        assertNull(repo.findByDate(LocalDate.of(2025, 8, 20)), "Date not present should return null");
    }
}
