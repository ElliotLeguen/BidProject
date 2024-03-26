package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import projet.bid_pro.bll.contexte.ArticleService;
import projet.bid_pro.bll.contexte.CategorieService;
import projet.bid_pro.bll.contexte.EnchereService;
import projet.bid_pro.bll.contexte.UtilisateurService;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.bo.Utilisateur;

import java.security.Principal;
import java.util.List;

@Controller
@SessionAttributes({"UtilisateurEnSession"})
public class EnchereController {
    private EnchereService enchereService;
    private CategorieService categorieService;
    private UtilisateurService utilisateurService;
    private ArticleService articleService;

    public EnchereController(EnchereService enchereService,  CategorieService categorieService, UtilisateurService utilisateurService,  ArticleService articleService) {
        this.enchereService = enchereService;
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String afficherAccueil(Model model) {
        List<Enchere> encheres = enchereService.consulterEncheres();
        var categories = categorieService.consulterCategories();
        model.addAttribute("categorie", categories);
        model.addAttribute("encheres", encheres);
        return "index";
    }

    @GetMapping("/profilEnchere")
    public String AfficherProfilEnchere() {
        return "profilEnchere";
    }

    @PostMapping("/encherir")
    public String encherir(@ModelAttribute(name = "enchere") Enchere enchere,
                           @ModelAttribute(name = "articleVendu") ArticleVendu articleVendu,
                           @ModelAttribute(name = "utilisateur") Utilisateur utilisateur){
        consultaionEnchere(articleVendu, utilisateur, enchere);
        return "encherir";
    }

    private void consultaionEnchere(ArticleVendu articleVendu, Utilisateur utilisateur, Enchere enchere){
        if (enchereService.isNotAlreadyExisting(articleVendu.getNoArticle()) == null){
            enchere.setDateEnchere(new java.util.Date());
            enchere.setMontantEnchere(enchere.getMontantEnchere());
            enchere.setUtilisateur(utilisateurService.charger(utilisateur.getNoUtilisateur()));
            enchere.setArticle(articleService.consulterArticleParId(articleVendu.getNoArticle()));
            enchereService.creerEnchere(enchere);
        }else{
            Enchere enchereS = (enchereService.consulterEnchereParId(articleVendu.getNoArticle()));
            enchereS.setMontantEnchere(enchere.getMontantEnchere());
            enchereService.updateEnchere(enchereS);
        }
    }
}

