package com.booking.bookingapp.telegram.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserService {
    String userService(Update update);
}
