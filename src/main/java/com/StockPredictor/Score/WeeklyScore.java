package com.StockPredictor.Score;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "weekly_scores")
public class WeeklyScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private double finalScore;

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

}
