package test.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Scanner;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import management.HabitManager;
import org.mockito.MockitoAnnotations;
import tracking.HabitTracker;
import entity.User;
import analytics.HabitAnalytics;
import entity.Habit;

class HabitManagerTest {

    private User user;
    private HabitTracker trackerMock;
    private HabitAnalytics analyticsMock;
    private Scanner scannerMock;
    private HabitManager habitManager;

    @BeforeEach
    void setUp() {
        // Инициализация моков
        MockitoAnnotations.openMocks(this);
        scannerMock = Mockito.mock(Scanner.class);
        trackerMock = Mockito.mock(HabitTracker.class);
        analyticsMock = Mockito.mock(HabitAnalytics.class);
        user = new User("testUser", "testPass", "Test", true);
        habitManager = new HabitManager(scannerMock, trackerMock, analyticsMock);
    }

    @Test
    void testCreateHabit() {
        when(scannerMock.nextLine()).thenReturn("Вода").thenReturn("Пить 2 литра").thenReturn("ежедневно");

        habitManager.createHabit(user);

        assertThat(user.getHabits()).hasSize(1);
        Habit createdHabit = user.getHabits().get(0);
        assertThat(createdHabit.getTitle()).isEqualTo("Вода");
        assertThat(createdHabit.getDescription()).isEqualTo("Пить 2 литра");
        assertThat(createdHabit.getFrequency()).isEqualTo("ежедневно");
    }

    @Test
    void testDeleteHabit() {
        // Добавляем привычку в список пользователя
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);
        when(scannerMock.nextInt()).thenReturn(1); // Выбор привычки для удаления

        habitManager.deleteHabit(user);

        assertThat(user.getHabits()).isEmpty();
    }

    @Test
    void testEditHabit() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);

        // Мокаем ввод для выбора привычки (выбор первой привычки в списке)
        when(scannerMock.nextInt()).thenReturn(1);

        when(scannerMock.nextLine())
                .thenReturn("")  // Пропускаем лишний перевод строки
                .thenReturn("Чай") // Название
                .thenReturn("Пить 3 чашки чая") // Описание
                .thenReturn("еженедельно"); // Частота


        habitManager.editHabit(user);

        assertThat(habit.getTitle()).isEqualTo("Чай");
        assertThat(habit.getDescription()).isEqualTo("Пить 3 чашки чая");
        assertThat(habit.getFrequency()).isEqualTo("еженедельно");
    }

    @Test
    void testMarkHabitCompleted() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);
        when(scannerMock.nextInt()).thenReturn(1); // Выбор привычки

        habitManager.markHabitCompleted(user);

        verify(trackerMock, times(1)).markHabitCompleted(habit); // Убедимся, что метод трекера вызван
    }

    @Test
    void testGenerateHabitReport() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);
        when(scannerMock.nextInt()).thenReturn(2); // Выбор отчета за неделю

        habitManager.generateHabitReport(user);

        verify(analyticsMock, times(1)).generateWeeklyReport(habit); // Проверяем, что отчет за неделю вызван единожды
    }
}
