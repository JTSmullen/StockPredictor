package com.StockPredictor.Controller;

import com.StockPredictor.League.Repositories.*;
import com.StockPredictor.User.UserRepository;
import com.StockPredictor.League.Entities.LeagueMembership;
import com.StockPredictor.League.Entities.League;
import com.StockPredictor.User.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leagues")
public class LeagueController {

    private final LeagueRepository leagueRepo;
    private final LeagueMembershipRepository membershipRepo;
    private final UserRepository userRepo;

    public LeagueController(LeagueRepository leagueRepo, LeagueMembershipRepository membershipRepo, UserRepository userRepo) {
        this.leagueRepo = leagueRepo;
        this.membershipRepo = membershipRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/create")
    public League createLeague(@RequestParam String leagueName, @RequestParam Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        League league = new League();
        league.setName(leagueName);
        league.setInviteCode(UUID.randomUUID().toString().substring(0,6));
        league = leagueRepo.save(league);

        LeagueMembership membership = new LeagueMembership();
        membership.setUser(user);
        membership.setLeague(league);
        membershipRepo.save(membership);

        return league;
    }

    @PostMapping("/join")
    public League joinLeague(@RequestParam String inviteCode, @RequestParam Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        League league = leagueRepo.findByInviteCode(inviteCode).orElseThrow();

        LeagueMembership membership = new LeagueMembership();
        membership.setUser(user);
        membership.setLeague(league);
        membershipRepo.save(membership);

        return league;
    }

    @GetMapping("/{leagueId}/members")
    public List<LeagueMembership> getMembers(@PathVariable Long leagueId) {
        return membershipRepo.findByLeagueId(leagueId);
    }

}
