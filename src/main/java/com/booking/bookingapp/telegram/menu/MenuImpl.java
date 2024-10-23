package com.booking.bookingapp.telegram.menu;

import com.booking.bookingapp.model.Role;
//import com.booking.bookingapp.telegram.bot.BotState;
import com.booking.bookingapp.telegram.service.BotService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@PropertySource("classpath:messages.properties")
@RequiredArgsConstructor
@Component
public class MenuImpl implements Menu {
    private static final String HELP = "Help";
    private static final String INFO = "Info";
    private static final String USER_NAME = "Registration";

    private final BotService botService;

    @Value("${button.help}")
    private String buttonHelp;

    @Value("${button.info}")
    private String buttonInfo;

    @Value("${button.register}")
    private String buttonRegister;

    @Value("${reply.help}")
    private String helpMessage;

    @Value("${reply.info}")
    private String infoMessage;

    @Value("${reply.not_info}")
    private String notFoundRole;

    @Value("${reply.registration}")
    private String registration;

    @Override
    public ReplyKeyboardMarkup keyboardMenu() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(buttonHelp));
        row.add(new KeyboardButton(buttonInfo));
        row.add(new KeyboardButton(buttonRegister));

        List<KeyboardRow> rows = List.of(row);
        markup.setKeyboard(rows);
        return markup;
    }

    @Override
    public SendMessage helpMenu(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(helpMessage)
                .replyMarkup(keyboardMenu())
                .build();
    }

    @Override
    public SendMessage infoMenu(Message message) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Enter to site");
        button.setUrl("http://booking.com/user");
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(button);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(buttonsRow);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(rows);

        Role role = botService.getInfo(message.getChatId());

        if (role.getRole() != null) {
            return SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(infoMessage + role.getRole())
                    .replyMarkup(keyboardMenu())
                    .replyMarkup(markup)
                    .build();
        }

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(notFoundRole)
                .replyMarkup(keyboardMenu())
                .build();
    }

    @Override
    public SendMessage registrationMenu(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(registration)
                .replyMarkup(keyboardMenu())
                .build();
    }

    @Override
    public SendMessage greetingMessage(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Hi, "
                        + message.getFrom().getUserName()
                        + ". You have send me: "
                        + message.getText()
                        + " \nSelect any menu button or wait notification")
                .replyMarkup(keyboardMenu())
                .build();
    }
}
