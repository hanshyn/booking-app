package com.booking.bookingapp.service.user;

import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.mapper.UserMapper;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            System.out.println("Email is DB");
            return null;
        }

        User user = new User();
        user.setEmail(requestDto.email());
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setPassword(requestDto.password());

        return userMapper.toDto(userRepository.save(user));
    }
}
