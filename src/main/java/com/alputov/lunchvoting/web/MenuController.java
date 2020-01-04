package com.alputov.lunchvoting.web;

import com.alputov.lunchvoting.AuthorizedUser;
import com.alputov.lunchvoting.service.MenuService;
import com.alputov.lunchvoting.service.VoteService;
import com.alputov.lunchvoting.to.MenuOfDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(MenuController.REST_URL)
public class MenuController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/menus";

    @Autowired
    MenuService menuService;

    @Autowired
    VoteService voteService;

    @GetMapping
    public List<MenuOfDay> getAll() {
        log.info("get all menuOfDay");
        return menuService.getAll();
    }

    @GetMapping("/vote")
    public MenuOfDay getMenuVotedFor(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get restaurant and menu voted for");
        return voteService.getMenuVotedFor(authUser.getId());
    }
}
