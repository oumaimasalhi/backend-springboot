
package com.FST.GestionDesVentes.Controlleres;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.FST.GestionDesVentes.Entities.Categorie;
import com.FST.GestionDesVentes.Entities.Produit;
import com.FST.GestionDesVentes.Repositories.CategorieRepository;
import com.FST.GestionDesVentes.Repositories.ProduitRepository;

import jakarta.validation.Valid;



@RestController
@CrossOrigin (origins="*")
@RequestMapping("/categories")
public class CategorieController {
	
	

    @Autowired
    private CategorieRepository categorierepository;

    @Autowired
    private ProduitRepository produitrepository;



    @GetMapping("/list")
    public List<Categorie> getAllCategories() {
        return  (List<Categorie>)categorierepository.findAll();
    }

 
    @PostMapping("/add")
    public Categorie createCategorie(@RequestBody Categorie categorie) {
        return categorierepository.save(categorie);
    }




    
    @PutMapping("/{categorieId}")
    public Categorie updateCategorie(@PathVariable Long categorieId, @Valid @RequestBody Categorie categorieRequest) {
        return categorierepository.findById(categorieId).map(categorie -> {
            categorie.setNom(categorieRequest.getNom());
            categorie.setDescription(categorieRequest.getDescription());
            return categorierepository.save(categorie);
        }).orElseThrow(() -> new IllegalArgumentException("CategorieId " + categorieId + " not found"));
    }


    
    
    
    @DeleteMapping("/{categorieId}")
    public ResponseEntity<?> deleteCategorie(@PathVariable Long categorieId) {
        return categorierepository.findById(categorieId).map(categorie -> {
            List<Produit> produits = produitrepository.findByCategory_Id(categorieId);
            if (produits.isEmpty()) {
                categorierepository.deleteById(categorieId);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossible de supprimer la catégorie car des produits y sont associés.");
            }
        }).orElseThrow(() -> new IllegalArgumentException("CategorieId " + categorieId + " not found"));
    }

    
    
    @GetMapping("/{categorieId}")
    public Categorie getCategorie(@PathVariable Long categorieId) {

     Optional<Categorie> c = categorierepository.findById(categorieId);

     return c.get();

}
        



}        
        
