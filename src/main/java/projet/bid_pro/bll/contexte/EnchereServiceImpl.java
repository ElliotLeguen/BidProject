package projet.bid_pro.bll.contexte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.dal.ArticleDAO;
import projet.bid_pro.dal.CategoriesDAO;
import projet.bid_pro.dal.EnchereDAO;

import java.util.List;

@Service
public class EnchereServiceImpl implements EnchereService{
    private EnchereDAO enchereDAO;
    private CategoriesDAO categoriesDAO;
    private ArticleDAO articleDAO;

    public EnchereServiceImpl(EnchereDAO enchereDAO, CategoriesDAO categoriesDAO, ArticleDAO articleDAO) {
        this.enchereDAO = enchereDAO;
        this.categoriesDAO = categoriesDAO;
        this.articleDAO = articleDAO;
    }


    @Override
    public List<Enchere> consulterEncheres() {
        return enchereDAO.findAll();
    }

    @Override
    public Enchere consulterEnchereParId(long id, long idUtil) {
        return enchereDAO.consulterEncheresParIdArticle(id, idUtil);
    }

    @Override
    public void creerEnchere(Enchere enchere){
        enchereDAO.creationEnchere(enchere);
    }

    @Override
    public List<Enchere> consulterEncheresParNomArticle(String nomArticle) {
        return null;
    }

    @Override
    public void creerArticle(ArticleVendu articleVendu){
        articleDAO.creerArticle(articleVendu);
    }

    @Override
    public void updateEnchere(Enchere enchere) {
        enchereDAO.updateEnchere(enchere);
    }

    @Override
    public Long consulterEnchereId(long id) {
         return enchereDAO.read(id);
    }
    @Override
    public List<Enchere> consulterAncienEnchere(long idUtil, long id) {
        return enchereDAO.consulterAncienEnchere(idUtil, id);
    }

}
