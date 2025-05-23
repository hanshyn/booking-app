package com.booking.bookingapp.telegram.service;

import static com.booking.bookingapp.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.user.UserRepository;
import java.util.Optional;
import java.util.Set;
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
class UserRoleTest {
    private static final Long CHAT_ID = 1234L;
    private static final String ROLE_ADMIN = "ADMIN";
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRole userRole;

    @Value("${reply.role_not_found}")
    private String roleNotFound;
    private Update update;

    @BeforeEach
    void setup() {
        update = mock(Update.class);

        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(update.getMessage().getChatId()).thenReturn(CHAT_ID);
    }

    @Test
    void userService_UserFoundWithRole() {
        User user = mock(User.class);

        Role role = mock(Role.class);
        role.setRole(ADMIN);

        when(role.getRole()).thenReturn(Role.RoleName.ADMIN);
        when(user.getRoles()).thenReturn(Set.of(role));
        when(userRepository.findUserByTelegramId(CHAT_ID)).thenReturn(Optional.of(user));

        String actual = userRole.userService(update);

        assertEquals(ROLE_ADMIN, actual);
    }

    @Test
    void userService_UserNotFoundRole() {
        when(userRepository.findUserByTelegramId(CHAT_ID)).thenReturn(Optional.empty());

        String actual = userRole.userService(update);

        assertEquals(roleNotFound, actual);
    }
}
