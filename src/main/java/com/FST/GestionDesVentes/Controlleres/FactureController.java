package com.FST.GestionDesVentes.Controlleres;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FST.GestionDesVentes.Entities.Commande;
import com.FST.GestionDesVentes.Entities.FacilitePaiement;
import com.FST.GestionDesVentes.Entities.Facture;
import com.FST.GestionDesVentes.Entities.PaiementType;
import com.FST.GestionDesVentes.Repositories.CommandeRepository;
import com.FST.GestionDesVentes.Repositories.FactureRepository;
import jakarta.validation.Valid;



@CrossOrigin("*")
@RestController
@RequestMapping("/facture")
public class FactureController {

	   @Autowired
	   FactureRepository factureRepository ;

	 
	   @Autowired
	   CommandeRepository commandeRepository;
	  
	    @GetMapping("/list")
	    public List<Facture> getAllFactures(){
	        return (List<Facture> )factureRepository.findAll();
	    }
	    

	    @PostMapping("/add")
		public ResponseEntity<String> creerFacture(@RequestParam Long commandeId, @RequestParam PaiementType paiementType, @RequestParam FacilitePaiement facilitePaiement) {
			Commande commande = commandeRepository.findById(commandeId).orElse(null);
			if (commande == null) {
				return ResponseEntity.badRequest().body("Commande non trouvée");
			}

			Facture facture = new Facture();
			facture.setCommande(commande);
			facture.setPaiementType(paiementType);
			facture.setFacilitePaiement(facilitePaiement);

			factureRepository.save(facture);

			return ResponseEntity.ok("Facture créée avec succès");
		}


	    
	    
	    @DeleteMapping("/{factureId}")
	    public ResponseEntity<?> deleteFacture(@PathVariable Long factureId){
	        return factureRepository.findById(factureId).map(facture -> {
	        	factureRepository.delete(facture);
	            return ResponseEntity.ok().build();
	        }).orElseThrow(() -> new IllegalArgumentException("factureId" + "factureId" + " not found"));
	    }

	    

	   


	    @PutMapping("/{factureId}")
	    public ResponseEntity<Map<String, String>> updateFacture(@PathVariable Long factureId,
	            @RequestBody Map<String, Object> factureRequest) {
	        return factureRepository.findById(factureId).map(facture -> {
	            try {
	                if (factureRequest.containsKey("typePaiement")) {
	                    Object typePaiementObj = factureRequest.get("typePaiement");
	                    if (typePaiementObj instanceof String) {
	                        String typePaiementString = (String) typePaiementObj;
	                        PaiementType paiementType = PaiementType.valueOf(typePaiementString.toUpperCase());
	                        facture.setPaiementType(paiementType);
	                    } else {
	                        return ResponseEntity.badRequest()
	                                .body(Collections.singletonMap("error", "Invalid typePaiement value"));
	                    }
	                }
	                if (factureRequest.containsKey("facilitePaiement")) {
	                    Object facilitePaiementObj = factureRequest.get("facilitePaiement");
	                    if (facilitePaiementObj instanceof String) {
	                        String facilitePaiementString = (String) facilitePaiementObj;
	                        FacilitePaiement facilitePaiementType = FacilitePaiement
	                                .valueOf(facilitePaiementString.toUpperCase());
	                        facture.setFacilitePaiement(facilitePaiementType);
	                    } else {
	                        return ResponseEntity.badRequest()
	                                .body(Collections.singletonMap("error", "Invalid facilitePaiement value"));
	                    }
	                }

	                factureRepository.save(facture);
	                return ResponseEntity.ok(Collections.singletonMap("message", "Facture updated successfully"));
	            } catch (IllegalArgumentException e) {
	                return ResponseEntity.badRequest()
	                        .body(Collections.singletonMap("error", "Invalid enum value or missing key"));
	            }
	        }).orElseThrow(() -> new IllegalArgumentException("FactureId " + factureId + " not found"));
	    }
	    
	    
	    
	    
       @GetMapping("/{factureId}")
        public Facture getFacture(@PathVariable Long factureId) {
        Optional<Facture> f = factureRepository.findById(factureId);
        return f.get();

        }

	    
}