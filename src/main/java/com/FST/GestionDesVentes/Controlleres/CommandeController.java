package com.FST.GestionDesVentes.Controlleres;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FST.GestionDesVentes.Entities.Commande;
import com.FST.GestionDesVentes.Entities.Panier;
import com.FST.GestionDesVentes.Entities.Client;
import com.FST.GestionDesVentes.Repositories.ClientRepository;
import com.FST.GestionDesVentes.Repositories.CommandeRepository;
import com.FST.GestionDesVentes.Repositories.PanierRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/commandes")
@CrossOrigin(origins = "*")
public class CommandeController {

	 private final PanierRepository panierRepository;
	    private final ClientRepository clientRepository;
	    private final CommandeRepository commandeRepository;

	    public CommandeController(PanierRepository panierRepository, ClientRepository clientRepository,
	            CommandeRepository commandeRepository) {
	        this.panierRepository = panierRepository;
	        this.clientRepository = clientRepository;
	        this.commandeRepository = commandeRepository;
	    }

	    
	    @GetMapping("/list")
	    public List<Commande> getAllCommandes() {
	        return (List<Commande>) commandeRepository.findAll();
	    }
	    
	    
	    

	    @PostMapping("/add/{panierId}/{clientId}")
	    public ResponseEntity<Map<String, String>> ajouterCommande(
	            @PathVariable Long panierId,
	            @PathVariable Long clientId,
	            @RequestBody Map<String, Object> requestBody) {

	        Optional<Panier> panierOpt = panierRepository.findById(panierId);
	        if (!panierOpt.isPresent()) {
	            return ResponseEntity.badRequest().body(Map.of("message", "Panier non trouvé"));
	        }

	        Panier panier = panierOpt.get();
	        Optional<Client> clientOpt = clientRepository.findById(clientId);
	        if (!clientOpt.isPresent()) {
	            return ResponseEntity.badRequest().body(Map.of("message", "Client non trouvé"));
	        }

	        Client client = clientOpt.get();
	        if (!"valide".equals(panier.getStatut())) {
	            return ResponseEntity.badRequest().body(Map.of("message", "Le panier n'est pas valide"));
	        }

	        Commande commande = new Commande();
	        commande.setClient(client);
	        commande.setPanierId(panierId);
	        commande.setTotal(panier.getTotal());
	        
	        // Extraire dateAjout du Map et le définir
	        if (requestBody.containsKey("dateCommande")) {
	            commande.setDateCommande(LocalDateTime.parse((String) requestBody.get("dateCommande")));
	        }

	        
	        commandeRepository.save(commande);

	        return ResponseEntity.ok(Map.of("message", "Commande créée avec succès"));
	    }
	    
	    
	    
	    @DeleteMapping("/{commandeId}")
	    public ResponseEntity<?> deleteCommande(@PathVariable Long commandeId) {
	        return commandeRepository.findById(commandeId).map(commande -> {
	            commandeRepository.delete(commande);
	            return ResponseEntity.ok().build();
	        }).orElseThrow(() -> new IllegalArgumentException("CommandeId " + commandeId + " not found"));
	    }

	    @PutMapping("/{commandeId}")
	    public ResponseEntity<?> updateCommande(
	            @PathVariable Long commandeId,
	            @Valid @RequestBody Map<String, Object> commandeRequest) {

	        System.out.println("Received update request for commandeId: " + commandeId);
	        System.out.println("Request body: " + commandeRequest);

	        return commandeRepository.findById(commandeId).map(commande -> {
	            // Extraire et définir la nouvelle date de commande
	            if (commandeRequest.containsKey("dateCommande")) {
	                try {
	                    commande.setDateCommande(LocalDateTime.parse((String) commandeRequest.get("dateCommande")));
	                } catch (Exception e) {
	                    System.out.println("Erreur de format pour dateCommande: " + e.getMessage());
	                    return ResponseEntity.badRequest().body("Erreur de format pour dateCommande");
	                }
	            } else {
	                System.out.println("Date de commande non fournie");
	                return ResponseEntity.badRequest().body("Date de commande non fournie");
	            }

	            // Vérifier si un ID de client a été fourni et mettre à jour le client si
	            // nécessaire
	            if (commandeRequest.containsKey("clientId")) {
	                try {
	                    Long clientId = Long.valueOf(commandeRequest.get("clientId").toString());
	                    Optional<Client> clientOpt = clientRepository.findById(clientId);
	                    if (clientOpt.isPresent()) {
	                        commande.setClient(clientOpt.get());
	                    } else {
	                        System.out.println("Client non trouvé");
	                        return ResponseEntity.badRequest().body("Client non trouvé");
	                    }
	                } catch (Exception e) {
	                    System.out.println("Erreur de format pour clientId: " + e.getMessage());
	                    return ResponseEntity.badRequest().body("Erreur de format pour clientId");
	                }
	            } else {
	                System.out.println("ID de client non fourni");
	                return ResponseEntity.badRequest().body("ID de client non fourni");
	            }

	            // Mettre à jour d'autres champs de la commande si nécessaire
	            if (commandeRequest.containsKey("panierId")) {
	                try {
	                    commande.setPanierId(Long.valueOf(commandeRequest.get("panierId").toString()));
	                } catch (Exception e) {
	                    System.out.println("Erreur de format pour panierId: " + e.getMessage());
	                    return ResponseEntity.badRequest().body("Erreur de format pour panierId");
	                }
	            }

	            // Enregistrer les modifications
	            Commande updatedCommande = commandeRepository.save(commande);
	            return ResponseEntity.ok(updatedCommande);
	        }).orElseThrow(() -> new IllegalArgumentException("CommandeId " + commandeId + " not found"));
	    }
	    @GetMapping("/{commandeId}")
	    public Commande getCommande(@PathVariable Long commandeId) {
	        Optional<Commande> commandeOpt = commandeRepository.findById(commandeId);
	        return commandeOpt.orElseThrow(() -> new IllegalArgumentException("CommandeId " + commandeId + " not found"));
	    }
	
}