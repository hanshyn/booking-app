package com.booking.bookingapp.service.notification;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@PropertySource("classpath:messages.properties")
@RequiredArgsConstructor
@Component
public class TelegramNotificationService extends TelegramLongPollingBot {
    //private static final String EMOJI_HELP = "\ud83c\udd98";
    //private static final String EMOJI_EMAIL = "\uD83D\uDCE7";
    //private static final String HELP = EMOJI_HELP + " Help";
    private static final String HELP = "Help";
    private static final String YOUR_ROLE = "Your role";
    //private static final String USER_NAME = EMOJI_EMAIL + " User name";
    private static final String USER_NAME = " User name";

    //    private final BotInitializer botConfig;
    //@Value("${bot.name}")
    //private String botName;

    //@Value("${bot.token}")
    //private String botToken;

    /*
    private TelegramNotificationService instance;

    @Autowired
    private TelegramNotificationService(@Value("${bot.token}") String botToken) {
        this.botToken = botToken;
    }

    public TelegramNotificationService getInstance() {
        if (instance == null) {
            instance = new TelegramNotificationService(botToken);
        }
        return instance;
    }
    */

    /*public TelegramNotificationService(@Value("${bot.token}") String botToken) {
        super(botToken);
    }

     */

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        /*var user = message.getFrom();
        Long id = user.getId();
        //sendText(id, message.getText());
        copyMessage(id, message.getMessageId());
        System.out.println(update);

        SendMessage response = new SendMessage();
        response.setText("Enter your email for notification");
        response.setChatId(message.getChatId());

        response.setReplyMarkup(getMainMenu());*/
        try {
            execute(getResponseMessage(message));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        System.out.println("User name: " + message.getFrom().getUserName()
                + " ChatID: " + message.getChatId()
                + " userId: " + message.getFrom().getId());
        System.out.println(message.getText());
    }

    @Override
    public String getBotUsername() {
        return "botConfig.getBotName()";
        //return "Booking_H_bot";
        //return botName;
    }

    @Override
    public String getBotToken() {
        return "";
        //return "7155694215:AAFw6HZkrFYhc-LoMgJnp7EKVf-ISn2caY0";
    }

    public void sendText(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())//Who are we sending a message to
                .text(text).build();//Message content
        try {
            execute(message);//Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);//Any error will be printed here
        }
    }

    public void copyMessage(Long who, Integer msgId) {
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())//We copy from the user
                .chatId(who.toString())//And send it back to him
                .messageId(msgId)//Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private ReplyKeyboardMarkup getMainMenu() {
        KeyboardRow row = new KeyboardRow();
        row.add(HELP);
        row.add(YOUR_ROLE);
        row.add(USER_NAME);

        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(row);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(rows);
        return markup;
    }

    private SendMessage getHelpResponse(Message message) {
        SendMessage response = new SendMessage();
        response.setText("Test");
        response.setChatId(message.getChatId());
        response.setReplyMarkup(getMainMenu());
        return response;
    }

    private SendMessage greetingMessage(Message message) {
        SendMessage response = new SendMessage();
        response.setText("Hello, " + message.getFrom().getUserName() + ". You have send me: "
                + message.getText());
        response.setChatId(message.getChatId());
        response.setReplyMarkup(getMainMenu());
        return response;
    }

    private SendMessage getUserName(Message message) {
        SendMessage response = new SendMessage();
        response.setText("Your name Telegram " + message.getFrom().getUserName());
        response.setChatId(message.getChatId());
        response.setReplyMarkup(getMainMenu());
        return response;
    }

    private SendMessage getInfo(Message message) {
        /*
        User user = userRepository.findUserByTelegramId(message.getChatId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't found user by tgId: " + message.getChatId()
                )
        );

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("User name: ").append(user.getEmail()).append("\n")
                .append("User Role: ").append(user.getRole()).append("\n");
        */

        SendMessage response = new SendMessage();
        response.setChatId(message.getChatId());
        response.setText("str");
        response.setReplyMarkup(getMainMenu());
        return response;
    }

    private SendMessage getResponseMessage(Message message) {
        switch (message.getText()) {
            case HELP:
                return getHelpResponse(message);
            case YOUR_ROLE:
                return getInfo(message);
            case USER_NAME:
                return getUserName(message);
            default:
                return greetingMessage(message);
        }
    }
}


