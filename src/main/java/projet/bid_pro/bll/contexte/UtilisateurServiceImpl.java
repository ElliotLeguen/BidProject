package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;

import projet.bid_pro.bo.Utilisateur;
import projet.bid_pro.dal.UtilisateurDAO;

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
	public void register(Utilisateur utilisateur) {
		utilisateurDAO.ajouterUtilisateur(utilisateur);
	}


}
