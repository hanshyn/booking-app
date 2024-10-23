package com.booking.bookingapp.telegram;

import com.booking.bookingapp.telegram.bot.BotState;
import com.booking.bookingapp.telegram.menu.Menu;
import com.booking.bookingapp.telegram.user.UserState;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Component
public class TelegramBotBookingApp extends TelegramLongPollingBot {
    private static final String START = "/start";
    /*private static final String HELP = "Help";
    private static final String INFO = "Info";
    private static final String REGISTRATION = "Registration";*/
    private static final String INLINE_BUTTON_DETAILS = "Details";

    private final UserState userState;
    private final BotStateContext botStateContext;
    private final Menu menu;

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    @Value("${button.help}")
    private String buttonHelp;

    @Value("${button.info}")
    private String buttonInfo;

    @Value("${button.register}")
    private String buttonRegister;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = new Message();

        if (update.hasMessage() && update.getMessage().hasText()) {
            message = update.getMessage();
            if (userState.getUserState(message.getChatId()).equals(BotState.REGISTRATION)
                    && message.hasText()) {
                SendMessage messageSend = botStateContext.processHandler(
                        userState.getUserState(message.getChatId()), message);

                sendMassage(messageSend);

                userState.setUserState(message.getChatId(), BotState.START);
                return;
            }

            if (message.getText().startsWith(START)) {
                userState.setUserState(message.getChatId(), BotState.START);
            }

            if (message.getText().startsWith(buttonHelp)) {
                userState.setUserState(message.getChatId(), BotState.HELP);
            }

            if (message.getText().startsWith(buttonInfo)) {
                userState.setUserState(message.getChatId(), BotState.INFO);
            }

            if (message.getText().startsWith(buttonRegister)) {
                userState.setUserState(message.getChatId(), BotState.REGISTRATION);
            }
        }
        try {
            execute(getResponseMessage(message));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    public void sendNotification(Long chatId, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMassage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public SendMessage getResponseMessage(Message message) {
        if (message.getText().equals(buttonHelp)) {
            return menu.helpMenu(message);
        } else if (message.getText().equals(buttonInfo)) {
            return menu.infoMenu(message);
        } else if (message.getText().equals(buttonRegister)) {
            return menu.registrationMenu(message);
        } else {
            return menu.greetingMessage(message);
        }

        /*return switch (message.getText()) {
            case buttonHelp -> menu.helpMenu(message);
            case buttonInfo -> menu.infoMenu(message);
            case buttonRegister -> menu.registrationMenu(message);
            default -> menu.greetingMessage(message);
        };*/
    }
}
