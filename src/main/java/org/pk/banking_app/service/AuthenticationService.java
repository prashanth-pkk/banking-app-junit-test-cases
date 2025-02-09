package org.pk.banking_app.service;

import org.pk.banking_app.repository.UserRepository;
import org.pk.banking_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String userName, String password) {
        if (userRepository.findByUsername(userName) != null) {
            throw new RuntimeException("User is already taken ");
        }
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean authenticateUser(String userName, String password) {
        User user = userRepository.findByUsername(userName);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("admin".equals(username)) {
            String encodedPassword = passwordEncoder.encode("password");
            return new org.springframework.security.core.userdetails.User("admin", encodedPassword, List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")));
        } else if ("user".equals(username)) {
            String encodedPassword = passwordEncoder.encode("userpassword");
            return new org.springframework.security.core.userdetails.User("user", encodedPassword, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
