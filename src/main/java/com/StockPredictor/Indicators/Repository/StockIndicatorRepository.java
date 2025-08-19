package com.StockPredictor.Indicators.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockIndicatorRepository extends JpaRepository<StockIndicator, String> {

    Optional<StockIndicator> findBySymbol(String symbol);

}
