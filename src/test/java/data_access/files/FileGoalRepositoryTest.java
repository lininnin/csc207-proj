// File: src/test/java/data_access/files/FileGoalRepositoryTest.java
package data_access.files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
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
     * Creates a serializable Goal mock with:
     * - name via getGoalInfo().getInfo().getName()
     * - frequency as int
     * - dates via an explicit BeginAndDueDates mock (so LocalDate is never mocked)
     * - stateful setters for progress/completed
     */
    private Goal goalMock(
            String name,
            int frequency,
            LocalDate begin,
            LocalDate due,
            boolean initiallyCompleted
    ) {
        // Serializable goal mock (deep stubs ok for name chain only)
        MockSettings gSettings = withSettings()
                .defaultAnswer(Answers.RETURNS_DEEP_STUBS)
                .serializable()
                .lenient();
        Goal g = mock(Goal.class, gSettings);

        // Backing state
        AtomicInteger progress = new AtomicInteger(0);
        AtomicBoolean completed = new AtomicBoolean(initiallyCompleted);

        // Name chain: goal.getGoalInfo().getInfo().getName()
        when(g.getGoalInfo().getInfo().getName()).thenReturn(name);

        // Frequency as int
        when(g.getFrequency()).thenReturn(frequency);

        // Completed flag
        when(g.isCompleted()).thenAnswer(inv -> completed.get());

        // ---- IMPORTANT: Avoid deep-stubbing into final JDK types like LocalDate.
        // Mock the intermediate dates object explicitly and return real LocalDate values.
        BeginAndDueDates dates = mock(BeginAndDueDates.class, withSettings().serializable().lenient());
        when(dates.getBeginDate()).thenReturn(begin);
        when(dates.getDueDate()).thenReturn(due);
        when(g.getBeginAndDueDates()).thenReturn(dates);
        // ----

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

    @Test
    void emptyRepository_hasNoGoals_orTodayGoal() {
        FileGoalRepository repo = newRepo();
        assertTrue(repo.getAllGoals().isEmpty());
        assertTrue(repo.getCurrentGoals().isEmpty());
        assertTrue(repo.getTodayGoals().isEmpty());
        assertNull(repo.getTodayGoal());
    }

    @Test
    void save_and_lookup_byName_and_exists() {
        FileGoalRepository repo = newRepo();

        Goal g = goalMock("Read", 5, LocalDate.now().minusDays(1), LocalDate.now().plusDays(7), false);
        repo.save(g);

        assertTrue(repo.existsByName("Read"));
        Optional<Goal> found = repo.findByName("Read");
        assertTrue(found.isPresent());
        assertEquals("Read", found.get().getGoalInfo().getInfo().getName());
        assertEquals(1, repo.getAllGoals().size());
    }

    @Test
    void currentGoals_add_persist_and_reload() {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("Exercise", 3, LocalDate.now().minusDays(2), LocalDate.now().plusDays(5), false);
        repo.save(g);
        repo.addToCurrentGoals(g);

        List<Goal> current1 = repo.getCurrentGoals();
        assertEquals(1, current1.size());
        assertEquals("Exercise", current1.get(0).getGoalInfo().getInfo().getName());

        // reload to confirm persistence via files
        FileGoalRepository repo2 = newRepo();
        List<Goal> current2 = repo2.getCurrentGoals();
        assertEquals(1, current2.size());
        assertEquals("Exercise", current2.get(0).getGoalInfo().getInfo().getName());
    }

    @Test
    void todayGoals_add_updateProgress_marksCompleted_and_persistsList() {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("Write", 2, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), false);
        repo.save(g);

        repo.addGoalToToday("Write");
        List<Goal> today = repo.getTodayGoals();
        assertEquals(1, today.size());
        assertEquals("Write", today.get(0).getGoalInfo().getInfo().getName());
        assertEquals("Write", repo.getTodayGoal().getGoalInfo().getInfo().getName());

        repo.updateTodayGoalProgress("Write", 2);
        verify(g).setCurrentProgress(2);
        verify(g).setCompleted(true);

        FileGoalRepository repo2 = newRepo();
        List<Goal> today2 = repo2.getTodayGoals();
        assertEquals(1, today2.size());
        assertEquals("Write", today2.get(0).getGoalInfo().getInfo().getName());
    }

    @Test
    void removeGoalFromToday_persistsAcrossReload() {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("Focus", 1, LocalDate.now(), LocalDate.now().plusDays(3), false);
        repo.save(g);
        repo.addGoalToToday("Focus");
        assertEquals(1, repo.getTodayGoals().size());

        repo.removeGoalFromToday("Focus");
        assertTrue(repo.getTodayGoals().isEmpty());

        FileGoalRepository repo2 = newRepo();
        assertTrue(repo2.getTodayGoals().isEmpty());
    }

    @Test
    void findAvailableGoals_filtersByDateWindow_andCompletion() {
        FileGoalRepository repo = newRepo();
        LocalDate today = LocalDate.now();

        Goal activeNow  = goalMock("ActiveNow", 1, today.minusDays(1), today.plusDays(1), false);
        Goal notStarted = goalMock("Future",    1, today.plusDays(1),  today.plusDays(10), false);
        Goal expired    = goalMock("Past",      1, today.minusDays(10), today.minusDays(1), false);
        Goal completed  = goalMock("Done",      1, today.minusDays(1),  today.plusDays(1), true);

        repo.save(activeNow);
        repo.save(notStarted);
        repo.save(expired);
        repo.save(completed);

        List<Goal> available = repo.findAvailableGoals();
        assertEquals(1, available.size());
        assertEquals("ActiveNow", available.get(0).getGoalInfo().getInfo().getName());
    }

    @Test
    void findByTargetAmountRange_filtersByFrequency() {
        FileGoalRepository repo = newRepo();
        Goal g1 = goalMock("G1", 1,  LocalDate.now(), LocalDate.now().plusDays(1), false);
        Goal g2 = goalMock("G2", 5,  LocalDate.now(), LocalDate.now().plusDays(1), false);
        Goal g3 = goalMock("G3", 10, LocalDate.now(), LocalDate.now().plusDays(1), false);

        repo.save(g1);
        repo.save(g2);
        repo.save(g3);

        List<Goal> range = repo.findByTargetAmountRange(2.0, 7.0);
        assertEquals(1, range.size());
        assertEquals("G2", range.get(0).getGoalInfo().getInfo().getName());
    }

    @Test
    void deleteByName_removesEverywhere_andPersists() {
        FileGoalRepository repo = newRepo();
        Goal g = goalMock("ZGoal", 3, LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), false);
        repo.save(g);
        repo.addToCurrentGoals(g);
        repo.addGoalToToday("ZGoal");

        assertTrue(repo.existsByName("ZGoal"));
        assertFalse(repo.getCurrentGoals().isEmpty());
        assertFalse(repo.getTodayGoals().isEmpty());

        repo.deleteByName("ZGoal");

        assertFalse(repo.existsByName("ZGoal"));
        assertTrue(repo.getCurrentGoals().isEmpty());
        assertTrue(repo.getTodayGoals().isEmpty());

        FileGoalRepository repo2 = newRepo();
        assertFalse(repo2.existsByName("ZGoal"));
        assertTrue(repo2.getCurrentGoals().isEmpty());
        assertTrue(repo2.getTodayGoals().isEmpty());
    }
}
