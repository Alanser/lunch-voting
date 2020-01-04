package com.alputov.lunchvoting.repository;

import com.alputov.lunchvoting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
}
