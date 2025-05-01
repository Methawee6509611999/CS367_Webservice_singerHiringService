package dev.methawee.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    
    List<Schedule> findBySingerId(Long singerId);



}


