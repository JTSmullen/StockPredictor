package com.StockPredictor.League.Entities;

import com.StockPredictor.League.Entities.League;
import com.StockPredictor.League.Entities.LeagueMembership;
import jakarta.persistence.*;

@Entity
@Table(name = "matchups")
public class Matchup {

    private enum Outcomes {
        HOME_WIN,
        AWAY_WIN,
        TIE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int week;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    @ManyToOne
    @JoinColumn(name = "home_membership_id")
    private LeagueMembership home;

    @ManyToOne
    @JoinColumn(name = "away_membership_id")
    private LeagueMembership away;

    private Double homeScore;
    private Double awayScore;
    private String result; // "HOME_WIN", "AWAY_WIN" ect.

    public void setLeague(League league) {
        this.league = league;
    }

    public void setWeek(int week){
        this.week = week;
    }

    public void setHome(LeagueMembership home){
        this.home = home;
    }

    public void setAway(LeagueMembership away) {
        this.away = away;
    }

    public Double getHomeScore(){
        return homeScore;
    }

    public void setHomeScore(Double homeScore){
        this.homeScore = homeScore;
    }

    public void setAwayScore(Double awayScore){
        this.awayScore = awayScore;
    }

    public Double getAwayScore(){
        return awayScore;
    }

    public LeagueMembership getHome(){
        return home;
    }

    public LeagueMembership getAway(){
        return away;
    }

    public void setResult(String result){
        try{
            Outcomes.valueOf(result);
            this.result = result;
        } catch (IllegalArgumentException e){
            // Throw error
        }
    }

}
