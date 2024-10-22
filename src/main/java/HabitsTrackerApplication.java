import analytics.HabitAnalytics;
import application.usecase.UserServiceImp;
import core.model.User;
import application.usecase.AdminServiceImp;
import application.usecase.HabitManager;
import presentation.CLI.AdminCLI;
import tracking.HabitTracker;

import java.util.Scanner;

public class HabitsTrackerApplication {
    private UserServiceImp userServiceImp = new UserServiceImp();
    private HabitManager habitManager;
    private AdminServiceImp adminServiceImp;
    private AdminCLI adminCLI;
    private HabitAnalytics habitAnalytics;
    private Scanner scanner = new Scanner(System.in);
    private User currentUser;

    public static void main(String... args) {
        HabitsTrackerApplication application = new HabitsTrackerApplication();
        application.run();
    }

    public HabitsTrackerApplication() {
        adminServiceImp = new AdminServiceImp(userServiceImp);
        adminCLI = new AdminCLI(adminServiceImp,scanner);
        habitManager = new HabitManager(scanner, new HabitTracker(), habitAnalytics);
        habitAnalytics = new HabitAnalytics(habitManager);
    }

    public void run() {
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
                mainMenu();
            }
        }
    }

    private void resetPassword() {
        System.out.print("Введите email для сброса пароля: ");
        String email = scanner.nextLine();
        userServiceImp.resetPassword(email);
    }

    // Метод для регистрации
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

        boolean success = userServiceImp.registerUser(email, password, name, isAdmin); // Передаем роль
        if (success) {
            System.out.println("Регистрация успешна. Теперь вы можете войти в систему.");
        } else {
            System.out.println("Ошибка регистрации. Попробуйте снова.");
        }
    }

    // Метод для авторизации
    private void login() {
        System.out.print("Введите email: ");
        String email = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        currentUser = userServiceImp.loginUser(email, password);
    }

    // Основное меню пользователя после входа
    private void mainMenu() {
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
                case 1 -> habitManager.habitMenu(currentUser);
                case 2 -> habitManager.markHabitCompleted(currentUser);
                case 3 -> habitManager.generateHabitReport(currentUser);
                case 4 -> habitAnalytics.generateUserProgressReport(currentUser);
                case 5 -> userServiceImp.editUserProfile(currentUser);
                case 6 -> deleteAccount();
                case 7 -> {
                    logout();
                    return;
                }
                case 8 -> {
                    if (currentUser.isAdmin()) adminCLI.adminMenu();
                }
                default -> System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    // Удаление аккаунта пользователя
    private void deleteAccount() {
        System.out.print("Вы уверены, что хотите удалить свой аккаунт? (да/нет): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("да")) {
            if (currentUser.isAdmin()) {
                System.out.println("Администратор не может удалить свой аккаунт.");
                return;
            }
            userServiceImp.deleteUser(currentUser.getEmail());
            System.out.println("Аккаунт удален.");
            currentUser = null;
            logout();
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    // Выход из аккаунта
    private void logout() {
        System.out.println("Вы вышли из аккаунта.");
        currentUser = null;
        run();
    }
}