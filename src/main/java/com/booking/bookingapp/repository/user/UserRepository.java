package com.booking.bookingapp.repository.user;

import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findUserByTelegramId(Long tgId);

    @EntityGraph(attributePaths = "roles")
    List<User> findAllByRoles_Role(Role.RoleName roleName);
}
