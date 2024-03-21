package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.ArticleVendu;


import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ArticleDAOImpl implements ArticleDAO{
    private UtilisateurDAO utilisateurDAO;
    public ArticleDAOImpl(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }
    private final String FIND_BY_ID = "SELECT * FROM ARTICLES_VENDUS WHERE no_article = :no_article";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public ArticleVendu read(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_article", id);
        return jdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new ArticleRowMapper());
    }

    class ArticleRowMapper implements RowMapper<ArticleVendu> {
        @Override
        public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {
            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("no_article"));
            articleVendu.setNomArticle(rs.getString("nom_article"));
            articleVendu.setDescription(rs.getString("description"));
            articleVendu.setDateDebutEncheres(rs.getDate("date_debut_encheres"));
            articleVendu.setDateFinEncheres(rs.getDate("date_fin_encheres"));
            articleVendu.setPrixInitial(rs.getInt("prix_initial"));
            articleVendu.setPrixVente(rs.getInt("prix_vente"));
            articleVendu.setNoUtilisateur(utilisateurDAO.read(rs.getInt("no_utilisateur")));
            articleVendu.setNoArticle(rs.getInt("no_categorie"));
            return articleVendu;
        }
    }
}
