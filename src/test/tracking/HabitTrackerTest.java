package test.tracking;

import analytics.HabitAnalytics;
import application.service.HabitService;
import application.usecase.HabitServiceImp;
import core.model.Habit;
import core.model.HabitLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class HabitTrackerTest {

    private Habit habit;
    private HabitService habitService;
    private Scanner scanner;
    private HabitAnalytics habitAnalytics;

    @BeforeEach
    void setUp() {
        habitService = new HabitServiceImp(scanner, habitAnalytics);
        habit = new Habit("Чай", "Пить 3 чашки", "ежедневно");
    }

    @Test
    @DisplayName("Проверка 0-вой серии стрика")
    void testGetCurrentStreak_noLogs() {
        assertThat(habitService.getCurrentStreakHabit(habit)).isEqualTo(0);
    }

    @Test
    @DisplayName("Проверка серии из 3-х подряд дней")
    void testGetCurrentStreak_continuousStreak() {
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(today.minusDays(2), true),
                new HabitLog(today.minusDays(1), true),
                new HabitLog(today, true)
        );
        habit.setLogs(logs);
        assertThat(habitService.getCurrentStreakHabit(habit)).isEqualTo(3);
    }

    @Test
    @DisplayName("Прерывание стрика")
    void testGetCurrentStreak_interruptedStreak() {
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(today.minusDays(5), true),
                new HabitLog(today.minusDays(4), true),
                new HabitLog(today.minusDays(3), true),
                new HabitLog(today.minusDays(1), true),
                new HabitLog(today, true)
        );
        habit.setLogs(logs);
        assertThat(habitService.getCurrentStreakHabit(habit)).isEqualTo(2);
    }

    @Test
    @DisplayName("Привычка не может быть выполнена дважды за день")
    void testMarkHabitCompleted_alreadyCompletedToday() {
        LocalDate today = LocalDate.now();
        habit.addLog(new HabitLog(today, true));
        habitService.markHabitCompleted(habit);
        assertThat(habit.getLogs().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Привычка отмечена успешно")
    void testMarkHabitCompleted_notCompletedYet() {
        habitService.markHabitCompleted(habit);
        assertThat(habit.getLogs().size()).isEqualTo(1);
        assertThat(habit.getLogs().get(0).isCompleted()).isTrue();
    }
}
