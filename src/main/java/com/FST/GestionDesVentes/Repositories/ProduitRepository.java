package com.FST.GestionDesVentes.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FST.GestionDesVentes.Entities.Produit;




@Repository
public interface ProduitRepository extends JpaRepository < Produit, Long> {

	
	 List<Produit> findByCategory_Id(Long categoryId);
}


