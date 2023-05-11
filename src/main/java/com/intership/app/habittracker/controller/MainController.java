package com.intership.app.habittracker.controller;

import com.intership.app.habittracker.entity.Habit;
import com.intership.app.habittracker.entity.User;
import com.intership.app.habittracker.service.CustomUserService;
import com.intership.app.habittracker.service.HabitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {

    @Value("${spring.profiles.active}")
    private String profile;
    private final HabitService habitService;

    private final CustomUserService customUserService;

    @Autowired
    public MainController(HabitService habitService, CustomUserService customUserService) {
        this.habitService = habitService;
        this.customUserService = customUserService;
    }

    @GetMapping
    public String main(Model model, @RequestHeader(name="Authorization") String authorizationHeader){
        if(authorizationHeader!=null){
            User user=customUserService.getFromAuthentication(authorizationHeader);
            List<Habit> habits=habitService.getList(user);
            model.addAttribute("habits",habits);
            model.addAttribute("profile",user);
        }
        else {
            model.addAttribute("habits",new ArrayList<Habit>());
            model.addAttribute("profile",null);
        }
        model.addAttribute("isDevMode","dev".equals(profile));
        return "index";
    }
}
