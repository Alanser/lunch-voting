package com.alputov.lunchvoting.web;

import com.alputov.lunchvoting.AuthorizedUser;
import com.alputov.lunchvoting.View;
import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.repository.RestaurantRepository;
import com.alputov.lunchvoting.service.MenuService;
import com.alputov.lunchvoting.service.RestaurantService;
import com.alputov.lunchvoting.service.VoteService;
import com.alputov.lunchvoting.to.DishTo;
import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.to.RestaurantTo;
import com.alputov.lunchvoting.util.RestaurantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.util.List;

import static com.alputov.lunchvoting.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(RestaurantController.REST_URL)
public class RestaurantController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    public static final String REST_URL = "/restaurants";

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuService menuService;

    @Autowired
    VoteService voteService;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get restaurant {} for user {}", id, authUser.getId());
        return RestaurantUtil.asTo(restaurantService.get(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Validated(View.Web.class) @RequestBody Restaurant r,
                                                           @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("create {} for user {}", r, authUser.getId());
        RestaurantTo created = RestaurantUtil.asTo(restaurantService.create(r));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Restaurant r,
                       @PathVariable int id,
                       @AuthenticationPrincipal AuthorizedUser authUser) {
        assureIdConsistent(r, id);
        log.info("update {} for user {}", r, authUser.getId());
        restaurantService.update(r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("delete restaurant {} for user {}", id, authUser.getId());
        restaurantService.delete(id);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    @GetMapping("/{id}/menu")
    public MenuOfDay getMenu(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get today menu by restaurant {} for user {}", id, authUser.getId());
        return menuService.get(id);
    }

    @PostMapping(value = "/{id}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
    public MenuOfDay createMenu(@Validated(View.Web.class) @RequestBody List<DishTo> dishes,
                                @PathVariable int id) {
        menuService.delete(id);
        return menuService.create(id, dishes);
    }

    @PostMapping(value = "/{id}/vote")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void createVote(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        voteService.create(id, authUser.getUser());
    }

    @PutMapping(value = "/{id}/vote")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void updateVote(@PathVariable int id, @AuthenticationPrincipal AuthorizedUser authUser) {
        voteService.update(id, authUser.getUser(), LocalTime.now());
    }
}
