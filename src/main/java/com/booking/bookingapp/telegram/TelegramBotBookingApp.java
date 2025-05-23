package com.booking.bookingapp.telegram;

import com.booking.bookingapp.exception.TelegramBotException;
import com.booking.bookingapp.telegram.handler.CommandHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramBotBookingApp extends TelegramLongPollingBot {
    private static final String INLINE_BUTTON_DETAILS = "Details";

    private final Map<Long, UserSession> sessions = new HashMap<>();
    private final BotStateContext botStateContext;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Override
    public void onUpdateReceived(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        UserSession session = sessions.computeIfAbsent(userId, id -> new UserSession());

        log.debug("UserId: {} received update: {} sessions: {} session: {}",
                userId, update, sessions.get(userId), session.getState());

        BotState newState = botStateContext.getOrKeepCurrentState(update.getMessage().getText(),
                session.getState());

        session.setState(newState);

        CommandHandler handler = botStateContext.getHandler(newState);
        SendMessage message = handler.handle(update, session);
        log.debug("Session: {}", session.getState());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramBotException(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendNotification(Long chatId, String text, String url) {
        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(
                                        InlineKeyboardButton.builder()
                                                .text(INLINE_BUTTON_DETAILS)
                                                .url(url)
                                                .build()
                                )
                        )
                )
                .build();

        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(markup)
                    .build());
        } catch (TelegramApiException e) {
            throw new TelegramBotException(e.getMessage());
        }
    }

    public void sendNotification(Long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            throw new TelegramBotException(e.getMessage());
        }
    }

    public void sendMassage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new TelegramBotException(e.getMessage());
        }
    }
}
