package com.booking.bookingapp.service.user;

import static com.booking.bookingapp.model.Role.RoleName.ADMIN;

import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.dto.user.UserUpdateRoleRequestDto;
import com.booking.bookingapp.mapper.RoleMapper;
import com.booking.bookingapp.mapper.UserMapper;
import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.user.RoleRepository;
import com.booking.bookingapp.repository.user.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;

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
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRole(Set.of(role));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updateRole(Long id, UserUpdateRoleRequestDto requestDto) {
        //User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find user")
        );

        Role role = roleRepository.findById(requestDto.roleId()).orElseThrow(
                () -> new RuntimeException("")
        );
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        if ((ADMIN != role.getRole())) {
            user.setRole(roleSet);
            userRepository.save(user);
            System.out.println("OK!");
        }

        UserResponseDto userResponseDto = userMapper.toDto(user);
        roleMapper.toDto(role);
        userResponseDto.role().add(roleMapper.toDto(role));

        return userResponseDto;
    }

    @Override
    public UserResponseDto getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new RuntimeException("Can't find user")
        );

        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        user = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new RuntimeException("Can't find user")
        );

        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());

        userRepository.save(user);

        return userMapper.toDto(user);
    }
}
