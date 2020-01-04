package com.alputov.lunchvoting.repository;

import com.alputov.lunchvoting.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.dish WHERE m.date=:date")
    public List<MenuItem> getAllByDateWithDishes(@Param("date") Date date);

    @Query("SELECT m FROM MenuItem m JOIN FETCH m.dish WHERE m.restaurant.id = ?1 and m.date = ?2")
    public List<MenuItem> findAllByRestaurant_IdAndDateWithDishes(int restId, Date date);

    public int deleteAllByRestaurant_IdAndDate(int restId, Date date);
}
