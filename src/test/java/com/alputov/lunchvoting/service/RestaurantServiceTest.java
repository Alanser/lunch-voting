package com.alputov.lunchvoting.service;


import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.util.exception.ErrorType;
import com.alputov.lunchvoting.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.alputov.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @Test
    public void create() throws Exception {
        Restaurant newR = getNewRestaurant();
        Restaurant created = restaurantService.create(newR);
        Integer newId = created.getId();
        newR.setId(newId);
        RESTAURANT_MATCHERS.assertMatch(created, newR);
        RESTAURANT_MATCHERS.assertMatch(restaurantService.get(newId), newR);
    }

    @Test
    public void update() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        restaurantService.update(updated);
        RESTAURANT_MATCHERS.assertMatch(restaurantService.get(NOMA_REST_ID), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException e = assertThrows(NotFoundException.class, () -> restaurantService.update(new Restaurant(1000000, "Some Restaurant")));
        String msg = e.getMessage();
        assertTrue(msg.contains(ErrorType.DATA_NOT_FOUND.name()));
        assertTrue(msg.contains(NotFoundException.NOT_FOUND_EXCEPTION));
        assertTrue(msg.contains(String.valueOf(1000000)));
    }

    @Test
    public void get() {
        Restaurant actual = restaurantService.get(NOMA_REST_ID);
        RESTAURANT_MATCHERS.assertMatch(actual, NOMA_REST);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.get(1000000));
    }

    @Test
    public void delete() {
        restaurantService.delete(NOMA_REST_ID);
        assertThrows(NotFoundException.class, () ->
                restaurantService.get(NOMA_REST_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.delete(1000000));
    }

    @Test
    public void getAll() {
        RESTAURANT_MATCHERS.assertMatch(restaurantService.getAll(), RESTAURANTS);
    }
}