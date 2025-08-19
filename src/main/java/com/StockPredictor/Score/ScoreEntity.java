package com.StockPredictor.Score;

import com.StockPredictor.Stock.Stock;
import com.StockPredictor.User.User;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "scores")
public class ScoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Stock stock;

    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(precision = 5, scale = 1)
    private Double score;

    public void setUser(User user){
        this.user = user;
    }

    public void setStock(Stock stock){
        this.stock = stock;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
