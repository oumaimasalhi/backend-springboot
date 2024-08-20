
package com.FST.GestionDesVentes.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FST.GestionDesVentes.Entities.ProduitPanier;

@Repository
public interface ProduitPanierRepository extends JpaRepository<ProduitPanier, Long> {
	
}