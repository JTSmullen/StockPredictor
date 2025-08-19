package com.StockPredictor.Service;

import com.StockPredictor.Indicators.Indicators.*;
import com.StockPredictor.Indicators.Repository.StockIndicator;
import com.StockPredictor.Score.Score;
import com.StockPredictor.Score.WeeklyScoreRepository;
import com.StockPredictor.Indicators.Repository.StockIndicatorRepository;

import com.StockPredictor.Score.WeeklyScore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class StockService {

    private final StockIndicatorRepository stockRepo;
    private final WeeklyScoreRepository weeklyScoreRepo;
    private final Map<String, EMA> emaMap = new HashMap<>();
    private final Map<String, SMA> smaMap = new HashMap<>();
    private final Map<String, RSI> rsiMap = new HashMap<>();
    private final Map<String, ATR> atrMap = new HashMap<>();
    private final Map<String, BollingerBands> bbMap = new HashMap<>();

    public StockService(StockIndicatorRepository stockRepo, WeeklyScoreRepository weeklyScoreRepo) {
        this.stockRepo = stockRepo;
        this.weeklyScoreRepo = weeklyScoreRepo;
    }

    public void seedStock(String symbol, List<Double> historicalPrices, List<Double> historicalHighs, List<Double> historicalLows) {
        EMA ema = new EMA(20);
        SMA sma = new SMA(20);
        RSI rsi = new RSI(14);
        ATR atr = new ATR(14);
        BollingerBands bb = new BollingerBands(20, 2);

        for (double price : historicalPrices) {
            ema.setEMA(price);
            sma.setSMA(price);
            rsi.addCandle(price);
            bb.setBollingerBands(price);
        }

        for (int i = 0; i < historicalPrices.size(); i++) {
            atr.setATR(historicalHighs.get(i), historicalLows.get(i), historicalPrices.get(0));
        }

        emaMap.put(symbol, ema);
        smaMap.put(symbol, sma);
        rsiMap.put(symbol, rsi);
        atrMap.put(symbol, atr);
        bbMap.put(symbol, bb);

        double lastPrice = historicalPrices.get(historicalPrices.size() -1);
        Score scoreCalculator = new Score(lastPrice, ema, sma, rsi, atr, bb);
        double score = scoreCalculator.calculateScore(lastPrice);

        StockIndicator stock = new StockIndicator();
        stock.setSymbol(symbol);
        stock.setLastPrice(lastPrice);
        stock.setScore(score);
        stock.setLastUpdated(LocalDateTime.now());
        stockRepo.save(stock);
    }

    public void updateStockPrice(String symbol, double newPrice, double newHigh, double newLow) {
        StockIndicator stock = stockRepo.findById(symbol).orElseThrow();

        EMA ema = emaMap.get(symbol);
        SMA sma = smaMap.get(symbol);
        RSI rsi = rsiMap.get(symbol);
        ATR atr = atrMap.get(symbol);
        BollingerBands bb = bbMap.get(symbol);

        ema.setEMA(newPrice);
        sma.setSMA(newPrice);
        rsi.addCandle(newPrice);
        atr.setATR(newHigh, newLow, newPrice);
        bb.setBollingerBands(newPrice);

        Score scoreCalculator = new Score(stock.getLastPrice(), ema, sma, rsi, atr, bb);
        double score = scoreCalculator.calculateScore(newPrice);

        stock.setLastPrice(newPrice);
        stock.setScore(score);
        stock.setLastUpdated(LocalDateTime.now());
        stockRepo.save(stock);
    }

    @Transactional
    public void saveWeeklyScores(LocalDate weekStart, LocalDate weekEnd) {
        List<StockIndicator> stocks = stockRepo.findAll();
        for (StockIndicator stock : stocks) {
            WeeklyScore ws = new WeeklyScore();
            ws.setSymbol(stock.getSymbol());
            ws.setWeekStart(weekStart);
            ws.setWeekEnd(weekEnd);
            ws.setFinalScore(stock.getScore());
            weeklyScoreRepo.save(ws);
        }
    }

}
