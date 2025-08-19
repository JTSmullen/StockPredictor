package com.StockPredictor.League.Entities;


import com.StockPredictor.League.Entities.League;
import com.StockPredictor.League.Entities.LeagueMembership;
import jakarta.persistence.*;

@Entity
@Table(name = "weekly_leaderboard")
public class WeeklyLeaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int week;
    private double totalPoints;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private LeagueMembership membership;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;
}
