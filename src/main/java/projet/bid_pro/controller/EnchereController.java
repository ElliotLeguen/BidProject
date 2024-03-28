package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public String afficherProfilEnchere(
            @RequestParam(name = "idUtilisateurEnchere") int idUtilisateurEnchere,
            Model model) {
        model.addAttribute("utilisateur", utilisateurService.charger(idUtilisateurEnchere));
        return "profilEnchere";
    }

    @PostMapping("/encherir")
    public String encherir(@Valid @ModelAttribute(name = "enchere") Enchere enchere,
                           BindingResult result,
                           @ModelAttribute(name = "articleVendu") ArticleVendu articleVendu,
                           Principal principal,
                           Model model){
        Utilisateur utilisateur = utilisateurService.charger(principal.getName());
        if (utilisateur.getCredit() < enchere.getMontantEnchere()){
            model.addAttribute("message","Solde insuffisant");
            return "errorEnchere";
        }else {
            return consultaionEnchere(articleVendu, utilisateur, enchere, result,model);
        }

    }
    private void gestionCredit(Utilisateur utilisateur, Enchere enchere, ArticleVendu articleVendu){
        int nouveauSolde = utilisateur.getCredit()-enchere.getMontantEnchere();
        utilisateur.setCredit(nouveauSolde);
        utilisateurService.enleverCredit(utilisateur);
        Enchere enchereFinal = enchereService.consulterAncienEnchere(utilisateur.getNoUtilisateur(), articleVendu.getNoArticle()).get(0);
        enchereFinal.getUtilisateur().setCredit(enchereFinal.getMontantEnchere());
        utilisateurService.ajouterCredit(enchereFinal.getUtilisateur());
    }

    private String consultaionEnchere(ArticleVendu articleVendu, Utilisateur utilisateur, Enchere enchere,BindingResult result,Model model) {
        Enchere enchereNouvelle = new Enchere();
        if (enchereService.consulterEnchereParId(articleVendu.getNoArticle(), utilisateur.getNoUtilisateur()) == null) {
            enchereNouvelle.setDateEnchere(new java.util.Date());
            enchereNouvelle.setMontantEnchere(enchere.getMontantEnchere());
            enchereNouvelle.setUtilisateur(utilisateurService.charger(utilisateur.getNoUtilisateur()));
            enchereNouvelle.setArticle(articleService.consulterArticleParId(articleVendu.getNoArticle()));
            enchereService.creerEnchere(enchereNouvelle);
            gestionCredit(utilisateur, enchere, articleVendu);
        } else {
            if (enchere.getMontantEnchere() > enchereService.consulterEnchereId(articleVendu.getNoArticle())) {
                enchereNouvelle = (enchereService.consulterEnchereParId(articleVendu.getNoArticle(), utilisateur.getNoUtilisateur()));
                enchereNouvelle.setUtilisateur(utilisateurService.charger(utilisateur.getNoUtilisateur()));
                enchereNouvelle.setMontantEnchere(enchere.getMontantEnchere());
                enchereService.updateEnchere(enchereNouvelle);
                gestionCredit(utilisateur,enchere, articleVendu);
            }else{
                model.addAttribute("message","Le montant de l'enchère doit être superieur à l'ancien");
                return "errorEnchere";
            }
        }
        return "redirect:/encheres";
    }
}

