package projet.bid_pro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/vendreArticle")
// Injection de la liste des attributs en session
public class ArticleController {

	// Dépendance
//	private Enche filmService;
//
//	public FilmController(FilmService filmService) {
//		this.filmService = filmService;
//	}

	/**
	 * La méthode réagit à l'url /films et la méthode Get du protocole HTTP
	 *
	 * @param model -- pour injecter des données à la vue
	 * @return l'alias de la page à afficher
	 */
	@GetMapping
	public String afficherArticle(Model model) {
		return "vendreArticle";
	}



}
