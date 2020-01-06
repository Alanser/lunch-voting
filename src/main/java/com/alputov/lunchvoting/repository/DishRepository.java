package com.alputov.lunchvoting.repository;

import com.alputov.lunchvoting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    public Optional<Dish> findByNameAndPrice(String name, int price);
}
