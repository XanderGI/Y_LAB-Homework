package application.service;

import core.model.User;

import java.util.Map;

public interface UserService {
    boolean registerUser(String email, String password, String name, boolean isAdmin);
    User loginUser(String email, String password);
    boolean deleteUser(String email);
    void editUserProfile(String email, String newName, String newEmail, String newPassword);
    void resetPassword(String email, String newPassword);
    void blockUser(String email);
    void unblockUser(String email);
    boolean isUserBlocked(String email);
    Map<String, User> getUsers();
}
