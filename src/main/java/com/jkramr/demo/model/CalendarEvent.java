package com.jkramr.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class CalendarEvent {

    private Integer id;

    @NonNull
    private LocalDateTime startTime;

    @NonNull
    private LocalDateTime endTime;

    @NonNull
    private String title;

    @Override
    public String toString() {
        return startTime + "," + endTime + "," + title;
    }
}
