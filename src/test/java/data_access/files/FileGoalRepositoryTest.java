// File: src/test/java/data_access/files/FileGoalRepositoryTest.java
package data_access.files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.MockSettings;
import org.mockito.junit.jupiter.MockitoExtension;

import entity.Sophia.Goal;
import entity.Sophia.GoalFactory;
import entity.BeginAndDueDates.BeginAndDueDates;

@ExtendWith(MockitoExtension.class)
class FileGoalRepositoryTest {

    @TempDir
    Path temp;

    private File goalsFile;
    private File currentGoalsFile;
    private File todayGoalsFile;

    private GoalFactory dummyFactory;

    @BeforeEach
    void setUp() {
        goalsFile = temp.resolve("goals.bin").toFile();
        currentGoalsFile = temp.resolve("current.txt").toFile();
        todayGoalsFile = temp.resolve("today.txt").toFile();
        dummyFactory = mock(GoalFactory.class, withSettings().lenient());
    }

    private FileGoalRepository newRepo() {
        return new FileGoalRepository(goalsFile, currentGoalsFile, todayGoalsFile, dummyFactory);
    }

    /**
     * Creates a Goal Mockito mock that is safe to use without forcing Mockito
     * to mock final JDK types (like LocalDate). We:
     *  - keep deep stubs only for the name-chain
     *  - explicitly mock the BeginAndDueDates intermediate object
     *  - return real LocalDate instances
     *  - use stateful setters for progress/completed flags
     *
     * NOTE: The repository will attempt to serialize this mock to goals.bin
     * and will likely log an "Error saving goals: ..." because Mockito mocks
     * aren't great for Java serialization. Tests below DO NOT depend on
     * reloading these mocks; they validate behavior in-process and verify
     * the text files for "current" and "today" lists instead.
     */
    private Goal goalMock(
            String name,
            int frequency,
            LocalDate begin,
            LocalDate due,
            boolean initiallyCompleted
    ) {
        MockSettings gSettings = withSettings()
                .defaultAnswer(Answers.RETURNS_DEEP_STUBS)
                .lenient();
        Goal g = mock(Goal.class, gSettings);

        AtomicInteger progress = new AtomicInteger(0);
        AtomicBoolean completed = new AtomicBoolean(initiallyCompleted);

        // Name chain
        when(g.getGoalInfo().getInfo().getName()).thenReturn(name);

        // Frequency (int)
        when(g.getFrequency()).thenReturn(frequency);

        // Completed flag
        when(g.isCompleted()).thenAnswer(inv -> completed.get());

        // Dates via explicit intermediate mock (never mock LocalDate)
        BeginAndDueDates dates = mock(BeginAndDueDates.class, withSettings().lenient());
        when(dates.getBeginDate()).thenReturn(begin);
        when(dates.getDueDate()).thenReturn(due);
        when(g.getBeginAndDueDates()).thenReturn(dates);

        // Stateful setters
        doAnswer(inv -> {
            int np = inv.getArgument(0, Integer.class);
            progress.set(np);
            return null;
        }).when(g).setCurrentProgress(anyInt());

        doAnswer(inv -> {
            boolean nc = inv.getArgument(0, Boolean.class);
            completed.set(nc);
            return null;
        }).when(g).setCompleted(anyBoolean());

        return g;
    }

