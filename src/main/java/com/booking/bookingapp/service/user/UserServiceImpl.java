package com.booking.bookingapp.service.user;

import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.dto.user.UserUpdateRoleRequestDto;
import com.booking.bookingapp.mapper.UserMapper;
import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.user.RoleRepository;
import com.booking.bookingapp.repository.user.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            System.out.println("Email is DB");
            return null;
        }

        Role role = roleRepository.findRolesById(requestDto.roleId());

        User user = new User();
        user.setEmail(requestDto.email());
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setPassword(requestDto.password());
        user.setRole(Set.of(role));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateRole(UserUpdateRoleRequestDto requestDto) {
        Role role = roleRepository.findRolesById(requestDto.roleId());
        return null;
    }

    @Override
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto) {
        return null;
    }
}
