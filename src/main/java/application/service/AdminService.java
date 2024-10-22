package application.service;

public interface AdminService {
    void viewAllUsers();
    void blockUser(String email);
    void unblockUser(String email);
    void deleteUser(String email);
}
