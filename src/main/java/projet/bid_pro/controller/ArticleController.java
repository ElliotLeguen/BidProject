package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projet.bid_pro.bll.contexte.ArticleService;
import projet.bid_pro.bll.contexte.EnchereService;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import projet.bid_pro.bll.contexte.EnchereService;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;

import java.security.Principal;

@Controller
@SessionAttributes({ "UtilisateurEnSession" })
public class ArticleController {

	private ArticleService articleService;
	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}
	@GetMapping("/article")
	public String afficherEncheres(Model model) {
		var categories = articleService.consulterCategories();
		model.addAttribute("categorie", categories);
		return "vendreArticle";
	}

	@GetMapping("/encheres")
	public String afficherEncheres(HttpServletRequest request,
								   @RequestParam(name = "nomArticle", required = false) String nomArticle,
								   @RequestParam(name = "categorie", required = false) String categorie,
								   @RequestParam(name = "mesVentesEnCours", required = false) boolean mesVentesEnCours,
								   @RequestParam(name = "ventesNonDebutees", required = false) boolean ventesNonDebutees,
								   @RequestParam(name = "ventesTerminees", required = false) boolean ventesTerminees,
								   @RequestParam(name = "enchereOuverte", required = false) boolean enchereOuverte,
								   @RequestParam(name = "mesEncheres", required = false) boolean mesEncheres,
								   @RequestParam(name = "mesEnchereRemportees", required = false) boolean mesEnchereRemportees,
								   Principal principal, Model model) {
		List<ArticleVendu> articleVendus;
		String rqt = "SELECT * FROM ARTICLES_VENDUS inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur";
		if (request.getParameter("mesVentesEnCours") != null || request.getParameter("ventesNonDebutees") != null || request.getParameter("ventesTerminees") != null || request.getParameter("enchereOuverte") != null || request.getParameter("mesEncheres") != null || request.getParameter("mesEnchereRemportees") != null) {
			if (request.getParameter("mesVentesEnCours") != null) {
				mesVentesEnCours = true;
			}
			if (request.getParameter("ventesNonDebutees") != null) {
				ventesNonDebutees = true;
			}
			if (request.getParameter("ventesTerminees") != null) {
				ventesTerminees = true;
			}
			if (request.getParameter("enchereOuverte") != null) {
				enchereOuverte = true;
			}
			if (request.getParameter("mesEncheres") != null) {
				mesEncheres = true;
			}
			if (request.getParameter("mesEnchereRemportees") != null) {
				mesEnchereRemportees = true;
			}

			if (nomArticle != null && !nomArticle.isEmpty()) {
				articleVendus = articleService.consulterEncheresParNomArticle(nomArticle);
			}
			if (categorie != null && !categorie.isEmpty()) {
				articleVendus = articleService.consulterEncheresParCategorie(categorie);
			}
			int cpt = 0;
//            if (mesVentesEnCours) {
//                cpt++;
//                rqt += " WHERE prix_vente IS NULL AND date_fin_encheres > GETDATE()";
//            }
//            if (ventesTerminees) {
//                if (cpt == 1) {
//                    rqt += " AND prix_vente IS NULL AND date_fin_encheres < GETDATE()";
//                } else rqt += " WHERE prix_vente IS NULL AND date_fin_encheres < GETDATE()";
//                if (ventesNonDebutees) {
//                    cpt++;
//                    if (cpt == 2) {
//                        rqt += " prix_initial IS NOT NULL AND date_debut_encheres > GETDATE()";
//                    } else rqt += " WHERE prix_initial IS NOT NULL AND date_debut_encheres > GETDATE()";
//
//                }
//            }
//            if (enchereOuverte) {
//                rqt += " WHERE date_enchere > GETDATE() AND date_fin_encheres < GETDATE()";
//            }
//            if (mesEncheres) {
//                rqt += " WHERE date_enchere > GETDATE() AND date_fin_encheres < GETDATE()"; // à faire avec l'id en session
//            }
//            if (mesEnchereRemportees) {
//                rqt += " WHERE date_enchere < GETDATE()"; // à faire
//            }
		}
		// Vérification des cases à cocher cochées dans le formulaire

		articleVendus = articleService.getVentes(rqt);

		var categories = articleService.consulterCategories();
		model.addAttribute("categorie", categories);
		model.addAttribute("articleVendus", articleVendus);
		System.out.println(articleVendus);
		return "encheres";
	private EnchereService enchereService;
	private UtilisateurService utilisateurService;
	public ArticleController(EnchereService enchereService, UtilisateurService utilisateurService) {
		this.enchereService = enchereService;
		this.utilisateurService = utilisateurService;
	}
	@GetMapping("/article")
	public String afficherEncheres(Model model, Principal principal) {
		var categorie = enchereService.consulterCategories();
		var article = new ArticleVendu();
		model.addAttribute("categorie", categorie);
		model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
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
