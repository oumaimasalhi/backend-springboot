package com.FST.GestionDesVentes.Entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_client")
public class Client {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Le nom est obligatoire")
    @Size(min = 3, max = 20, message = "Le nom doit contenir entre 3 et 20 caractères")
    @Column(name = "nom")
    private String nom;

    @NotEmpty(message = "Le prénom est obligatoire")
    @Size(min = 3, max = 20, message = "Le prénom doit contenir entre 3 et 20 caractères")
    @Column(name = "prenom")
    private String prenom;

    @NotEmpty(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "L'adresse est obligatoire")
    @Column(name = "adresse")
    private String adresse;

    @NotEmpty(message = "Le téléphone est obligatoire")
    @Column(name = "telephone")
    private String telephone;
	
	
    @OneToMany
    private List<Commande> commandes;


  
    
    
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public List<Commande> getCommandes() {
		return commandes;
	}


	public void setCommandes(List<Commande> commandes) {
		this.commandes = commandes;
	}
    
   

	
   
    
	
}