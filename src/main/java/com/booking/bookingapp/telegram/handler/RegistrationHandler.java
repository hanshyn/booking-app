package com.booking.bookingapp.telegram.handler;

import com.booking.bookingapp.telegram.bot.BotState;
import com.booking.bookingapp.telegram.service.BotService;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class RegistrationHandler implements HandlerMessage {
    private final BotService botService;

    @Value("${reply.not_registration}")
    private String invalidEmail;

    @Override
    public SendMessage handler(Message message) {
        if (isValidEmail(message.getText())) {
            return new SendMessage(message.getChatId().toString(),
                    botService.saveTelegramId(message.getText(), message.getChatId()));
        }

        return new SendMessage(message.getChatId().toString(), invalidEmail);
    }

    @Override
    public BotState getHandler() {
        return BotState.REGISTRATION;
    }

    public boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]"
                + "+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        return Pattern.compile(regex)
                    .matcher(email)
                    .matches();
    }
}
