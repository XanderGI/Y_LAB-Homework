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
    public Scanner scanner = new Scanner(System.in);

    // Регистрация нового пользователя
    public boolean registerUser(String email, String password, String name, boolean isAdmin) {
        if (users.containsKey(email)) {
            System.out.println("Пользователь с таким email уже существует!");
            return false;
        }
        users.put(email, new User(email, password, name, isAdmin));
        blockedUsers.put(email, false); // Изначально пользователь не заблокирован
        System.out.println("Регистрация успешна!");
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
            blockedUsers.remove(email); // Удаляем из списка блокировок
            System.out.println("Пользователь успешно удалён.");
            return true;
        }
        System.out.println("Пользователь с таким email не найден.");
        return false;
    }

    // Редактирование профиля пользователя
    public void editUserProfile(User user) {
        System.out.println("Редактирование профиля...");
        System.out.print("Введите новое имя (текущие: " + user.getName() + "): ");
        String newName = scanner.nextLine();
        System.out.print("Введите новый email (текущий: " + user.getEmail() + "): ");
        String newEmail = scanner.nextLine();
        System.out.print("Введите новый пароль: ");
        String newPassword = scanner.nextLine();
        user.setName(newName);
        user.setPassword(newPassword);
        String oldEmail = user.getEmail();  // Сохраняем старый email перед его обновлением
        // Обновляем email пользователя
        if (!oldEmail.equals(newEmail)) {
            user.setEmail(newEmail);  // Меняем email на новый
            users.remove(oldEmail);  // Удаляем запись со старым email
            users.put(newEmail, user);  // Добавляем запись с новым email
            blockedUsers.put(newEmail, blockedUsers.remove(oldEmail));  // Переносим статус блокировки
        } else {
            System.out.println("Новый email не должен совпадать со старым email!");
        }

        System.out.println("Профиль обновлен.");
    }

    // Сброс пароля через email
    public void resetPassword(String email) {
        User user = users.get(email);
        if (user != null) {
            System.out.print("Введите новый пароль для пользователя " + email + ": ");
            String newPassword = scanner.nextLine();
            user.setPassword(newPassword);
            System.out.println("Пароль успешно сброшен!");
        } else {
            System.out.println("Пользователь с таким email не найден.");
        }
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
