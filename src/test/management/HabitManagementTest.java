package test.management;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Scanner;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import application.usecase.HabitManager;
import org.mockito.MockitoAnnotations;
import tracking.HabitTracker;
import core.model.User;
import analytics.HabitAnalytics;
import core.model.Habit;

class HabitManagerTest {

    private User user;
    private HabitTracker trackerMock;
    private HabitAnalytics analyticsMock;
    private Scanner scannerMock;
    private HabitManager habitManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scannerMock = Mockito.mock(Scanner.class);
        trackerMock = Mockito.mock(HabitTracker.class);
        analyticsMock = Mockito.mock(HabitAnalytics.class);
        user = new User("testUser", "testPass", "Test", true);
        habitManager = new HabitManager(scannerMock, trackerMock, analyticsMock);
    }

    @Test
    @DisplayName("Создание привычки")
    void testCreateHabit() {
        when(scannerMock.nextLine()).thenReturn("Вода").thenReturn("Пить 2 литра").thenReturn("1");

        habitManager.createHabit(user);

        assertThat(user.getHabits()).hasSize(1);
        Habit createdHabit = user.getHabits().get(0);
        assertThat(createdHabit.getTitle()).isEqualTo("Вода");
        assertThat(createdHabit.getDescription()).isEqualTo("Пить 2 литра");
        assertThat(createdHabit.getFrequency()).isEqualTo("ежедневно");
    }

    @Test
    @DisplayName("Удаление привычки")
    void testDeleteHabit() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);
        when(scannerMock.nextInt()).thenReturn(1);

        habitManager.deleteHabit(user);

        assertThat(user.getHabits()).isEmpty();
    }

    @Test
    @DisplayName("Изменение привычки")
    void testEditHabit() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);

        when(scannerMock.nextInt()).thenReturn(1);

        when(scannerMock.nextLine())
                .thenReturn("")
                .thenReturn("Чай")
                .thenReturn("Пить 3 чашки чая")
                .thenReturn("2");


        habitManager.editHabit(user);

        assertThat(habit.getTitle()).isEqualTo("Чай");
        assertThat(habit.getDescription()).isEqualTo("Пить 3 чашки чая");
        assertThat(habit.getFrequency()).isEqualTo("еженедельно");
    }

    @Test
    @DisplayName("Отметка о выполнении привычки")
    void testMarkHabitCompleted() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);
        when(scannerMock.nextInt()).thenReturn(1);

        habitManager.markHabitCompleted(user);

        verify(trackerMock, times(1)).markHabitCompleted(habit);
    }

    @Test
    @DisplayName("Создание отчета за определенный период")
    void testGenerateHabitReport() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);
        when(scannerMock.nextInt()).thenReturn(2);

        habitManager.generateHabitReport(user);

        verify(analyticsMock, times(1)).generateWeeklyReport(habit);
    }
}
