package com.intership.app.habittracker.service;

import com.intership.app.habittracker.entity.Habit;
import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.resository.HabitRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class HabitService {
    private final HabitRepository habitRepository;

    private final CustomUserService customUserService;


    @Autowired
    public HabitService(HabitRepository habitRepository, CustomUserService customUserService) {
        this.habitRepository = habitRepository;
        this.customUserService = customUserService;
    }

    public Habit create(User user, Habit habit) {
        habit.setRepetitionRate(0);
        habit.setProfile(user);
        return habitRepository.save(habit);
    }

    public List<Habit> getList(User user) {
        return habitRepository.findByProfile(user);
    }

    public Habit update(Habit habitFromDb, Habit habit,User user) {
        if(user.equals(habit.getProfile())){
            habit.setProfile(user);
            BeanUtils.copyProperties(habit,habitFromDb,"id");
            return habitRepository.save(habitFromDb);
        }
        return habitFromDb;
    }

    public void delete(Habit habit,User user) {
        if(user.equals(habit.getProfile())){
            habitRepository.delete(habit);
        }
    }

    public Habit completeReps(Habit habit, Integer reps,User user) {
        if(habit.getProfile().equals(user)){
            LocalDate localDate=LocalDate.now();
            if(habit.getHabitData().get(localDate)!=null){
                reps+=habit.getHabitData().get(localDate);
            }
            habit.getHabitData().put(localDate,reps);
            habit.setRepetitionRate(repsRate(habit,localDate));
            if(localDate.equals(habit.getEndDate())){
                int point= user.getPoint()+100;
                if(habit.getRepetitionRate()==habit.getRepetitionsPerDay())
                    point+=50;
                user.setPoint(point);
                customUserService.setPoint(user);
            }

            return habitRepository.save(habit);
        }
        return habit;
    }

    public int repsRate(Habit habit,LocalDate localDate){
        int size=localDate.getDayOfMonth()-habit.getStartDate().getDayOfMonth();
        int total=totalReps(habit);
        return total/size;
    }

    public int totalReps(Habit habit){
        int total=0;
        for(Map.Entry<LocalDate,Integer> entry:habit.getHabitData().entrySet()){
            total+= entry.getValue();
        }
        return total;
    }
}
