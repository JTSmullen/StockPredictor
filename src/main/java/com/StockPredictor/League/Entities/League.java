package com.StockPredictor.League.Entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "leagues")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String inviteCode;
    private int leagueSize;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<LeagueMembership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<WeeklyLeaderboard> leaderboards = new ArrayList<>();

    public void setName(String name){
        this.name = name;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

}
