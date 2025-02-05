package com.booking.bookingapp.telegram;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class UserSession {
    private BotState state = BotState.SHOW_MAIN_MANU;
    private final Map<String, Object> data = new HashMap<>();

    public void putData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
