package test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Scanner;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import application.usecase.HabitServiceImp;
import org.mockito.MockitoAnnotations;
import core.model.User;
import analytics.HabitAnalytics;
import core.model.Habit;

class HabitServiceTest {

    private User user;
    private HabitAnalytics analyticsMock;
    private Scanner scannerMock;
    private HabitServiceImp habitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scannerMock = Mockito.mock(Scanner.class);
        analyticsMock = Mockito.mock(HabitAnalytics.class);
        user = new User("testUser", "testPass", "Test", true);
        habitService = Mockito.spy(new HabitServiceImp(scannerMock, analyticsMock));
    }

    @Test
    @DisplayName("Создание привычки")
    void testCreateHabit() {
        habitService.createHabit(user, "Вода", "Пить 2 литра", "ежедневно");
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

        habitService.deleteHabit(user, habit);

        assertThat(user.getHabits()).isEmpty();
    }

    @Test
    @DisplayName("Изменение привычки")
    void testEditHabit() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);

        when(scannerMock.nextInt()).thenReturn(1);

        habitService.editHabit(habit, "Чай", "Пить 3 чашки чая", "еженедельно");

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

        habitService.markHabitCompleted(habit);

        verify(habitService, times(1)).markHabitCompleted(habit);
    }

    @Test
    @DisplayName("Создание отчета за определенный период")
    void testGenerateHabitReport() {
        Habit habit = new Habit("Вода", "Пить 2 литра", "ежедневно");
        user.addHabit(habit);
        when(scannerMock.nextInt()).thenReturn(2);

        habitService.generateHabitReport(user);

        verify(analyticsMock, times(1)).generateWeeklyReport(habit);
    }
}
