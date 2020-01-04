package com.alputov.lunchvoting.web;


import com.alputov.lunchvoting.TestData;
import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.service.MenuService;
import com.alputov.lunchvoting.service.RestaurantService;
import com.alputov.lunchvoting.service.VoteService;
import com.alputov.lunchvoting.to.DishTo;
import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.util.DishUtil;
import com.alputov.lunchvoting.util.RestaurantUtil;
import com.alputov.lunchvoting.util.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static com.alputov.lunchvoting.TestData.*;
import static com.alputov.lunchvoting.TestUtil.readFromJson;
import static com.alputov.lunchvoting.util.exception.ErrorType.VALIDATION_ERROR;
import static com.alputov.lunchvoting.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_NAME_RESTAURANT_SAME_OWNER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest extends AbstractControllerTest {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    MenuService menuService;

    @Autowired
    VoteService voteService;

    public RestaurantControllerTest() {
        super(RestaurantController.REST_URL);
    }

    @Test
    public void get() throws Exception {
        perform(doGet(NOMA_REST_ID).basicAuth(ADMIN))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHERS.contentJson(NOMA_REST));
    }

    @Test
    void getUnauth() throws Exception {
        perform(doGet(NOMA_REST_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotOwn() throws Exception {
        perform(doGet(NOMA_REST_ID).basicAuth(USER))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getAll() throws Exception {
        perform(doGet().basicAuth(ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHERS.contentJson(RESTAURANTS));
    }

    @Test
    void createWithLocation() throws Exception {
        Restaurant newR = TestData.getNewRestaurant();
        ResultActions action = perform(doPost().jsonBody(newR).basicAuth(ADMIN));

        Restaurant created = readFromJson(action, Restaurant.class);
        Integer newId = created.getId();
        newR.setId(newId);
        RESTAURANT_MATCHERS.assertMatch(created, newR);
        RESTAURANT_MATCHERS.assertMatch(restaurantService.get(newId, ADMIN), newR);
    }

    @Test
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null);
        perform(doPost().jsonBody(invalid).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Restaurant invalid = new Restaurant(null, NOMA_REST.getName());
        perform(doPost().jsonBody(invalid).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_NAME_RESTAURANT_SAME_OWNER));
    }

    @Test
    void delete() throws Exception {
        perform(doDelete(NOMA_REST_ID).basicAuth(ADMIN))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> restaurantService.get(NOMA_REST_ID, ADMIN));
    }

    @Test
    void update() throws Exception {
        Restaurant updated = TestData.getUpdatedRestaurant();
        perform(doPut(NOMA_REST_ID).jsonBody(updated).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHERS.assertMatch(restaurantService.get(NOMA_REST_ID, ADMIN), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(NOMA_REST_ID, null);
        perform(doPut(NOMA_REST_ID).jsonBody(invalid).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andDo(print());
    }

    @Test
    void getMenu() throws Exception {
        perform(doGet("/" + NOMA_REST_ID + "/menu").basicAuth(USER))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHERS.contentJson(NOMA_MENU_OF_DAY));
    }

    @Test
    void createMenu() throws Exception {
        List<DishTo> dishes = DishUtil.asTo(NEW_DISHES);
        MenuOfDay menu = new MenuOfDay(RestaurantUtil.asTo(ASADOR_REST), dishes);
        perform(doPost("/" + ASADOR_REST_ID + "/menu").jsonBody(dishes).basicAuth(ADMIN))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHERS.contentJson(menu));
    }

    @Test
    void voteForRestaurant() throws Exception {
        MenuOfDay menu = menuService.get(NOMA_REST_ID);
        if (LocalTime.now().getHour() < 11) {
            perform(doPost("/" + NOMA_REST_ID + "/vote").basicAuth(USER))
                    .andDo(print())
                    .andExpect(status().isNoContent());
            MENU_MATCHERS.assertMatch(voteService.getMenuVotedFor(USER_ID), menu);
        } else {
            perform(doPost("/" + NOMA_REST_ID + "/vote").basicAuth(USER))
                    .andDo(print())
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(errorType(VALIDATION_ERROR))
                    .andExpect(detailMessage(ExceptionInfoHandler.EXCEPTION_VOTE_LATE));
        }

    }
}