package application.usecase;

import analytics.HabitAnalytics;
import core.model.Habit;
import core.model.User;
import tracking.HabitTracker;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HabitManager {
    private Scanner scanner;
    private HabitTracker tracker;
    private HabitAnalytics analytics;

    public HabitManager(Scanner scanner, HabitTracker tracker, HabitAnalytics analytics) {
        this.scanner = scanner;
        this.tracker = tracker;
        this.analytics = analytics;
    }

    // Новый метод для управления привычками
    public void habitMenu(User user) {
        while (true) {
            System.out.println("\nМеню управления привычками:");
            System.out.println("1. Создать привычку");
            System.out.println("2. Редактировать привычку");
            System.out.println("3. Удалить привычку");
            System.out.println("4. Просмотр всех привычек");
            System.out.println("5. Назад в главное меню");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createHabit(user);
                    break;
                case 2:
                    editHabit(user);
                    break;
                case 3:
                    deleteHabit(user);
                    break;
                case 4:
                    viewHabits(user);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    // Создание новой привычки
    public void createHabit(User user) {
        System.out.print("Введите название привычки: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание привычки: ");
        String description = scanner.nextLine();

        // Валидация частоты
        String frequency;
        while (true) {
            System.out.print("Введите частоту привычки (ежедневно, еженедельно): ");
            frequency = scanner.nextLine().trim().toLowerCase();
            if (frequency.equals("ежедневно") || frequency.equals("еженедельно")) {
                break;
            } else {
                System.out.println("Ошибка: частота должна быть 'ежедневно' или 'еженедельно'. Пожалуйста, попробуйте снова.");
            }
        }

        Habit newHabit = new Habit(title, description, frequency);
        user.addHabit(newHabit);
        System.out.println("Привычка '" + title + "' успешно создана!");
    }

    // Редактирование привычки
    public void editHabit(User user) {
        List<Habit> habits = user.getHabits();
        if (habits.isEmpty()) {
            System.out.println("У вас нет привычек для редактирования.");
            return;
        }

        Habit habit = chooseHabit(user);
        if (habit == null) {
            System.out.println("Привычка не найдена.");
            return;
        }

        System.out.print("Введите новое название привычки (текущее: " + habit.getTitle() + "): ");
        String newTitle = scanner.nextLine();
        System.out.print("Введите новое описание (текущее: " + habit.getDescription() + "): ");
        String newDescription = scanner.nextLine();

        // Валидация частоты
        String newFrequency;
        while (true) {
            System.out.print("Введите новую частоту (ежедневно, еженедельно) (текущая: " + habit.getFrequency() + "): ");
            newFrequency = scanner.nextLine().trim().toLowerCase();
            if (newFrequency.equals("ежедневно") || newFrequency.equals("еженедельно")) {
                break;
            } else {
                System.out.println("Ошибка: частота должна быть 'ежедневно' или 'еженедельно'. Пожалуйста, попробуйте снова.");
            }
        }

        habit.setTitle(newTitle);
        habit.setDescription(newDescription);
        habit.setFrequency(newFrequency);

        System.out.println("Привычка обновлена!");
    }

    // Удаление привычки
    public void deleteHabit(User user) {
        Habit habit = chooseHabit(user);
        if (habit != null) {
            habit.clearLogs(); // Очищаем логи привычки перед удалением
            user.removeHabit(habit);
            System.out.println("Привычка '" + habit.getTitle() + "' и ее статистика успешно удалены!");
        } else {
            System.out.println("Привычка не найдена.");
        }
    }

    // Просмотр всех привычек пользователя
    public void viewHabits(User user) {
        List<Habit> habits = user.getHabits();
        if (habits.isEmpty()) {
            System.out.println("У вас нет созданных привычек.");
            return;
        }

        System.out.println("Ваши привычки:");
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            System.out.println((i + 1) + ". " + habit.getTitle() + " (частота: " + habit.getFrequency() + ")");
        }

        System.out.print("\nХотите применить фильтр? (да/нет): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("да")) {
            applyFilters(user);
        }
    }

    // Применение фильтров при просмотре привычек
    private void applyFilters(User user) {
        System.out.print("Выберите фильтр: \n1. По дате создания\n2. По статусу выполнения\nВаш выбор: ");
        int filterChoice = scanner.nextInt();
        scanner.nextLine();

        List<Habit> filteredHabits;
        switch (filterChoice) {
            case 1:
                // Фильтрация по дате создания (для примера сортировка по алфавиту заголовков)
                filteredHabits = user.getHabits().stream()
                        .sorted(Comparator.comparing(Habit::getTitle))
                        .collect(Collectors.toList());
                break;
            case 2:
                // Фильтрация по статусу выполнения (показываем только те привычки, которые были выполнены сегодня)
                filteredHabits = user.getHabits().stream()
                        .filter(habit -> habit.getLogs().stream().anyMatch(log -> log.getDate().equals(LocalDate.now()) && log.isCompleted()))
                        .collect(Collectors.toList());
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
    public void markHabitCompleted(User user) {
        Habit habit = chooseHabit(user);
        if (habit != null) {
            tracker.markHabitCompleted(habit);
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

        System.out.println("Выберите период для отчета: ");
        System.out.println("1. День");
        System.out.println("2. Неделя");
        System.out.println("3. Месяц");
        int choice = scanner.nextInt();
        scanner.nextLine();

        for (Habit habit : habits) {
            switch (choice) {
                case 1:
                    analytics.generateDailyReport(habit);
                    break;
                case 2:
                    analytics.generateWeeklyReport(habit);
                    break;
                case 3:
                    analytics.generateMonthlyReport(habit);
                    break;
                default:
                    System.out.println("Неверный выбор периода.");
                    return;
            }
        }
    }

    // Вспомогательный метод для выбора привычки
    private Habit chooseHabit(User user) {
        List<Habit> habits = user.getHabits();
        if (habits.isEmpty()) {
            System.out.println("У вас нет созданных привычек.");
            return null;
        }

        System.out.println("Выберите привычку:");
        for (int i = 0; i < habits.size(); i++) {
            System.out.println((i + 1) + ". " + habits.get(i).getTitle());
        }
        int habitChoice = scanner.nextInt();
        scanner.nextLine(); // Очистка перехода на новую строку

        if (habitChoice > 0 && habitChoice <= habits.size()) {
            return habits.get(habitChoice - 1);
        } else {
            System.out.println("Неверный выбор.");
            return null;
        }
    }
}