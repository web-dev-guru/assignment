package com.college.assignment.controller;

import com.college.assignment.model.TrainSchedule;
import com.college.assignment.service.TrainScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/schedule")
@RequiredArgsConstructor
@Validated
public class TrainScheduleController {
    @Autowired
    private final TrainScheduleService trainScheduleService;

    @GetMapping()
    public List<TrainSchedule> getEntireSchedule()
    {
        return trainScheduleService.getFullSchedule();
    }


    @GetMapping("/{line}")
    public ResponseEntity<List<TrainSchedule>> getScheduleByLineAndDeparture(@PathVariable String line, @RequestParam(required = false) String departure) {
        final Integer departureTime ;
        Integer deferDepartureTime;
        if (departure != null) {
            if(departure.matches("\\d+")){
                deferDepartureTime =  Integer.parseInt(departure);
                if (deferDepartureTime >= 0 && deferDepartureTime <= 2359 && deferDepartureTime % 100 < 60) {
                    //valid time format
                } else {
                    return ResponseEntity.badRequest().build();
                }
            }else{
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
                    LocalTime localTime = LocalTime.parse(departure.toUpperCase(), formatter);
                    deferDepartureTime = localTime.getHour() * 100 + localTime.getMinute();
                } catch (DateTimeParseException e) {
                    return ResponseEntity.badRequest().build();
                }
            }

        }else {
            deferDepartureTime = null;
        }
        departureTime = deferDepartureTime;
        List<TrainSchedule> allTrains = trainScheduleService.getFullSchedule();
        List<TrainSchedule> filteredTrains = allTrains.stream()
                .filter(train -> train.getLine().toUpperCase().equals(line.toUpperCase()))
                .filter(train -> departureTime == null || train.getDeparture().equals(departureTime) )
                .collect(Collectors.toList());
        if (filteredTrains.isEmpty()) {
            // Check if the line exists
            if (allTrains.stream().anyMatch(train -> train.getLine().equals(line))) {
                // The line exists but the departure time does not
                return ResponseEntity.ok(Collections.emptyList());
            } else {
                // The line does not exist
                return ResponseEntity.notFound().build();
            }
        } else {
            // Return the filtered trains
            return ResponseEntity.ok(filteredTrains);
        }

    }
}
