package com.booking.bookingapp.service.user;

import static com.booking.bookingapp.model.Role.RoleName.ADMIN;

import com.booking.bookingapp.dto.user.RoleResponseDto;
import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.dto.user.UserUpdateRoleRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.exception.RegistrationException;
import com.booking.bookingapp.mapper.RoleMapper;
import com.booking.bookingapp.mapper.UserMapper;
import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.user.RoleRepository;
import com.booking.bookingapp.repository.user.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
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
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new RegistrationException("Can't register user, the email address exists DB");
        }

        Role role = roleRepository.findById(requestDto.roleId()).orElseThrow(
                () -> new EntityNotFoundException("Can't found role by id: " + requestDto.roleId())
        );

        User user = new User();
        user.setEmail(requestDto.email());
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        user.setRoles(Set.of(role));

        userRepository.save(user);

        Set<RoleResponseDto> roleResponseDtos = user.getRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());

        return userMapper.toDto(user, roleResponseDtos);
    }

    @Override
    public UserResponseDto updateRole(Long id, UserUpdateRoleRequestDto requestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Role role = roleRepository.findById(requestDto.roleId()).orElseThrow(
                () -> new EntityNotFoundException("Can't found role by id: " + requestDto.roleId())
        );

        if ((ADMIN != role.getRole())) {
            user.setRoles(Set.of(role));
            userRepository.save(user);
        }

        Set<RoleResponseDto> roleResponseDtos = user.getRoles().stream()
                .map(roleMapper::toDto)
                .collect(Collectors.toSet());

        return userMapper.toDto(user, roleResponseDtos);
    }

    @Override
    public UserResponseDto getUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by email")
        );

        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setFirstName(requestDto.firstName());
        user.setLastName(requestDto.lastName());

        userRepository.save(user);

        return userMapper.toDto(user);
    }
}
