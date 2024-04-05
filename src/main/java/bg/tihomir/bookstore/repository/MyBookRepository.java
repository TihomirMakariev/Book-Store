package bg.tihomir.bookstore.repository;

import bg.tihomir.bookstore.model.entity.MyBookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyBookRepository extends JpaRepository<MyBookEntity, Long> {

    List<MyBookEntity> findByUserEntity(Long userId);

}
