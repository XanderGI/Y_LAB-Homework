package test.management;

import entity.User;
import management.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserManagerTest {
    private UserManager userManager;
    private Scanner scannerMock;

    @BeforeEach
    void setUp() {
        scannerMock = mock(Scanner.class);
        userManager = new UserManager();
        userManager.scanner = scannerMock;
    }

    @Test
    void testRegisterUser_Success() {
        // Тестируем успешную регистрацию пользователя
        boolean result = userManager.registerUser("test@example.com", "password", "Test User", false);
        assertThat(result).isTrue();
        assertThat(userManager.getUsers()).containsKey("test@example.com");
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Регистрация с уже существующим email
        userManager.registerUser("test@example.com", "password", "Test User", false);
        boolean result = userManager.registerUser("test@example.com", "newpassword", "New User", false);
        assertThat(result).isFalse();
    }

    @Test
    void testLoginUser_Success() {
        // Тестируем успешную авторизацию
        userManager.registerUser("test@example.com", "password", "Test User", false);
        User user = userManager.loginUser("test@example.com", "password");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testLoginUser_IncorrectPassword() {
        // Тестируем авторизацию с неправильным паролем
        userManager.registerUser("test@example.com", "password", "Test User", false);
        User user = userManager.loginUser("test@example.com", "wrongpassword");
        assertThat(user).isNull();  // Ожидаем, что пользователь не будет авторизован
    }

    @Test
    void testLoginUser_UserBlocked() {
        // Тестируем авторизацию заблокированного пользователя
        userManager.registerUser("test@example.com", "password", "Test User", false);
        userManager.blockUser("test@example.com");
        User user = userManager.loginUser("test@example.com", "password");
        assertThat(user).isNull();
    }

    @Test
    void testDeleteUser_Success() {
        // Тестируем успешное удаление пользователя
        userManager.registerUser("test@example.com", "password", "Test User", false);
        boolean result = userManager.deleteUser("test@example.com");
        assertThat(result).isTrue();
        assertThat(userManager.getUsers()).doesNotContainKey("test@example.com");
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Тестируем удаление несуществующего пользователя
        boolean result = userManager.deleteUser("nonexistent@example.com");
        assertThat(result).isFalse();
    }

    @Test
    void testEditUserProfile() {
        // Тестируем успешное редактирование профиля пользователя
        userManager.registerUser("test@example.com", "password", "Test User", false);
        User user = userManager.getUsers().get("test@example.com");

        when(scannerMock.nextLine())
                .thenReturn("New Name")
                .thenReturn("new@example.com")
                .thenReturn("newpassword");

        userManager.editUserProfile(user);

        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getPassword()).isEqualTo("newpassword");
        assertThat(userManager.getUsers()).containsKey("new@example.com");  // Новый email в списке пользователей
        assertThat(userManager.getUsers()).doesNotContainKey("test@example.com");  // Старого email не должно быть
    }

    @Test
    void testResetPassword() {
        // Тестируем сброс пароля
        userManager.registerUser("test@example.com", "password", "Test User", false);
        when(scannerMock.nextLine()).thenReturn("newpassword");

        userManager.resetPassword("test@example.com");

        User user = userManager.getUsers().get("test@example.com");
        assertThat(user.getPassword()).isEqualTo("newpassword");
    }

    @Test
    void testBlockAndUnblockUser() {
        // Тестируем блокировку и разблокировку пользователя
        userManager.registerUser("test@example.com", "password", "Test User", false);
        userManager.blockUser("test@example.com");
        assertThat(userManager.isUserBlocked("test@example.com")).isTrue();
        userManager.unblockUser("test@example.com");
        assertThat(userManager.isUserBlocked("test@example.com")).isFalse();
    }
}
