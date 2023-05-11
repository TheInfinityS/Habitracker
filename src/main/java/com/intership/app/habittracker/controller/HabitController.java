package com.intership.app.habittracker.controller;

import com.intership.app.habittracker.entity.Habit;
import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.service.CustomUserService;
import com.intership.app.habittracker.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habit")
public class HabitController {
    private final HabitService habitService;
    private final CustomUserService customUserService;

    @Autowired
    public HabitController(HabitService habitService, CustomUserService customUserService) {
        this.habitService = habitService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public List<Habit> getList(@RequestHeader(name="Authorization") String authorizationHeader){
        User user=customUserService.getFromAuthentication(authorizationHeader);
        return habitService.getList(user);
    }

    @PostMapping
    public Habit create(@RequestHeader(name="Authorization") String authorizationHeader,@RequestBody Habit habit){
        User user=customUserService.getFromAuthentication(authorizationHeader);
        return habitService.create(user,habit);
    }

    @PutMapping("{id}")
    public Habit update(@PathVariable("id") Habit habitFromDb,
                        @RequestBody Habit habit,
                        @RequestHeader(name="Authorization") String authorizationHeader){
        User user=customUserService.getFromAuthentication(authorizationHeader);
        return habitService.update(habitFromDb,habit,user);
    }

    @PostMapping("updateCompleteReps/{id}")
    public Habit completeReps(@PathVariable("id") Habit habit,
                              @RequestBody Integer reps,
                              @RequestHeader(name="Authorization") String authorizationHeader){
        User user=customUserService.getFromAuthentication(authorizationHeader);
        return habitService.completeReps(habit,reps,user);
    }


    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Habit habit,@RequestHeader(name="Authorization") String authorizationHeader){
        User user=customUserService.getFromAuthentication(authorizationHeader);
        habitService.delete(habit,user);
    }


}
