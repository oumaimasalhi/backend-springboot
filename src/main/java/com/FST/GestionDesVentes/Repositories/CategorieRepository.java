package com.FST.GestionDesVentes.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.FST.GestionDesVentes.Entities.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository < Categorie, Long>{

}



