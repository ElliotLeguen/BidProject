package projet.bid_pro.dal;

import jdk.jshell.execution.Util;
import projet.bid_pro.bo.Utilisateur;

import java.util.List;

public interface UtilisateurDAO {
	Utilisateur read(int id);

	Utilisateur read(String email);

	Utilisateur getByPseudo(String pseudo);

	Utilisateur ajouterUtilisateur(Utilisateur utilisateur);

	Utilisateur edit(Utilisateur utilisateur);

	void delete(int id);

	List<Utilisateur> listeUtilisateurs();

	void changeEtat (int id);

	void ajouterCredit(Utilisateur utilisateur);

	Utilisateur findByResetPasswordToken(String token);

	void updatePassword(Utilisateur utilisateur, String newPassword);

	void updateResetPasswordToken(String token,String email);

	void enleverCredit(Utilisateur utilisateur);

}
