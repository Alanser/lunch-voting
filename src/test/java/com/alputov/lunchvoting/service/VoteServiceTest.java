package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.util.exception.ApplicationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;

import static com.alputov.lunchvoting.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    VoteService voteService;

    @Test
    void create() {
        voteService.create(NOMA_REST_ID, USER2);
        MenuOfDay voted = voteService.getMenuVotedFor(USER2_ID);
        assertEquals(NOMA_REST_ID, voted.getRestaurant().getId());
    }

    @Test
    void createWithoutMenu() {
        assertThrows(ApplicationException.class,
                () -> voteService.create(ASADOR_REST_ID, USER2));
    }

    @Test
    void update() {
        voteService.update(MIRAZUR_REST_ID, USER, LocalTime.of(10, 0));
        MenuOfDay updated = voteService.getMenuVotedFor(USER_ID);
        assertEquals(MIRAZUR_REST_ID, updated.getRestaurant().getId());
    }

    @Test
    void updateVoteLate() {
        assertThrows(ApplicationException.class,
                () -> voteService.update(NOMA_REST_ID, USER, LocalTime.of(11, 30)));
    }
}