package projet.bid_pro.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;

@Controller
//Injection de la liste des attributs en session
@SessionAttributes({ "UtilisateurEnSession" })
public class UtilisateurController {


    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}

	// Cette méthode met par défaut un nouveau utilisateur en session
	@ModelAttribute("UtilisateurEnSession")
	public Utilisateur membreEnSession(Principal principal) {
		return utilisateurService.charger(principal.getName());
	}
	@GetMapping("/loginSuccessHandler")
	public String loginSuccess() {
		return "redirect:/";
	}

	@GetMapping("/profil")
	public String afficherProfil() {
		return "profil";
	}



}
