package com.StockPredictor.League.Entities;

import com.StockPredictor.League.Entities.LeagueMembership;
import jakarta.persistence.*;

@Entity
@Table(name = "standings")
public class Standings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private LeagueMembership membership;

    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private double pointsFor = 0;
    private double pointsAgainst = 0;

    public Long getId(){
        return id;
    }

    public int getWins(){
        return wins;
    }

    public int getLosses(){
        return losses;
    }

    public void setMembership(LeagueMembership membership) {
        this.membership = membership;
    }

    public void setWins(int wins){
        this.wins = wins;
    }

    public void setPointsFor(double pointsFor) {
        this.pointsFor = pointsFor;
    }

    public double getPointsFor(){
        return pointsFor;
    }

    public void setPointsAgainst(double pointsAgainst){
        this.pointsAgainst = pointsAgainst;
    }

    public double getPointsAgainst(){
        return pointsAgainst;
    }

    public void setLosses(int losses){
        this.losses = losses;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getTies() {
        return ties;
    }

}
