package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.bo.Utilisateur;
import projet.bid_pro.dal.EnchereDAO;
import projet.bid_pro.dal.UtilisateurDAO;

import java.util.List;

@Service
public class EnchereServiceImpl implements EnchereService{
    private EnchereDAO enchereDAO;
    private UtilisateurDAO utilisateurDAO;

    public EnchereServiceImpl(EnchereDAO enchereDAO) {
        this.enchereDAO = enchereDAO;
    }

    @Override
    public List<Enchere> consulterEncheres() {
        List<Enchere> encheres = enchereDAO.findAll();
        return encheres;
    }

    @Override
    public Enchere consulterEnchereParId(long id) {
        return null;
    }

    @Override
    public void creerEnchere(Enchere enchere) {

    }
}
