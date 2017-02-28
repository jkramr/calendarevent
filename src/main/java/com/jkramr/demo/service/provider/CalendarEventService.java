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

    public Collection<CalendarEvent> getAllEvents() {
        return events.values();
    }

    public void checkTimes(TreeSet<LocalDateTime> times) {

        System.out.println();
        System.out.println("Checking: ");
        System.out.println();
        times.forEach(this::checkTime);
        System.out.println();
    }

    private void checkTime(LocalDateTime time) {
        Integer closestEventByStart = startTimeIndex.floorEntry(time).getValue();
        Integer closestEventByEnd = endTimeIndex.ceilingEntry(time).getValue();

        CalendarEvent floor = events.get(closestEventByStart);
        CalendarEvent ceiling = events.get(closestEventByEnd);

        if (floor.getEndTime().isAfter(time)) {
            System.out.println(time + "," + floor.getTitle());
        } else if (ceiling.getStartTime().isBefore(time)) {
            System.out.println(time + "," + ceiling.getTitle());
        } else {
            System.out.println(time + ",Nothing");
        }
    }
}
