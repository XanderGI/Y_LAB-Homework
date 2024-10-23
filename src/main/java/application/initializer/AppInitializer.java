package application.initializer;

import analytics.HabitAnalytics;
import application.service.AdminService;
import application.service.HabitService;
import application.service.UserService;
import application.usecase.AdminServiceImp;
import application.usecase.HabitServiceImp;
import application.usecase.UserServiceImp;
import presentation.CLI.AdminCLI;
import presentation.CLI.HabitCLI;
import presentation.CLI.UserCLI;

import java.util.Scanner;

public class AppInitializer {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final HabitService habitService;
    private final HabitAnalytics habitAnalytics;
    private final AdminService adminService;
    private final UserCLI userCLI;
    private final HabitCLI habitCLI;
    private final AdminCLI adminCLI;

    public AppInitializer() {
        userService = new UserServiceImp();
        habitService = new HabitServiceImp(scanner, null);
        habitAnalytics = new HabitAnalytics(habitService);
        ((HabitServiceImp) habitService).setHabitAnalytics(habitAnalytics);
        adminService = new AdminServiceImp(userService);
        adminCLI = new AdminCLI(adminService, scanner);
        habitCLI = new HabitCLI(habitService, scanner);
        userCLI = new UserCLI(userService, habitService, habitAnalytics, adminCLI, habitCLI, scanner);
    }

    public void run() {
        userCLI.userMenu();
    }
}
