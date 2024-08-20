package com.FST.GestionDesVentes.Controlleres;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.FST.GestionDesVentes.Entities.Fournisseur;
import com.FST.GestionDesVentes.Repositories.FournisseurRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/fournisseur")
@CrossOrigin(origins="*")
public class FournisseurController {

	
	@Autowired
    private FournisseurRepository fournisseurRepository;
	
	
	 @GetMapping("/list")
	    public List<Fournisseur> getAllFournisseurs() {
	        return fournisseurRepository.findAll();
	    }

	 
	 
	  @PostMapping("/add")
	    public Fournisseur createFournisseur(@Valid @RequestBody Fournisseur fournisseur) {
	        return fournisseurRepository.save(fournisseur);
	    }
	    
	    
	    
	    
	  @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteFournisseur(@PathVariable Long id) {
	        return fournisseurRepository.findById(id).map(fournisseur -> {
	            fournisseurRepository.delete(fournisseur);
	            return ResponseEntity.ok().build();
	        }).orElseThrow(() -> new IllegalArgumentException("FournisseurId " + id + " not found"));
	    }

	    
	  
	  @GetMapping("/{fournisseurId}")
	    public Fournisseur getFournisseur(@PathVariable Long fournisseurId) {
	        Optional<Fournisseur> f = fournisseurRepository.findById(fournisseurId);
	        return f.orElseThrow(() -> new IllegalArgumentException("Fournisseur with ID " + fournisseurId + " not found"));
	    }    
	
	
       
	  
	  
	  @PutMapping("/{fournisseurId}")
	    public Fournisseur updateFournisseur(@PathVariable Long fournisseurId, @Valid @RequestBody Fournisseur fournisseurRequest) {
	        return fournisseurRepository.findById(fournisseurId).map(fournisseur -> {
	            fournisseur.setNomSociete(fournisseurRequest.getNomSociete());
	            fournisseur.setNom(fournisseurRequest.getNom());
	            fournisseur.setEmail(fournisseurRequest.getEmail());
	            fournisseur.setAdresse(fournisseurRequest.getAdresse());
	            fournisseur.setTelephone(fournisseurRequest.getTelephone());
	            return fournisseurRepository.save(fournisseur);
	        }).orElseThrow(() -> new IllegalArgumentException("Fournisseur avec l'ID " + fournisseurId + " non trouvé"));
	    }





    

    
    
   

    

   
}
