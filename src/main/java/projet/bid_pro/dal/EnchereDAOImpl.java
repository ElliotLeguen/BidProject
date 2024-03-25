package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.bo.Utilisateur;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class EnchereDAOImpl implements EnchereDAO{

    private final String FIND_ALL = "SELECT *  FROM ENCHERES inner join ARTICLES_VENDUS on ENCHERES.no_article = ARTICLES_VENDUS.no_article inner join UTILISATEURS on ENCHERES.no_utilisateur = UTILISATEURS.no_utilisateur";
    private final String FIND_ENCHERES_BY_ARTICLE = "SELECT * FROM ARTICLES_VENDUS inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";
    private final String FIND_ENCHERES_BY_CATEGORIE = "SELECT * FROM CATEGORIES inner join ARTICLES_VENDUS on CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";
    private final String FIND_VENTES_BY_ID = "SELECT * FROM ENCHERES inner join ARTICLES_VENDUS on ENCHERES.no_article = ARTICLES_VENDUS.no_article inner join UTILISATEURS on ENCHERES.no_utilisateur = UTILISATEURS.no_utilisateur WHERE ARTICLES_VENDUS.prix_vente IS NOT NULL";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Enchere read(long id) {
        String FIND_BY_ID = "SELECT * FROM ENCHERES " +
                " INNER JOIN UTILISATEURS ON UTILISATEURS.no_utilisateur = ENCHERES.no_utilisateur" +
                " INNER JOIN ARTICLES_VENDUS ON articleVendu.no_article = ENCHERES.no_article"+
                " WHERE ENCHERES.no_article = ? ";
        return jdbcTemplate.queryForObject(FIND_BY_ID,new Object[]{id}, new EnchereRowMapper());
    }
    @Override
    public List<Enchere> findAll() {
        return jdbcTemplate.query(FIND_ALL, new EnchereRowMapper());
    }

    @Override
    public List<Enchere> consulterEncheresParNomArticle(String nomArticle) {
        return jdbcTemplate.query(FIND_ENCHERES_BY_ARTICLE, new EnchereRowMapper());
    }

    public class EnchereRowMapper implements RowMapper<Enchere> {

        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere enchere = new Enchere();
            Utilisateur utilisateur = new Utilisateur();
            ArticleVendu articleVendu = new ArticleVendu();

            articleVendu.setNoArticle(rs.getInt("no_article"));
            articleVendu.setNomArticle(rs.getString("nom_article"));
            articleVendu.setDescription(rs.getString("description"));
            articleVendu.setDateDebutEncheres(rs.getDate("date_debut_encheres"));
            articleVendu.setDateFinEncheres(rs.getDate("date_fin_encheres"));
            articleVendu.setPrixInitial(rs.getInt("prix_initial"));
            articleVendu.setPrixVente(rs.getInt("prix_vente"));

            utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));
            utilisateur.setPseudo(rs.getString("pseudo"));
            utilisateur.setNom(rs.getString("nom"));
            utilisateur.setPrenom(rs.getString("prenom"));
            utilisateur.setEmail(rs.getString("email"));
            utilisateur.setTelephone(rs.getString("telephone"));
            utilisateur.setRue(rs.getString("rue"));
            utilisateur.setCodePostal(rs.getString("code_postal"));
            utilisateur.setVille(rs.getString("ville"));
            utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
            utilisateur.setCredit(rs.getInt("credit"));
            utilisateur.setAdministrateur(rs.getString("administrateur"));

            enchere.setUtilisateur(utilisateur);
            enchere.setArticle(articleVendu);

            enchere.setDateEnchere(rs.getTimestamp("date_enchere"));
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));
            return enchere;
        }
    }


}
