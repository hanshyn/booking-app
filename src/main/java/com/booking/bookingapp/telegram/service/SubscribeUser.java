package com.booking.bookingapp.telegram.service;

import com.booking.bookingapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class SubscribeUser implements UserService {
    private final UserRepository userRepository;

    @Value("${reply.subscribe_success}")
    private String sub;

    @Value("${reply.subscribe_unsuccessful}")
    private String unsub;

    @Override
    public String userService(Update update) {
        System.out.printf("User messsage received: %s\n", update.getMessage().getText());
        return userRepository.findByEmail(update.getMessage().getText())
                .map(user -> {
                    user.setTelegramId(update.getMessage().getChatId());
                    userRepository.save(user);
                    return sub;
                }).orElse(unsub);
    }
}

