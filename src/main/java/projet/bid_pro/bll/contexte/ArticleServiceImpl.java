package projet.bid_pro.bll.contexte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.dal.*;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{
    private ArticleDAO articleDAO;
    private CategoriesDAO categoriesDAO;
    private EnchereDAO enchereDAO;

    public ArticleServiceImpl(ArticleDAO articleDAO, CategoriesDAO categoriesDAO, EnchereDAO enchereDAO) {
        this.articleDAO = articleDAO;
        this.categoriesDAO = categoriesDAO;
        this.enchereDAO = enchereDAO;
    }

    @Override
    public List<ArticleVendu> getVentes(String rqt) {
        return articleDAO.getArticles(rqt);
    }
    @Override
    public List<Categorie> consulterCategories() {
        return categoriesDAO.readCategories();
    }

    @Override
    public List<ArticleVendu> consulterEncheresParNomArticle(String nomArticle) {
        return articleDAO.consulterEncheresParNomArticle(nomArticle);
    }

    @Override
    public List<ArticleVendu> consulterEncheresParCategorie(String categorie) {
        return null;
    }

    @Override
    public void creerArticle(ArticleVendu articleVendu) {
        articleDAO.creerArticle(articleVendu);
    }

    @Override
    public Categorie consulterCategorieParId(int id) {
        return null;
    }
}
