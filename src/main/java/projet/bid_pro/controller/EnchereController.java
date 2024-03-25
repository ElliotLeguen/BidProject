package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import projet.bid_pro.bll.contexte.EnchereService;
import projet.bid_pro.bo.Enchere;

import java.security.Principal;
import java.util.List;

@Controller
@SessionAttributes({"UtilisateurEnSession"})
public class EnchereController {

    private EnchereService enchereService;

    public EnchereController(EnchereService enchereService) {
        this.enchereService = enchereService;
    }

    @GetMapping("/")
    public String afficherAccueil(Model model) {
        List<Enchere> encheres = enchereService.consulterEncheres();
        var categories = enchereService.consulterCategories();
        model.addAttribute("categorie", categories);
        model.addAttribute("encheres", encheres);
        return "index";
    }

    @GetMapping("/profilEnchere")
    public String AfficherProfilEnchere() {
        return "profilEnchere";
    }

	/*

	@GetMapping("/detail")
	public String afficherUneEnchere(@RequestParam(name = "id", required = true) long id, Model model) {
		if (id > 0) {
			Enchere enchere = enchereService.consulterEnchereParId(id);
			if (enchere != null) {
				model.addAttribute("encheres", enchere); // Correction de la variable ajoutée au modèle
				return "detailEnchere"; // Correction de l'alias de la vue
			} else {
				System.out.println("Enchère inconnue!!");
			}
		} else {
			System.out.println("Identifiant inconnu");
		}
		return "redirect:/encheres/encheres"; // Redirection vers la page d'accueil des enchères
	}

	 */

}
