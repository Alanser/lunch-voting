package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.model.Dish;
import com.alputov.lunchvoting.model.MenuItem;
import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.repository.DishRepository;
import com.alputov.lunchvoting.repository.MenuItemRepository;
import com.alputov.lunchvoting.to.DishTo;
import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.util.DishUtil;
import com.alputov.lunchvoting.util.MenuUtil;
import com.alputov.lunchvoting.util.RestaurantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("menuService")
public class MenuService {

    @Autowired
    DishRepository dishRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantService restaurantService;

    public MenuOfDay get(int restId) {
        Restaurant r = restaurantService.get(restId);
        List<MenuItem> items = menuItemRepository.findAllByRestaurant_IdAndDateWithDishesAndRestaurants(restId, new Date());
        return MenuUtil.getMenuOfDay(RestaurantUtil.asTo(r), items);
    }

    @Transactional
    public MenuOfDay create(int restId, List<DishTo> dishTos) {
        Assert.notEmpty(dishTos, "dishes list must not be empty");
        Restaurant r = restaurantService.get(restId);
        List<Dish> dishes = dishTos.stream()
                .map(dishTo -> dishRepository.findByNameAndPrice(dishTo.getName(), dishTo.getPrice())
                        .orElseGet(() -> dishRepository.save(DishUtil.newFromTo(dishTo))))
                .collect(Collectors.toList());
        menuItemRepository.saveAll(MenuUtil.getMenuItemList(r, dishes));
        return new MenuOfDay(RestaurantUtil.asTo(r), dishTos);
    }

    @Transactional
    public void delete(int restId) {
        menuItemRepository.deleteAllByRestaurant_IdAndDate(restId, new Date());
    }

    public List<MenuOfDay> getAll() {
        List<MenuItem> menuItems = menuItemRepository.getAllByDateWithDishesAndRestaurants(new Date());
        return MenuUtil.getMenuOfDayList(menuItems);
    }
}