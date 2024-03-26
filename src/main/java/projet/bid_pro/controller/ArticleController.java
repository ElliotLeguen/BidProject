package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projet.bid_pro.bll.contexte.*;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.dal.EnchereDAO;

@Controller
@SessionAttributes({ "UtilisateurEnSession" })
public class ArticleController {
	private ArticleService articleService;
	private UtilisateurService utilisateurService;
    private CategorieService categorieService;
    private  EnchereService enchereService;
    public ArticleController(ArticleService articleService, UtilisateurService utilisateurService, CategorieService categorieService, EnchereService enchereService) {
		this.articleService = articleService;
		this.utilisateurService = utilisateurService;
        this.categorieService = categorieService;
        this.enchereService = enchereService;
	}

	@GetMapping("/article")
	public String afficherEncheres(Model model, Principal principal) {
		model.addAttribute("categories", categorieService.consulterCategories());
		model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
		model.addAttribute("article", new ArticleVendu());
		return "vendreArticle";
	}

	@PostMapping("/soumettre")
	public String ajouterArticle(@ModelAttribute("article") ArticleVendu article, Principal principal) {
		article.setUtilisateur(utilisateurService.charger(principal.getName()));
		article.setCategorie(categorieService.consulterCategorieParId(article.getCategorie().getNoCategorie()));
		articleService.creerArticle(article);
		return "redirect:/article";
	}

	@GetMapping("/gestionCategorie")
	public String gestionCategorie(Model model) {
		List<Categorie> categories = categorieService.consulterCategories();
		model.addAttribute("categories", categories);
		return "admin/gestionCategorie";
	}
	@GetMapping("/detail")
	public String afficherUneEnchere(@RequestParam(name = "id", required = true) long id, Model model, Principal principal) {
		if (id > 0) {
			ArticleVendu articleVendu = articleService.consulterArticleParId(id);
			if (articleVendu != null) {
                Enchere enchere = new Enchere();
                enchere.setMontantEnchere(articleVendu.getPrixInitial());
                model.addAttribute("enchere", enchere);
				model.addAttribute("articleVendu", articleVendu);
                model.addAttribute("utilisteur", (utilisateurService.charger(principal.getName())));
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
        var user = utilisateurService.charger(principal.getName());
        List<ArticleVendu> articleVendus;
        String rqt = "SELECT * FROM ARTICLES_VENDUS inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur inner join CATEGORIES on ARTICLES_VENDUS.no_categorie = CATEGORIES.no_categorie";
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
            if (mesVentesEnCours) {
                cpt++;
                rqt += " WHERE ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + " AND (prix_vente IS NULL AND date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE())";
            }if (ventesTerminees) {
                if (cpt > 0) {
                    rqt += " OR (prix_vente IS NOT NULL AND date_fin_encheres <= GETDATE()) AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + ")";
                } else {
                    rqt += " WHERE ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + " AND (prix_vente IS NOT NULL AND date_fin_encheres <= GETDATE())";
                }
            }if (ventesNonDebutees) {
                if (cpt > 0 || ventesTerminees) {
                    rqt += " OR (prix_initial IS NOT NULL AND date_debut_encheres >= GETDATE() AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + ")";
                } else {
                    rqt += " WHERE ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + " AND (prix_initial IS NOT NULL AND date_debut_encheres >= GETDATE())";
                }
            }if(mesVentesEnCours && ventesTerminees && ventesNonDebutees){
                String rqt2 = "SELECT * \n" +
                        "FROM ARTICLES_VENDUS \n" +
                        "inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur\n" +
                        "WHERE ARTICLES_VENDUS.no_utilisateur = 2 \n" +
                        "AND (prix_vente IS NULL AND date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE()) \n" +
                        "OR (prix_vente IS NOT NULL AND date_fin_encheres <= GETDATE()) ";
                articleVendus = articleService.getVentes(rqt2);
            }
            System.out.println(rqt);
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

		model.addAttribute("categories", categorieService.consulterCategories());
		model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
		model.addAttribute("article", articleService.getVentes(rqt));
		return "encheres";
	}
}
