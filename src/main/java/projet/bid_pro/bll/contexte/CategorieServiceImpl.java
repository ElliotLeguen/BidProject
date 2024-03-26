package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.dal.ArticleDAO;
import projet.bid_pro.dal.CategoriesDAO;
import projet.bid_pro.dal.EnchereDAO;

import java.util.List;
@Service
public class CategorieServiceImpl implements CategorieService{

    private CategoriesDAO categoriesDAO;

    public CategorieServiceImpl(CategoriesDAO categoriesDAO) {
        this.categoriesDAO = categoriesDAO;
    }

    @Override
    public Categorie consulterCategorieParId(int id) {
        return categoriesDAO.readById(id);
    }

    @Override
    public List<Categorie> consulterCategories() {
        return categoriesDAO.readCategories();
    }
}
