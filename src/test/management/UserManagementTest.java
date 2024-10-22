package test.management;

import core.model.User;
import application.usecase.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Успешное добавление пользователя")
    void testRegisterUser_Success() {
        boolean result = userManager.registerUser("test@example.com", "password", "Test User", false);
        assertThat(result).isTrue();
        assertThat(userManager.getUsers()).containsKey("test@example.com");
    }

    @Test
    @DisplayName("Регистрация с уже существующим email")
    void testRegisterUser_EmailAlreadyExists() {
        userManager.registerUser("test@example.com", "password", "Test User", false);
        boolean result = userManager.registerUser("test@example.com", "newpassword", "New User", false);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Успешное авторизация пользователя")
    void testLoginUser_Success() {
        userManager.registerUser("test@example.com", "password", "Test User", false);
        User user = userManager.loginUser("test@example.com", "password");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    void testLoginUser_IncorrectPassword() {
        userManager.registerUser("test@example.com", "password", "Test User", false);
        User user = userManager.loginUser("test@example.com", "wrongpassword");
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Авторизация заблокированного пользователя")
    void testLoginUser_UserBlocked() {
        userManager.registerUser("test@example.com", "password", "Test User", false);
        userManager.blockUser("test@example.com");
        User user = userManager.loginUser("test@example.com", "password");
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void testDeleteUser_Success() {
        userManager.registerUser("test@example.com", "password", "Test User", false);
        boolean result = userManager.deleteUser("test@example.com");
        assertThat(result).isTrue();
        assertThat(userManager.getUsers()).doesNotContainKey("test@example.com");
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя")
    void testDeleteUser_UserNotFound() {
        boolean result = userManager.deleteUser("nonexistent@example.com");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Успешное редактирование профиля пользователя")
    void testEditUserProfile() {
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
        assertThat(userManager.getUsers()).containsKey("new@example.com");
        assertThat(userManager.getUsers()).doesNotContainKey("test@example.com");
    }

    @Test
    @DisplayName("Успешный сброс пароля")
    void testResetPassword() {
        userManager.registerUser("test@example.com", "password", "Test User", false);
        when(scannerMock.nextLine()).thenReturn("newpassword");

        userManager.resetPassword("test@example.com");

        User user = userManager.getUsers().get("test@example.com");
        assertThat(user.getPassword()).isEqualTo("newpassword");
    }

    @Test
    @DisplayName("Успешная блокировка и разблокировка пользователя")
    void testBlockAndUnblockUser() {
        userManager.registerUser("test@example.com", "password", "Test User", false);
        userManager.blockUser("test@example.com");
        assertThat(userManager.isUserBlocked("test@example.com")).isTrue();
        userManager.unblockUser("test@example.com");
        assertThat(userManager.isUserBlocked("test@example.com")).isFalse();
    }
}
