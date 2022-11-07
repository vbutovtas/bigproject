package com.project.integration.serv.security;

import com.project.integration.dao.entity.User;
import com.project.integration.serv.enums.UserStatus;
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

  private UserStatus status;

  public static UserDetails fromUserEntityToUserDetails(User userEntity) {
    return new UserDetailsImpl(
        userEntity.getLogin(),
        userEntity.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole().getName())),
        UserStatus.getEnumByValue(userEntity.getStatus()));
  }

  public UserStatus getStatus() {
    return status;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !UserStatus.DEACTIVATED.equals(status);
  }//TODO catch exception when account is locked

  @Override
  public boolean isAccountNonLocked() {
    return !UserStatus.BLOCKED.equals(status);
  }//TODO catch exception when account is locked

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return UserStatus.ACTIVE.equals(status);
  }//TODO catch exception when account is locked
}
