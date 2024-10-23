package com.booking.bookingapp.telegram.init;

import com.booking.bookingapp.telegram.TelegramBotBookingApp;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {
    private final TelegramBotBookingApp telegramBotBookingApp;

    public BotInitializer(TelegramBotBookingApp bot) {
        this.telegramBotBookingApp = bot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotBookingApp);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
