package kr.easw.lesson07.auth;

import kr.easw.lesson07.model.dto.UserData;
import kr.easw.lesson07.model.repository.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JPAUserDetailService implements UserDetailsService {
    private final UserDataRepository userDataRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userDataRepository.findUserDataByuserId(username.toLowerCase()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        return new User(user.getUserId(), user.getPassword(), List.of(
                user.isAdmin() ? new SimpleGrantedAuthority("ADMIN") : new SimpleGrantedAuthority("GUEST")
        ));
    }
}