package com.college.assignment.service;

import com.college.assignment.model.TrainSchedule;
import com.college.assignment.service.repository.TrainScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainScheduleService {
    @Autowired
    private final TrainScheduleRepository trainScheduleRepository;
    @Cacheable(cacheNames = "fullSchedule")
    public List<TrainSchedule> getFullSchedule() {
        return trainScheduleRepository.findAll();
    }
}
