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
    void createOrUpdate() {
        voteService.createOrUpdate(NOMA_REST, USER, LocalTime.of(10, 0));
        MenuOfDay voted = voteService.getMenuVotedFor(USER_ID);
        assertEquals(NOMA_REST_ID, voted.getRestaurant().getId());

        voteService.createOrUpdate(MIRAZUR_REST, USER, LocalTime.of(10, 0));
        MenuOfDay updated = voteService.getMenuVotedFor(USER_ID);
        assertEquals(MIRAZUR_REST_ID, updated.getRestaurant().getId());
    }

    @Test
    void lateVote() {
        assertThrows(ApplicationException.class,
                () -> voteService.createOrUpdate(NOMA_REST, USER, LocalTime.of(11, 30)));
    }
}