package com.FST.GestionDesVentes.Entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_Facture")
public class Facture {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private long id;
	  
	  
	  @Column(nullable = false)
	  private Double totalAvecInteret;



	  @OneToOne
	  @JoinColumn(name = "commande_id", nullable = false)
	  private Commande commande;

	  
	  @Enumerated(EnumType.STRING)
	  @Column(nullable = false)
	  private PaiementType PaiementType;
	  
	  

		
	  @Enumerated(EnumType.STRING)
	  @Column(nullable = false)
	   private FacilitePaiement facilitePaiement;

	 

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Double getTotalAvecInteret() {
		return totalAvecInteret;
	}


	public void setTotalAvecInteret(Double totalAvecInteret) {
		this.totalAvecInteret = totalAvecInteret;
	}


	public Commande getCommande() {
		return commande;
	}


	public void setCommande(Commande commande) {
		this.commande = commande;
	}


	public PaiementType getPaiementType() {
		return PaiementType;
	}


	public void setPaiementType(PaiementType paiementType) {
		PaiementType = paiementType;
	}


	public FacilitePaiement getFacilitePaiement() {
		return facilitePaiement;
	}


	public void setFacilitePaiement(FacilitePaiement facilitePaiement) {
		this.facilitePaiement = facilitePaiement;
	}

		

		@PrePersist
		public void calculerTotalAvecInteret() {
			double total = commande.getTotal();
			switch (facilitePaiement) {
				case TROIS_MOIS:
					totalAvecInteret = total * 1.05;
					break;
				case SIX_MOIS:
					totalAvecInteret = total * 1.10;
					break;
				case UN_AN:
					totalAvecInteret = total * 1.20;
					break;
				case CONTANT:
				default:
					totalAvecInteret = total;
					break;
			}
		}
		public void mettreAJourTotalAvecInteret() {
			double total = commande.getTotal();
			switch (facilitePaiement) {
				case TROIS_MOIS:
					totalAvecInteret = total * 1.05;
					break;
				case SIX_MOIS:
					totalAvecInteret = total * 1.10;
					break;
				case UN_AN:
					totalAvecInteret = total * 1.20;
					break;
				case CONTANT:
				default:
					totalAvecInteret = total;
					break;
			}
		}
		
		
		
		
}