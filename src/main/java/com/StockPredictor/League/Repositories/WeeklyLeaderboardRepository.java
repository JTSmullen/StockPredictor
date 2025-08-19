package com.StockPredictor.League.Repositories;

import com.StockPredictor.League.Entities.WeeklyLeaderboard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyLeaderboardRepository extends JpaRepository<WeeklyLeaderboard, Long> {

    List<WeeklyLeaderboard> findByLeagueIdAndWeek(Long leagueId, int week);

}
