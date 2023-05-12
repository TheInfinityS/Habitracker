package com.intership.app.habittracker.repository;

import com.intership.app.habittracker.entity.Habit;
import com.intership.app.habittracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit,Long> {
    List<Habit> findByProfile(User profile);
}
