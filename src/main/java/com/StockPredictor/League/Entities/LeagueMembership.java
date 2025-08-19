package com.StockPredictor.League.Entities;

import com.StockPredictor.User.User;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "league_memberships")
public class LeagueMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    @OneToMany(mappedBy = "membership", cascade = CascadeType.ALL)
    private List<TeamStock> teamStocks = new ArrayList<>();

    public Long getId(){
        return id;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setLeague(League league) {
        this.league = league;
    }

}
