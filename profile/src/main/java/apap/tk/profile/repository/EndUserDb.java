package apap.tk.profile.repository;

import apap.tk.profile.model.EndUser;
import apap.tk.profile.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EndUserDb extends JpaRepository<EndUser, UUID> {
    EndUser findByEmail(String email);
    EndUser findByUsername(String username);
    List<EndUser> findAllByRole(Role role);

} 