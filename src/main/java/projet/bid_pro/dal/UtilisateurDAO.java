package projet.bid_pro.dal;

import jdk.jshell.execution.Util;
import projet.bid_pro.bo.Utilisateur;

public interface UtilisateurDAO {
	Utilisateur read(long id);

	Utilisateur read(String email);
}
