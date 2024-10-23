package com.booking.bookingapp.telegram;

import com.booking.bookingapp.telegram.bot.BotState;
import com.booking.bookingapp.telegram.handler.HandlerMessage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class BotStateContext {
    private final Map<BotState, HandlerMessage> messageHandler = new HashMap<>();

    public BotStateContext(List<HandlerMessage> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandler.put(handler.getHandler(), handler));
    }

    public SendMessage processHandler(BotState state, Message message) {
        HandlerMessage inputMessageHandler = findHandler(state);
        return inputMessageHandler.handler(message);
    }

    private HandlerMessage findHandler(BotState state) {
        return messageHandler.get(state);
    }
}
