package analytics;

import entity.Habit;
import entity.HabitLog;
import entity.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static tracking.HabitTracker.getCurrentStreak;

public class HabitAnalytics {

    // Генерация отчета по привычкам пользователя за день
    public void generateDailyReport(Habit habit) {
        LocalDate today = LocalDate.now();
        List<HabitLog> logsForToday = habit.getLogsForPeriod(today, today);

        long completedLogs = logsForToday.stream().filter(HabitLog::isCompleted).count();
        int streak = getCurrentStreak(habit);
        System.out.println("Привычка: " + habit.getTitle());
        System.out.println("Выполнение за сегодня: " + (completedLogs > 0 ? "выполнена" : "не выполнена"));
        System.out.println("Текущий стрик: " + streak + " дней.");
    }

    // Генерация отчета по привычкам пользователя за неделю
    public void generateWeeklyReport(Habit habit) {
        LocalDate weekAgo = LocalDate.now().minusWeeks(1);
        List<HabitLog> logsForWeek = habit.getLogsForPeriod(weekAgo, LocalDate.now());

        long completedLogs = logsForWeek.stream().filter(HabitLog::isCompleted).count();
        long totalDays = weekAgo.until(LocalDate.now()).getDays() + 1;
        int streak = getCurrentStreak(habit);

        double completionRate = ((double) completedLogs / totalDays) * 100;
        System.out.println("Привычка: " + habit.getTitle());
        System.out.println("Процент выполнения за последнюю неделю: (" + completedLogs + " из 7)" + " или " + completionRate + "%");
        System.out.println("Текущий стрик: " + streak + " дней.");
    }

    // Генерация отчета по привычкам пользователя за последний месяц
    public void generateMonthlyReport(Habit habit) {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<HabitLog> logsForMonth = habit.getLogsForPeriod(oneMonthAgo, LocalDate.now());

        long completedLogs = logsForMonth.stream().filter(HabitLog::isCompleted).count();
        long totalDays = ChronoUnit.DAYS.between(oneMonthAgo, LocalDate.now()) + 1;
        int streak = getCurrentStreak(habit);

        double completionRate = ((double) completedLogs / totalDays) * 100;
        System.out.println("Привычка: " + habit.getTitle());
        System.out.println("Процент выполнения за последний месяц: (" + completedLogs + " из " + totalDays + ") или " + completionRate + "%");
        System.out.println("Текущий стрик: " + streak + " дней.");
    }

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
