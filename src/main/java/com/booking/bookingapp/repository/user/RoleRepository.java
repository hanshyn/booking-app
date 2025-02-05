package com.booking.bookingapp.repository.user;

import com.booking.bookingapp.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRolesById(Long id);

    Optional<Role> findRoleByRole(Role.RoleName roleName);
}
