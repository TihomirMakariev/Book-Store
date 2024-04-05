package bg.tihomir.bookstore.model.mapper;

import bg.tihomir.bookstore.model.dto.UserRegisterDTO;
import bg.tihomir.bookstore.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "active",  constant = "true")
    UserEntity userDtoToUserEntity(UserRegisterDTO userRegisterDTO);
}
