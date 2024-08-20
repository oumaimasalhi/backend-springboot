package com.FST.GestionDesVentes.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.FST.GestionDesVentes.Entities.Panier;


@Repository
public interface PanierRepository extends JpaRepository < Panier, Long>{

}


