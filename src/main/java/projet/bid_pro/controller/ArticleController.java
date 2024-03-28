package projet.bid_pro.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projet.bid_pro.bll.contexte.*;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;

import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Utilisateur;
import projet.bid_pro.dal.EnchereDAO;

@Controller
@SessionAttributes({"UtilisateurEnSession"})
public class ArticleController {
    private ArticleService articleService;
    private UtilisateurService utilisateurService;
    private CategorieService categorieService;
    private EnchereService enchereService;

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
    public String ajouterArticle(@Valid @ModelAttribute("article") ArticleVendu article,
                                 BindingResult result,
                                 Model model,
                                 Principal principal) {
        if (article.getDateFinEncheres().compareTo(article.getDateDebutEncheres()) <= 0) {
            ObjectError error = new ObjectError("globalError", "La date fin enchère doit être strictement supérieure à la date début enchère");
            result.addError(error);
            model.addAttribute("categories", categorieService.consulterCategories());
            model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
            return "error";
        }
        if (result.hasErrors()) {
            model.addAttribute("article", article);
            model.addAttribute("categories", categorieService.consulterCategories());
            model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
            return "vendreArticle";
        }

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
    @PostMapping("/gestionCategorieEdit")
    public String gestionCategorie(@Valid @ModelAttribute("categorie") Categorie categorie ,Model model) {

        categorieService.edit(categorie);

        return "redirect:/gestionCategorie";
    }
    @PostMapping("/gestionCategorieAdd")
    public String gestionCategorieAdd(@Valid @ModelAttribute("categorie") Categorie categorie ,Model model) {

        categorieService.add(categorie);

        return "redirect:/gestionCategorie";
    }
    @GetMapping("/categorieSupprimer")
    public String utilisateurSupprimer( @RequestParam(name = "idCategorie", required = true) int idCategorie,Model model) {
        categorieService.delete(idCategorie);
        return "redirect:/gestionCategorie";
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
                if(articleVendu.getIdUtilisateurGagnant() != 0){
                    model.addAttribute("utlisateurGagnant", (utilisateurService.charger(articleVendu.getIdUtilisateurGagnant())));
                }

                return "detailEnchere"; // Correction de l'alias de la vue
            } else {
                System.out.println("Enchère inconnue!!");
            }
        } else {
            System.out.println("Identifiant inconnu");
        }
        return "redirect:/encheres/encheres"; // Redirection vers la page d'accueil des enchères
    }

    @GetMapping("/detailEdit")
    public String afficherUneEnchereEdit(@RequestParam(name = "id") long id, Model model, Principal principal) {
        if (id > 0) {
            ArticleVendu articleVendu = articleService.consulterArticleParId(id);
            if(articleVendu.getDateFinEncheres().before(new Date())){
                return "redirect:/detail?id=" + articleVendu.getNoArticle();
            }
            if (articleVendu != null) {
                Enchere enchere = new Enchere();
                enchere.setMontantEnchere(articleVendu.getPrixInitial());
                model.addAttribute("enchere", enchere);
                model.addAttribute("articleVendu", articleVendu);
                model.addAttribute("utilisateur", (utilisateurService.charger(principal.getName())));
                List<Categorie> categories = categorieService.consulterCategories();
                model.addAttribute("categories", categories);
                return "detailEnchereEdit"; // Correction de l'alias de la vue
            } else {
                System.out.println("Enchère inconnue!!");
            }
        } else {
            System.out.println("Identifiant inconnu");
        }
        return "/detailEdit"; // Redirection vers la page d'accueil des enchères
    }

    @PostMapping("/detailEdit")
    public String EnchereEdit(@Valid @ModelAttribute("articleVendu") ArticleVendu articleVendu, BindingResult result, Model model, Principal principal) {

        System.out.println(articleVendu);
        if (result.hasErrors()) {
            model.addAttribute("articleVendu", articleVendu);
            List<Categorie> categories = categorieService.consulterCategories();
            model.addAttribute("categories", categories);
            return "redirect:/detailEdit?id=" + articleVendu.getNoArticle();
        }

        articleService.EditArticle(articleVendu);
        return "redirect:/encheres"; // Redirection vers la page d'accueil des enchères
    }

    @GetMapping("/supprArticle")
    public String supprimerArticle(@RequestParam(name = "idArticle", required = true) int idArticle) {
        articleService.SupprArticle(idArticle);
        return "redirect:/encheres";
    }

    @GetMapping("/encheres")
    public String afficherEncheres(HttpServletRequest request,
                                   @RequestParam(name = "nomArticle", required = false) String nomArticle,
                                   @RequestParam(name = "categorie", required = false, defaultValue = "0") int categorie,
                                   @RequestParam(name = "mesVentesEnCours", required = false) boolean mesVentesEnCours,
                                   @RequestParam(name = "ventesNonDebutees", required = false) boolean ventesNonDebutees,
                                   @RequestParam(name = "ventesTerminees", required = false) boolean ventesTerminees,
                                   @RequestParam(name = "enchereOuverte", required = false) boolean enchereOuverte,
                                   @RequestParam(name = "mesEncheres", required = false) boolean mesEncheres,
                                   @RequestParam(name = "mesEnchereRemportees", required = false) boolean mesEnchereRemportees,
                                   @RequestParam(name = "page", defaultValue = "1") int page,
                                   Principal principal, Model model) throws JsonProcessingException {
        var user = utilisateurService.charger(principal.getName());
        List<ArticleVendu> articleVendus;
        String rqt = "SELECT * FROM ARTICLES_VENDUS inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur inner join CATEGORIES on ARTICLES_VENDUS.no_categorie = CATEGORIES.no_categorie";
        int cate = 0;
        if (categorie != 0) {
            cate++;
            System.out.println(cate);

        }
        int cpt = 0;
        if (request.getParameter("mesVentesEnCours") != null || request.getParameter("ventesNonDebutees") != null || request.getParameter("ventesTerminees") != null) {

            if (mesVentesEnCours) {
                if (cate > 0) {
                    rqt += " WHERE CATEGORIES.no_categorie = " + categorie + " AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE())";
                } else {
                    rqt += " WHERE ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE())";
                }
                cpt++;
            }
            if (ventesTerminees) {
                if (cate > 0 && cpt == 0) {
                    rqt += " WHERE CATEGORIES.no_categorie = " + categorie + " AND (prix_vente IS NOT NULL AND date_fin_encheres <= GETDATE()) AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur();
                } else if (cate > 0 && cpt > 0) {
                    rqt += " AND CATEGORIES.no_categorie = " + categorie + " AND (prix_vente IS NOT NULL AND date_fin_encheres <= GETDATE()) AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur();
                } else if (cate == 0 && cpt > 0) {
                    rqt += " OR CATEGORIES.no_categorie = " + categorie + " AND (prix_vente IS NOT NULL AND date_fin_encheres <= GETDATE()) AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur();
                } else {
                    rqt += " WHERE ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + " AND date_fin_encheres <= GETDATE()";
                }
                cpt++;
            }
            if (ventesNonDebutees) {
                if (cate > 0 && cpt == 0) {
                    rqt += " WHERE CATEGORIES.no_categorie = " + categorie + " AND (prix_initial IS NOT NULL AND date_debut_encheres >= GETDATE() AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + ")";
                } else if (cate > 0 && cpt > 0) {
                    rqt += " AND CATEGORIES.no_categorie = " + categorie + " AND (prix_initial IS NOT NULL AND date_debut_encheres >= GETDATE() AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + ")";
                } else if (cate == 0 && cpt > 0) {
                    rqt += " OR (prix_initial IS NOT NULL AND date_debut_encheres >= GETDATE() AND ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + ")";
                } else {
                    rqt += " WHERE ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur() + " AND (prix_initial IS NOT NULL AND date_debut_encheres >= GETDATE())";
                }
            }
            if (mesVentesEnCours && ventesTerminees && ventesNonDebutees) {
                rqt = "SELECT * FROM ARTICLES_VENDUS \n" +
                        "inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur \n" +
                        "inner join CATEGORIES on ARTICLES_VENDUS.no_categorie = CATEGORIES.no_categorie WHERE ARTICLES_VENDUS.no_utilisateur = " + user.getNoUtilisateur();
            }
        } else if (request.getParameter("enchereOuverte") != null || request.getParameter("mesEncheres") != null || request.getParameter("mesEnchereRemportees") != null) {
            rqt += " INNER JOIN  ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article ";
            if (enchereOuverte) {
                if (cate > 0) {
                    rqt += " AND CATEGORIES.no_categorie = " + categorie + " AND date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE()";
                } else {
                    rqt += " WHERE date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE()";
                }
                cpt++;
            }
            if (mesEncheres) {
                if (cate > 0 && cpt == 0) {
                    rqt += " WHERE CATEGORIES.no_categorie = " + categorie + " AND ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE()) ";
                } else if (cate > 0 && cpt > 0) {
                    rqt += " AND CATEGORIES.no_categorie = " + categorie + " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE())";
                } else if (cate == 0 && cpt > 0) {
                    rqt += " OR (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE())";
                } else {
                    rqt += " WHERE ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE())"; // à faire avec l'id en session
                }
                cpt++;
            }
            if (mesEnchereRemportees) {
                if (cate > 0 && cpt == 0) {
                    rqt += " WHERE CATEGORIES.no_categorie = " + categorie + " AND ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + " AND ARTICLES_VENDUS.prix_vente != 0 ";
                } else if (cate > 0 && cpt > 0) {
                    rqt += " AND CATEGORIES.no_categorie = " + categorie + " AND ARTICLES_VENDUS.prix_vente != 0";
                } else if (cate == 0 && cpt > 0) {
                    rqt += " OR ARTICLES_VENDUS.prix_vente != 0";
                } else {
                    rqt += " WHERE ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + "  AND ARTICLES_VENDUS.prix_vente != 0"; // à faire
                }
            }
            if (enchereOuverte && mesEncheres && mesEnchereRemportees) {
                rqt = " SELECT * FROM ARTICLES_VENDUS inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur inner join CATEGORIES on ARTICLES_VENDUS.no_categorie = CATEGORIES.no_categorie INNER JOIN  ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article ";
                if (cate > 0) {
                    rqt += " WHERE CATEGORIES.no_categorie = " + categorie + " AND date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE() OR  CATEGORIES.no_categorie = " + categorie + " AND ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE()) OR  CATEGORIES.no_categorie = " + categorie + " AND ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + " AND ARTICLES_VENDUS.prix_vente != 0";
                } else {
                    rqt += " WHERE date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE() OR ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + " AND (date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE()) OR ENCHERES.no_utilisateur = " + user.getNoUtilisateur() + " AND ARTICLES_VENDUS.prix_vente != 0";
                }
            }
        } else rqt= "SELECT * FROM ARTICLES_VENDUS LEFT JOIN  ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article  inner join UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur inner join CATEGORIES on ARTICLES_VENDUS.no_categorie = CATEGORIES.no_categorie WHERE date_debut_encheres <= GETDATE() AND date_fin_encheres >= GETDATE() ORDER BY 1 ASC OFFSET 4 ROWS FETCH NEXT 6 ROWS ONLY";

        model.addAttribute("currentPage", page);
        System.out.println(rqt);

        List<ArticleVendu> list = articleService.getVentes(rqt);
        System.out.println(list);
        List<ArticleVendu> distinctArticles = list.stream()
                .collect(Collectors.toMap(ArticleVendu::getNoArticle,
                        article -> article,
                        (existing, replacement) -> existing.getActuelMeilleurPrix() >= replacement.getActuelMeilleurPrix() ? existing : replacement))
                .values().stream()
                .toList();
        System.out.println(distinctArticles);
        model.addAttribute("categories", categorieService.consulterCategories());
        model.addAttribute("userEdit", utilisateurService.charger(principal.getName()));
        model.addAttribute("articles", distinctArticles);
        return "encheres";
    }
}
