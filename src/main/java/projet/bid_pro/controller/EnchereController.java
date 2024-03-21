package projet.bid_pro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;
import projet.bid_pro.bll.contexte.EnchereService;
import projet.bid_pro.bo.Enchere;

import java.util.List;

@Controller
public class EnchereController {

	private EnchereService enchereService;

	public EnchereController(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@GetMapping("/")
	public String afficherAccueil(Model model){
		List<Enchere> encheres = enchereService.consulterEncheres();
		var categories = enchereService.consulterCategories();
		model.addAttribute("categorie", categories);
		model.addAttribute("encheres", encheres);
		return "index";
	}

  	@GetMapping("/encheres")
	public String afficherEncheres(HttpServletRequest request, @RequestParam(name = "nomArticle", required = false) String nomArticle,
                                   @RequestParam(name = "categorie", required = false) String categorie,
                                   @RequestParam(name = "radio", required = false) String radio,
                                   Model model) {
		List<Enchere> encheres;

        boolean ventesEnCours = false;
        boolean ventesNonDebutees = false;
        boolean ventesTerminees = false;
        if (request.getParameter("mesVentesEnCours") != null) {
            ventesEnCours = true;
        }
        if (request.getParameter("ventesNonDebutees") != null) {
            ventesNonDebutees = true;
        }
        if (request.getParameter("ventesTerminees") != null) {
            ventesTerminees = true;
        }
        if (ventesEnCours && ventesNonDebutees && ventesTerminees) {
            // Filtrer par toutes les types de ventes
            // Appelez la méthode appropriée dans votre service pour obtenir toutes les types de ventes
            // en fonction de l'utilisateur connecté
            encheres = enchereService.getToutesVentes();
        } else if (ventesEnCours && ventesNonDebutees) {
            // Filtrer par les ventes en cours et non débutées
            encheres = enchereService.getVentesEnCoursEtNonDebutees();
        } else if (ventesEnCours && ventesTerminees) {
            // Filtrer par les ventes en cours et terminées
            encheres = enchereService.getVentesEnCoursEtTerminees();
        } else if (ventesNonDebutees && ventesTerminees) {
            // Filtrer par les ventes non débutées et terminées
            encheres = enchereService.getVentesNonDebuteesEtTerminees();
        } else if (ventesEnCours) {
            // Filtrer par les ventes en cours uniquement
            encheres = enchereService.getVentesEnCours();
        } else if (ventesNonDebutees) {
            // Filtrer par les ventes non débutées uniquement
            encheres = enchereService.getVentesNonDebutees();
        }  else {
            // Aucun filtre sélectionné, obtenir toutes les ventes
            encheres = enchereService.getToutesVentes();
        }

        encheres = enchereService.consulterEncheres();
        if (nomArticle != null && !nomArticle.isEmpty()) {
            // Si un nom d'article est spécifié, filtrer les enchères par ce nom
            encheres = enchereService.consulterEncheresParNomArticle(nomArticle);
        } if ("radioVentes".equals(radio)) {
            // Si "Mes ventes" est sélectionné
            if (ventesTerminees) {
                // Filtrer par les ventes terminées uniquement
                encheres = enchereService.getVentesTerminees();
            }
        }

//		if (categorie != null && !categorie.isEmpty()) {
//			// Filtrer par catégorie si spécifié
//			encheres = enchereService.consulterEncheresParCategorie(categorie);
//		}

		var categories = enchereService.consulterCategories();
		model.addAttribute("categorie", categories);
		model.addAttribute("encheres", encheres);
		System.out.println(encheres);
		return "encheres";
	}


	@GetMapping("/profilEnchere")
	public String AfficherProfilEnchere(){
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

	/*

	@ModelAttribute("genresEnSession")
	public List<Genre> chargerGenres() {
		System.out.println("Chargement en Session - GENRES");
		return filmService.consulterGenres();
	}


	@GetMapping("/creer")
	public String creerFilm(Model model, @ModelAttribute("membreEnSession") Membre membreEnSession) {
		if (membreEnSession != null && membreEnSession.getId() >= 1 && membreEnSession.isAdmin()) {
			// Il y a un membre en session et c'est un administrateur
			// Ajout de l'instance dans le modèle
			model.addAttribute("film", new Film());
			return "view-film-form";
		} else {
			// redirection vers la page des films
			return "redirect:/films";
		}
	}

	// Création d'un nouveau film
	@PostMapping("/creer")
	public String creerFilm(@Valid @ModelAttribute("film") Film film, BindingResult bindingResult,
			@ModelAttribute("membreEnSession") Membre membreEnSession) {

		if (membreEnSession != null && membreEnSession.getId() > 1 && membreEnSession.isAdmin()) {
			// Il y a un membre en session et c'est un administrateur
			if (!bindingResult.hasErrors()) {
				try {
					filmService.creerFilm(film);
					return "redirect:/films";
				} catch (BusinessException e) {
					System.err.println(e.getClefsExternalisations());
					// Afficher les messages d’erreur - il faut les injecter dans le contexte de
					// BindingResult
					e.getClefsExternalisations().forEach(key -> {
						ObjectError error = new ObjectError("globalError", key);
						bindingResult.addError(error);
					});
				}
			}
		} else {
			// Gestion d'une exception à afficher dans le cas où aucun membre administrateur
			// en session
			// Afficher les messages d’erreur - il faut les injecter dans le contexte de
			// BindingResult
			System.out.println("Aucun administrateur en session");
			ObjectError error = new ObjectError("globalError", BusinessCode.VALIDATION_MEMBRE_ADMIN);
			bindingResult.addError(error);
		}
		// Il y a des erreurs sur le formulaire
		return "view-film-form";
	}

	// Injection en session des listes représentant les participants
	@ModelAttribute("participantsEnSession")
	public List<Participant> chargerParticipants() {
		System.out.println("Chargement en Session - PARTICIPANTS");
		return filmService.consulterParticipants();
	}
	 */

}
