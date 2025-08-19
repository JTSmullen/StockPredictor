package com.StockPredictor.Indicators.Repository;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_indicators")
public class StockIndicator {

    @Id
    private String symbol;
    private double lastPrice;
    private double score;
    private LocalDateTime lastUpdated;

    // getters / setters

    public void setLastPrice(double lastPrice){
        this.lastPrice = lastPrice;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public double getScore() {
        return score;
    }
}

