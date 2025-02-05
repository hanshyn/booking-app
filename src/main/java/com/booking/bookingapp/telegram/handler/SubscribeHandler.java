package com.booking.bookingapp.telegram.handler;

import com.booking.bookingapp.telegram.BotState;
import com.booking.bookingapp.telegram.UserSession;
import com.booking.bookingapp.telegram.service.SubscribeUser;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class SubscribeHandler implements CommandHandler {
    private final SubscribeUser subscribeUser;

    @Value("${reply.enter_email}")
    private String enterEmailText;

    @Override
    public SendMessage handle(Update update, UserSession session) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());

        if (session.getState() == BotState.SUBSCRIBE) {
            message.setText(enterEmailText);
            session.setState(BotState.AWAITING_EMAIL);
        } else if (session.getState() == BotState.AWAITING_EMAIL) {
            if (isValidEmail(update.getMessage().getText())) {
                message.setText(subscribeUser.userService(update));
                session.setState(BotState.SHOW_MAIN_MANU);
            } else {
                message.setText(enterEmailText);
            }
        }

        return message;
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]"
                + "+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
