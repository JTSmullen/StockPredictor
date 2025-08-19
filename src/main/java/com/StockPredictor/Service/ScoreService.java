package com.StockPredictor.Service;

import com.StockPredictor.Indicators.Indicators.*;
import com.StockPredictor.Indicators.Repository.StockIndicatorRepository;
import com.StockPredictor.Indicators.Repository.StockIndicator;
import com.StockPredictor.Indicators.Repository.StockIndicatorRepository;
import com.StockPredictor.Score.*;
import com.StockPredictor.User.*;
import com.StockPredictor.Stock.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepo;
    private final StockIndicatorRepository stockIndicatorRepo;

    public ScoreService(ScoreRepository scoreRepo, StockRepository stockRepo, StockIndicatorRepository stockIndicatorRepo) {
        this.scoreRepo = scoreRepo;
        this.stockIndicatorRepo = stockIndicatorRepo;
    }

    public StockIndicator calculateAndSave(String symbol, double entryPrice, double currentPrice,
                                           EMA ema, SMA sma, RSI rsi, ATR atr, BollingerBands bb) {

        Score scorer = new Score(entryPrice, ema, sma, rsi, atr, bb);
        double fantasyScore = scorer.calculateScore(currentPrice);

        StockIndicator indicator = stockIndicatorRepo.findById(symbol)
                .orElseGet(() -> {
                    StockIndicator si = new StockIndicator();
                    si.setSymbol(symbol);
                    return si;
                });

        indicator.setLastPrice(currentPrice);
        indicator.setScore(fantasyScore);
        indicator.setLastUpdated(LocalDateTime.now());

        return stockIndicatorRepo.save(indicator);
    }

}
