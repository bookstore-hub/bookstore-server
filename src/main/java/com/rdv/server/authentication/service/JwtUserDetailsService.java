package com.rdv.server.authentication.service;

import com.rdv.server.authentication.to.GrantedAuthority;
import com.rdv.server.authentication.to.JwtUserDetails;
import com.rdv.server.core.entity.User;
import com.rdv.server.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;


@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    JwtUserDetails jwtUserDetails;

    Optional<User> user = getUser(username);

    if(user.isPresent()) {
        jwtUserDetails = new JwtUserDetails(user.get().getId(), user.get().getEmail(), user.get().getPassword(), GrantedAuthority.ROLE_USER.name(), user.get().isEnabled());
    } else {
        throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
    }

    return jwtUserDetails;

  }

  private Optional<User> getUser(String username) {
    Optional<User> userFound = Optional.empty();
    if(isEmailAddress(username)) { //The username of a user is an email address
        userFound = Optional.ofNullable(userRepository.findByEmail(username));
    }
    return userFound;
  }

  public static boolean isEmailAddress(String emailAddress) {
      String regexPattern = "^(.+)@(\\S+)$";
      return Pattern.compile(regexPattern).matcher(emailAddress).matches();
  }

}