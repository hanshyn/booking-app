package com.booking.bookingapp.telegram;

import com.booking.bookingapp.telegram.bot.BotState;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class RegistrationHandler implements InputMessageHandler {
    private TelegramMenu telegramMenu;
    @Override
    public SendMessage handler(Message message) {
        System.out.println("Handler REGISTRATION");
        return new SendMessage(message.getChatId().toString(), message.getText());
    }

    @Override
    public BotState getHandler() {
        return BotState.REGISTRATION;
    }
}
