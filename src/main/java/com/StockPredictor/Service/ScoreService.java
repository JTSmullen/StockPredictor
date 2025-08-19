package com.StockPredictor.Service;

import com.StockPredictor.Indicators.Indicators.*;
import com.StockPredictor.Score.*;
import com.StockPredictor.User.*;
import com.StockPredictor.Stock.*;

import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepo;
    private final StockRepository stockRepo;

    public ScoreService(ScoreRepository scoreRepo, StockRepository stockRepo) {
        this.scoreRepo = scoreRepo;
        this.stockRepo = stockRepo;
    }

    public ScoreEntity calculateAndSave(User user, String symbol, double entryPrice, double currentPrice,
                                        EMA ema, SMA sma, RSI rsi, ATR atr, BollingerBands bb) {

        // Ensure Stock is in database
        Stock stock = stockRepo.findBySymbol(symbol)
                .orElseGet(() -> {
                    Stock s = new Stock();
                    s.setSymbol(symbol);
                    return stockRepo.save(s);
                });

        Score scorer = new Score(entryPrice, ema, sma, rsi, atr, bb);
        double fantasyScore = scorer.calculateScore(currentPrice);

        ScoreEntity scoreEntity = new ScoreEntity();
        scoreEntity.setUser(user);
        scoreEntity.setStock(stock);
        scoreEntity.setScore(fantasyScore);

        return scoreRepo.save(scoreEntity);

    }

}
