package com.booking.bookingapp.telegram;

import static com.booking.bookingapp.telegram.BotState.AWAITING_EMAIL;
import static com.booking.bookingapp.telegram.BotState.SHOW_BOT_INFO;
import static com.booking.bookingapp.telegram.BotState.SHOW_MAIN_MANU;
import static com.booking.bookingapp.telegram.BotState.SHOW_USER_INFO;
import static com.booking.bookingapp.telegram.BotState.SUBSCRIBE;

import com.booking.bookingapp.telegram.handler.CommandHandler;
import com.booking.bookingapp.telegram.handler.HowUserRoleHandlerImpl;
import com.booking.bookingapp.telegram.handler.MainManuHandler;
import com.booking.bookingapp.telegram.handler.ShowBotInfoHandler;
import com.booking.bookingapp.telegram.handler.SubscribeHandler;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BotStateContext {
    private static final String START = "/start";

    private final MainManuHandler mainManuHandler;
    private final ShowBotInfoHandler showBotInfoHandler;
    private final SubscribeHandler subscribeHandler;
    private final HowUserRoleHandlerImpl howUserRoleHandler;
    private final Map<BotState, CommandHandler> handlers = new HashMap<>();
    private final Map<String, BotState> commandToStateMap = new HashMap<>();

    @Value("${button.help}")
    private String buttonInfo;

    @Value("${button.info}")
    private String buttonInfoUser;

    @Value("${button.register}")
    private String buttonSubscribe;

    @PostConstruct
    public void init() {
        handlers.put(SHOW_MAIN_MANU, mainManuHandler);
        handlers.put(SHOW_BOT_INFO, showBotInfoHandler);
        handlers.put(SHOW_USER_INFO, howUserRoleHandler);
        handlers.put(SUBSCRIBE, subscribeHandler);
        handlers.put(AWAITING_EMAIL, subscribeHandler);

        commandToStateMap.put(START, SHOW_MAIN_MANU);
        commandToStateMap.put(buttonInfo, SHOW_BOT_INFO);
        commandToStateMap.put(buttonInfoUser, SHOW_USER_INFO);
        commandToStateMap.put(buttonSubscribe, SUBSCRIBE);
        //commandToStateMap.put(buttonSubscribe, AWAITING_EMAIL);
    }

    public BotState getOrKeepCurrentState(String command, BotState currentState) {
        return commandToStateMap.getOrDefault(command, currentState);
    }

    public CommandHandler getHandler(BotState state) {
        return handlers.getOrDefault(state, mainManuHandler);
    }
}
