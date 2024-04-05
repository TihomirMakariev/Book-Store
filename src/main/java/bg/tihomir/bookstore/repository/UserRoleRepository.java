package bg.tihomir.bookstore.repository;

import bg.tihomir.bookstore.model.entity.UserRoleEntity;
import bg.tihomir.bookstore.model.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {

    UserRoleEntity findByUserRole(UserRoleEnum userRoleEnum);
}
