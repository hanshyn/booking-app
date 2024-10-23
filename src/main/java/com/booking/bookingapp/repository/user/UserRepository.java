package com.booking.bookingapp.repository.user;

import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "role")
    Optional<User> findUserByTelegramId(Long tgId);

    @EntityGraph(attributePaths = "role")
    List<User> findAllByRole_Role(Role.RoleName role);
}
