package kr.ganjuproject.auth;

import jakarta.servlet.http.HttpSession;
import kr.ganjuproject.entity.Users;
import kr.ganjuproject.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    //Security session = Authentication = UserDetails
    //session(내부 Authenticaiton (내부 UserDetails ))

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<Users> userEntityOptional = managerRepository.findByLoginId(loginId);
        if(userEntityOptional.isPresent()) {
            Users userEntity = userEntityOptional.get();
            // 사용자 정보를 이용하여 PrincipalDetails 객체 생성
            return new PrincipalDetails(userEntity);
        } else {
            throw new UsernameNotFoundException("User not found with loginId: " + loginId);
        }
    }
}
