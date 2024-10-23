package presentation.CLI;

import application.service.HabitService;
import core.model.Habit;
import core.model.User;

import java.util.List;
import java.util.Scanner;

public class HabitCLI {
    private final HabitService habitService;
    private final Scanner scanner;

    public HabitCLI(HabitService habitService, Scanner scanner) {
        this.habitService = habitService;
        this.scanner = scanner;
    }

    public void habitMenu(User user) {
        while (true) {
            System.out.println("""
                    Меню управления привычками:
                    1. Создать привычку
                    2. Редактировать привычку
                    3. Удалить привычку
                    4. Просмотр всех привычек
                    5. Назад в главное меню
                    """);
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> createHabit(user);
                case 2 -> editHabit(user);
                case 3 -> deleteHabit(user);
                case 4 -> viewHabits(user);
                case 5 -> { return; }
                default -> System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void createHabit(User user) {
        System.out.print("Введите название привычки: ");
        String title = scanner.nextLine();
        System.out.print("Введите описание привычки: ");
        String description = scanner.nextLine();

        String frequency = chooseFrequency();
        habitService.createHabit(user, title, description, frequency);
    }

    private void editHabit(User user) {
        List<Habit> habits = habitService.getHabits(user);
        if (habits.isEmpty()) {
            System.out.println("У вас нет привычек для редактирования.");
            return;
        }

        Habit habit = chooseHabit(habits);
        if (habit == null) {
            System.out.println("Привычка не найдена.");
            return;
        }

        System.out.print("Введите новое название привычки (текущее: " + habit.getTitle() + "): ");
        String newTitle = scanner.nextLine();
        System.out.print("Введите новое описание (текущее: " + habit.getDescription() + "): ");
        String newDescription = scanner.nextLine();
        String newFrequency = chooseFrequency();

        habitService.editHabit(habit, newTitle, newDescription, newFrequency);
    }

    public void deleteHabit(User user) {
        List<Habit> habits = habitService.getHabits(user);
        Habit habit = chooseHabit(habits);
        if (habit != null) {
            habitService.deleteHabit(user, habit);
        } else {
            System.out.println("Привычка не найдена.");
        }
    }

    public void viewHabits(User user) {
        List<Habit> habits = habitService.getHabits(user);
        if (habits.isEmpty()) {
            System.out.println("У вас нет созданных привычек.");
        } else {
            System.out.println("Ваши привычки:");
            habits.forEach(habit -> System.out.println("Название: " + habit.getTitle() + " | описание: " + habit.getDescription() + " | частота: " + habit.getFrequency()));
        }

        System.out.print("\nХотите применить фильтр? (да/нет): ");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("да")) {
            habitService.applyHabitFilters(user);
        }

    }

    public Habit chooseHabit(List<Habit> habits) {
        if (habits.isEmpty()) return null;

        System.out.println("Выберите привычку: ");
        for (int i = 0; i < habits.size(); i++) {
            System.out.println((i + 1) + ". " + habits.get(i).getTitle());
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice >= 1 && choice <= habits.size()) {
            return habits.get(choice - 1);
        } else {
            System.out.println("Неверный выбор.");
            return null;
        }
    }

    private String chooseFrequency() {
        while (true) {
            System.out.println("""
                    Выберите частоту привычки: 
                    1. Ежедневно
                    2. Еженедельно
                    """);
            System.out.print("Введите 1 или 2: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                return "ежедневно";
            } else if (choice.equals("2")) {
                return "еженедельно";
            } else {
                System.out.println("Ошибка: введите '1' для ежедневной привычки или '2' для еженедельной. Пожалуйста, попробуйте снова.");
            }
        }
    }
}
