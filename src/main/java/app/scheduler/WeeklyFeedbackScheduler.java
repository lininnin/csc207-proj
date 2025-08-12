package app.scheduler;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import use_case.generate_feedback.GenerateFeedbackInputBoundary;
import use_case.generate_feedback.GenerateFeedbackInputData;

// style checked
public class WeeklyFeedbackScheduler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final GenerateFeedbackInputBoundary feedbackUseCase;

    public WeeklyFeedbackScheduler(GenerateFeedbackInputBoundary feedbackUseCase) {
        this.feedbackUseCase = feedbackUseCase;
    }

    /**
     * Starts the weekly feedback generation scheduler.
     * Schedules {@link #runFeedbackGeneration()} to run after computed intiial delay,
     * for every Monday.
     */
    public void start() {
        final long initialDelay = computeInitialDelay();
        final long period = TimeUnit.DAYS.toMillis(7);

        executor.scheduleAtFixedRate(
                this::runFeedbackGeneration,
                initialDelay,
                period,
                TimeUnit.MILLISECONDS
        );
    }

    private void runFeedbackGeneration() {
        feedbackUseCase.execute(new GenerateFeedbackInputData());
    }

    private long computeInitialDelay() {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime nextMondayMidnight = now
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        final Duration duration = Duration.between(now, nextMondayMidnight);
        return duration.toMillis();
    }
}
