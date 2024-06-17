package com.portfolio.reservation.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDateTimeDto {

    private LocalDate date;

    private LocalTime time;

    private Integer persons;
}

