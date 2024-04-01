package kr.ganjuproject.repository;

import kr.ganjuproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Users, Long> {
}
