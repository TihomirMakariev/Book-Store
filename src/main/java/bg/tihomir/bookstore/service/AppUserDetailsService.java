package bg.tihomir.bookstore.service;

import bg.tihomir.bookstore.model.entity.UserEntity;
import bg.tihomir.bookstore.model.entity.UserRoleEntity;
import bg.tihomir.bookstore.repository.UserRepository;
import bg.tihomir.bookstore.user.AppUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class AppUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .map(this::map)
                .orElseThrow();
    }


    private UserDetails map(UserEntity userEntity) {
        return new AppUserDetails(
                userEntity.getId(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity
                        .getUserRoles()
                        .stream().map(this::map)
                        .toList()
        );
    }

    private GrantedAuthority map(UserRoleEntity userRole) {
        return new SimpleGrantedAuthority("ROLE_" +
                userRole.getUserRole().name());
    }
}
