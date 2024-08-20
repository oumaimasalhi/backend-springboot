package com.FST.GestionDesVentes.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_panier")
public class Panier {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@NotNull(message = "La quantité est obligatoire")
	@Min(value = 0, message = "La quantité ne peut pas être négative")
	@Column(name = "quantite")
	private int quantite;

	@Pattern(regexp = "^(valide|non valide)$", message = "Le statut doit être 'valide' ou 'non valide'")
	@Column(name = "statut")
	private String statut;

	@Column(name = "total", nullable = false)
	private double total;

	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "panier", orphanRemoval = true)
	private List<ProduitPanier> produitsPanier = new ArrayList<>();

	public void addProduit(ProduitPanier produitPanier) {
		produitPanier.setPanier(this);
		this.produitsPanier.add(produitPanier);
	}

	public void removeProduit(ProduitPanier produitPanier) {
		produitPanier.setPanier(null);
		this.produitsPanier.remove(produitPanier);
	}

	public double calculateTotal() {
		double total = 0.0;
		for (ProduitPanier produitPanier : produitsPanier) {
			total += produitPanier.getQuantite() * produitPanier.getProduit().getPrix();
		}
		return total;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getQuantite() {
		return quantite;
	}

	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<ProduitPanier> getProduitsPanier() {
		return produitsPanier;
	}

	public void setProduitsPanier(List<ProduitPanier> produitsPanier) {
		this.produitsPanier = produitsPanier;
	}
	
	
	
}

