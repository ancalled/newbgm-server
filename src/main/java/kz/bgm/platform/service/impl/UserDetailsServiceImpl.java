package kz.bgm.platform.service.impl;

import kz.bgm.platform.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MainService service;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        kz.bgm.platform.model.User user = service.getUser(login);
        if (user != null) {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new User(user.getUsername(), user.getPassword(), authorityList);
        } else {
            throw new UsernameNotFoundException("Invalid username/password.");
        }
    }
}
