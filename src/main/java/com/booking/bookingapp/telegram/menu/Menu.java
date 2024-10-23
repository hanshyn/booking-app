package com.booking.bookingapp.telegram.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

public interface Menu {
    ReplyKeyboardMarkup keyboardMenu();

    SendMessage helpMenu(Message message);

    SendMessage infoMenu(Message message);

    SendMessage registrationMenu(Message message);

    SendMessage greetingMessage(Message message);
}
