package entity;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private String name;
    private boolean isAdmin;
    private ArrayList<Habit> habits;

    public User(String email, String password, String name, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.habits = new ArrayList<>();
        this.isAdmin = isAdmin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Habit> getHabits() {
        return habits;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public void removeHabit(Habit habit) {
        habits.remove(habit);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
