package com.jkramr.demo.service.provider;

import com.jkramr.demo.model.CalendarEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class CalendarEventService {

    /**
     * Emulated repository with indices by start and end date
     */
    private HashMap<Integer, CalendarEvent> events;

    private TreeMap<LocalDateTime, Integer> startTimeIndex;

    private TreeMap<LocalDateTime, Integer> endTimeIndex;

    public CalendarEventService() {
        this.events = new HashMap<>();
        this.startTimeIndex = new TreeMap<>();
        this.endTimeIndex = new TreeMap<>();
    }

    public void registerEvent(CalendarEvent calendarEvent) {
        if (calendarEvent == null) {
            return;
        }

        if (calendarEvent.getId() == null) {
            Integer autoincrement = events.size();

            calendarEvent.setId(autoincrement);
        }

        Integer id = calendarEvent.getId();
        LocalDateTime startTime = calendarEvent.getStartTime();
        LocalDateTime endTime = calendarEvent.getEndTime();

        events.putIfAbsent(id, calendarEvent);


        startTimeIndex.put(startTime, calendarEvent.getId());
        endTimeIndex.put(endTime, calendarEvent.getId());
    }

    public void checkTimes(TreeSet<LocalDateTime> times) {

        System.out.println();
        System.out.println("Checking: ");
        System.out.println();

        times.stream()
                .map(this::checkTime)
                .forEach(System.out::println);

        System.out.println();
    }

    private String checkTime(LocalDateTime time) {
        Map.Entry<LocalDateTime, Integer> floorEntry = startTimeIndex.floorEntry(time);
        Map.Entry<LocalDateTime, Integer> ceilingEntry = endTimeIndex.ceilingEntry(time);

        if (floorEntry != null) {
            CalendarEvent floor = events.get(floorEntry.getValue());

            if (floor.getEndTime().isAfter(time)) {
                return time + "," + floor.getTitle();
            }
        }

        if (ceilingEntry != null) {
            CalendarEvent ceiling = events.get(ceilingEntry.getValue());

            if (ceiling.getStartTime().isBefore(time)) {
                return time + "," + ceiling.getTitle();
            }
        }

        return time + ",Nothing";
    }
}
