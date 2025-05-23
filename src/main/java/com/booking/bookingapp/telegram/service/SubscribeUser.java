package com.booking.bookingapp.telegram.service;

import com.booking.bookingapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
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
        log.info("Received user message {}", update.getMessage().getText());
        return userRepository.findByEmail(update.getMessage().getText())
                .map(user -> {
                    user.setTelegramId(update.getMessage().getChatId());
                    userRepository.save(user);
                    return sub;
                }).orElse(unsub);
    }
}

