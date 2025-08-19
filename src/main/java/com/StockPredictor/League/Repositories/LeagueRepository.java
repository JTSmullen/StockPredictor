package com.StockPredictor.League.Repositories;

import com.StockPredictor.League.Entities.League;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeagueRepository extends JpaRepository<League, Long> {

    Optional<League> findByInviteCode(String inviteCode);

}
