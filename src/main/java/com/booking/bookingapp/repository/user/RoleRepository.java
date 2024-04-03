package com.booking.bookingapp.repository.user;

import com.booking.bookingapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRolesById(Long id);

    Role findRoleByRole(Role.RoleName roleName);
}
