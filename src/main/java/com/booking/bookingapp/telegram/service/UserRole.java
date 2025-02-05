package com.booking.bookingapp.telegram.service;

import com.booking.bookingapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class UserRole implements UserService {
    private final UserRepository userRepository;

    @Value("${reply.role_not_found}")
    private String roleNotFound;

    @Override
    public String userService(Update update) {
        return userRepository.findUserByTelegramId(update.getMessage().getChatId())
                .map(user -> user.getRole().iterator().next().getRole().toString())
                .orElse(roleNotFound);
    }
}

