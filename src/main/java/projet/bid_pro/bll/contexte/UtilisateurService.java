package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Utilisateur;

import java.util.List;
public interface UtilisateurService {
	Utilisateur charger(String email);
	Utilisateur charger(int id);

	Utilisateur register(Utilisateur utilisateur);

	Utilisateur edit(Utilisateur utilisateur);
	void delete(int id);

	List<Utilisateur> consulterUtilisateurs();

	void changeEtat(int id);

	void ajouterCredit(Utilisateur utilisateur);
}
