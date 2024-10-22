package presentation.CLI;

import application.service.AdminService;

import java.util.Scanner;

public class AdminCLI {
    private final AdminService adminService;
    private final Scanner scanner;

    public AdminCLI(AdminService adminService, Scanner scanner) {
        this.adminService = adminService;
        this.scanner = scanner;
    }

    public void adminMenu() {
        while (true) {
            System.out.println("""
                    Меню администратора:
                    1. Просмотр всех пользователей
                    2. Блокировка пользователя
                    3. Удаление пользователя
                    4. Разблокировка пользователя
                    5. Вернуться в главное меню
                    """);
            System.out.print("Выберите опцию: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> blockUser();
                case 3 -> deleteUser();
                case 4 -> unblockUser();
                case 5 -> { return; }
                default -> System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private void viewAllUsers() {
        adminService.viewAllUsers();
    }

    private void blockUser() {
        System.out.print("Введите email пользователя для блокировки: ");
        String email = scanner.nextLine();
        adminService.blockUser(email);
    }

    private void deleteUser() {
        System.out.print("Введите email пользователя для удаления: ");
        String email = scanner.nextLine();
        adminService.deleteUser(email);
    }

    private void unblockUser() {
        System.out.print("Введите email пользователя для разблокировки: ");
        String email = scanner.nextLine();
        adminService.unblockUser(email);
    }

}
