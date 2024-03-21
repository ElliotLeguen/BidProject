package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.dal.ArticlesDAO;
import projet.bid_pro.dal.ArticlesDAOImpl;
import projet.bid_pro.dal.CategoriesDAO;
import projet.bid_pro.dal.EnchereDAO;
import projet.bid_pro.dal.UtilisateurDAO;

import java.util.List;

@Service
public class EnchereServiceImpl implements EnchereService{
    private EnchereDAO enchereDAO;
    private CategoriesDAO categoriesDAO;
    private ArticlesDAO articlesDAO;

    public EnchereServiceImpl(EnchereDAO enchereDAO, CategoriesDAO categoriesDAO, ArticlesDAO articlesDAO) {
        this.enchereDAO = enchereDAO;
        this.categoriesDAO = categoriesDAO;
        this.articlesDAO = articlesDAO;
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
        return null;
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
        return enchereDAO.getVentesTerminees();
    }

    @Override
    public Categorie consulterCategorieParId(int id) {
        return categoriesDAO.readById(id);
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
        return null;
    }

    @Override
    public void creerArticle(ArticleVendu articleVendu){
        articlesDAO.creerArticle(articleVendu);
    }
}
