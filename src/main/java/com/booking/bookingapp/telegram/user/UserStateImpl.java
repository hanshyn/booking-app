package com.booking.bookingapp.telegram.user;

import com.booking.bookingapp.telegram.bot.BotState;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UserStateImpl implements UserState {
    private final Map<Long, BotState> userState = new HashMap<>();

    @Override
    public void setUserState(Long userId, BotState botState) {
        userState.put(userId, botState);
    }

    @Override
    public BotState getUserState(Long userId) {
        BotState botState = userState.get(userId);
        if (botState == null) {
            return BotState.START;
        }

        return botState;
    }
}
