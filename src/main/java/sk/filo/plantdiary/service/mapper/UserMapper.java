package sk.filo.plantdiary.service.mapper;

import org.mapstruct.*;
import sk.filo.plantdiary.dao.domain.User;
import sk.filo.plantdiary.service.so.*;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    UserSO toSO(User user);

    @Mappings({
            @Mapping(target = "password", ignore = true)
    })
    User toBO(CreateUserSO userSO);

    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "username", ignore = true)
    })
    void toBO(UpdateUserSO userSO, @MappingTarget User user);

}
