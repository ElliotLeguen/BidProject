package projet.bid_pro.bll.contexte;

import projet.bid_pro.bo.Utilisateur;

public interface UtilisateurService {
	Utilisateur charger(String email);

	Utilisateur register(Utilisateur utilisateur);

	Utilisateur edit(Utilisateur utilisateur);
}
