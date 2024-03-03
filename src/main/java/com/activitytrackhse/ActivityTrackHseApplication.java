package com.activitytrackhse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.activitytrackhse"})
public class ActivityTrackHseApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivityTrackHseApplication.class, args);
    }

}
