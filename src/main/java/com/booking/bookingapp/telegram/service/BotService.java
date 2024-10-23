package com.booking.bookingapp.telegram.service;

import com.booking.bookingapp.model.Role;

public interface BotService {
    Role getInfo(Long userTgId);

    String saveTelegramId(String email, Long userTgId);
}
