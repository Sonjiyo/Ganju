package kr.ganjuproject.service;

import kr.ganjuproject.entity.Users;
import kr.ganjuproject.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static kr.ganjuproject.entity.RoleUsers.ROLE_MANAGER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    public List<Users> getManagerList() {
        List<Users> list = managerRepository.findByRole(ROLE_MANAGER);
        Collections.reverse(list);
        return list;
    }

    public boolean isVaildLoginId(String loginId) {
        return managerRepository.findByLoginId(loginId).isEmpty();
    }

    @Transactional
    public Users insertUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return managerRepository.save(user);
    }

    public Users getOneUser(Long id) {
        return managerRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteUser(Long id) {
        Users user = getOneUser(id);
        if(user == null) return;
        if(user.getRestaurant() != null){
            System.out.println("id = " + id);
            s3Uploader.deleteFolder(id+"");
        }
        managerRepository.deleteById(id);
    }

    @Transactional
    public Users updateUser(Users user) {
        return managerRepository.save(user);
    }
}
