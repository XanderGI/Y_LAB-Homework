package application.usecase;

import analytics.HabitAnalytics;
import application.service.HabitService;
import core.model.Habit;
import core.model.HabitLog;
import core.model.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HabitServiceImp implements HabitService {
    private Scanner scanner;
    private HabitAnalytics analytics;

    public HabitServiceImp(Scanner scanner, HabitAnalytics analytics) {
        this.scanner = scanner;
        this.analytics = analytics;
    }

    public void setHabitAnalytics(HabitAnalytics habitAnalytics) {
        this.analytics = habitAnalytics;
    }

    // Создание новой привычки
    public void createHabit(User user, String title, String description, String frequency) {
        Habit newHabit = new Habit(title, description, frequency);
        user.addHabit(newHabit);
        System.out.println("Привычка '" + title + "' успешно создана!");
    }

    // Редактирование привычки
    public void editHabit(Habit habit, String newTitle, String newDescription, String newFrequency) {
        habit.setTitle(newTitle);
        habit.setDescription(newDescription);
        habit.setFrequency(newFrequency);
        System.out.println("Привычка обновлена!");
    }

    public List<Habit> getHabits(User user) {
        return user.getHabits();
    }

    // Удаление привычки
    public void deleteHabit(User user, Habit habit) {
        habit.clearLogs();
        user.removeHabit(habit);
        System.out.println("Привычка '" + habit.getTitle() + "' и ее статистика успешно удалены!");
    }

    // Применение фильтров при просмотре привычек
    public void applyHabitFilters(User user) {
        System.out.print("Выберите фильтр: \n1. По дате создания\n2. По статусу выполнения\nВаш выбор: ");
        int filterChoice = scanner.nextInt();
        scanner.nextLine();

        List<Habit> filteredHabits;
        switch (filterChoice) {
            case 1:
                filteredHabits = user.getHabits().stream()
                        .sorted(Comparator.comparing(Habit::getTitle))
                        .collect(Collectors.toList());
                break;
            case 2:
                filteredHabits = user.getHabits().stream()
                        .filter(habit -> habit.getLogs().stream()
                                .anyMatch(log -> log.getDate().equals(LocalDate.now()) && log.isCompleted()))
                        .toList();

                break;
            default:
                System.out.println("Неверный выбор фильтра.");
                return;
        }

        if (filteredHabits.isEmpty()) {
            System.out.println("Нет привычек, удовлетворяющих выбранному фильтру.");
        } else {
            System.out.println("Отфильтрованные привычки:");
            for (int i = 0; i < filteredHabits.size(); i++) {
                Habit habit = filteredHabits.get(i);
                System.out.println((i + 1) + ". " + habit.getTitle());
            }
        }
    }

    // Отметка выполнения привычки
    public void markHabitCompleted(Habit habit) {
        if (habit != null) {
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
        } else {
            System.out.println("Привычка не найдена.");
        }
    }

    // Генерация отчета по привычкам с выбором периода
    public void generateHabitReport(User user) {
        List<Habit> habits = user.getHabits();
        if (habits.isEmpty()) {
            System.out.println("У вас нет созданных привычек.");
            return;
        }

        System.out.println("""
                Выберите период для отчета:
                1. День
                2. Неделя
                3. Месяц
                """);
        int choice = scanner.nextInt();
        scanner.nextLine();

        for (Habit habit : habits) {
            switch (choice) {
                case 1 -> analytics.generateDailyReport(habit);
                case 2 -> analytics.generateWeeklyReport(habit);
                case 3 -> analytics.generateMonthlyReport(habit);
                default -> {
                    System.out.println("Неверный выбор периода.");
                    return;
                }
            }
        }
    }

    // метод для фильтрации логов по периоду
    public List<HabitLog> getLogsForPeriod(Habit habit, LocalDate startDate, LocalDate endDate) {
        return habit.getLogs().stream()
                .filter(log -> !log.getDate().isBefore(startDate) && !log.getDate().isAfter(endDate))
                .toList();
    }

    // Получение текущей серии выполнения привычки (streak)
    public int getCurrentStreakHabit(Habit habit) {
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