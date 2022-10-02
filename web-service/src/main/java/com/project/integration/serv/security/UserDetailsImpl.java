package com.project.integration.serv.security;

import com.project.integration.dao.entity.User;
import java.util.Collection;
import java.util.Collections;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private String login;
  private String password;
  private Collection<? extends GrantedAuthority> grantedAuthorities;

  public static UserDetails fromUserEntityToUserDetails(User userEntity) {
    return new UserDetailsImpl(
        userEntity.getLogin(),
        userEntity.getPassword(), //TODO
        Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().getName())));
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return password;
  } //TODO

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
