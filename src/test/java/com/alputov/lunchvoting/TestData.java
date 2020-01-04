package com.alputov.lunchvoting;

import com.alputov.lunchvoting.model.Dish;
import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.model.Role;
import com.alputov.lunchvoting.model.User;
import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.util.DishUtil;
import com.alputov.lunchvoting.util.RestaurantUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TestData {
    public static final int ADMIN_ID = 1;
    public static final int USER_ID = 2;
    public static final int USER2_ID = 3;

    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "adminPassword", Role.ROLE_ADMIN);
    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "userPassword", Role.ROLE_USER);
    public static final User USER2 = new User(USER2_ID, "User2", "user2@yandex.ru", "user2Password", Role.ROLE_USER);

    public static final int MIRAZUR_REST_ID = 1;
    public static final int NOMA_REST_ID = 2;
    public static final int ASADOR_REST_ID = 3;

    public static final Restaurant MIRAZUR_REST = new Restaurant(MIRAZUR_REST_ID, "Mirazur Restaurant");
    public static final Restaurant NOMA_REST = new Restaurant(NOMA_REST_ID, "Noma Restaurant");
    public static final Restaurant ASADOR_REST = new Restaurant(ASADOR_REST_ID, "Asador Restaurant");
    public static final List<Restaurant> RESTAURANTS = List.of(MIRAZUR_REST, NOMA_REST, ASADOR_REST);

    public static final Dish DISH1 = new Dish(1, "Ranch Crusted Chicken", 1056);
    public static final Dish DISH2 = new Dish(2, "House Salad", 500);
    public static final Dish DISH3 = new Dish(3, "Crispy Fish Tacos", 780);
    public static final Dish DISH4 = new Dish(4, "Cheese Burger", 655);
    public static final Dish DISH5 = new Dish(5, "Sundried Tomato Chicken Kabobs", 998);
    public static final Dish DISH6 = new Dish(6, "Balsamic Grilled Chicken", 1300);
    public static final Dish DISH7 = new Dish(7, "Fresh Salmon", 760);
    public static final Dish NEW_DISH1 = new Dish(null, "Cobb Salad", 756);
    public static final Dish NEW_DISH2 = new Dish(null, "Fluffy Pancakes", 500);
    public static final Dish NEW_DISH3 = new Dish(null, "Broiled Grapefruit", 380);
    public static final Dish NEW_DISH4 = new Dish(null, "Eggnog French Toast", 395);
    public static final Dish NEW_DISH5 = new Dish(null, "Spicy Grilled Cheese Sandwich", 998);
    public static final List<Dish> NEW_DISHES = List.of(NEW_DISH1, NEW_DISH2, NEW_DISH3, NEW_DISH4, NEW_DISH5);


    public static final MenuOfDay MIRAZUR_MENU_OF_DAY = new MenuOfDay(RestaurantUtil.asTo(MIRAZUR_REST),
            DishUtil.asTo(List.of(DISH1, DISH2, DISH3)));
    public static final MenuOfDay NOMA_MENU_OF_DAY = new MenuOfDay(RestaurantUtil.asTo(NOMA_REST),
            DishUtil.asTo(List.of(DISH3, DISH4, DISH5, DISH6, DISH7)));
    public static final MenuOfDay NEW_MENU_OF_DAY = new MenuOfDay(RestaurantUtil.asTo(ASADOR_REST), DishUtil.asTo(NEW_DISHES));

    public static User getNewUser() {
        return new User(null, "New", "new@gmail.com", "newPass", new Date(), Collections.singleton(Role.ROLE_USER));
    }

    public static User getUpdatedUser() {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setRoles(Collections.singletonList(Role.ROLE_ADMIN));
        return updated;
    }

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdatedRestaurant() {
        Restaurant updated = new Restaurant(NOMA_REST);
        updated.setName("Updated Noma Restaurant");
        return updated;
    }

    public static TestMatchers<User> USER_MATCHERS = TestMatchers.useFieldsComparator(User.class, "registered", "password");
    public static TestMatchers<Restaurant> RESTAURANT_MATCHERS = TestMatchers.useFieldsComparator(Restaurant.class, "dishes");
    public static TestMatchers<MenuOfDay> MENU_MATCHERS = TestMatchers.useFieldsComparator(MenuOfDay.class, "date");
}
