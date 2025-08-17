package ru.erulaev.restaurantvoting.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.erulaev.restaurantvoting.user.model.Dish;
import ru.erulaev.restaurantvoting.user.to.dish.DishTo;
import ru.erulaev.restaurantvoting.user.to.dish.DishToWithMenuId;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DishMapper {

    DishToWithMenuId createToWithMenuId(Dish dish);

    DishTo createTo(Dish dish);
}
