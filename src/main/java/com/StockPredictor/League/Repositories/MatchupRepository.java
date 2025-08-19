package com.StockPredictor.League.Repositories;

import com.StockPredictor.League.Entities.Matchup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchupRepository extends JpaRepository<Matchup, Long> {

    List<Matchup> findByLeagueIdAndWeek(Long leagueId, int week);

}
