package org.voetsky.dispatcherBot.repository.viewEntities.AdminUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepo extends JpaRepository<AdminUser, Long> {
    AdminUser findAdminUserByUsername(String username);
}
