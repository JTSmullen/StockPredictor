package com.StockPredictor.Indicators.Repository;

import com.StockPredictor.Stock.Stock;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_indicators")
public class StockIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @OneToOne
    @JoinColumn(name = "stock_id", nullable = false, unique = true)
    private Stock stock;

    private double lastPrice;
    private double startOfWeekPrice;
    private int lastSnapshotWeek;

    private double score;
    private LocalDateTime lastUpdated;

    // getters / setters

    public Stock getStock() { return stock; }
    public void setStock(Stock stock) { this.stock = stock; }

    public double getLastPrice() { return lastPrice; }
    public void setLastPrice(double lastPrice) { this.lastPrice = lastPrice; }

    public double getStartOfWeekPrice() { return startOfWeekPrice; }
    public void setStartOfWeekPrice(double startOfWeekPrice) { this.startOfWeekPrice = startOfWeekPrice; }

    public int getLastSnapshotWeek() { return lastSnapshotWeek; }
    public void setLastSnapshotWeek(int lastSnapshotWeek) { this.lastSnapshotWeek = lastSnapshotWeek; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
