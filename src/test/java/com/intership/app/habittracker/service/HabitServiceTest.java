package com.intership.app.habittracker.service;

import com.intership.app.habittracker.entity.Habit;
import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.exception.AccessDeniedException;
import com.intership.app.habittracker.repository.HabitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @InjectMocks
    private HabitService habitService;

    @Mock
    private HabitRepository habitRepository;

    @Test
    void create() {
        //given
        User user=new User(1l,"Max","asuraitf@mail.ru",true,"123",null,"ru",0,null,null,false);

        Habit habit=new Habit();
        habit.setName("Test");
        habit.setDescription("Junit");
        habit.setRepetitionsPerDay(100);
        habit.setMeasurement("tests");
        habit.setStartDate(LocalDate.now());
        habit.setEndDate(LocalDate.of(2023,6,30));

        //when
        Habit createdHabit=habitService.create(user,habit);

        //then
        Mockito.verify(habitRepository,Mockito.times(1)).save(habit);

    }

    @Test
    void getList() {
        User user=new User(1l,"Max","asuraitf@mail.ru",true,"123",null,"ru",0,null,null,false);


        habitService.getList(user);

        Mockito.verify(habitRepository,Mockito.times(1)).findByProfile(user);

    }

    @Test
    void update() {
        User user=new User(1l,"Max","asuraitf@mail.ru",true,"123",null,"ru",0,null,null,false);

        Habit habitFromDb=new Habit();
        habitFromDb.setProfile(user);
        habitFromDb.setId(1L);
        habitFromDb.setName("Test");
        habitFromDb.setDescription("Junit");
        habitFromDb.setRepetitionsPerDay(100);
        habitFromDb.setMeasurement("tests");
        habitFromDb.setStartDate(LocalDate.now());
        habitFromDb.setEndDate(LocalDate.of(2023,6,30));


        Habit habit=new Habit();
        habit.setProfile(user);
        habit.setId(1L);
        habit.setName("Junit");
        habit.setDescription("Creating some tests");
        habit.setRepetitionsPerDay(100);
        habit.setMeasurement("tests");
        habit.setStartDate(LocalDate.now());
        habit.setEndDate(LocalDate.of(2023,6,30));


        habitService.update(habitFromDb,habit,user);

        assertNotEquals(habitFromDb.getName(),"Test");
        assertEquals(habitFromDb.getName(),habit.getName());
        Mockito.verify(habitRepository,Mockito.times(1)).save(habitFromDb);

    }


    @Test
    void delete() {
        User user=new User(1l,"Max","asuraitf@mail.ru",true,"123",null,"ru",0,null,null,false);

        Habit habit=new Habit();
        habit.setProfile(user);
        habit.setId(1L);
        habit.setName("Test");
        habit.setDescription("Junit");
        habit.setRepetitionsPerDay(100);
        habit.setMeasurement("tests");
        habit.setStartDate(LocalDate.now());
        habit.setEndDate(LocalDate.of(2023,6,30));

        habitService.delete(habit,user);

        Mockito.verify(habitRepository,Mockito.times(1)).delete(habit);
    }

    @Test
    void completeReps() {
        User user=new User(1l,"Max","asuraitf@mail.ru",true,"123",null,"ru",0,null,null,false);

        Habit habit=new Habit();
        habit.setProfile(user);
        habit.setId(1L);
        habit.setName("Test");
        habit.setDescription("Junit");
        habit.setRepetitionsPerDay(100);
        habit.setMeasurement("tests");
        habit.setStartDate(LocalDate.now());
        habit.setEndDate(LocalDate.of(2023,6,30));
        habit.setHabitData(new HashMap<>(){{
            put(LocalDate.now(),100);
        }});

        Integer reps=100;

        habitService.completeReps(habit,reps,user);


        assertNotNull(habit.getHabitData());
        assertEquals(200,habit.getRepetitionRate());
        assertEquals(200,habit.getHabitData().get(LocalDate.now()));
    }
}