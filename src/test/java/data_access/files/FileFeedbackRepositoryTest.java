package data_access.files;

import entity.feedback_entry.FeedbackEntryInterf;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileFeedbackRepositoryTest {

    private static final Path CACHE = Paths.get("feedback_cache.json");
    private FileFeedbackRepository repo;

    @BeforeEach
    void setup() throws Exception {
        // start fresh
        Files.deleteIfExists(CACHE);
        repo = new FileFeedbackRepository();
    }

    @AfterEach
    void teardown() throws Exception {
        Files.deleteIfExists(CACHE);
    }

    @Test
    void save_thenLoadByDate_roundTrips() {
        LocalDate date = LocalDate.of(2025, 8, 18);
        FeedbackEntryInterf entry = mockEntry(
                date, "A1", "{\"corr\":1}", "R1"
        );

        repo.save(entry);
        assertTrue(Files.exists(CACHE), "feedback_cache.json should be created");

        FeedbackEntryInterf loaded = repo.loadByDate(date);
        assertNotNull(loaded);
        assertEquals(date, loaded.getDate());
        assertEquals("A1", loaded.getAiAnalysis());
        assertEquals("{\"corr\":1}", loaded.getCorrelationData());
        assertEquals("R1", loaded.getRecommendations());
    }

    @Test
    void loadByDate_returnsNull_whenFileMissingOrKeyMissing() {
        // No file yet
        assertFalse(Files.exists(CACHE));
        assertNull(repo.loadByDate(LocalDate.of(2025, 8, 1)));

        // Save one date
        LocalDate d1 = LocalDate.of(2025, 8, 11);
        repo.save(mockEntry(d1, "A", null, "R"));

        // Ask for different date -> null
        assertNull(repo.loadByDate(LocalDate.of(2025, 8, 12)));
    }

    @Test
    void loadAll_returnsSortedDescendingByDate() {
        // Save three entries in arbitrary order
        repo.save(mockEntry(LocalDate.of(2025, 8, 10), "A10", null, "R10"));
        repo.save(mockEntry(LocalDate.of(2025, 8, 12), "A12", "{\"c\":12}", "R12"));
        repo.save(mockEntry(LocalDate.of(2025, 8, 11), "A11", "", "R11"));

        List<FeedbackEntryInterf> all = repo.loadAll();
        assertEquals(3, all.size());

        // Expect 12, 11, 10
        assertEquals(LocalDate.of(2025, 8, 12), all.get(0).getDate());
        assertEquals(LocalDate.of(2025, 8, 11), all.get(1).getDate());
        assertEquals(LocalDate.of(2025, 8, 10), all.get(2).getDate());
    }

    @Test
    void save_mergesIntoExistingFile_withoutDroppingPriorEntries() {
        LocalDate d1 = LocalDate.of(2025, 8, 18);
        LocalDate d2 = LocalDate.of(2025, 8, 19);

        repo.save(mockEntry(d1, "A1", null, "R1"));
        repo.save(mockEntry(d2, "A2", "{\"x\":2}", "R2"));

        // Both dates retrievable
        assertNotNull(repo.loadByDate(d1));
        assertNotNull(repo.loadByDate(d2));

        // And loadAll has both
        List<FeedbackEntryInterf> all = repo.loadAll();
        assertEquals(2, all.size());
    }

    /* ---------------- helpers ---------------- */

    private static FeedbackEntryInterf mockEntry(LocalDate date, String analysis, String correlation, String rec) {
        FeedbackEntryInterf e = Mockito.mock(FeedbackEntryInterf.class);
        when(e.getDate()).thenReturn(date);
        when(e.getAiAnalysis()).thenReturn(analysis);
        when(e.getCorrelationData()).thenReturn(correlation);
        when(e.getRecommendations()).thenReturn(rec);
        return e;
    }
}
