package test.analytics;

import analytics.HabitAnalytics;
import core.model.Habit;
import core.model.HabitLog;
import application.usecase.HabitServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class HabitAnalyticsTest {

    @Mock
    private Habit habitMock;

    @Mock
    private HabitServiceImp habitServiceMock;

    private HabitAnalytics habitAnalytics;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        habitAnalytics = new HabitAnalytics(habitServiceMock);
    }

    @Test
    @DisplayName("Генерация ежедневного отчета")
    void testGenerateDailyReport() {
        LocalDate today = LocalDate.now();
        HabitLog completedLog = new HabitLog(today, true);

        when(habitMock.getTitle()).thenReturn("Чай");
        when(habitServiceMock.getLogsForPeriod(habitMock, today, today))
                .thenReturn(Collections.singletonList(completedLog));

        habitAnalytics.generateDailyReport(habitMock);

        verify(habitServiceMock).getLogsForPeriod(habitMock, today, today);
    }

    @Test
    @DisplayName("Генерация еженедельного отчета")
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
        when(habitServiceMock.getLogsForPeriod(habitMock, weekAgo, LocalDate.now()))
                .thenReturn(logs);
        long completedLogs = logs.stream().filter(HabitLog::isCompleted).count();

        habitAnalytics.generateWeeklyReport(habitMock);

        verify(habitServiceMock).getLogsForPeriod(habitMock, weekAgo, LocalDate.now());
        assertThat(completedLogs).isEqualTo(4);
    }

    @Test
    @DisplayName("Генерация ежемесячного отчета")
    void testGenerateMonthlyReport() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(oneMonthAgo.plusDays(1), true),
                new HabitLog(oneMonthAgo.plusDays(2), false),
                new HabitLog(oneMonthAgo.plusDays(3), true),
                new HabitLog(oneMonthAgo.plusDays(4), true)
        );

        HabitServiceImp HabitServiceImpMock = mock(HabitServiceImp.class);
        Habit habitMock = mock(Habit.class);
        HabitAnalytics habitAnalytics = new HabitAnalytics(HabitServiceImpMock);

        when(habitMock.getTitle()).thenReturn("Чай");
        when(HabitServiceImpMock.getLogsForPeriod(habitMock, oneMonthAgo, LocalDate.now()))
                .thenReturn(logs);

        habitAnalytics.generateMonthlyReport(habitMock);

        verify(HabitServiceImpMock).getLogsForPeriod(habitMock, oneMonthAgo, LocalDate.now());
    }
}

