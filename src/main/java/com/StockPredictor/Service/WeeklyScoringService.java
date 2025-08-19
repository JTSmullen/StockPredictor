package com.StockPredictor.Service;

import com.StockPredictor.League.Repositories.*;
import com.StockPredictor.League.Entities.Matchup;
import com.StockPredictor.League.Entities.Standings;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeeklyScoringService {

    private final MatchupRepository matchupRepo;
    private final StandingsRepository standingsRepo;
    private final StockService stockService;
    private final PortfolioRepository portfolioRepository;

    public WeeklyScoringService(MatchupRepository matchupRepo, StandingsRepository standingsRepo) {
        this.matchupRepo = matchupRepo;
        this.standingsRepo = standingsRepo;
    }

    public void finalizeWeek(Long leagueId, int week) {
        List<Matchup> matchups = matchupRepo.findByLeagueIdAndWeek(leagueId, week);

        for (Matchup m : matchups) {
            double home = m.getHomeScore();
            double away = m.getAwayScore();

            Standings homeS = standingsRepo.findByMembershipId(m.getHome().getId()).orElse(new Standings());
            homeS.setMembership(m.getHome());

            Standings awayS = standingsRepo.findByMembershipId(m.getAway().getId()).orElse(new Standings());
            awayS.setMembership(m.getAway());

            homeS.setPointsFor(homeS.getPointsFor() + home);
            homeS.setPointsAgainst(homeS.getPointsAgainst() + away);

            awayS.setPointsFor(awayS.getPointsFor() + away);
            awayS.setPointsAgainst(awayS.getPointsAgainst() + home);

            if (home > away) {
                homeS.setWins(homeS.getWins() + 1);
                awayS.setLosses(awayS.getLosses() + 1);
                m.setResult("HOME_WIN");
            }else if(away > home) {
                awayS.setWins(awayS.getWins() + 1);
                homeS.setLosses(homeS.getLosses() + 1);
                m.setResult("AWAY_WIN");
            } else {
                homeS.setTies(homeS.getTies() + 1);
                awayS.setTies(awayS.getTies() + 1);
                m.setResult("TIE");
            }

            standingsRepo.save(homeS);
            standingsRepo.save(awayS);
            matchupRepo.save(m);
        }
    }

}
