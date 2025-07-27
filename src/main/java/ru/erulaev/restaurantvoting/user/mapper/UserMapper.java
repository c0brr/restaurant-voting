package ru.erulaev.restaurantvoting.user.mapper;

import org.mapstruct.*;
import ru.erulaev.restaurantvoting.user.model.Role;
import ru.erulaev.restaurantvoting.user.model.User;
import ru.erulaev.restaurantvoting.user.to.UserTo;

import java.time.Instant;
import java.util.EnumSet;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {EnumSet.class, Role.class, Instant.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", qualifiedByName = "getEmailLowerCase")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "registered", expression = "java(Instant.now())")
    @Mapping(target = "roles", expression = "java(EnumSet.of(Role.USER))")
    User createNewFromTo(UserTo userTo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", qualifiedByName = "getEmailLowerCase")
    User updateFromTo(@MappingTarget User user, UserTo userTo);

    @Named("getEmailLowerCase")
    default String getEmailLowerCase(String email) {
        return email.toLowerCase();
    }
}