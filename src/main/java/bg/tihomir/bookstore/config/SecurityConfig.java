package bg.tihomir.bookstore.config;

import bg.tihomir.bookstore.repository.UserRepository;
import bg.tihomir.bookstore.service.AppUserDetailsService;
import bg.tihomir.bookstore.user.AppUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = false)
public class SecurityConfig {

    //Here we have to expose 3 things:
    //1. PasswordEncoder
    //2. SecurityFilterChain
    //3. UserDetailsService


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        return http.authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                // All static resources which are situated in js, images, css are available for anyone
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                // everyone can login and register
                                .requestMatchers("/", "/users/login", "/users/register").permitAll()
                                .requestMatchers("/books/add", "/books/edit", "/books/delete").hasRole("ADMIN")
                                .requestMatchers("/books/my-books").authenticated()
                                // all other pages are available for logger in users
                                .anyRequest().authenticated()
                )
                .formLogin(
                        formLogin -> {
                            formLogin
                                    // redirect here when we access something which is not allowed.
                                    // also this is the page where we perform login.
                                    .loginPage("/users/login")
                                    // The names of the input fields (in our case in auth-login.html)
                                    .usernameParameter("username")
                                    .passwordParameter("password")
                                    .defaultSuccessUrl("/")
                                    .failureForwardUrl("/users/login-error");
                        }
                ).logout(
                        logout -> {
                            logout
                                    // the URL where we should POST something in order to perform the logout
                                    .logoutUrl("/users/logout")
                                    // where to go when logged out?
                                    .logoutSuccessUrl("/")
                                    // invalidate the HTTP session
                                    .invalidateHttpSession(true);
                        }).build();
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }


}
