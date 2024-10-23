package presentation.CLI;

import analytics.HabitAnalytics;
import application.service.HabitService;
import application.service.UserService;
import core.model.User;

import java.util.Scanner;

public class UserCLI {
    private final UserService userService;
    private final HabitService habitService;
    private final HabitAnalytics habitAnalytics;
    private final AdminCLI adminCLI;
    private final HabitCLI habitCLI;
    private final Scanner scanner;
    private User currentUser;

    public UserCLI(UserService userService, HabitService habitService, HabitAnalytics habitAnalytics, AdminCLI adminCLI, HabitCLI habitCLI, Scanner scanner) {
        this.userService = userService;
        this.habitService = habitService;
        this.habitAnalytics = habitAnalytics;
        this.adminCLI = adminCLI;
        this.habitCLI = habitCLI;
        this.scanner = scanner;
    }

    public void userMenu() {
        System.out.println("Добро пожаловать в трекер привычек!");

        while (true) {
            System.out.println("""
                    1. Вход
                    2. Регистрация
                    3. Сброс пароля
                    4. Выход
                    """);
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> resetPassword();
                case 4 -> {
                    System.out.println("До свидания!");
                    System.exit(0);
                }
                default -> System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }

            if (currentUser != null) {
                mainMenu(currentUser);
            }
        }
    }

    private void login() {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        currentUser = userService.loginUser(email, password);
        if (currentUser == null) {
            System.out.println("Ошибка: неверные учетные данные.");
        }
    }

    private void register() {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        System.out.print("Хотите зарегистрироваться как администратор? (да/нет): ");
        String isAdminInput = scanner.nextLine().trim().toLowerCase();
        boolean isAdmin = isAdminInput.equals("да");
        if (userService.registerUser(email, password, name, isAdmin)) {
            System.out.println("Регистрация успешна.");
        }
    }

    private void resetPassword() {
        System.out.print("Введите email для сброса пароля: ");
        String email = scanner.nextLine();
        User user = userService.getUsers().get(email);
        if (user != null) {
            System.out.print("Введите новый пароль для пользователя " + email + ": ");
            String newPassword = scanner.nextLine();
            userService.resetPassword(user, newPassword);
            currentUser = null;
        } else {
            System.out.println("Пользователь с таким email не найден.");
        }
    }

    private void mainMenu(User currentUser) {
        while (true) {
            System.out.print("""
                    Главное меню:
                    1. Управление привычками
                    2. Отметить выполнение привычки
                    3. Просмотр отчета по привычкам
                    4. Отчет о прогрессе пользователя
                    5. Редактирование профиля
                    6. Удалить аккаунт
                    7. Выход из аккаунта
                    """);

            if (currentUser.isAdmin()) {
                System.out.println("8. Меню администратора\n");
            }

            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> habitCLI.habitMenu(currentUser);
                case 2 -> habitService.markHabitCompleted(habitCLI.chooseHabit(currentUser.getHabits()));
                case 3 -> habitService.generateHabitReport(currentUser);
                case 4 -> habitAnalytics.generateUserProgressReport(currentUser);
                case 5 -> editUserProfile();
                case 6 -> deleteAccount();
                case 7 -> {
                    currentUser = null;
                    return;
                }
                case 8 -> {
                    if (currentUser.isAdmin()) adminCLI.adminMenu();
                }
                default -> System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void editUserProfile() {
        System.out.println("Редактирование профиля...");
        System.out.print("Введите новое имя (текущие: " + currentUser.getName() + "): ");
        String newName = scanner.nextLine();
        System.out.print("Введите новый email (текущий: " + currentUser.getEmail() + "): ");
        String newEmail = scanner.nextLine();
        System.out.print("Введите новый пароль: ");
        String newPassword = scanner.nextLine();
        userService.editUserProfile(currentUser, newName, newEmail, newPassword);
    }

    private void deleteAccount() {
        System.out.print("Вы уверены, что хотите удалить свой аккаунт? (да/нет): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("да")) {
            if (currentUser.isAdmin()) {
                System.out.println("Администратор не может удалить свой аккаунт.");
                return;
            }
            if (userService.deleteUser(currentUser.getEmail())) {
                logout();
            }
            logout();
        } else {
            System.out.println("Удаление отменено.");
        }

    }

    // Выход из аккаунта
    private void logout() {
        System.out.println("Вы вышли из аккаунта.");
        currentUser = null;
        userMenu();
    }
}