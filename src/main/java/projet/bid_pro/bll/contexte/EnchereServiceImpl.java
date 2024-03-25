package projet.bid_pro.bll.contexte;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.dal.ArticlesDAO;
import projet.bid_pro.dal.ArticlesDAOImpl;
import projet.bid_pro.dal.CategoriesDAO;
import projet.bid_pro.dal.EnchereDAO;
import projet.bid_pro.dal.EnchereDAOImpl;

import java.util.List;

@Service
public class EnchereServiceImpl implements EnchereService{
    private EnchereDAO enchereDAO;
    private CategoriesDAO categoriesDAO;
    private ArticlesDAO articlesDAO;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
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
    public List<Enchere> consulterEncheresParCategorie(String categorie) {
        String requete = "SELECT * FROM CATEGORIES " +
                "INNER JOIN ARTICLES_VENDUS ON CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie " +
                "INNER JOIN ENCHERES ON ARTICLES_VENDUS.no_article = ENCHERES.no_article " +
                "WHERE CATEGORIES.libelle = :categorie";
        MapSqlParameterSource parametres = new MapSqlParameterSource();
        parametres.addValue("categorie", categorie);
        return jdbcTemplate.query(requete, parametres, new EnchereDAOImpl.EnchereRowMapper());

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
