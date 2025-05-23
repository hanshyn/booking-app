package com.booking.bookingapp.telegram.handler;

import com.booking.bookingapp.telegram.BotState;
import com.booking.bookingapp.telegram.UserSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@PropertySource("classpath:message.properties")
@Component
public class MainManuHandler implements CommandHandler {
    @Value("${button.help}")
    private String buttonHelp;

    @Value("${button.info}")
    private String buttonInfo;

    @Value("${button.register:red}")
    private String buttonRegister;

    @Override
    public SendMessage handle(Update update, UserSession session) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText(update.getMessage().getText());

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(buttonHelp));
        row.add(new KeyboardButton(buttonInfo));
        row.add(new KeyboardButton(buttonRegister));

        List<KeyboardRow> rows = List.of(row);
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        session.setState(BotState.SHOW_MAIN_MANU);

        return message;
    }
}
