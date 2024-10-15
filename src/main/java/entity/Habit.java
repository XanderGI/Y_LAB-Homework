package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habit {
    private String title;
    private String description;
    private String frequency;  // Например, "ежедневно", "еженедельно"
    private List<HabitLog> logs = new ArrayList<>();

    public Habit(String title, String description, String frequency) {
        this.title = title;
        this.description = description;
        this.frequency = frequency;
    }

    public void setLogs(List<HabitLog> logs) {
        this.logs = logs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public List<HabitLog> getLogs() {
        return logs;
    }

    public void addLog(HabitLog log) {
        logs.add(log);
    }

    public void removeLog(HabitLog log) {
        logs.remove(log);
    }

    public void clearLogs() {
        logs.clear(); // Удаляем все логи при удаление привычки
    }

    // Получение всех логов за указанный период
    public List<HabitLog> getLogsForPeriod(LocalDate startDate, LocalDate endDate) {
        List<HabitLog> result = new ArrayList<>();
        for (HabitLog log : logs) {
            if ((log.getDate().isEqual(startDate) || log.getDate().isAfter(startDate)) &&
                    (log.getDate().isEqual(endDate) || log.getDate().isBefore(endDate))) {
                result.add(log);
            }
        }
        return result;
    }
}
