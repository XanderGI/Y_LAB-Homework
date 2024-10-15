package tracking;

import entity.Habit;
import entity.HabitLog;

import java.time.LocalDate;
import java.util.List;

public class HabitTracker {

    // Отметка выполнения привычки
    public void markHabitCompleted(Habit habit) {
        LocalDate today = LocalDate.now();

        // Проверка, был ли лог за сегодняшний день
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

        // Сортируем логи по дате в обратном порядке (от свежих к старым)
        logs.sort((log1, log2) -> log2.getDate().compareTo(log1.getDate()));

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        for (HabitLog log : logs) {
            // Если лог выполнен и дата совпадает с ожидаемой
            if (log.isCompleted() && log.getDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);  // Ожидаем следующий день
            } else {
                break;
            }
        }

        return streak;
    }
}
