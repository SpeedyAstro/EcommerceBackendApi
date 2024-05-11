package in.astro.repository;

import in.astro.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface RoleRepo extends JpaRepository<Role, Long> {

//    find by role name
    @Query("SELECT r FROM Role r WHERE r.roleName = ?1")
    Optional<Role> findByRoleName(String roleName);
}
