package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.dal.ArticlesDAO;
import projet.bid_pro.dal.ArticlesDAOImpl;
import projet.bid_pro.dal.CategoriesDAO;
import projet.bid_pro.dal.EnchereDAO;

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
    public Enchere consulterEnchereParId(long id) {
        return null;
    }

    @Override
    public void creerEnchere(Enchere enchere) {
    }
    @Override
    public void creerArticle(ArticleVendu articleVendu){
        articlesDAO.creerArticle(articleVendu);
    }

    @Override
    public Categorie consulterCategorieParId(int id) {
        return categoriesDAO.readById(id);
    }
}
