package application.service;

import core.model.Habit;
import core.model.HabitLog;
import core.model.User;

import java.time.LocalDate;
import java.util.List;

public interface HabitService {
    void createHabit(User user, String title, String description, String frequency);
    void editHabit(Habit habit, String newTitle, String newDescription, String newFrequency);
    void deleteHabit(User user, Habit habit);
    List<Habit> getHabits(User user);
    void applyHabitFilters(User user);
    void markHabitCompleted(Habit habit);
    void generateHabitReport(User user);
    List<HabitLog> getLogsForPeriod(Habit habit, LocalDate startDate, LocalDate endDate);
    int getCurrentStreakHabit(Habit habit);
}
