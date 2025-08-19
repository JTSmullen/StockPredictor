package com.StockPredictor.League.Repositories;

import com.StockPredictor.League.Entities.TeamStock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamStockRepository extends JpaRepository<TeamStock, Long> {

    List<TeamStock> findByMembershipId(Long membershipId);

}
