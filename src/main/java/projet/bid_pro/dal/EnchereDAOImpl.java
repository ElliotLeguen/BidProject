package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.bo.Utilisateur;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO{

    private UtilisateurDAO utilisateurDAO;
    private final String FIND_BY_ID = "SELECT * FROM ENCHERES WHERE no_utilisateur = :no_utilisateur";
    private final String FIND_ALL = "SELECT ENCHERES.no_article, ENCHERES.no_utilisateur, UTILISATEURS.nom, ARTICLES_VENDUS.nom_article, date_enchere, montant_enchere  FROM ENCHERES inner join ARTICLES_VENDUS on ENCHERES.no_article = ARTICLES_VENDUS.no_article inner join UTILISATEURS on ENCHERES.no_utilisateur = UTILISATEURS.no_utilisateur";

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

    class EnchereRowMapper implements RowMapper<Enchere> {
        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere enchere = new Enchere();
            enchere.setDateEnchere(rs.getDate("date_enchere"));
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));
            //enchere.setNoUtilisateur(utilisateurDAO.read(rs.getInt("no_utilisateur")));

            System.out.println(enchere);
            Utilisateur utilisateur = new Utilisateur();

            enchere.setNoUtilisateur(utilisateur);

            ArticleVendu articleVendu = new ArticleVendu();
            articleVendu.setNoArticle(rs.getInt("no_article"));
            enchere.setNoArticle(articleVendu);

            return enchere;
        }
    }
}
