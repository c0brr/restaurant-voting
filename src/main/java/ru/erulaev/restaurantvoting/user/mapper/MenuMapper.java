package ru.erulaev.restaurantvoting.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.erulaev.restaurantvoting.user.model.Menu;
import ru.erulaev.restaurantvoting.user.to.menu.AdminMenuTo;
import ru.erulaev.restaurantvoting.user.to.menu.RegularMenuTo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DishMapper.class)
public interface MenuMapper {

    AdminMenuTo createTo(Menu menu);

    @Mapping(target = "dishTos", source = "dishes")
    RegularMenuTo createToWithDishes(Menu menu);
}
