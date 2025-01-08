package qoraa.net.common.security.authorization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public interface UserAccountDetails extends UserDetails {

    @Override
    default Collection<GrantedAuthority> getAuthorities() {
       return  getPermissions()!=null?Arrays.stream(getPermissions().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
               :Collections.emptySet();
    }

    String getFirstName();

    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullName();

    String getLastName();

    String getPermissions();

    @Value("#{target.username}")
    String getUsername();

    @Override
    @Value("#{true}")
    boolean isAccountNonExpired();

    @Override
    @Value("#{true}")
    boolean isEnabled();

    @Override
    @Value("#{true}")
    boolean isAccountNonLocked();

    @Override
    @Value("#{true}")
    boolean isCredentialsNonExpired();

}
