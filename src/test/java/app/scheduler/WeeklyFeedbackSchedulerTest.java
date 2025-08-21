package app.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import use_case.generate_feedback.GenerateFeedbackInputBoundary;

import java.lang.reflect.Field;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeeklyFeedbackSchedulerTest {

    @Mock
    GenerateFeedbackInputBoundary useCase;

    @Mock
    ScheduledExecutorService executor;

    /**
     * Replace the private final executor field with our mock via reflection.
     */
    private static void injectExecutor(WeeklyFeedbackScheduler scheduler, ScheduledExecutorService mockExec) {
        try {
            Field f = WeeklyFeedbackScheduler.class.getDeclaredField("executor");
            f.setAccessible(true);
            f.set(scheduler, mockExec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock executor", e);
        }
    }

    @Test
    void start_schedulesAtFixedRateWithWeeklyPeriod_andRunnableInvokesUseCase() {
        WeeklyFeedbackScheduler scheduler = new WeeklyFeedbackScheduler(useCase);
        injectExecutor(scheduler, executor);

        // Capture timing just before calling start()
        LocalDateTime t0 = LocalDateTime.now();

        ArgumentCaptor<Runnable> runnableCap = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Long> initialDelayCap = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Long> periodCap = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> unitCap = ArgumentCaptor.forClass(TimeUnit.class);

        // Act
        scheduler.start();

        // Assert scheduleAtFixedRate was called once
        verify(executor, times(1)).scheduleAtFixedRate(
                runnableCap.capture(),
                initialDelayCap.capture(),
                periodCap.capture(),
                unitCap.capture()
        );

        // 1) Verify period = 7 days, TimeUnit.MILLISECONDS
        assertEquals(TimeUnit.MILLISECONDS, unitCap.getValue(), "TimeUnit must be MILLISECONDS");
        assertEquals(TimeUnit.DAYS.toMillis(7), periodCap.getValue(), "Period must be 7 days in ms");

        // 2) Verify initial delay equals time until next-or-same Monday 00:00 (within [t1..t0] window)
        // Compute a conservative range using t0 (before start) and t1 (after start)
        LocalDateTime t1 = LocalDateTime.now();

        long expectedFromT0 = millisUntilNextMondayMidnight(t0);
        long expectedFromT1 = millisUntilNextMondayMidnight(t1);
        long actualDelay = initialDelayCap.getValue();

        // The delay computed between t0 and t1 should fall within that shrinking window.
        // Because time passed between t0 and t1, expectedFromT1 <= actualDelay <= expectedFromT0.
        assertTrue(actualDelay <= expectedFromT0,
                "Initial delay should be <= delay computed at t0");
        assertTrue(actualDelay >= expectedFromT1,
                "Initial delay should be >= delay computed at t1");

        // 3) Invoking the scheduled runnable should call useCase.execute()
        Runnable scheduledTask = runnableCap.getValue();
        assertNotNull(scheduledTask, "Scheduled runnable must not be null");
        scheduledTask.run();
        verify(useCase, times(1)).execute();
    }

    private static long millisUntilNextMondayMidnight(LocalDateTime from) {
        LocalDateTime nextMondayMidnight = from
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Duration.between(from, nextMondayMidnight).toMillis();
    }
}
