package bg.tihomir.bookstore.init;

import bg.tihomir.bookstore.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInit implements CommandLineRunner {

    private final UserService userService;

    public DatabaseInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userService.initializeRolesAndUsers();
    }
}
