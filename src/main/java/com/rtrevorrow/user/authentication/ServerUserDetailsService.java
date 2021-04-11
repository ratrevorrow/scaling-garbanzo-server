package com.rtrevorrow.user.authentication;

import com.rtrevorrow.user.model.User;
import com.rtrevorrow.user.model.UserDetails;
import com.rtrevorrow.user.model.UserRole;
import com.rtrevorrow.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service("userDetailsService")
public class ServerUserDetailsService implements UserDetailsService {

//    @Autowired
//    ServerAuthenticationManager serverAuthenticationManager;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
//        serverAuthenticationManager.processPolicy(user);
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setCredentialsNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setEnabled(true);
        userDetails.setAccountNonExpired(true);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRole userRole : user.getUserRoles()) {
            authorities.add(new SimpleGrantedAuthority(userRole.getRole().getRoleName()));
        }

        userDetails.setAuthorities(authorities);

        return userDetails;
    }
}
