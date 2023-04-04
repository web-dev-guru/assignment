package com.college.assignment.service.repository;

import com.college.assignment.model.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, Integer> {
}
