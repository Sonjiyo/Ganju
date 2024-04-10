package kr.ganjuproject.service;

import kr.ganjuproject.entity.Users;
import kr.ganjuproject.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ganjuproject.entity.RoleUsers.ROLE_MANAGER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Users> getManagerList(){return managerRepository.findByRole(ROLE_MANAGER);}
    public boolean isVaildLoginId(String loginId){
        return managerRepository.findByLoginId(loginId).isEmpty();
    }
    @Transactional
    public Users insertUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return managerRepository.save(user);
    }
}
