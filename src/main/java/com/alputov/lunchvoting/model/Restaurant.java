package com.alputov.lunchvoting.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurant_unique_name_idx")})
public class Restaurant extends AbstractNamedEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "menu_item", joinColumns = {@JoinColumn(name = "restaurant_id")}, inverseJoinColumns = {@JoinColumn(name = "dish_id")})
    private List<Dish> dishes;

    public Restaurant() {
    }

    public Restaurant(Restaurant r) {
        this(r.getId(), r.getName(), r.getDishes());
    }

    public Restaurant(Integer id, String name, List<Dish> dishes) {
        this(id, name);
        this.dishes = dishes;
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}