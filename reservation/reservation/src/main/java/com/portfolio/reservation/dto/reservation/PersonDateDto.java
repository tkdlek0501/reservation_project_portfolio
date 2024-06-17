package com.portfolio.reservation.dto.reservation;

import com.portfolio.reservation.domain.timetable.DateTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDateDto {

    private DateTable dateTable;

    private Integer persons;
}
