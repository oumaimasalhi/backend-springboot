package com.FST.GestionDesVentes.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "t_Produit")
public class Produit {

	@Id
	@GeneratedValue( strategy= GenerationType.AUTO)
	private long id ;
	
	@NotBlank(message = "Le nom est obligatoire")
	@Column(name = "nom")
	private String nom;
	
	@Column(name = "description")
	private String description;
	
	@NotNull(message = "Le prix est obligatoire")
	@Column (name="prix")
	private Double prix ;
	
	@NotNull(message = "Le stock est obligatoire")
	@Column(name = "stock")
	private int stock;

	@Column(name = "image",length = 1000000)
	@Lob
	private byte[] image;
	
	

	
	@ManyToOne
	private Categorie category;


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


	public Double getPrix() {
		return prix;
	}


	public void setPrix(Double prix) {
		this.prix = prix;
	}


	public int getStock() {
		return stock;
	}


	public void setStock(int stock) {
		this.stock = stock;
	}


	public byte[] getImage() {
		return image;
	}


	public void setImage(byte[] image) {
		this.image = image;
	}





	public Categorie getCategory() {
		return category;
	}


	public void setCategory(Categorie category) {
		this.category = category;
	}





	
	

	
}