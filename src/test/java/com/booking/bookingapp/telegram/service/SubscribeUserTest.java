package com.booking.bookingapp.telegram.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@ExtendWith(MockitoExtension.class)
class SubscribeUserTest {
    private static final String VALID_EMAIL = "valid@mail.com";
    private static final Long TELEGRAM_ID = 1234L;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubscribeUser subscribeUser;

    @Value("${reply.subscribe_success}")
    private String messageSubscribe;

    @Value("${reply.subscribe_unsuccessful}")
    private String messageUnsubscribe;

    private Update update;
    private User user;
    private Message message;

    @BeforeEach
    void setup() {
        update = mock(Update.class);
        user = mock(User.class);
        message = mock(Message.class);

        when(update.getMessage()).thenReturn(message);
        when(update.getMessage().getText()).thenReturn(VALID_EMAIL);
        when(message.getChatId()).thenReturn(TELEGRAM_ID);
        lenient().when(update.getMessage().getChatId()).thenReturn(TELEGRAM_ID);
    }

    @Test
    void userService_subscribeUser() {

        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        String actual = subscribeUser.userService(update);

        assertEquals(messageSubscribe, actual);

        verify(user).setTelegramId(TELEGRAM_ID);
        verify(userRepository).save(user);
    }

    @Test
    void userService_NotFoundEmail() {
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());

        String actual = subscribeUser.userService(update);

        assertEquals(messageUnsubscribe, actual);

        verify(userRepository, never()).save(any(User.class));
    }
}
