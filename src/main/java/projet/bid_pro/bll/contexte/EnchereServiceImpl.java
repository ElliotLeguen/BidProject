package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.bo.Utilisateur;
import projet.bid_pro.dal.CategoriesDAO;
import projet.bid_pro.dal.EnchereDAO;
import projet.bid_pro.dal.UtilisateurDAO;

import java.util.List;

@Service
public class EnchereServiceImpl implements EnchereService{
    private EnchereDAO enchereDAO;
    private CategoriesDAO categoriesDAO;

    public EnchereServiceImpl(EnchereDAO enchereDAO, CategoriesDAO categoriesDAO) {
        this.enchereDAO = enchereDAO;
        this.categoriesDAO = categoriesDAO;
    }
    @Override
    public List<Enchere> consulterEncheres() {
        List<Enchere> encheres = enchereDAO.findAll();
        return encheres;
    }
    @Override
    public List<Categorie> consulterCategories() {
        return categoriesDAO.readCategories();
    }

    @Override
    public List<Enchere> getToutesVentes() {
        List<Enchere> encheres = enchereDAO.getToutesVentes();
        return encheres;
    }

    @Override
    public List<Enchere> getVentesEnCoursEtNonDebutees() {
        return null;
    }

    @Override
    public List<Enchere> getVentesEnCoursEtTerminees() {
        return null;
    }

    @Override
    public List<Enchere> getVentesNonDebuteesEtTerminees() {
        return null;
    }

    @Override
    public List<Enchere> getVentesEnCours() {
        return null;
    }

    @Override
    public List<Enchere> getVentesNonDebutees() {
        return null;
    }

    @Override
    public List<Enchere> getVentesTerminees() {
        return null;
    }

    @Override
    public Enchere consulterEnchereParId(long id) {
        return null;
    }

    @Override
    public void creerEnchere(Enchere enchere) {
    }

    @Override
    public List<Enchere> consulterEncheresParNomArticle(String nomArticle) {
        return enchereDAO.consulterEncheresParNomArticle(nomArticle);
    }
}
