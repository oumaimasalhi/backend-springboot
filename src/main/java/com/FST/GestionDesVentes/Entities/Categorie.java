package com.FST.GestionDesVentes.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "t_Categorie")
public class Categorie {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	@NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit comporter entre 3 et 50 caractères")
	@NotBlank(message = "Le nom est obligatoire")
	@Column (name="Nom")
    private String nom;
	
	@NotBlank(message = "La description est obligatoire")
    @Size(min = 5, max = 500, message = "La description doit comporter entre 5 et 500 caractères")
	@Column (name="Description")
    private String description ;
    
	@JsonIgnore //pour éviter boucle infinie
	@OneToMany(mappedBy = "category")
    private List<Produit> produits;




	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Produit> getProduits() {
		return produits;
	}

	public void setProduits(List<Produit> produits) {
		this.produits = produits;
	}

	public Categorie(@NotBlank(message = "Le nom est obligatoire") String nom, String description,
			List<Produit> produits) {
		super();
		this.nom = nom;
		this.description = description;
		this.produits = produits;
	}

	public Categorie() {
		super();
	}

	
	
	
	
	
}