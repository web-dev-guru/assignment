package com.college.assignment.model;
import jakarta.persistence.*;
import lombok.Data;
import static jakarta.persistence.GenerationType.IDENTITY;
@Entity
@Table(name = "TRAINSCHEDULE")
@Data
public class TrainSchedule {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @Column
    private String line;
    @Column
    private Integer departure;
    @Column
    private Integer arrival;
}
