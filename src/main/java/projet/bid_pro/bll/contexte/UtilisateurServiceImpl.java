package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;

import projet.bid_pro.bo.Utilisateur;
import projet.bid_pro.dal.UtilisateurDAO;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
	private UtilisateurDAO utilisateurDAO;

	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;
	}

	@Override
	public Utilisateur charger(String email) {
		return utilisateurDAO.read(email);
	}

	@Override
	public Utilisateur chargerParPseudo(String pseudo) {
		return utilisateurDAO.getByPseudo(pseudo);
	}

	@Override
	public Utilisateur charger(int id) {
		return utilisateurDAO.read(id);
	}

	@Override
	public Utilisateur register(Utilisateur utilisateur) {
		return utilisateurDAO.ajouterUtilisateur(utilisateur);
	}

	@Override
	public Utilisateur edit(Utilisateur utilisateur) {
		return utilisateurDAO.edit(utilisateur);
	}

	@Override
	public void delete(int id) {
		utilisateurDAO.delete(id);
	}

	@Override
	public List<Utilisateur> consulterUtilisateurs() {
		return utilisateurDAO.listeUtilisateurs();
	}

	@Override
	public void changeEtat(int id) {
		utilisateurDAO.changeEtat(id);
	}

	@Override
	public void ajouterCredit(Utilisateur utilisateur) {
		utilisateurDAO.ajouterCredit(utilisateur);
	}

	@Override
	public Utilisateur findByResetPasswordToken(String token) {
		return utilisateurDAO.findByResetPasswordToken(token);
	}

	@Override
	public void updatePassword(Utilisateur utilisateur, String newPassword) {
		utilisateurDAO.updatePassword(utilisateur,newPassword);
	}

	@Override
	public void updateResetPasswordToken(String token, String email) {
		utilisateurDAO.updateResetPasswordToken(token,email);
	}

	@Override
	public void enleverCredit(Utilisateur utilisateur){
		utilisateurDAO.ajouterCredit(utilisateur);
	}

	@Override
	public void consulterAncienEnchere(int noArticle, int noUtilisateur) {

	}
}
