package com.StockPredictor.Scheduler;

import com.StockPredictor.Service.StockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;

@Component
public class StockScheduler {

    private final StockService stockService;

    @Autowired
    public StockScheduler(StockService stockService) {
        this.stockService = stockService;
    }

    @Scheduled(fixedRate = 10 * 60 * 1000) // Every 10 minutes
    public void updateStockPrices() throws InterruptedException {
        List<String> symbols = List.of("AAPL", "MSFT", "TSLA", "SPY", "NVDA");
        for (String symbol : symbols) {
            Map<String, Double> stockData = fetchLatestPrice(symbol);
            stockService.updateStockPrice(symbol, stockData.get("newPrice"), stockData.get("newHigh"), stockData.get("newLow"));
            Thread.sleep(1000);
        }
    }

    @Scheduled(cron = "0 0 23 ? * SUN") // Every Sunday at 11 PM
    public void saveWeeklyScores() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = today.with(DayOfWeek.FRIDAY);
        stockService.saveWeeklyScores(weekStart, weekEnd);
    }

    private Map<String, Double> fetchLatestPrice(String symbol) {
        // finnhub API call here
        // Filler data
        Map<String, Double> stockData = new HashMap<>();
        stockData.put("newPrice", 100.0);
        stockData.put("newHigh", 101.0);
        stockData.put("newLow", 99.0);
        return stockData;
    }

}
