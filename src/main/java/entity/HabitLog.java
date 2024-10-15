package entity;

import java.time.LocalDate;

// Сущность логирования привычек (отслеживание выполнения)
public class HabitLog {
    private LocalDate date; // Дата выполнения привычки
    private boolean completed; // Выполнена или нет

    public HabitLog(LocalDate date, boolean completed) {
        this.date = date;
        this.completed = completed;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }

}
