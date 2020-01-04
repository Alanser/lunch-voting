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
        Restaurant created = restaurantService.create(newR, ADMIN);
        Integer newId = created.getId();
        newR.setId(newId);
        RESTAURANT_MATCHERS.assertMatch(created, newR);
        RESTAURANT_MATCHERS.assertMatch(restaurantService.get(newId, ADMIN), newR);
    }

    @Test
    public void update() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        restaurantService.update(updated, ADMIN);
        RESTAURANT_MATCHERS.assertMatch(restaurantService.get(NOMA_REST_ID, ADMIN), updated);
    }

    @Test
    void updateNotFound() throws Exception {
        NotFoundException e = assertThrows(NotFoundException.class, () -> restaurantService.update(ASADOR_REST, USER));
        String msg = e.getMessage();
        assertTrue(msg.contains(ErrorType.DATA_NOT_FOUND.name()));
        assertTrue(msg.contains(NotFoundException.NOT_FOUND_EXCEPTION));
        assertTrue(msg.contains(String.valueOf(ASADOR_REST_ID)));
    }

    @Test
    public void get() {
        Restaurant actual = restaurantService.get(NOMA_REST_ID, ADMIN);
        RESTAURANT_MATCHERS.assertMatch(actual, NOMA_REST);
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.get(1000000, ADMIN));
    }

    @Test
    void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.get(ASADOR_REST_ID, USER));
    }

    @Test
    public void delete() {
        restaurantService.delete(NOMA_REST_ID, ADMIN);
        assertThrows(NotFoundException.class, () ->
                restaurantService.get(NOMA_REST_ID, ADMIN));
    }

    @Test
    void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.delete(1000000, ADMIN));
    }

    @Test
    void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () ->
                restaurantService.delete(ASADOR_REST_ID, USER));
    }

    @Test
    public void getAll() {
        RESTAURANT_MATCHERS.assertMatch(restaurantService.getAll(ADMIN), RESTAURANTS);
    }
}