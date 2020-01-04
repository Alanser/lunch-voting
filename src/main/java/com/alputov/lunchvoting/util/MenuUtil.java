package com.alputov.lunchvoting.util;

import com.alputov.lunchvoting.model.Dish;
import com.alputov.lunchvoting.model.MenuItem;
import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.to.DishTo;
import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.to.RestaurantTo;

import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {
    public static List<MenuItem> getMenuItemList(Restaurant r, List<Dish> dishes) {
        return dishes.stream().map(dish -> new MenuItem(r, dish)).collect(Collectors.toList());
    }

    public static MenuOfDay getMenuOfDay(RestaurantTo r, List<MenuItem> items) {
        List<DishTo> dishes = items.stream()
                .map(item -> DishUtil.asTo(item.getDish()))
                .collect(Collectors.toList());
        return new MenuOfDay(r, dishes);
    }

    public static List<MenuOfDay> getMenuOfDayList(List<MenuItem> items) {
        return items.stream()
                .collect(Collectors.groupingBy(MenuItem::getRestaurant,
                        Collectors.mapping(MenuItem::getDish, Collectors.toList())))
                .entrySet().stream()
                .map(entry -> new MenuOfDay(RestaurantUtil.asTo(entry.getKey()), DishUtil.asTo(entry.getValue())))
                .collect(Collectors.toList());
    }
}
