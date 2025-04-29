package com.booking.bookingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class BookingApplication {

    public static void main(String[] args) throws TelegramApiException {
        SpringApplication.run(BookingApplication.class, args);
    }
}
