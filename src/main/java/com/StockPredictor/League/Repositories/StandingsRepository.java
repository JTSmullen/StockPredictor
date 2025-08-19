package com.StockPredictor.League.Repositories;

import com.StockPredictor.League.Entities.Standings;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StandingsRepository extends JpaRepository<Standings, Long> {
    Optional<Standings> findByMembershipId(Long membershipId);
    List<Standings> findByMembershipLeagueId(Long leagueId);
}
