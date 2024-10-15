import analytics.HabitAnalytics;
import entity.User;
import management.AdminManager;
import management.HabitManager;
import management.UserManager;
import tracking.HabitTracker;

import java.util.Scanner;

public class HabitsTrackerApplication {
    private UserManager userManager = new UserManager();
    private HabitManager habitManager;
    private AdminManager adminManager;
    private HabitAnalytics habitAnalytics = new HabitAnalytics();
    private Scanner scanner = new Scanner(System.in);
    private User currentUser;

    public static void main(String... args) {
        HabitsTrackerApplication application = new HabitsTrackerApplication();
        application.run();
    }

    public HabitsTrackerApplication() {
        adminManager = new AdminManager(userManager, scanner);
        habitManager = new HabitManager(scanner, new HabitTracker(), habitAnalytics);
    }

    public void run() {
        System.out.println("Добро пожаловать в трекер привычек!");

        while (true) {
            System.out.println("\n1. Вход");
            System.out.println("2. Регистрация");
            System.out.println("3. Сброс пароля");
            System.out.println("4. Выход");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    resetPassword();
                    break;
                case 4:
                    System.out.println("До свидания!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }

            if (currentUser != null) {
                mainMenu();
            }
        }
    }

    private void resetPassword() {
        System.out.print("Введите email для сброса пароля: ");
        String email = scanner.nextLine();
        userManager.resetPassword(email);
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

        boolean success = userManager.registerUser(email, password, name, isAdmin); // Передаем роль
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

        currentUser = userManager.loginUser(email, password);
    }

    // Основное меню пользователя после входа
    private void mainMenu() {
        while (true) {
            System.out.println("\nГлавное меню:");
            System.out.println("1. Управление привычками");
            System.out.println("2. Отметить выполнение привычки");
            System.out.println("3. Просмотр отчета по привычкам");
            System.out.println("4. Отчет о прогрессе пользователя");
            System.out.println("5. Редактирование профиля");
            System.out.println("6. Удалить аккаунт");
            System.out.println("7. Выход из аккаунта");

            if (currentUser.isAdmin()) {
                System.out.println("8. Меню администратора");
            }

            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    habitManager.habitMenu(currentUser);
                    break;
                case 2:
                    habitManager.markHabitCompleted(currentUser);
                    break;
                case 3:
                    habitManager.generateHabitReport(currentUser);
                    break;
                case 4:
                    habitAnalytics.generateUserProgressReport(currentUser);
                    break;
                case 5:
                    userManager.editUserProfile(currentUser);
                    break;
                case 6:
                    deleteAccount();
                    break;
                case 7:
                    logout();
                    return;
                case 8:
                    if (currentUser.isAdmin()) {
                        adminManager.adminMenu();  // Вызов меню администратора
                    }
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
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
            userManager.deleteUser(currentUser.getEmail());
            System.out.println("Аккаунт удален.");
            currentUser = null;  // Очистка текущего пользователя
            logout();
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    // Выход из аккаунта
    private void logout() {
        System.out.println("Вы вышли из аккаунта.");
        currentUser = null;  // Обнуление текущего пользователя
        run();  // Возвращение в главное меню (начать заново)
    }
}
