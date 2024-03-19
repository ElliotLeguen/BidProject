package projet.bid_pro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.Utilisateur;

@Controller
@RequestMapping("/profil")
//Injection de la liste des attributs en session
@SessionAttributes({ "UtilisateurEnSession" })
public class UtilisateurController {


    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	// Cette méthode met par défaut un nouveau membre en session
	@ModelAttribute("UtilisateurEnSession")
	public Utilisateur membreEnSession() {
		System.out.println("Add Attribut Session");
		return new Utilisateur();
	}

	@GetMapping
	public String afficherProfil(Model model) {
		return "profil";
	}

	@GetMapping("/oui")
	public String connexion(@ModelAttribute("UtilisateurEnSession") Utilisateur UtilisateurEnSession,
			@RequestParam(name = "email", required = false, defaultValue = "dupont@example.com") String email) {
		Utilisateur aCharger = utilisateurService.charger(email);

		if (aCharger != null) {
            UtilisateurEnSession.setNoUtilisateur(aCharger.getNoUtilisateur());
            UtilisateurEnSession.setNom(aCharger.getNom());
            UtilisateurEnSession.setPrenom(aCharger.getPrenom());
            UtilisateurEnSession.setPseudo(aCharger.getPseudo());
            UtilisateurEnSession.setAdministrateur(aCharger.isAdministrateur());

		} else {
            UtilisateurEnSession.setNoUtilisateur(0);
            UtilisateurEnSession.setNom(null);
            UtilisateurEnSession.setPrenom(null);
            UtilisateurEnSession.setPseudo(null);
            UtilisateurEnSession.setAdministrateur(Byte.parseByte("1"));

		}
		System.out.println(UtilisateurEnSession);
		// Evidemment on évite de mettre un mot de passe en session
		// (surtout non chiffré)

		return "redirect:/";
	}

}
