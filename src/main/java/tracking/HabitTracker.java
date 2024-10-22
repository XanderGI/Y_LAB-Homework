package tracking;

import core.model.Habit;
import core.model.HabitLog;

import java.time.LocalDate;
import java.util.List;

public class HabitTracker {
    // Отметка выполнения привычки
    public void markHabitCompleted(Habit habit) {
        LocalDate today = LocalDate.now();
        boolean alreadyCompletedToday = habit.getLogs().stream()
                .anyMatch(log -> log.getDate().equals(today) && log.isCompleted());

        if (alreadyCompletedToday) {
            System.out.println("Привычка '" + habit.getTitle() + "' уже была отмечена как выполненная сегодня.");
        } else {
            HabitLog log = new HabitLog(today, true);
            habit.addLog(log);
            System.out.println("Привычка '" + habit.getTitle() + "' отмечена как выполненная сегодня.");
        }
    }

    // Получение текущей серии выполнения привычки (streak)
    public static int getCurrentStreak(Habit habit) {
        List<HabitLog> logs = habit.getLogs();

        if (logs.isEmpty()) {
            return 0;
        }

        logs.sort((log1, log2) -> log2.getDate().compareTo(log1.getDate()));

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        for (HabitLog log : logs) {
            if (log.isCompleted() && log.getDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }

        return streak;
    }
}