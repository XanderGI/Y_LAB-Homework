package application.usecase;

import application.service.AdminService;
import core.model.Habit;
import core.model.User;

import java.util.Map;

public class AdminServiceImp implements AdminService {
    private UserServiceImp userService;

    public AdminServiceImp(UserServiceImp userService) {
        this.userService = userService;
    }

    // Просмотр всех пользователей
    @Override
    public void viewAllUsers() {
        Map<String, User> users = userService.getUsers();
        if (users.isEmpty()) {
            System.out.println("Нет зарегистрированных пользователей.");
            return;
        }
        System.out.println("Список всех пользователей:");
        for (Map.Entry<String, User> entry : users.entrySet()) {
            User user = entry.getValue();
            String role = user.isAdmin() ? "Admin" : "User";
            String status = userService.isUserBlocked(user.getEmail()) ? "Заблокирован" : "Активен";
            System.out.println("Email: " + user.getEmail() + " | Имя: " + user.getName() + " | Роль: " + role + " | Статус: " + status);
            for (Habit habit : user.getHabits()) {
                System.out.println("  Привычка: " + habit.getTitle() + ", Частота: " + habit.getFrequency());
            }
        }
    }

    // Блокировка пользователя
    @Override
    public void blockUser(String email) {
        User user = findUserByEmail(email);
        if (user == null || user.isAdmin()) {
            System.out.println("Невозможно заблокировать администратора или пользователь не найден");
            return;
        }
        userService.blockUser(email);
    }

    @Override
    public void unblockUser(String email) {
        User user = findUserByEmail(email);
        if (user == null) {
            return;
        }
        userService.unblockUser(email);
    }

    // Удаление пользователя
    @Override
    public void deleteUser(String email) {
        User user = findUserByEmail(email);
        if (user == null || user.isAdmin()) {
            System.out.println("Невозможно удалить администратора или пользователь не найден.");
            return;
        }

        userService.deleteUser(email);
    }

    private User findUserByEmail(String email) {
        User user = userService.getUsers().get(email);
        if (user == null) {
            System.out.println("Пользователь с таким email не найден.");
        }
        return user;
    }
}
