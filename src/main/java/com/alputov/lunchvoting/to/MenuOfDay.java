package com.alputov.lunchvoting.to;

import java.util.List;
import java.util.Objects;

public class MenuOfDay {
    private RestaurantTo restaurant;
    private List<DishTo> dishes;

    public MenuOfDay() {
    }

    public MenuOfDay(RestaurantTo rTo, List<DishTo> dishes) {
        this.restaurant = rTo;
        this.dishes = dishes;
    }

    public RestaurantTo getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantTo restaurant) {
        this.restaurant = restaurant;
    }

    public List<DishTo> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishTo> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuOfDay menuOfDay = (MenuOfDay) o;
        return restaurant.equals(menuOfDay.restaurant) &&
                dishes.equals(menuOfDay.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurant, dishes);
    }

    @Override
    public String toString() {
        return "MenuOfDay{" +
                "restaurant=" + restaurant +
                ", dishes=" + dishes +
                '}';
    }
}
