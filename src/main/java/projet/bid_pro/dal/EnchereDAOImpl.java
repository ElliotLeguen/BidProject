package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.Enchere;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO{

    private static UtilisateurDAO utilisateurDAO;
    private static ArticleDAO articleDAO;

    public EnchereDAOImpl(UtilisateurDAO utilisateurDAO, ArticleDAO articleDAO) {
        this.utilisateurDAO = utilisateurDAO;
        this.articleDAO = articleDAO;
    }

    private final String FIND_BY_ID = "SELECT * FROM ENCHERES WHERE no_utilisateur = :no_utilisateur";
    private final String FIND_ALL = "SELECT *  FROM ENCHERES inner join ARTICLES_VENDUS on ENCHERES.no_article = ARTICLES_VENDUS.no_article inner join UTILISATEURS on ENCHERES.no_utilisateur = UTILISATEURS.no_utilisateur";
    private final String FIND_ENCHERES_BY_ARTICLE = "SELECT * FROM ARTICLES_VENDUS inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";
    private final String FIND_ENCHERES_BY_CATEGORIE = "SELECT * FROM CATEGORIES inner join ARTICLES_VENDUS on CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Enchere read(long id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_utilisateur", id);
        return jdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new EnchereRowMapper());
    }
    @Override
    public List<Enchere> findAll() {
        return jdbcTemplate.query(FIND_ALL, new EnchereRowMapper());
    }

    @Override
    public List<Enchere> consulterEncheresParNomArticle(String nomArticle) {
        return jdbcTemplate.query(FIND_ENCHERES_BY_ARTICLE, new EnchereRowMapper());
    }

    public static class EnchereRowMapper implements RowMapper<Enchere> {
        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere enchere = new Enchere();
            enchere.setNoUtilisateur(utilisateurDAO.read(rs.getInt("no_utilisateur")));
            enchere.setNoArticle(articleDAO.read(rs.getInt("no_article")));
            enchere.setDateEnchere(rs.getDate("date_enchere"));
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));
            return enchere;
        }
    }
}
