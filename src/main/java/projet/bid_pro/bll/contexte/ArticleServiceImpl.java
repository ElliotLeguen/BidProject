package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.dal.*;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{
    private ArticleDAO articleDAO;
    private CategoriesDAO categoriesDAO;

    public ArticleServiceImpl(ArticleDAO articleDAO, CategoriesDAO categoriesDAO) {
        this.articleDAO = articleDAO;
        this.categoriesDAO = categoriesDAO;
    }

    @Override
    public List<ArticleVendu> getVentes(String rqt) {
    return articleDAO.getArticles(rqt);
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
    public ArticleVendu consulterArticleParId(long id) {
        return articleDAO.read(id);
    }

}
