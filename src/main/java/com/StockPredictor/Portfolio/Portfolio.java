package com.StockPredictor.Portfolio;

import com.StockPredictor.League.Entities.LeagueMembership;
import com.StockPredictor.Stock.Stock;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "portfolios",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"membership_id", "stock_id"})
        }
)
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Belongs to a specific league membership (user in league)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id", nullable = false)
    private LeagueMembership membership;

    // The drafted stock
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // Whether it was picked in the draft
    private boolean picked = false;

    public Portfolio() {}

    public Portfolio(LeagueMembership membership, Stock stock) {
        this.membership = membership;
        this.stock = stock;
        this.picked = true;
    }

    public Stock getStock(){
        return stock;
    }
}
