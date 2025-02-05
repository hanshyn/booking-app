package com.booking.bookingapp.telegram.handler;

import com.booking.bookingapp.telegram.UserSession;
import com.booking.bookingapp.telegram.service.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class HowUserRoleHandlerImpl implements CommandHandler {
    private final UserRole userRole;

    @Value("${reply.your_role}")
    private String yourRole;

    @Override
    public SendMessage handle(Update update, UserSession session) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(yourRole + userRole.userService(update))
                .build();
    }
}
