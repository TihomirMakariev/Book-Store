package bg.tihomir.bookstore.service;

import bg.tihomir.bookstore.model.dto.UserRegisterDTO;
import bg.tihomir.bookstore.model.entity.UserEntity;
import bg.tihomir.bookstore.model.entity.UserRoleEntity;
import bg.tihomir.bookstore.model.enums.UserRoleEnum;
import bg.tihomir.bookstore.model.mapper.UserMapper;
import bg.tihomir.bookstore.repository.UserRepository;
import bg.tihomir.bookstore.repository.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final UserDetailsService userDetailsService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper,
                       UserRoleRepository userRoleRepository,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRoleRepository = userRoleRepository;
        this.userDetailsService = userDetailsService;
    }

    public void registerAndLogin(UserRegisterDTO userRegisterDTO) {

        UserEntity newUser = userMapper.userDtoToUserEntity(userRegisterDTO);
        UserRoleEntity userRoleEntity = this.userRoleRepository.findByUserRole(UserRoleEnum.USER);

        newUser.setUserRoles(List.of(userRoleEntity));
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        this.userRepository.save(newUser);
        login(newUser);
    }

    private void login(UserEntity userEntity) {

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(userEntity.getEmail());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(auth);
    }

    public void initializeRolesAndUsers() {
        initializeRoles();
        initializeUsers();
    }
    public void initializeRoles() {
        if (this.userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity();
            adminRole.setUserRole(UserRoleEnum.ADMIN);

            UserRoleEntity userRole = new UserRoleEntity();
            userRole.setUserRole(UserRoleEnum.USER);

            this.userRoleRepository.saveAll(List.of(adminRole, userRole));
        }
    }

    public void initializeUsers() {
        if (this.userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setEmail("admin@mail.com");
            admin.setActive(true);
            admin.setImageUrl(null);
            admin.setPassword(this.passwordEncoder.encode("12345"));

            admin.setUserRoles(List.of(
                    this.userRoleRepository.findByUserRole(UserRoleEnum.ADMIN),
                    this.userRoleRepository.findByUserRole(UserRoleEnum.USER))
            );
            this.userRepository.save(admin);

            UserEntity user = new UserEntity();
            user.setFirstName("User");
            user.setLastName("Userov");
            user.setEmail("user@mail.com");
            user.setActive(true);
            user.setImageUrl(null);
            user.setPassword(this.passwordEncoder.encode("12345"));

            user.setUserRoles(List.of(
                    this.userRoleRepository.findByUserRole(UserRoleEnum.USER))
            );
            this.userRepository.save(user);
        }
    }
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByEmail(username).orElse(null);
    }

    private boolean isAdmin (UserEntity userEntity) {
        return userEntity.getUserRoles()
                .stream()
                .anyMatch(userRole -> userRole.getUserRole() == UserRoleEnum.ADMIN);
    }
}
