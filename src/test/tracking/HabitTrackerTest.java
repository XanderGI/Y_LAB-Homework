package test.tracking;

import core.model.Habit;
import core.model.HabitLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Проверка 0-вой серии стрика")
    void testGetCurrentStreak_noLogs() {
        assertThat(HabitTracker.getCurrentStreak(habit)).isEqualTo(0);
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

        assertThat(HabitTracker.getCurrentStreak(habit)).isEqualTo(3);
    }

    @Test
    @DisplayName("Прерывание стрика")
    void testGetCurrentStreak_interruptedStreak() {
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = Arrays.asList(
                new HabitLog(today.minusDays(5),true),
                new HabitLog(today.minusDays(4),true),
                new HabitLog(today.minusDays(3), true),
                new HabitLog(today.minusDays(1), true),
                new HabitLog(today, true)
        );
        habit.setLogs(logs);

        assertThat(HabitTracker.getCurrentStreak(habit)).isEqualTo(2);
    }

    @Test
    @DisplayName("Проверяем, что привычка не может быть выполнена дважды за день")
    void testMarkHabitCompleted_alreadyCompletedToday() {
        LocalDate today = LocalDate.now();
        habit.addLog(new HabitLog(today, true));

        habitTracker.markHabitCompleted(habit);

        assertThat(habit.getLogs().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Привычка отмечена успешно")
    void testMarkHabitCompleted_notCompletedYet() {
        habitTracker.markHabitCompleted(habit);

        assertThat(habit.getLogs().size()).isEqualTo(1);
        assertThat(habit.getLogs().get(0).isCompleted()).isTrue();
    }
}
