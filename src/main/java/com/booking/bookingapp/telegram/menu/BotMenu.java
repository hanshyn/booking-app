package com.booking.bookingapp.telegram.menu;

import com.booking.bookingapp.telegram.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@PropertySource("classpath:messages.properties")
@RequiredArgsConstructor
@Component
public class BotMenu {
    private static final String HELP = "Help";
    private static final String YOUR_ROLE = "Info";
    private static final String USER_NAME = "Registration";

    private final BotService botService;

    @Value("${reply.help}")
    private String helpMessage;

    @Value("${reply.info}")
    private String infoMessage;

    /*public ReplyKeyboardMarkup getMainMenu() {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton(HELP));
        row.add(new KeyboardButton(YOUR_ROLE));
        row.add(new KeyboardButton(USER_NAME));

        List<KeyboardRow> rows = List.of(row);
        markup.setKeyboard(rows);
        return markup;
    }*/

    public SendMessage getResponseMessage(Message message) {
        switch (message.getText()) {
            case HELP:
                return getHelpResponse(message);
            case YOUR_ROLE:
                return getInfo(message);
            case USER_NAME:
                return getRegistration(message);
            default:
                return greetingMessage(message);
        }
    }

    private SendMessage greetingMessage(Message message) {
        /*SendMessage response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setText("Hi, "
                + message.getFrom().getUserName()
                + ". You have send me: "
                + message.getText());
        response.setReplyMarkup(getMainMenu());
        return response;
        */
        return null;
    }

    private SendMessage getHelpResponse(Message message) {
        /*SendMessage response = new SendMessage();
        response.setText(helpMessage);
        response.setChatId(message.getChatId());
        response.setReplyMarkup(getMainMenu());
        return response;
        */
        return null;
    }

    private SendMessage getInfo(Message message) {
        /*String role = botService.getInfo(message.getChatId());

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(infoMessage + role)
                .replyMarkup(getMainMenu())
                .build();
                */
        return null;
    }

    private SendMessage getRegistration(Message message) {
        /*@Email
        String email;
        email = message.getText();
        String str = service.saveTgId(email, message.getChatId());*/
        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId());

        response.setText("Enter your Email (username from BookingApp.com)");
        //response.setReplyMarkup(inlineKeyboardMarkup());

        return response;
    }

    /*public InlineKeyboardMarkup inlineKeyboardMarkup() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Test inline button");
        inlineKeyboardButton.setCallbackData("callback_inline");
        inlineKeyboardButton.setText("Test set text");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(inlineKeyboardButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(rowInline);
        markupInline.setKeyboard(rowList);

        return markupInline;
    }*/
}
