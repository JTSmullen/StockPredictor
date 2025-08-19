package com.StockPredictor.Service;

import com.StockPredictor.Indicators.Repository.StockIndicator;
import com.StockPredictor.Indicators.Repository.StockIndicatorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklySnapshotService {

    private final StockIndicatorRepository stockIndicatorRepo;

    public WeeklySnapshotService(StockIndicatorRepository stockIndicatorRepo){
        this.stockIndicatorRepo = stockIndicatorRepo;
    }

    public void snapshotWeek(int weekNumber) {
        List<StockIndicator> indicators = stockIndicatorRepo.findAll();

        for (StockIndicator s : indicators) {
            s.setStartOfWeekPrice(s.getLastPrice());
            s.setLastSnapshotWeek(weekNumber);
            stockIndicatorRepo.save(s);
        }
    }
}
