package com.StockPredictor.Service;

import com.StockPredictor.League.Repositories.*;
import com.StockPredictor.League.Entities.Matchup;
import com.StockPredictor.League.Entities.Standings;
import com.StockPredictor.League.Entities.League;
import com.StockPredictor.League.Entities.LeagueMembership;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@Service
public class MatchupService {

    private final MatchupRepository matchupRepo;
    private final StandingsRepository standingsRepo;

    public MatchupService(MatchupRepository matchupRepo, StandingsRepository standingsRepo) {
        this.matchupRepo = matchupRepo;
        this.standingsRepo = standingsRepo;
    }

    public List<Matchup> generateWeeklyMatchups(League league, int week, List<LeagueMembership> members) {
        Collections.shuffle(members);
        List<Matchup> matchups = new ArrayList<>();

        for (int i = 0; i < members.size(); i += 2) {
            if (i + 1 < members.size()) {
                Matchup m = new Matchup();
                m.setLeague(league);
                m.setWeek(week);
                m.setHome(members.get(i));
                m.setAway(members.get(i + 1));
                matchups.add(m);
            } else {
                Standings s = standingsRepo.findByMembershipId(members.get(i).getId()).orElse(new Standings());
                s.setMembership(members.get(i));
                s.setWins(s.getWins() + 1);
                standingsRepo.save(s);
            }
        }

        return matchupRepo.saveAll(matchups);
    }

}
