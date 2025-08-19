package com.StockPredictor.Service;

import com.StockPredictor.League.Repositories.*;
import com.StockPredictor.League.Entities.Matchup;
import com.StockPredictor.League.Entities.Standings;
import com.StockPredictor.Portfolio.Portfolio;
import com.StockPredictor.Portfolio.PortfolioRepository;
import com.StockPredictor.Indicators.Repository.StockIndicator;
import com.StockPredictor.Indicators.Repository.StockIndicatorRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyScoringService {

    private final MatchupRepository matchupRepo;
    private final StandingsRepository standingsRepo;
    private final PortfolioRepository portfolioRepo;
    private final StockIndicatorRepository stockIndicatorRepo;

    public WeeklyScoringService(MatchupRepository matchupRepo, StandingsRepository standingsRepo, PortfolioRepository portfolioRepo, StockIndicatorRepository stockIndicatorRepo){
        this.matchupRepo = matchupRepo;
        this.standingsRepo = standingsRepo;
        this.portfolioRepo = portfolioRepo;
        this.stockIndicatorRepo = stockIndicatorRepo;
    }

    /**
     * Finalize weekly matchups, calculate portfolio scores, update standings,
     * and reset stock scores for the next week.
     */
    public void finalizeWeek(Long leagueId, int week) {
        List<Matchup> matchups = matchupRepo.findByLeagueIdAndWeek(leagueId, week);

        for (Matchup m : matchups) {
            // ✅ calculate weekly portfolio scores
            double home = calculateWeeklyPortfolioScore(m.getHome().getId());
            double away = calculateWeeklyPortfolioScore(m.getAway().getId());

            m.setHomeScore(home);
            m.setAwayScore(away);

            Standings homeS = standingsRepo.findByMembershipId(m.getHome().getId())
                    .orElse(new Standings());
            homeS.setMembership(m.getHome());

            Standings awayS = standingsRepo.findByMembershipId(m.getAway().getId())
                    .orElse(new Standings());
            awayS.setMembership(m.getAway());

            homeS.setPointsFor(homeS.getPointsFor() + home);
            homeS.setPointsAgainst(homeS.getPointsAgainst() + away);

            awayS.setPointsFor(awayS.getPointsFor() + away);
            awayS.setPointsAgainst(awayS.getPointsAgainst() + home);

            // ✅ decide winner
            if (home > away) {
                homeS.setWins(homeS.getWins() + 1);
                awayS.setLosses(awayS.getLosses() + 1);
                m.setResult("HOME_WIN");
            } else if (away > home) {
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

        // ✅ reset stock scores so next week starts fresh
        resetStockScores();
    }

    /**
     * Calculates score for a user’s portfolio (for this week only).
     * Currently: % change from start-of-week price to last price.
     */
    private double calculateWeeklyPortfolioScore(Long membershipId) {
        List<Portfolio> portfolio = portfolioRepo.findByMembershipId(membershipId);

        return portfolio.stream()
                .mapToDouble(p -> {
                    StockIndicator si = stockIndicatorRepo.findBySymbol(p.getStock().getSymbol())
                            .orElseThrow();

                    if (si.getStartOfWeekPrice() == 0) {
                        return 0.0; // avoid divide-by-zero
                    }

                    double pctChange = ((si.getLastPrice() - si.getStartOfWeekPrice()) / si.getStartOfWeekPrice()) * 100;
                    return pctChange;
                })
                .sum();
    }

    /**
     * After a week ends, clear scores so next week starts fresh.
     */
    private void resetStockScores() {
        List<StockIndicator> all = stockIndicatorRepo.findAll();
        for (StockIndicator si : all) {
            si.setScore(0.0);
            stockIndicatorRepo.save(si);
        }
    }
}
