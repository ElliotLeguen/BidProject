package projet.bid_pro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import projet.bid_pro.bll.contexte.EnchereService;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;

import java.security.Principal;

@Controller
@SessionAttributes({ "UtilisateurEnSession" })
public class ArticleController {

	private EnchereService enchereService;
	private UtilisateurService utilisateurService;
	public ArticleController(EnchereService enchereService, UtilisateurService utilisateurService) {
		this.enchereService = enchereService;
		this.utilisateurService = utilisateurService;
	}
	@GetMapping("/article")
	public String afficherEncheres(Model model) {
		var categorie = enchereService.consulterCategories();
		var article = new ArticleVendu();
		model.addAttribute("categorie", categorie);
		model.addAttribute("article", article);
		return "vendreArticle";
	}
	@PostMapping("/soumettre")
	public String ajouterArticle(@ModelAttribute("article") ArticleVendu article, Principal principal) {
			article.setUtilisateur(utilisateurService.charger(principal.getName()));
			article.setCategorie(enchereService.consulterCategorieParId(article.getCategorie().getNoCategorie()));
		enchereService.creerArticle(article);
		return "redirect:/article";
	}
}
