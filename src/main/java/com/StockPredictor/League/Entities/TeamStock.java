package com.StockPredictor.League.Entities;

import com.StockPredictor.League.Entities.LeagueMembership;
import jakarta.persistence.*;

@Entity
@Table(name = "team_stocks")
public class TeamStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @ManyToOne
    @JoinColumn(name = "membership_id")
    private LeagueMembership membership;

}
