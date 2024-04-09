package kr.ganjuproject.auth;

import kr.ganjuproject.entity.Users;
import kr.ganjuproject.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    //Security session = Authentication = UserDetails
    //session(내부 Authenticaiton (내부 UserDetails ))

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users userEntity = managerRepository.findByLoginId(username).get();
        if(userEntity!= null){
            System.out.println(" 유저 디테일 객체 생성 !!! " + userEntity);
            return new PrincipalDetails(userEntity); // 이 함수가 종료가 될때 @Authentication 객체가 만들어진다
        }
        return null;
    }
}
