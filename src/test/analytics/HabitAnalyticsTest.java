package test.analytics;

import analytics.HabitAnalytics;
import entity.Habit;
import entity.HabitLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class HabitAnalyticsTest {

    @Mock
    private Habit habitMock;

    private HabitAnalytics habitAnalytics;

    @BeforeEach
    void setUp() {
        habitAnalytics = new HabitAnalytics();
        habitMock = mock(Habit.class);
    }

    @Test
    void testGenerateDailyReport() {
        LocalDate today = LocalDate.now();
        HabitLog completedLog = new HabitLog(today, true);

        when(habitMock.getTitle()).thenReturn("Чай");
        when(habitMock.getLogsForPeriod(today, today)).thenReturn(Collections.singletonList(completedLog));

        habitAnalytics.generateDailyReport(habitMock);

        verify(habitMock).getLogsForPeriod(today, today);
    }

    @Test
    void testGenerateWeeklyReport() {
        LocalDate weekAgo = LocalDate.now().minusWeeks(1);
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(weekAgo.plusDays(1), true),
                new HabitLog(weekAgo.plusDays(2), true),
                new HabitLog(weekAgo.plusDays(3), true),
                new HabitLog(weekAgo.plusDays(4), true),
                new HabitLog(weekAgo.plusDays(5), false)
        );

        when(habitMock.getTitle()).thenReturn("Чай");
        when(habitMock.getLogsForPeriod(weekAgo, LocalDate.now())).thenReturn(logs);
        long completedLogs = logs.stream().filter(HabitLog::isCompleted).count();

        habitAnalytics.generateWeeklyReport(habitMock);

        verify(habitMock).getLogsForPeriod(weekAgo, LocalDate.now());
        assertThat(completedLogs).isEqualTo(4);
    }

    @Test
    void testGenerateMonthlyReport() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(oneMonthAgo.plusDays(1), true),
                new HabitLog(oneMonthAgo.plusDays(2), false),
                new HabitLog(oneMonthAgo.plusDays(3), true),
                new HabitLog(oneMonthAgo.plusDays(4), true)
        );

        when(habitMock.getTitle()).thenReturn("Чай");
        when(habitMock.getLogsForPeriod(oneMonthAgo, LocalDate.now())).thenReturn(logs);
        long completedLogs = logs.stream().filter(HabitLog::isCompleted).count();

        habitAnalytics.generateMonthlyReport(habitMock);

        verify(habitMock).getLogsForPeriod(oneMonthAgo, LocalDate.now());
        assertThat(completedLogs).isEqualTo(3);
    }
}