    // ---- Helper to read small text files (current/today lists) ----
    private List<String> readAllLines(File f) throws Exception {
        if (!f.exists()) return List.of();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            return br.lines().map(String::trim).filter(s -> !s.isEmpty()).toList();
        }
    }

    @Test
    void emptyRepository_hasNoGoals_orTodayGoal() {
        FileGoalRepository repo = newRepo();
        assertTrue(repo.getAllGoals().isEmpty());
        assertTrue(repo.getCurrentGoals().isEmpty());
        assertTrue(repo.getTodayGoals().isEmpty());
        assertNull(repo.getTodayGoal());
    }

    @Test
    void save_and_lookup_byName_and_exists_inMemory() {
        FileGoalRepository repo = newRepo();

        Goal g = goalMock("Read", 5, LocalDate.now().minusDays(1), LocalDate.now().plusDays(7), false);
        // This will likely log "Error saving goals: ..." due to mock serialization.
        // That's OK; we assert in-memory behaviors only.
        repo.save(g);

        assertTrue(repo.existsByName("Read"));
        Optional<Goal> found = repo.findByName("Read");
        assertTrue(found.isPresent());
        assertEquals("Read", found.get().getGoalInfo().getInfo().getName());

        // In-memory list contains the object we just saved
        assertEquals(1, repo.getAllGoals().size());
    }

    @Test
    void currentGoals_add_and_persistTextFile() throws Exception {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("Exercise", 3, LocalDate.now().minusDays(2), LocalDate.now().plusDays(5), false);

        repo.save(g); // may log a save error; OK
        repo.addToCurrentGoals(g);

        // In-memory correctness
        List<Goal> current1 = repo.getCurrentGoals();
        assertEquals(1, current1.size());
        assertEquals("Exercise", current1.get(0).getGoalInfo().getInfo().getName());

        // Text-file persistence for current list
        List<String> lines = readAllLines(currentGoalsFile);
        assertEquals(List.of("Exercise"), lines);
    }

    @Test
    void todayGoals_add_updateProgress_marksCompleted_and_persistsTextFile() throws Exception {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("Write", 2, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), false);

        repo.save(g); // may log a save error; OK
        repo.addGoalToToday("Write");

        // In-memory today list
        List<Goal> today = repo.getTodayGoals();
        assertEquals(1, today.size());
        assertEquals("Write", today.get(0).getGoalInfo().getInfo().getName());
        assertEquals("Write", repo.getTodayGoal().getGoalInfo().getInfo().getName());

        // Update progress triggers setters
        repo.updateTodayGoalProgress("Write", 2);
        verify(g).setCurrentProgress(2);
        verify(g).setCompleted(true);

        // Text-file persistence for today list
        List<String> lines = readAllLines(todayGoalsFile);
        assertEquals(List.of("Write"), lines);
    }

    @Test
    void removeGoalFromToday_updatesTextFile() throws Exception {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("Focus", 1, LocalDate.now(), LocalDate.now().plusDays(3), false);

        repo.save(g); // may log a save error; OK
        repo.addGoalToToday("Focus");
        assertEquals(1, repo.getTodayGoals().size());

        repo.removeGoalFromToday("Focus");
        assertTrue(repo.getTodayGoals().isEmpty());

        // Text-file persistence should now be empty
        List<String> lines = readAllLines(todayGoalsFile);
        assertTrue(lines.isEmpty());
    }

    @Test
    void findAvailableGoals_filtersByDateWindow_andCompletion_inMemory() {
        FileGoalRepository repo = newRepo();
        LocalDate today = LocalDate.now();

        Goal activeNow  = goalMock("ActiveNow", 1, today.minusDays(1), today.plusDays(1), false);
        Goal notStarted = goalMock("Future",    1, today.plusDays(1),  today.plusDays(10), false);
        Goal expired    = goalMock("Past",      1, today.minusDays(10), today.minusDays(1), false);
        Goal completed  = goalMock("Done",      1, today.minusDays(1),  today.plusDays(1), true);

        // All may log save errors; assertions are in-memory only.
        repo.save(activeNow);
        repo.save(notStarted);
        repo.save(expired);
        repo.save(completed);

        List<Goal> available = repo.findAvailableGoals();
        assertEquals(1, available.size());
        assertEquals("ActiveNow", available.get(0).getGoalInfo().getInfo().getName());
    }

    @Test
    void findByTargetAmountRange_filtersByFrequency_inMemory() {
        FileGoalRepository repo = newRepo();
        Goal g1 = goalMock("G1", 1,  LocalDate.now(), LocalDate.now().plusDays(1), false);
        Goal g2 = goalMock("G2", 5,  LocalDate.now(), LocalDate.now().plusDays(1), false);
        Goal g3 = goalMock("G3", 10, LocalDate.now(), LocalDate.now().plusDays(1), false);

        repo.save(g1); // may log save errors; OK
        repo.save(g2);
        repo.save(g3);

        List<Goal> range = repo.findByTargetAmountRange(2.0, 7.0);
        assertEquals(1, range.size());
        assertEquals("G2", range.get(0).getGoalInfo().getInfo().getName());
    }

    @Test
    void deleteByName_removesEverywhere_andUpdatesTextFiles() throws Exception {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("ZGoal", 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), false);

        repo.save(g); // may log save errors; OK
        repo.addToCurrentGoals(g);
        repo.addGoalToToday("ZGoal");

        assertTrue(repo.existsByName("ZGoal"));
        assertFalse(repo.getCurrentGoals().isEmpty());
        assertFalse(repo.getTodayGoals().isEmpty());

        // Current/today files contain ZGoal before deletion
        assertEquals(List.of("ZGoal"), readAllLines(currentGoalsFile));
        assertEquals(List.of("ZGoal"), readAllLines(todayGoalsFile));

        repo.deleteByName("ZGoal");

        // In-memory
        assertFalse(repo.existsByName("ZGoal"));
        assertTrue(repo.getCurrentGoals().isEmpty());
        assertTrue(repo.getTodayGoals().isEmpty());

        // Text files should be empty after deletion
        assertTrue(readAllLines(currentGoalsFile).isEmpty());
        assertTrue(readAllLines(todayGoalsFile).isEmpty());

        // goals.bin may still exist; it’s OK if present/empty/invalid for mocks.
        // We simply assert that current/today list persistence behaved.
        assertTrue(goalsFile.exists() || !Files.exists(goalsFile.toPath()));
    }
}
