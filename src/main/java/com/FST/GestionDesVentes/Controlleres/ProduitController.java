package com.FST.GestionDesVentes.Controlleres;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.FST.GestionDesVentes.Entities.Categorie;
import com.FST.GestionDesVentes.Entities.Panier;
import com.FST.GestionDesVentes.Entities.Produit;
import com.FST.GestionDesVentes.Entities.ProduitPanier;
import com.FST.GestionDesVentes.Repositories.PanierRepository;
import com.FST.GestionDesVentes.Repositories.ProduitRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.FST.GestionDesVentes.Repositories.CategorieRepository;

@RestController
@RequestMapping({ "/produits", "/home*" })
@CrossOrigin(origins = "*")
public class ProduitController {

	private final PanierRepository panierRepository;
	private final ProduitRepository produitRepository;
	private final CategorieRepository categorieRepository;

	@Autowired
	public ProduitController(PanierRepository panierRepository, ProduitRepository produitRepository,
			CategorieRepository categorieRepository) {
		this.panierRepository = panierRepository;
		this.produitRepository = produitRepository;
		this.categorieRepository = categorieRepository;
	}

	@GetMapping("/list")
	public List<Produit> getAllProduits() {
		return (List<Produit>) produitRepository.findAll();
	}

	@PostMapping("/add")
	public ResponseEntity<Produit> createProduit(
			@RequestParam("nom") String nom,
			@RequestParam("description") String description,
			@RequestParam("prix") Double prix,
			@RequestParam("stock") int stock,
			@RequestParam("category") Long categoryId,
			@RequestParam("file") MultipartFile file) throws IOException {

		Produit produit = new Produit();
		produit.setNom(nom);
		produit.setDescription(description);
		produit.setPrix(prix);
		produit.setStock(stock);

		Categorie categorie = categorieRepository.findById(categoryId)
				.orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + categoryId));
		produit.setCategory(categorie);

		if (file != null && !file.isEmpty()) {
			produit.setImage(file.getBytes());
		}

		Produit savedProduit = produitRepository.save(produit);
		return ResponseEntity.ok(savedProduit);
	}

	@PostMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestParam Long produitId, @RequestParam Long panierId) {
		Produit produit = produitRepository.findById(produitId)
				.orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + produitId));

		Panier panier = panierRepository.findById(panierId)
				.orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));

		ProduitPanier produitPanier = new ProduitPanier();
		produitPanier.setProduit(produit);

		panier.addProduit(produitPanier);
		panierRepository.save(panier);

		return ResponseEntity.ok("Produit ajouté au panier avec succès.");
	}

	@PutMapping("/{produitId}")
	public ResponseEntity<Produit> updateProduit(
			@PathVariable Long produitId,
			@RequestParam(value = "image", required = false) MultipartFile image,
			@RequestParam("nom") String nom,
			@RequestParam("description") String description,
			@RequestParam("prix") double prix,
			@RequestParam("stock") int stock,
			@RequestParam("category") String categoryJson) {

		Optional<Produit> produitOptional = produitRepository.findById(produitId);

		if (!produitOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Produit produit = produitOptional.get();

		produit.setNom(nom);
		produit.setDescription(description);
		produit.setPrix(prix);
		produit.setStock(stock);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Categorie category = objectMapper.readValue(categoryJson, Categorie.class);
			produit.setCategory(category);
		} catch (JsonProcessingException e) {
			return ResponseEntity.badRequest().build();
		}

		if (image != null && !image.isEmpty()) {
			try {
				produit.setImage(image.getBytes());
			} catch (IOException e) {
				return ResponseEntity.badRequest().build();
			}
		}

		produitRepository.save(produit);
		return ResponseEntity.ok(produit);
	}

	@DeleteMapping("/{produitId}")
	public ResponseEntity<?> deleteProduit(@PathVariable Long produitId) {
		return produitRepository.findById(produitId).map(produit -> {
			produitRepository.delete(produit);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new IllegalArgumentException("ProduitId " + produitId + " not found"));
	}

	
	
	@GetMapping("/{produitId}")
	public Produit getProduit(@PathVariable Long produitId) {
		Optional<Produit> p = produitRepository.findById(produitId);
		return p.orElseThrow(() -> new IllegalArgumentException("ProduitId " + produitId + " not found"));
	}

	
	
	@GetMapping("/byCategory/{categoryId}")
	public List<Produit> getProduitsByCategoryId(@PathVariable long categoryId) {
		return produitRepository.findByCategory_Id(categoryId);
	}
}