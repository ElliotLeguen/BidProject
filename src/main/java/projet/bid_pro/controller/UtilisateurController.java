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



}
