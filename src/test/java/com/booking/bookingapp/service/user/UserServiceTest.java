package com.booking.bookingapp.service.user;

import static com.booking.bookingapp.model.Role.RoleName.ADMIN;
import static com.booking.bookingapp.model.Role.RoleName.MANAGER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String EMAIL = "test@mail.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String FIRST_NAME = "firstNAme";
    private static final String UPDATE_FIRST_NAME = "updateFirstName";
    private static final String LAST_NAME = "lastName";
    private static final String UPDATE_LAST_NAME = "updateLastName";
    private static final Long VALID_ROLE_MANAGER_ID = 2L;
    private static final Long VALID_ROLE_ADMIN_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_ID = 100L;

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRegistrationRequestDto userRegistrationRequestDto;
    private UserResponseDto userResponseDto;
    private Role role;
    private RoleResponseDto roleDto;

    @BeforeEach
    void setup() {
        role = mock(Role.class);
        role.setRole(Role.RoleName.ADMIN);
        role.setId(VALID_ROLE_ADMIN_ID);

        roleDto = new RoleResponseDto(Role.RoleName.ADMIN);

        userRegistrationRequestDto = mock(UserRegistrationRequestDto.class);
        userRegistrationRequestDto = new UserRegistrationRequestDto(
                EMAIL,
                FIRST_NAME,
                LAST_NAME,
                PASSWORD,
                PASSWORD,
                VALID_ROLE_ADMIN_ID
        );

        userResponseDto = new UserResponseDto(
                VALID_USER_ID,
                EMAIL,
                FIRST_NAME,
                LAST_NAME,
                Set.of(roleDto)
        );

        user = mock(User.class);
        user.setEmail(userRegistrationRequestDto.email());
        user.setFirstName(userRegistrationRequestDto.firstName());
        user.setLastName(userRegistrationRequestDto.lastName());
        user.setPassword(ENCODED_PASSWORD);
        user.setRole(Set.of(role));
    }

    @Test
    @DisplayName("Should registration user successfully")
    void register_ValidEmail_ReturnUserResponseDto() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(roleRepository.findById(userRegistrationRequestDto.roleId()))
                .thenReturn(Optional.of(role));
        when(passwordEncoder.encode(userRegistrationRequestDto.password()))
                .thenReturn(ENCODED_PASSWORD);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto);
        when(userMapper.toDto(any(User.class), anySet())).thenReturn(userResponseDto);

        UserResponseDto actual = userService.register(userRegistrationRequestDto);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(userResponseDto, actual);
        assertEquals(userRegistrationRequestDto.email(), capturedUser.getEmail());
        assertEquals(userRegistrationRequestDto.firstName(), capturedUser.getFirstName());
        assertEquals(userRegistrationRequestDto.lastName(), capturedUser.getLastName());
        assertEquals(ENCODED_PASSWORD, capturedUser.getPassword());
        assertTrue(capturedUser.getRole().contains(role));
    }

    @Test
    @DisplayName("Should throw RegistrationException if email exist")
    void register_EmailExist_ReturnRegistrationException() {
        when(userRepository.findByEmail(userRegistrationRequestDto.email()))
                .thenReturn(Optional.of(user));

        assertThrows(RegistrationException.class,
                () -> userService.register(userRegistrationRequestDto));
    }

    @Test
    @DisplayName("Should throw EntityNotFound if role is not found")
    void register_RoleNotFound_ReturnEntityNotFoundException() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.register(userRegistrationRequestDto));
    }

    @Test
    @DisplayName("Should update user role successfully")
    void updateRole_ValidRole_ReturnUserResponseDto() {
        User userManager = new User();
        userManager.setId(VALID_USER_ID);
        userManager.setEmail(EMAIL);
        userManager.setFirstName(FIRST_NAME);
        userManager.setLastName(LAST_NAME);

        Role managerRole = new Role();
        managerRole.setId(VALID_ROLE_MANAGER_ID);
        managerRole.setRole(MANAGER);

        userManager.setRole(new HashSet<>());

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userManager);

        SecurityContextHolder.setContext(securityContext);

        RoleResponseDto responseDto = new RoleResponseDto(MANAGER);

        UserResponseDto userManagerResponseDto = new UserResponseDto(
                userManager.getId(),
                userManager.getEmail(),
                userManager.getFirstName(),
                userManager.getLastName(),
                Collections.singleton(responseDto)
        );

        when(roleRepository.findById(VALID_ROLE_MANAGER_ID)).thenReturn(Optional.of(managerRole));
        when(userRepository.save(userManager)).thenReturn(userManager);
        when(roleMapper.toDto(managerRole)).thenReturn(responseDto);
        when(userMapper.toDto(userManager, Set.of(responseDto))).thenReturn(userManagerResponseDto);

        UserUpdateRoleRequestDto requestDto = new UserUpdateRoleRequestDto(VALID_ROLE_MANAGER_ID);

        UserResponseDto actual = userService.updateRole(VALID_USER_ID, requestDto);

        assertEquals(userManagerResponseDto, actual);
        verify(roleRepository).findById(VALID_ROLE_MANAGER_ID);
        verify(userRepository).save(userManager);
        verify(userMapper).toDto(userManager, Set.of(responseDto));
    }

    @Test
    @DisplayName("Should throw exception when role not found")
    void updateRole_RoleNotFound_NotOk() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new User());

        SecurityContextHolder.setContext(securityContext);

        UserUpdateRoleRequestDto roleRequestDto = new UserUpdateRoleRequestDto(INVALID_ID);

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateRole(VALID_USER_ID, roleRequestDto));

        verify(roleRepository).findById(INVALID_ID);
        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Should skip update if role is ADMIN")
    void updateRole_RoleIsAdmin_NotOk() {
        User userAdmin = new User();
        userAdmin.setRole(new HashSet<>());

        Role adminRole = new Role();
        adminRole.setId(VALID_ROLE_ADMIN_ID);
        adminRole.setRole(ADMIN);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userAdmin);

        SecurityContextHolder.setContext(securityContext);

        UserUpdateRoleRequestDto roleRequestDto = new UserUpdateRoleRequestDto(VALID_ROLE_ADMIN_ID);

        assertThrows(EntityNotFoundException.class,
                () -> userService.updateRole(VALID_USER_ID, roleRequestDto));

        verifyNoInteractions(userRepository);
    }

    @Test
    @DisplayName("Should return user response dto")
    void getUser_ValidUser_ReturnUserResponseDto() {
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userResponseDto);

        UserResponseDto actual = userService.getUser(authentication);

        assertEquals(userResponseDto, actual);
    }

    @Test
    @DisplayName("Should throw EntityNotFound if user invalid date")
    void getUser_InvalidUser_ReturnEntityNotFoundException() {
        User invalidUser = new User();
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(invalidUser);

        assertThrows(EntityNotFoundException.class,
                () -> userService.getUser(authentication));
    }

    @Test
    void updateUser_ValidRequest_ReturnUserResponseDto() {
        User mockUser = new User();
        mockUser.setId(VALID_USER_ID);
        mockUser.setEmail(EMAIL);
        mockUser.setFirstName("OldFirstName");
        mockUser.setLastName("OldLastName");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUser);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        User updateUser = new User();
        updateUser.setId(VALID_USER_ID);
        updateUser.setEmail(EMAIL);
        updateUser.setFirstName(UPDATE_FIRST_NAME);
        updateUser.setLastName(UPDATE_LAST_NAME);
        updateUser.setRole(Set.of());

        UserResponseDto updateResponseDto = new UserResponseDto(
                VALID_USER_ID,
                EMAIL,
                UPDATE_FIRST_NAME,
                UPDATE_LAST_NAME,
                Set.of()
        );

        when(userRepository.save(any(User.class))).thenReturn(updateUser);
        when(userMapper.toDto(updateUser)).thenReturn(updateResponseDto);

        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto(
                UPDATE_FIRST_NAME,
                UPDATE_LAST_NAME
        );

        UserResponseDto actual = userService.updateUser(updateRequestDto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper).toDto(updateUser);

        assertEquals(updateResponseDto, actual);
    }

    @Test
    void updateUser_InvalidSecurityContext_NotOk() {
        SecurityContextHolder.clearContext();

        UserUpdateRequestDto requestDto = new UserUpdateRequestDto(
                UPDATE_FIRST_NAME,
                UPDATE_LAST_NAME
        );

        assertThrows(NullPointerException.class, () -> userService.updateUser(requestDto));
    }
}
