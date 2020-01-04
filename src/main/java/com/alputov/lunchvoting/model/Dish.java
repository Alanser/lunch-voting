package com.alputov.lunchvoting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "price"}, name = "dishes_unique_name_price_idx")})
public class Dish extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    private Integer price;

    @ManyToMany
    @JoinTable(name = "menu_item", joinColumns = {@JoinColumn(name = "dish_id")}, inverseJoinColumns = {@JoinColumn(name = "restaurant_id")})
    private List<Restaurant> restaurants;

    public Dish() {
    }

    public Dish(String name, Integer price) {
        super(null, name);
        this.price = price;
    }

    public Dish(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return name.equals(dish.name) && price.equals(dish.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
