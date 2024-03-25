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
	private  UtilisateurService utilisateurService;
	public ArticleController(ArticleService articleService, UtilisateurService utilisateurService) {
		this.articleService = articleService;
		this.utilisateurService = utilisateurService;
	}

	@GetMapping("/article")
	public String afficherEncheres(Model model, Principal principal) {
		model.addAttribute("categories", articleService.consulterCategories());
		model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
		model.addAttribute("article", new ArticleVendu());
		return "vendreArticle";
	}
	@PostMapping("/soumettre")
	public String ajouterArticle(@ModelAttribute("article") ArticleVendu article, Principal principal) {
		article.setUtilisateur(utilisateurService.charger(principal.getName()));
		article.setCategorie(articleService.consulterCategorieParId(article.getCategorie().getNoCategorie()));
		articleService.creerArticle(article);
		return "redirect:/article";
	}
	@GetMapping("/detail")
	public String afficherUneEnchere(@RequestParam(name = "id", required = true) long id, Model model) {
		if (id > 0) {
			ArticleVendu articleVendu = articleService.consulterArticleParId(id);
			if (articleVendu != null) {
				model.addAttribute("articleVendu", articleVendu); // Correction de la variable ajoutée au modèle
				return "detailEnchere"; // Correction de l'alias de la vue
			} else {
				System.out.println("Enchère inconnue!!");
			}
		} else {
			System.out.println("Identifiant inconnu");
		}
		return "redirect:/encheres/encheres"; // Redirection vers la page d'accueil des enchères
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
		String rqt = "SELECT * FROM ARTICLES_VENDUS " +
				"inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur" +
				" inner join CATEGORIES on CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie";
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
	}
}
