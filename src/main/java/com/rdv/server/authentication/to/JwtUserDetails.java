package com.rdv.server.authentication.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class  JwtUserDetails implements UserDetails {

  private static final long serialVersionUID = 5155720064139820502L;
  private String token;
  private final Long id;
  private final String username;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;
  private final boolean enabled;
  private String code;

  public JwtUserDetails(Long id, String username, String password, String role, boolean enabled) {
      this.id = id;
      this.username = username;
      this.password = password;
      List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
      authorities.add(new SimpleGrantedAuthority(role));
      this.authorities = authorities;
      this.enabled = enabled;
  }

  public JwtUserDetails(Long id, String username, String password, String role, boolean enabled, String code) {
    this.id = id;
    this.username = username;
    this.password = password;
    List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority(role));
    this.authorities = authorities;
    this.enabled = enabled;
    this.code = code;
  }


  public Long getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @JsonIgnore
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public String getToken() { return token; }

  public void setToken(String token) { this.token = token; }

  public String getCode() { return code; }

}
