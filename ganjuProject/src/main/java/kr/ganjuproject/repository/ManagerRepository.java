package kr.ganjuproject.repository;

import kr.ganjuproject.entity.RoleUsers;
import kr.ganjuproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Users, Long> {
    List<Users> findByRole(RoleUsers role);
    Optional<Users> findByUsername(String username);
    Optional<Users> findByProviderAndProviderId(String provider , String providerId);
}
