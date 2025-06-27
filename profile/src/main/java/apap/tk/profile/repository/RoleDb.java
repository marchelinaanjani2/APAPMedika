package apap.tk.profile.repository;

import apap.tk.profile.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDb extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
