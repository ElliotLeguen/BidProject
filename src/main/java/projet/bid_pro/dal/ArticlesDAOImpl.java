package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.ArticleVendu;

@Repository
public class ArticlesDAOImpl implements ArticlesDAO{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void creerArticle(ArticleVendu article) {
        String sql = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                article.getNomArticle(),
                article.getDescription(),
                article.getDateDebutEncheres(),
                article.getDateFinEncheres(),
                article.getPrixInitial(),
                article.getPrixVente(),
                article.getUtilisateur().getNoUtilisateur(),
                article.getCategorie().getNoCategorie());
    }
}
