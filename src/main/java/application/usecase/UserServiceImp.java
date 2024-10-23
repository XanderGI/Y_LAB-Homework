package application.usecase;

import application.service.UserService;
import core.model.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Класс для управления пользователями
public class UserServiceImp implements UserService {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Boolean> blockedUsers = new HashMap<>(); // Для хранения статуса блокировки пользователей

    // Регистрация нового пользователя
    public boolean registerUser(String email, String password, String name, boolean isAdmin) {
        if (users.containsKey(email)) {
            System.out.println("Пользователь с таким email уже существует!");
            return false;
        }
        users.put(email, new User(email, password, name, isAdmin));
        blockedUsers.put(email, false);
        return true;
    }

    // Авторизация пользователя
    public User loginUser(String email, String password) {
        if (isUserBlocked(email)) {
            System.out.println("Этот пользователь заблокирован. Вход запрещён.");
            return null;
        }

        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Вход успешен!");
            return user;
        } else {
            System.out.println("Неверный email или пароль.");
            return null;
        }
    }

    // Удаление пользователя
    public boolean deleteUser(String email) {
        if (users.remove(email) != null) {
            blockedUsers.remove(email);
            System.out.println("Пользователь успешно удалён.");
            return true;
        }
        System.out.println("Пользователь с таким email не найден.");
        return false;
    }

    // Редактирование профиля пользователя
    public void editUserProfile(User user, String newName, String newEmail, String newPassword) {
        user.setName(newName);
        user.setPassword(newPassword);
        String oldEmail = user.getEmail();
        if (!oldEmail.equals(newEmail)) {
            user.setEmail(newEmail);
            users.remove(oldEmail);
            users.put(newEmail, user);
            blockedUsers.put(newEmail, blockedUsers.remove(oldEmail));
        } else {
            System.out.println("Новый email не должен совпадать со старым email!");
        }
        System.out.println("Профиль обновлен.");
    }

    // Сброс пароля
    public void resetPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        System.out.println("Пароль успешно сброшен!");
    }

    // Блокировка пользователя
    public void blockUser(String email) {
        if (users.containsKey(email)) {
            blockedUsers.put(email, true);
            System.out.println("Пользователь " + email + " заблокирован.");
        } else {
            System.out.println("Пользователь с таким email не найден.");
        }
    }

    // Разблокировка пользователя
    public void unblockUser(String email) {
        if (users.containsKey(email)) {
            blockedUsers.put(email, false);
            System.out.println("Пользователь " + email + " разблокирован.");
        } else {
            System.out.println("Пользователь с таким email не найден.");
        }
    }

    // Проверка, заблокирован ли пользователь
    public boolean isUserBlocked(String email) {
        return blockedUsers.getOrDefault(email, false);
    }

    public Map<String, User> getUsers() {
        return users;
    }
}
