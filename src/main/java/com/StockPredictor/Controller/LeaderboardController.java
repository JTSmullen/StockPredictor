package com.StockPredictor.Controller;

import com.StockPredictor.League.Entities.WeeklyLeaderboard;
import com.StockPredictor.League.Repositories.WeeklyLeaderboardRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
public class LeaderboardController {

    private final WeeklyLeaderboardRepository leaderboardRepo;

    public LeaderboardController(WeeklyLeaderboardRepository leaderboardRepo){
        this.leaderboardRepo = leaderboardRepo;
    }

    @GetMapping("/{leagueId}/{week}")
    public List<WeeklyLeaderboard> getLeaderboard(@PathVariable Long leagueId, @PathVariable int week) {
        return leaderboardRepo.findByLeagueIdAndWeek(leagueId, week);
    }

}
