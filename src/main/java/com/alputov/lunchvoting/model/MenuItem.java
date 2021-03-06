package com.alputov.lunchvoting.model;

import com.alputov.lunchvoting.View;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "dish_id", "date"}, name = "menu_item_unique_idx")})
public class MenuItem extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(groups = View.Persist.class)
    private Dish dish;

    @Column(name = "date", nullable = false, columnDefinition = "date default getdate()")
    @NotNull
    private Date date = new Date();

    public MenuItem() {
    }

    public MenuItem(@NotNull Restaurant restaurant, @NotNull Dish dish) {
        this(null, restaurant, dish);
    }

    public MenuItem(Integer id, @NotNull Restaurant restaurant, @NotNull Dish dish) {
        super(id);
        this.restaurant = restaurant;
        this.dish = dish;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
