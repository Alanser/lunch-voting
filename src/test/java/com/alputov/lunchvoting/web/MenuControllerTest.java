package com.alputov.lunchvoting.web;

import com.alputov.lunchvoting.service.MenuService;
import com.alputov.lunchvoting.service.VoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static com.alputov.lunchvoting.TestData.*;
import static com.alputov.lunchvoting.util.exception.ErrorType.DATA_NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {

    @Autowired
    MenuService menuService;

    @Autowired
    VoteService voteService;

    public MenuControllerTest() {
        super(MenuController.REST_URL);
    }

    @Test
    void getAll() throws Exception {
        perform(doGet().basicAuth(ADMIN))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHERS.contentJson(List.of(MIRAZUR_MENU_OF_DAY, NOMA_MENU_OF_DAY)));
    }

    @Test
    void getMenuVotedFor() throws Exception {
        perform(doGet("/vote").basicAuth(USER))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHERS.contentJson(NOMA_MENU_OF_DAY));
    }

    @Test
    void getNotVoted() throws Exception {
        perform(doGet("/vote").basicAuth(USER2))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND))
                .andExpect(detailMessage(ExceptionInfoHandler.EXCEPTION_VOTE_DOES_NOT_EXIST));
    }
}