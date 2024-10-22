package core.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class Habit {
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String frequency;  // "ежедневно", "еженедельно"
    private List<HabitLog> logs = new ArrayList<>();

    public void clearLogs() {
        logs.clear();
    }

    public void addLog(HabitLog log) {
        logs.add(log);
    }
}
