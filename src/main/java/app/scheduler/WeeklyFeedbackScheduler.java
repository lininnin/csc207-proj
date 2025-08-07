package app.scheduler;

import use_case.generate_feedback.GenerateFeedbackInputBoundary;
import use_case.generate_feedback.GenerateFeedbackInputData;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.*;

public class WeeklyFeedbackScheduler {
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final GenerateFeedbackInputBoundary feedbackUseCase;

    public WeeklyFeedbackScheduler(GenerateFeedbackInputBoundary feedbackUseCase) {
        this.feedbackUseCase = feedbackUseCase;
    }

    public void start() {
        long initialDelay = computeInitialDelay();
        long period = TimeUnit.DAYS.toMillis(7);

        executor.scheduleAtFixedRate(
                this::runFeedbackGeneration,
                initialDelay,
                period,
                TimeUnit.MILLISECONDS
        );
    }

    private void runFeedbackGeneration() {
        feedbackUseCase.execute(new GenerateFeedbackInputData(/*...*/));
    }

    private long computeInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMondayMidnight = now
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        Duration duration = Duration.between(now, nextMondayMidnight);
        return duration.toMillis();
    }
}
