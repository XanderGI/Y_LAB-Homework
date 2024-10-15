package management;

import entity.Habit;
import entity.User;

import java.util.Map;
import java.util.Scanner;

public class AdminManager {
    private UserManager userManager;
    private Scanner scanner;

    public AdminManager(UserManager userManager, Scanner scanner) {
        this.userManager = userManager;
        this.scanner = scanner;
    }

    public void adminMenu() {
        while (true) {
            System.out.println("\nМеню администратора:");
            System.out.println("1. Просмотр всех пользователей");
            System.out.println("2. Блокировка пользователя");
            System.out.println("3. Удаление пользователя");
            System.out.println("4. Разблокировка пользователя");
            System.out.println("5. Вернуться в главное меню");
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    blockUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    unblockUser();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    // Просмотр всех пользователей
    private void viewAllUsers() {
        Map<String, User> users = userManager.getUsers();
        if (users.isEmpty()) {
            System.out.println("Нет зарегистрированных пользователей.");
            return;
        }
        System.out.println("Список всех пользователей:");
        for (Map.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();
            String role = user.isAdmin() ? "Admin" : "User";
            String status = userManager.isUserBlocked(user.getEmail()) ? "Заблокирован" : "Активен";
            System.out.println("Email: " + user.getEmail() + " | Имя: " + user.getName() + " | Роль: " + role + " | Статус: " + status);
            for (Habit habit : user.getHabits()) {
                System.out.println("  Привычка: " + habit.getTitle() + ", Частота: " + habit.getFrequency());
            }
        }
    }

    // Блокировка пользователя
    private void blockUser() {
        System.out.print("Введите email пользователя для блокировки: ");
        String email = scanner.nextLine();
        User user = userManager.getUsers().get(email);

        if (user == null) {
            System.out.println("Пользователь с таким email не найден.");
            return;
        }

        if (user.isAdmin()) {
            System.out.println("Невозможно заблокировать администратора.");
            return;
        }

        userManager.blockUser(email);
    }

    private void unblockUser() {
        System.out.print("Введите email пользователя для разблокировки: ");
        String email = scanner.nextLine();
        User user = userManager.getUsers().get(email);

        if (user == null) {
            System.out.println("Пользователь с таким email не найден.");
            return;
        }

        userManager.unblockUser(email);
    }

    // Удаление пользователя
    private void deleteUser() {
        System.out.print("Введите email пользователя для удаления: ");
        String email = scanner.nextLine();
        User user = userManager.getUsers().get(email);

        if (user == null) {
            System.out.println("Пользователь с таким email не найден.");
            return;
        }

        if (user.isAdmin()) {
            System.out.println("Невозможно удалить администратора.");
            return;
        }

        userManager.deleteUser(email);
    }
}
