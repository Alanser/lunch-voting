package com.alputov.lunchvoting.util;

import com.alputov.lunchvoting.model.Dish;
import com.alputov.lunchvoting.to.DishTo;

import java.util.List;
import java.util.stream.Collectors;

public class DishUtil {
    public static DishTo asTo(Dish dish) {
        return new DishTo(dish.getName(), dish.getPrice());
    }

    public static List<DishTo> asTo(List<Dish> dishes) {
        return dishes.stream().map(DishUtil::asTo).collect(Collectors.toList());
    }

    public static Dish newFromTo(DishTo dishTo) {
        return new Dish(null, dishTo.getName(), dishTo.getPrice());
    }

    public static List<Dish> newFromTo(List<DishTo> dishTos) {
        return dishTos.stream().map(DishUtil::newFromTo).collect(Collectors.toList());
    }
}
