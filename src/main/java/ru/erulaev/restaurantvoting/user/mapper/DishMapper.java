package ru.erulaev.restaurantvoting.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.erulaev.restaurantvoting.user.entity.Dish;
import ru.erulaev.restaurantvoting.user.to.dish.DishTo;
import ru.erulaev.restaurantvoting.user.to.dish.DishWithMenuIdTo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DishMapper {

    DishWithMenuIdTo createWithMenuIdTo(Dish dish);

    DishTo createTo(Dish dish);
}
