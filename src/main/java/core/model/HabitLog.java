package core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

// Сущность логирования привычек (отслеживание выполнения)
@Getter
@AllArgsConstructor
public class HabitLog {
    private LocalDate date; // Дата выполнения привычки
    private boolean completed; // Выполнена или нет
}
