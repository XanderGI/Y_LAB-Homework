package analytics;

import core.model.Habit;
import core.model.HabitLog;
import core.model.User;
import application.usecase.HabitManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static tracking.HabitTracker.getCurrentStreak;

public class HabitAnalytics {
    private HabitManager habitManager;

    public HabitAnalytics(HabitManager habitManager) {
        this.habitManager = habitManager;
    }

    // Генерация отчета по привычкам пользователя за день
    public void generateDailyReport(Habit habit) {
        LocalDate today = LocalDate.now();
        List<HabitLog> logsForToday = habitManager.getLogsForPeriod(habit, today, today);

        long completedLogs = logsForToday.stream().filter(HabitLog::isCompleted).count();
        int streak = getCurrentStreak(habit);

        System.out.println(String.format("Привычка: %s", habit.getTitle()));
        System.out.println("Выполнение за сегодня: " + (completedLogs > 0 ? "выполнена" : "не выполнена"));
        System.out.println(String.format("Текущий стрик: %d дней.", streak));
    }

    // Генерация отчета по привычкам пользователя за неделю
    public void generateWeeklyReport(Habit habit) {
        LocalDate weekAgo = LocalDate.now().minusWeeks(1);
        List<HabitLog> logsForWeek = habitManager.getLogsForPeriod(habit, weekAgo, LocalDate.now());

        long completedLogs = logsForWeek.stream().filter(HabitLog::isCompleted).count();
        long totalDays = weekAgo.until(LocalDate.now()).getDays() + 1;
        int streak = getCurrentStreak(habit);

        double completionRate = ((double) completedLogs / totalDays) * 100;
        System.out.println(String.format("Привычка: %s", habit.getTitle()));
        System.out.println(String.format("Процент выполнения за последнюю неделю: (%d из 7) или %.2f%%", completedLogs, completionRate));
        System.out.println(String.format("Текущий стрик: %d дней.", streak));
    }

    // Генерация отчета по привычкам пользователя за последний месяц
    public void generateMonthlyReport(Habit habit) {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<HabitLog> logsForMonth = habitManager.getLogsForPeriod(habit, oneMonthAgo, LocalDate.now());

        long completedLogs = logsForMonth.stream().filter(HabitLog::isCompleted).count();
        long totalDays = ChronoUnit.DAYS.between(oneMonthAgo, LocalDate.now()) + 1;
        int streak = getCurrentStreak(habit);

        double completionRate = ((double) completedLogs / totalDays) * 100;
        System.out.println(String.format("Привычка: %s", habit.getTitle()));
        System.out.println(String.format("Процент выполнения за последний месяц: (%d из %d) или %.2f%%", completedLogs, totalDays, completionRate));
        System.out.println(String.format("Текущий стрик: %d дней.", streak));
    }

    // Генерация отчета по всем привычкам пользователя
    public void generateUserProgressReport(User user) {
        List<Habit> habits = user.getHabits();
        if (habits.isEmpty()) {
            System.out.println("У вас нет созданных привычек.");
            return;
        }

        System.out.println("Отчет о прогрессе пользователя:");

        for (Habit habit : habits) {
            generateDailyReport(habit);
            generateWeeklyReport(habit);
            generateMonthlyReport(habit);
        }
    }
}
