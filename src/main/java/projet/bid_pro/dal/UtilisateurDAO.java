package projet.bid_pro.dal;

import jdk.jshell.execution.Util;
import projet.bid_pro.bo.Utilisateur;

import java.util.List;

public interface UtilisateurDAO {
	Utilisateur read(int id);

	Utilisateur read(String email);

	Utilisateur ajouterUtilisateur(Utilisateur utilisateur);

	Utilisateur edit(Utilisateur utilisateur);

	void delete(int id);

	List<Utilisateur> listeUtilisateurs();

	void changeEtat (int id);
}
