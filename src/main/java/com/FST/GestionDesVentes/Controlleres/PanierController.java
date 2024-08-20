package com.FST.GestionDesVentes.Controlleres;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FST.GestionDesVentes.Entities.Commande;
import com.FST.GestionDesVentes.Entities.Panier;
import com.FST.GestionDesVentes.Entities.Produit;
import com.FST.GestionDesVentes.Entities.ProduitPanier;
import com.FST.GestionDesVentes.Repositories.CommandeRepository;
import com.FST.GestionDesVentes.Repositories.PanierRepository;
import com.FST.GestionDesVentes.Repositories.ProduitRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
@RequestMapping("/panier")
public class PanierController {

	private final PanierRepository panierRepository;
	private final ProduitRepository produitRepository;
	private final CommandeRepository commandeRepository;

	@Autowired
	public PanierController(PanierRepository panierRepository, ProduitRepository produitRepository,
			CommandeRepository commandeRepository) {
		this.panierRepository = panierRepository;
		this.produitRepository = produitRepository;
		this.commandeRepository = commandeRepository;
	}

	@GetMapping("/list")
	public List<Panier> getAllPaniers() {
		return (List<Panier>) panierRepository.findAll();
	}

	
	
	@PostMapping("/add")
	public ResponseEntity<String> createPanier(@RequestBody Panier panier) {
		
		
		// Enregistrez le panier
		try {
			panierRepository.save(panier);
			return ResponseEntity.ok("Panier créé avec succès");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Erreur lors de la création du panier : " + e.getMessage());
		}
	}
	
	
	@PutMapping("/{panierId}")
	public ResponseEntity<Panier> updatePanier(@PathVariable Long panierId, @Valid @RequestBody Panier panierRequest) {
		return panierRepository.findById(panierId).map(panier -> {
			panier.setQuantite(panierRequest.getQuantite());
			panier.setStatut(panierRequest.getStatut().trim());
			Panier updatedPanier = panierRepository.save(panier);
			return ResponseEntity.ok(updatedPanier);
		}).orElseThrow(() -> new IllegalArgumentException("PanierId " + panierId + " not found"));
	}
	
	
	@Transactional
	@DeleteMapping("/{panierId}")
	public ResponseEntity<?> deletePanier(@PathVariable Long panierId) {
		return panierRepository.findById(panierId).map(panier -> {
			// Trouver les commandes associées au panier
			List<Commande> commandes = commandeRepository.findByPanierId(panierId);

			// Supprimer les commandes associées
			commandeRepository.deleteAll(commandes);

			// Supprimer le panier
			panierRepository.delete(panier);

			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new IllegalArgumentException("PanierId " + panierId + " not found"));
	}

	
	
	@GetMapping("/{panierId}")
	public ResponseEntity<Panier> getPanier(@PathVariable Long panierId) {
		Optional<Panier> p = panierRepository.findById(panierId);
		return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	
	
	@GetMapping("/increaseQuantity/{panierId}")
	public ResponseEntity<Panier> increaseQuantity(@PathVariable Long panierId) {
		return panierRepository.findById(panierId).map(panier -> {
			panier.setQuantite(panier.getQuantite() + 1);
			Panier updatedPanier = panierRepository.save(panier);
			return ResponseEntity.ok(updatedPanier);
		}).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PostMapping("/addProduitToPanier/{idPanier}/{idProduit}")
	public ResponseEntity<String> addProduitToPanier(
			@PathVariable long idPanier,
			@PathVariable long idProduit,
			@RequestBody Map<String, Integer> requestBody) {
		Panier panier = panierRepository.findById(idPanier).orElse(null);
		Produit produit = produitRepository.findById(idProduit).orElse(null);
		int quantite = requestBody.get("quantite");

		// Vérifier si le panier et le produit existent
		if (panier == null || produit == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Panier ou Produit non trouvé");
		}

		if (produit.getStock() < quantite) {
			return ResponseEntity.badRequest().body("Stock insuffisant pour le produit : " + produit.getNom());
		}

		produit.setStock(produit.getStock() - quantite);
		produitRepository.save(produit);


		try {
			// Créer une nouvelle instance de ProduitPanier et l'ajouter au panier
			ProduitPanier produitPanier = new ProduitPanier();
			produitPanier.setProduit(produit);
			produitPanier.setPanier(panier);
			produitPanier.setQuantite(quantite); // Example, set to 1 or any other logic

			panier.getProduitsPanier().add(produitPanier);

			panierRepository.findById(panier.getId()).map(panierC -> {
				double total = panierC.calculateTotal();
				panierC.setTotal(total);
				return ResponseEntity.ok(total);
			}).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

			panierRepository.save(panier);
			return ResponseEntity.ok("Produit ajouté au panier");
		} catch (Exception e) {
			// Gérer les autres exceptions
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de l'ajout du produit au panier");
		}
	}

	@GetMapping("removeProduitFromPanier/{idPanier}/{idProduit}")
	public ResponseEntity<Panier> removeProduitFromPanier(@PathVariable long idPanier, @PathVariable long idProduit) {
		Panier panier = panierRepository.findById(idPanier).orElse(null);
		Produit produit = produitRepository.findById(idProduit).orElse(null);

		if (panier == null || produit == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		panier.getProduitsPanier().removeIf(produitPanier -> produitPanier.getProduit().getId() == idProduit);
		panierRepository.findById(panier.getId()).map(panierC -> {
			double total = panierC.calculateTotal();
			panierC.setTotal(total);
			return ResponseEntity.ok(total);
		}).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		Panier updatedPanier = panierRepository.save(panier);

		return ResponseEntity.ok(updatedPanier);
	}

}