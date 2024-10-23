package com.booking.bookingapp.telegram.handler;

import com.booking.bookingapp.telegram.bot.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface HandlerMessage {
    SendMessage handler(Message message);

    BotState getHandler();
}
