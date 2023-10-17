package ru.stephen.filmlibrary.library.service.userdetails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.stephen.filmlibrary.library.constants.UserRolesConstants;
import ru.stephen.filmlibrary.library.model.User;
import ru.stephen.filmlibrary.library.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.stephen.filmlibrary.library.constants.UserRolesConstants.ADMIN;

@Service
@Slf4j
public class CustomUserDetailsService
        implements UserDetailsService {
    private final UserRepository userRepository;
    @Value("${spring.security.user.name}")
    private String adminUserName;
    @Value("${spring.security.user.password}")
    private String adminPassword;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUserName)) {
            return new CustomUserDetails(null, username, adminPassword, List.of(new SimpleGrantedAuthority("ROLE_" + ADMIN)));
        } else {
            User user = userRepository.findUserByLoginAndIsDeletedFalse(username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().getId() == 1L
                    ? "ROLE_" + UserRolesConstants.USER
                    : "ROLE_" + UserRolesConstants.MANAGER));
            return new CustomUserDetails(user.getId().intValue(), username, user.getPassword(), authorities);
        }
    }
}
