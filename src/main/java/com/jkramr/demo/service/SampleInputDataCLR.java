package com.jkramr.demo.service;

import com.jkramr.demo.model.CalendarEvent;
import com.jkramr.demo.service.provider.CalendarEventService;
import com.jkramr.demo.util.SimpleFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

@Component
class SampleInputDataCLR implements CommandLineRunner {

    private CalendarEventService calendarEventService;

    @Value("${path:null}")
    private String filePath;

    @Autowired
    SampleInputDataCLR(
            CalendarEventService calendarEventService
    ) {
        this.calendarEventService = calendarEventService;
    }

    @Override
    public void run(String... args) throws Exception {
        SimpleFileReader ratingsFileReader = readFile();

        TreeSet<LocalDateTime> times = new TreeSet<>();

        Arrays.stream(ratingsFileReader.readFile()
                .split("\n"))
                .forEach(movieEntry -> {
                    CalendarEvent calendarEvent = parseEventFromString(movieEntry, times::add);

                    calendarEventService.registerEvent(calendarEvent);
                });

        // publish sample data slice
        calendarEventService.checkTimes(times);
    }


    private CalendarEvent parseEventFromString(String calendarEntry, Consumer<LocalDateTime> times) {

        try {
            String[] split = calendarEntry.split(",");

            if (split.length == 1) {
                LocalDateTime time = LocalDateTime.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                times.accept(time);
                return null;
            }

            LocalDateTime from = LocalDateTime.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime to = LocalDateTime.parse(split[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String title = split[2];

            return new CalendarEvent(from, to, title);
        } catch (Exception ignored) {
            return null;
        }
    }

    private SimpleFileReader readFile() {

        String path = filePath;

        try {
            return new SimpleFileReader(new FileReader(new File(path)));
        } catch (FileNotFoundException ignored) {
        }

        return new SimpleFileReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("1_calendar.txt")));
    }
}
