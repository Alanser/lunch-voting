package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.model.MenuItem;
import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.model.User;
import com.alputov.lunchvoting.model.Vote;
import com.alputov.lunchvoting.repository.MenuItemRepository;
import com.alputov.lunchvoting.repository.RestaurantRepository;
import com.alputov.lunchvoting.repository.VoteRepository;
import com.alputov.lunchvoting.to.MenuOfDay;
import com.alputov.lunchvoting.util.exception.ApplicationException;
import com.alputov.lunchvoting.util.exception.ErrorType;
import com.alputov.lunchvoting.web.ExceptionInfoHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static com.alputov.lunchvoting.util.ValidationUtil.checkNotFound;

@Service("voteService")
public class VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuItemRepository itemRepository;

    @Transactional
    public Vote create(int restId, User user) {
        List<MenuItem> items = itemRepository.findAllByRestaurant_IdAndDate(restId, new Date());
        if (items.isEmpty()) {
            throw new ApplicationException(ErrorType.VALIDATION_ERROR, ExceptionInfoHandler.EXCEPTION_RESTAURANT_DOES_NOT_HAVE_MENU);
        }
        Restaurant r = restaurantRepository.getOne(restId);
        return voteRepository.save(new Vote(user, r));
    }

    public Vote update(int restId, User user, LocalTime time) {
        if (time.getHour() >= Vote.HOUR_EXPIRED) {
            throw new ApplicationException(ErrorType.VALIDATION_ERROR, ExceptionInfoHandler.EXCEPTION_VOTE_LATE);
        }
        Vote vote = voteRepository.findByUser_idAndDate(user.getId(), new Date());
        checkNotFound(vote, "You haven't voted yet");
        vote.setRestaurant(restaurantRepository.getOne(restId));
        return voteRepository.save(vote);
    }

    @Transactional
    public MenuOfDay getMenuVotedFor(int userId) {
        Vote vote = voteRepository.findByUser_idAndDate(userId, new Date());
        if (vote == null) {
            throw new ApplicationException(ErrorType.DATA_NOT_FOUND, ExceptionInfoHandler.EXCEPTION_VOTE_DOES_NOT_EXIST);
        }
        return menuService.get(vote.getRestaurant().getId());
    }
}
