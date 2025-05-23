package com.booking.bookingapp.telegram.service;

import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BotServiceImpl implements BotService {
    private final UserRepository userRepository;

    @Value("${reply.save_successful}")
    private String successfulMessage;

    @Value("${reply.save_unsuccessful}")
    private String unsuccessfulMessage;

    @Value("${reply.not_info}")
    private String notFoundRole;

    @Override
    public Role getInfo(Long userTgId) {
        if (userRepository.findUserByTelegramId(userTgId).isPresent()) {
            return userRepository.findUserByTelegramId(userTgId)
                    .get()
                    .getRoles()
                    .iterator()
                    .next();
        }
        return new Role();
    }

    @Override
    public String saveTelegramId(String email, Long userTgId) {
        if (userRepository.findByEmail(email).isPresent()) {
            User user = userRepository.findByEmail(email).get();

            user.setTelegramId(userTgId);
            userRepository.save(user);

            return successfulMessage;
        }
        return unsuccessfulMessage;
    }
}
