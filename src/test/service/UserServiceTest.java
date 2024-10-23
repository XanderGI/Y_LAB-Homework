package test.service;

import core.model.User;
import application.usecase.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {
    private UserServiceImp userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImp();
    }

    @Test
    @DisplayName("Успешное добавление пользователя")
    void testRegisterUser_Success() {
        boolean result = userService.registerUser("test@example.com", "password", "Test User", false);
        assertThat(result).isTrue();
        assertThat(userService.getUsers()).containsKey("test@example.com");
    }

    @Test
    @DisplayName("Регистрация с уже существующим email")
    void testRegisterUser_EmailAlreadyExists() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        boolean result = userService.registerUser("test@example.com", "newpassword", "New User", false);
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Успешное авторизация пользователя")
    void testLoginUser_Success() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        User user = userService.loginUser("test@example.com", "password");
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    void testLoginUser_IncorrectPassword() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        User user = userService.loginUser("test@example.com", "wrongpassword");
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Авторизация заблокированного пользователя")
    void testLoginUser_UserBlocked() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        userService.blockUser("test@example.com");
        User user = userService.loginUser("test@example.com", "password");
        assertThat(user).isNull();
    }

    @Test
    @DisplayName("Успешное удаление пользователя")
    void testDeleteUser_Success() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        boolean result = userService.deleteUser("test@example.com");
        assertThat(result).isTrue();
        assertThat(userService.getUsers()).doesNotContainKey("test@example.com");
    }

    @Test
    @DisplayName("Удаление несуществующего пользователя")
    void testDeleteUser_UserNotFound() {
        boolean result = userService.deleteUser("nonexistent@example.com");
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Успешное редактирование профиля пользователя")
    void testEditUserProfile() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        User user = userService.getUsers().get("test@example.com");

        userService.editUserProfile(user, "New Name", "new@example.com", "newpassword");

        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getPassword()).isEqualTo("newpassword");
        assertThat(userService.getUsers()).containsKey("new@example.com");
        assertThat(userService.getUsers()).doesNotContainKey("test@example.com");
    }

    @Test
    @DisplayName("Успешный сброс пароля")
    void testResetPassword() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        User user = userService.getUsers().get("test@example.com");

        userService.resetPassword(user, "newpassword");


        assertThat(user.getPassword()).isEqualTo("newpassword");
    }

    @Test
    @DisplayName("Успешная блокировка и разблокировка пользователя")
    void testBlockAndUnblockUser() {
        userService.registerUser("test@example.com", "password", "Test User", false);
        userService.blockUser("test@example.com");
        assertThat(userService.isUserBlocked("test@example.com")).isTrue();
        userService.unblockUser("test@example.com");
        assertThat(userService.isUserBlocked("test@example.com")).isFalse();
    }
}
