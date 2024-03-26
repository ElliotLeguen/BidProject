package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import projet.bid_pro.bll.contexte.CategorieService;
import projet.bid_pro.bll.contexte.EnchereService;
import projet.bid_pro.bll.contexte.UtilisateurService;
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

    public EnchereController(EnchereService enchereService,  CategorieService categorieService, UtilisateurService utilisateurService) {
        this.enchereService = enchereService;
        this.categorieService = categorieService;
        this.utilisateurService = utilisateurService;
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
    public String afficherProfilEnchere( @RequestParam(name = "idUtilisateurEnchere", required = true) int idUtilisateurEnchere,Model model) {
        Utilisateur uti =  utilisateurService.charger(idUtilisateurEnchere);
        System.out.println(uti);
        model.addAttribute("utilisateur", utilisateurService.charger(idUtilisateurEnchere));
        return "profilEnchere";
    }
}

