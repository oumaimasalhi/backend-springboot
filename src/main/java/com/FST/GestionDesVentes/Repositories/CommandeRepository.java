package com.FST.GestionDesVentes.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FST.GestionDesVentes.Entities.Commande;

@Repository
public interface CommandeRepository extends JpaRepository < Commande, Long>{

	List<Commande> findByPanierId(Long panierId);
}
