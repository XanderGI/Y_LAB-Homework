package core.model;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class User {
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String name;
    @NonNull
    private boolean isAdmin;
    private ArrayList<Habit> habits;

    public User(String email, String password, String name, boolean isAdmin) {
        this(email,password,name,isAdmin, new ArrayList<>());
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public void removeHabit(Habit habit) {
        habits.remove(habit);
    }
}
