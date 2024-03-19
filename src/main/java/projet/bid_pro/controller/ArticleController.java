package projet.bid_pro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import projet.bid_pro.bll.EnchereService;

@Controller
// Injection de la liste des attributs en session
public class ArticleController {

	private EnchereService enchereService;
	public ArticleController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}
	@GetMapping("/article")
	public String afficherEncheres(Model model) {
		var categories = enchereService.consulterCategories();
		model.addAttribute("categorie", categories);
		return "vendreArticle";
	}
}
