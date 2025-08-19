package com.StockPredictor.Controller;

import com.StockPredictor.Indicators.Indicators.*;
import com.StockPredictor.Score.ScoreEntity;
import com.StockPredictor.User.User;
import com.StockPredictor.Service.ScoreService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scores")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService){
        this.scoreService = scoreService;
    }

    @PostMapping("/{symbol}")
    public ScoreEntity scoreStock(@PathVariable String symbol,
                                  @RequestParam double entryPrice,
                                  @RequestParam double currentPrice) {
        // TODO: fetch proper indicators not dummy ones
        EMA ema = new EMA(12);
        SMA sma = new SMA(20);
        RSI rsi = new RSI(14);
        ATR atr = new ATR(14);
        BollingerBands bb = new BollingerBands(20, 2);

        User user = new User();
        user.setUsername("demo");
        user.setEmail("demo@example.com");
        user.setPasswordHash("hashed");

        return scoreService.calculateAndSave(user, symbol, entryPrice, currentPrice, ema, sma, rsi, atr, bb);
    }

}
