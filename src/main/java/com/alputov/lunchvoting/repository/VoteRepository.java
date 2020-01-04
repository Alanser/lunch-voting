package com.alputov.lunchvoting.repository;

import com.alputov.lunchvoting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Vote findByUser_idAndDate(int userId, Date date);
}
