package com.booking.bookingapp.telegram.handler;

import com.booking.bookingapp.telegram.UserSession;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {
    SendMessage handle(Update update, UserSession session);
}
