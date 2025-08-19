package com.StockPredictor.League.Repositories;

import com.StockPredictor.League.Entities.LeagueMembership;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeagueMembershipRepository extends JpaRepository<LeagueMembership, Long> {

    List<LeagueMembership> findByLeagueId(Long leagueId);
    List<LeagueMembership> findByUserId(Long userId);

}
