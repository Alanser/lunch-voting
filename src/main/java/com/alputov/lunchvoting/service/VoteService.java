package com.alputov.lunchvoting.service;

import com.alputov.lunchvoting.model.Restaurant;
import com.alputov.lunchvoting.model.User;
import com.alputov.lunchvoting.model.Vote;
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

@Service("voteService")
public class VoteService {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    MenuService menuService;

    @Transactional
    public Vote createOrUpdate(Restaurant r, User user, LocalTime time) {
        if (time.getHour() >= 11) {
            throw new ApplicationException(ErrorType.VALIDATION_ERROR, ExceptionInfoHandler.EXCEPTION_VOTE_LATE);
        }
        Vote vote = voteRepository.findByUser_idAndDate(user.getId(), new Date());
        if (vote == null) {
            return voteRepository.save(new Vote(user, r));
        } else {
            vote.setRestaurant(r);
            return voteRepository.save(vote);
        }
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
