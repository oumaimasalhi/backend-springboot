package com.FST.GestionDesVentes.Controlleres;

import java.util.List;
import java.util.Optional;

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

import com.FST.GestionDesVentes.Entities.Client;
import com.FST.GestionDesVentes.Entities.Commande;
import com.FST.GestionDesVentes.Repositories.ClientRepository;
import com.FST.GestionDesVentes.Repositories.CommandeRepository;
import jakarta.validation.Valid;





@RestController
@RequestMapping({"/clients","/home*"})
@CrossOrigin(origins="*")
public class ClientController {
	
	
	private final ClientRepository clientrepository;
	private final CommandeRepository commandeRepository;
	

	public ClientController(ClientRepository clientrepository, CommandeRepository commanderepository) {    
	    this.clientrepository = clientrepository;
	    this.commandeRepository = commanderepository;
	    
	}

	    @GetMapping("/list")
	    public List<Client> getAllClients() {
	        return (List<Client>) clientrepository.findAll();
	    }


	    @PostMapping("/add")
	    public Client createClient(@Valid @RequestBody Client client) {

	        return clientrepository.save(client); 
	    }


	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
	        return clientrepository.findById(id).map(client -> {
	            clientrepository.delete(client);
	            return ResponseEntity.ok().build();
	        }).orElseThrow(() -> new IllegalArgumentException("ClientId " +
	                id + " not found"));
	    }


	    
	    
	    @GetMapping("/{clientId}")
	    public Client getClient(@PathVariable Long clientId) {

	        Optional<Client> c = clientrepository.findById(clientId);

	        return c.get();


	    }
	
	    
	    @PutMapping("/{clientId}")
	    public Client updateClient(@PathVariable Long clientId, @Valid @RequestBody Client clientRequest) {
	        return clientrepository.findById(clientId).map(client -> {
	            client.setNom(clientRequest.getNom());
	            client.setPrenom(clientRequest.getPrenom());
	            client.setEmail(clientRequest.getEmail());
	            client.setAdresse(clientRequest.getAdresse());
	            client.setTelephone(clientRequest.getTelephone());
	            return clientrepository.save(client);
	        }).orElseThrow(() -> new IllegalArgumentException("Client with ID " + clientId + " not found"));
	    }

	 
	    
	    
	    @PostMapping("/addCommandeToClient/{clientId}/{commandeId}")
	    public ResponseEntity<String> addCommandeToClient(@PathVariable Long clientId, @PathVariable Long commandeId) {
	        Client client = clientrepository.findById(clientId).orElse(null);
	        Commande commande = commandeRepository.findById(commandeId).orElse(null);

	        // Vérifier si client et commande existent
	        if (client == null || commande == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client or Commande not found");
	        }

	        // Ajouter  la commande au client
	        
	        client.getCommandes().add(commande);

	        // Enregistrer les modifications
	        commandeRepository.save(commande);
	        clientrepository.save(client);

	        return ResponseEntity.ok("Commande added to client");
	    }

	    
	    @GetMapping("/removeCommandeFromClient/{clientId}/{commandeId}")
	    public ResponseEntity<Client> removeCommandeFromClient(@PathVariable Long clientId, @PathVariable Long commandeId) {
	        Client client = clientrepository.findById(clientId).orElse(null);
	        Commande commande = commandeRepository.findById(commandeId).orElse(null);

	        if (client == null || commande == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        client.getCommandes().remove(commande);
	        commandeRepository.delete(commande);
	        clientrepository.save(client);

	        return ResponseEntity.ok(client);
	    }
	    
	    
	    
	   /* @PostMapping("/addPaiementToClient/{clientId}/{paiementId}")
	    public ResponseEntity<String> addPaiementToClient(@PathVariable Long clientId, @PathVariable Long paiementId) {
	        Client client = clientrepository.findById(clientId).orElse(null);
	        Paiement paiement = paiementRepository.findById(paiementId).orElse(null);

	        // Vérifier si client et paiement existent
	        if (client == null || paiement == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client or Paiement not found");
	        }

	        // Associer le paiement au client
	        client.getPaiementType().add(paiement);

	        // Enregistrer les modifications
	        paiementRepository.save(paiement);
	        clientrepository.save(client);

	        return ResponseEntity.ok("Paiement added to client");
	    }

	    
	    @GetMapping("/removePaiementFromClient/{clientId}/{paiementId}")
	    public ResponseEntity<Client> removePaiementFromClient(@PathVariable Long clientId, @PathVariable Long paiementId) {
	        Client client = clientrepository.findById(clientId).orElse(null);
	        Paiement paiement = paiementRepository.findById(paiementId).orElse(null);

	        if (client == null || paiement == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        client.getPaiementType().remove(paiement);
	        paiementRepository.delete(paiement);
	        clientrepository.save(client);

	        return ResponseEntity.ok(client);
	    }

*/
	 
}	