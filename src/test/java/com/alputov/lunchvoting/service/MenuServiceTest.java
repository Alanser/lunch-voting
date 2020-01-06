package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.util.DishUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.alputov.lunchvoting.TestData.*;

class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    RestaurantService restaurantService;

    @Test
    void create() {
        MenuOfDay newMoD = menuService.create(ASADOR_REST_ID, DishUtil.asTo(NEW_DISHES));
        MENU_MATCHERS.assertMatch(newMoD, NEW_MENU_OF_DAY);
    }

    @Test
    void getAll() {
        List<MenuOfDay> menus = menuService.getAll();
        MENU_MATCHERS.assertMatch(menus, List.of(MIRAZUR_MENU_OF_DAY, NOMA_MENU_OF_DAY));
    }
}