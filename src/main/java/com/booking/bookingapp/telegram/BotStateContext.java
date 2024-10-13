package com.booking.bookingapp.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotSateContext {
    private Map<BotState, InputMessageHandler> messageHandler = new HashMap<>();

    public BotSateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandler.put(handler.getHandler(), handler));
    }

    public SendMessage processInputMessage(BotState state, Message message) {
        InputMessageHandler inputMessageHandler = findHandler(state);
        return inputMessageHandler.handler(message);
    }

    private InputMessageHandler findHandler(BotState state) {
        return messageHandler.get(state);
    }
}
