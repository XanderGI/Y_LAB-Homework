package test.tracking;

import entity.Habit;
import entity.HabitLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracking.HabitTracker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HabitTrackerTest {

    private Habit habit;
    private HabitTracker habitTracker;

    @BeforeEach
    void setUp() {
        habitTracker = new HabitTracker();
        habit = new Habit("Чай", "Пить 3 чашки", "ежедневно");
    }

    @Test
    void testGetCurrentStreak_noLogs() {
        // Когда нет логов, серия должна быть 0
        assertThat(HabitTracker.getCurrentStreak(habit)).isEqualTo(0);
    }

    @Test
    void testGetCurrentStreak_continuousStreak() {
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(today.minusDays(2), true),
                new HabitLog(today.minusDays(1), true),
                new HabitLog(today, true)
        );
        habit.setLogs(logs);

        // Серия 3 дня подряд
        assertThat(HabitTracker.getCurrentStreak(habit)).isEqualTo(3);
    }

    @Test
    void testGetCurrentStreak_interruptedStreak() {
        //Тестирование, если стрик прервался
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(today.minusDays(5),true),
                new HabitLog(today.minusDays(4),true),
                new HabitLog(today.minusDays(3), true),
                new HabitLog(today.minusDays(1), true),  // Пропущен день 2
                new HabitLog(today, true)
        );
        habit.setLogs(logs);

        assertThat(HabitTracker.getCurrentStreak(habit)).isEqualTo(2);
    }

    @Test
    void testMarkHabitCompleted_alreadyCompletedToday() {
        LocalDate today = LocalDate.now();
        habit.addLog(new HabitLog(today, true));

        habitTracker.markHabitCompleted(habit);

        // Проверяем, что второй раз выполнение не отмечено
        assertThat(habit.getLogs().size()).isEqualTo(1);
    }

    @Test
    void testMarkHabitCompleted_notCompletedYet() {
        habitTracker.markHabitCompleted(habit);

        // Проверяем, что привычка была успешно отмечена как выполненная
        assertThat(habit.getLogs().size()).isEqualTo(1);
        assertThat(habit.getLogs().get(0).isCompleted()).isTrue();
    }
}
