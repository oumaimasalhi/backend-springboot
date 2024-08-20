package com.FST.GestionDesVentes.Entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_commande")
public class Commande {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;


	@Column(name = "date_commande", nullable = false)
	private LocalDateTime dateCommande;
	
	@Column(name = "total", nullable = false)
	private double total; // Ajout d'un champ total

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	@Column(name = "panier_id", nullable = false)
	private Long panierId;

	@PrePersist
	protected void onCreate() {
		this.dateCommande = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public LocalDateTime getDateCommande() {
		return dateCommande;
	}

	public void setDateCommande(LocalDateTime dateCommande) {
		this.dateCommande = dateCommande;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Long getPanierId() {
		return panierId;
	}

	public void setPanierId(Long panierId) {
		this.panierId = panierId;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}