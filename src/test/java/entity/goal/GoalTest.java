package entity.Sophia;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalTest {

    /* ---------- helpers ---------- */

    private static Goal makeGoal(GoalInfo info, BeginAndDueDates dates, Goal.TimePeriod tp, int freq) {
        return new Goal(info, dates, tp, freq);
    }

    private static GoalInfo mockGoalInfo(String name, String targetTaskName) {
        GoalInfo gi = mock(GoalInfo.class);
        Info info = mock(Info.class);
        when(info.getName()).thenReturn(name);
        Info target = mock(Info.class);
        when(target.getName()).thenReturn(targetTaskName);
        when(gi.getInfo()).thenReturn(info);
        when(gi.getTargetTaskInfo()).thenReturn(target);
        return gi;
    }

    private static BeginAndDueDates mockDates(LocalDate begin, LocalDate due) {
        BeginAndDueDates bd = mock(BeginAndDueDates.class);
        when(bd.getBeginDate()).thenReturn(begin);
        when(bd.getDueDate()).thenReturn(due);
        return bd;
    }

    /* ---------- constructor validation ---------- */

    @Test
    void ctor_throws_whenGoalInfoNull() {
        BeginAndDueDates dates = mockDates(LocalDate.now(), LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(null, dates, Goal.TimePeriod.WEEK, 1),
                "GoalInfo cannot be null.");
    }

    @Test
    void ctor_throws_whenDatesNull() {
        GoalInfo gi = mockGoalInfo("G", "T");
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(gi, null, Goal.TimePeriod.WEEK, 1),
                "BeginAndDueDates cannot be null.");
    }

    @Test
    void ctor_throws_whenTimePeriodNull() {
        GoalInfo gi = mockGoalInfo("G", "T");
        BeginAndDueDates dates = mockDates(LocalDate.now(), LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(gi, dates, null, 1),
                "TimePeriod cannot be null.");
    }

    @Test
    void ctor_throws_whenFrequencyNegative() {
        GoalInfo gi = mockGoalInfo("G", "T");
        BeginAndDueDates dates = mockDates(LocalDate.now(), LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class,
                () -> new Goal(gi, dates, Goal.TimePeriod.WEEK, -1),
                "Frequency cannot be negative.");
    }

    /* ---------- simple getters ---------- */

    @Test
    void getters_returnValuesFromConstructor() {
        LocalDate begin = LocalDate.of(2025, 8, 20);
        LocalDate due   = LocalDate.of(2025, 8, 27);
        GoalInfo gi = mockGoalInfo("MyGoal", "TargetTask");
        BeginAndDueDates dates = mockDates(begin, due);

        Goal g = makeGoal(gi, dates, Goal.TimePeriod.WEEK, 3);

        assertSame(gi, g.getGoalInfo());
        assertSame(dates, g.getBeginAndDueDates());
        assertEquals(Goal.TimePeriod.WEEK, g.getTimePeriod());
        assertEquals(3, g.getFrequency());
        assertEquals(0, g.getCurrentProgress());
        assertFalse(g.isCompleted());
        assertNull(g.getCompletedDateTime());
        assertSame(gi.getInfo(), g.getInfo());
        assertSame(gi.getTargetTaskInfo(), g.getTargetTaskInfo());
    }

    /* ---------- recordCompletion / progress ---------- */

    @Test
    void recordCompletion_incrementsUntilFrequency_thenMarksCompletedAndSetsTime() {
        GoalInfo gi = mockGoalInfo("G", "T");
        BeginAndDueDates dates = mockDates(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        Goal g = makeGoal(gi, dates, Goal.TimePeriod.WEEK, 2);

        assertFalse(g.isCompleted());
        assertEquals(0, g.getCurrentProgress());

        g.recordCompletion(); // 1/2
        assertEquals(1, g.getCurrentProgress());
        assertFalse(g.isCompleted());
        assertNull(g.getCompletedDateTime());

        g.recordCompletion(); // 2/2 -> complete
        assertTrue(g.isCompleted());
        assertEquals(2, g.getCurrentProgress());
        assertNotNull(g.getCompletedDateTime());

        LocalDateTime doneAt = g.getCompletedDateTime();
        // further calls should not change progress or completedDateTime
        g.recordCompletion();
        assertEquals(2, g.getCurrentProgress());
        assertSame(doneAt, g.getCompletedDateTime());
    }

    @Test
    void minusCurrentProgress_neverBelowZero() {
        Goal g = makeGoal(mockGoalInfo("G", "T"),
                mockDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Goal.TimePeriod.WEEK, 5);

        assertEquals(0, g.getCurrentProgress());
        g.minusCurrentProgress();
        assertEquals(0, g.getCurrentProgress());

        // increase once, then decrement
        g.recordCompletion(); // progress 1
        assertEquals(1, g.getCurrentProgress());
        g.minusCurrentProgress();
        assertEquals(0, g.getCurrentProgress());
    }

    /* ---------- isAvailable ---------- */

    @Test
    void isAvailable_true_whenTodayWithinRange_andNotCompleted() {
        LocalDate today = LocalDate.now();
        Goal g = makeGoal(
                mockGoalInfo("G", "T"),
                mockDates(today.minusDays(1), today.plusDays(1)),
                Goal.TimePeriod.WEEK, 1
        );
        assertTrue(g.isAvailable(), "Should be available within date range and not completed");
    }

    @Test
    void isAvailable_false_whenBeforeBegin() {
        LocalDate today = LocalDate.now();
        Goal g = makeGoal(
                mockGoalInfo("G", "T"),
                mockDates(today.plusDays(1), today.plusDays(7)),
                Goal.TimePeriod.WEEK, 1
        );
        assertFalse(g.isAvailable());
    }

    @Test
    void isAvailable_false_whenAfterDue() {
        LocalDate today = LocalDate.now();
        Goal g = makeGoal(
                mockGoalInfo("G", "T"),
                mockDates(today.minusDays(7), today.minusDays(1)),
                Goal.TimePeriod.WEEK, 1
        );
        assertFalse(g.isAvailable());
    }

    @Test
    void isAvailable_false_whenCompletedEvenIfInRange() {
        LocalDate today = LocalDate.now();
        Goal g = makeGoal(
                mockGoalInfo("G", "T"),
                mockDates(today.minusDays(1), today.plusDays(1)),
                Goal.TimePeriod.WEEK, 1
        );
        g.setCompleted(true);
        assertFalse(g.isAvailable());
    }

    /* ---------- setters & validation ---------- */

    @Test
    void setFrequency_updates_whenNonNegative_elseThrows() {
        Goal g = makeGoal(mockGoalInfo("G", "T"),
                mockDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Goal.TimePeriod.MONTH, 2);

        g.setFrequency(5);
        assertEquals(5, g.getFrequency());

        assertThrows(IllegalArgumentException.class, () -> g.setFrequency(-1));
    }

    @Test
    void setCurrentProgress_updates_whenNonNegative_elseThrows() {
        Goal g = makeGoal(mockGoalInfo("G", "T"),
                mockDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Goal.TimePeriod.MONTH, 3);

        g.setCurrentProgress(2);
        assertEquals(2, g.getCurrentProgress());

        assertThrows(IllegalArgumentException.class, () -> g.setCurrentProgress(-1));
    }

    @Test
    void setCompleted_setsFlag_andManagesCompletedDateTime() {
        Goal g = makeGoal(mockGoalInfo("G", "T"),
                mockDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Goal.TimePeriod.WEEK, 1);

        // mark complete -> sets timestamp
        g.setCompleted(true);
        assertTrue(g.isCompleted());
        assertNotNull(g.getCompletedDateTime());
        LocalDateTime first = g.getCompletedDateTime();

        // mark complete again -> keeps existing timestamp
        g.setCompleted(true);
        assertTrue(g.isCompleted());
        assertSame(first, g.getCompletedDateTime());

        // mark not complete -> clears timestamp
        g.setCompleted(false);
        assertFalse(g.isCompleted());
        assertNull(g.getCompletedDateTime());
    }

    @Test
    void setCompletedDateTime_setsArbitraryTimestamp() {
        Goal g = makeGoal(mockGoalInfo("G", "T"),
                mockDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Goal.TimePeriod.MONTH, 2);

        LocalDateTime ts = LocalDateTime.of(2025, 8, 21, 12, 34, 56);
        g.setCompletedDateTime(ts);
        assertEquals(ts, g.getCompletedDateTime());
    }

    /* ---------- string formatting ---------- */

    @Test
    void toString_includesName_period_progress_andDates() {
        LocalDate begin = LocalDate.of(2025, 8, 20);
        LocalDate due   = LocalDate.of(2025, 8, 31);

        GoalInfo gi = mockGoalInfo("Read Books", "Read Chapter");
        BeginAndDueDates dates = mockDates(begin, due);

        Goal g = makeGoal(gi, dates, Goal.TimePeriod.MONTH, 10);
        g.setCurrentProgress(3);

        String s = g.toString();
        // Expected format:
        // "%s - %s (Progress: %d/%d, %s to %s)"
        String expected = String.format("Read Books - %s (Progress: 3/10, %s to %s)",
                Goal.TimePeriod.MONTH.toString(), begin, due);
        assertEquals(expected, s);
    }

    @Test
    void getSimpleProgress_formatsCurrentOverFrequency() {
        Goal g = makeGoal(mockGoalInfo("G", "T"),
                mockDates(LocalDate.now(), LocalDate.now().plusDays(1)),
                Goal.TimePeriod.WEEK, 200);
        g.setCurrentProgress(2);
        assertEquals("2/200", g.getSimpleProgress());
    }
}
