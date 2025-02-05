package com.booking.bookingapp.telegram.handler;

import com.booking.bookingapp.telegram.UserSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ShowBotInfoHandler implements CommandHandler {
    @Value("${reply.info_bot}")
    private String textAboutBot;

    @Override
    public SendMessage handle(Update update, UserSession session) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(textAboutBot);
        return message;
    }
}
