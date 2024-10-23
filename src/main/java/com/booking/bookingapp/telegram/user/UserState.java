package com.booking.bookingapp.telegram.user;

import com.booking.bookingapp.telegram.bot.BotState;

public interface UserState {
    void setUserState(Long userId, BotState botState);

    BotState getUserState(Long userId);
}
