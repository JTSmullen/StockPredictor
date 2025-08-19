package com.StockPredictor.Controller;

import com.StockPredictor.League.Repositories.StandingsRepository;
import com.StockPredictor.League.Entities.Standings;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/v1/standings")
public class StandingsController {

    private final StandingsRepository standingsRepo;

    public StandingsController(StandingsRepository standingsRepo) {
        this.standingsRepo = standingsRepo;
    }

    @GetMapping("/{leagueId}")
    public List<Standings> getStandings(@PathVariable Long leagueId) {
        return standingsRepo.findByMembershipLeagueId(leagueId);
    }

}
