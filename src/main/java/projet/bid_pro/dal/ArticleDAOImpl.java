package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Utilisateur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ArticleDAOImpl implements ArticleDAO{
    private final String FIND_ENCHERES_BY_ARTICLE = "SELECT * FROM ARTICLES_VENDUS inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";
    private final String MODIFIER_ARTICLE = "UPDATE ARTICLES_VENDUS SET nom_article = :nom_article, description = :description, date_debut_encheres = :date_debut_encheres, date_fin_encheres = :date_fin_encheres, prix_initial = :prix_initial, prix_vente = :prix_vente, no_utilisateur =:no_utilisateur , no_categorie = :no_categorie, image= :image WHERE no_article = :no_article";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplateNamedParameter;

    @Override
    public ArticleVendu creerArticle(ArticleVendu article) {
        String sql = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, article.getNomArticle());
            ps.setString(2, article.getDescription());
            ps.setDate(3,new java.sql.Date(article.getDateDebutEncheres().getTime()));
            ps.setDate(4,new java.sql.Date(article.getDateFinEncheres().getTime()));
            ps.setInt(5, article.getPrixInitial());
            ps.setInt(6, article.getPrixVente() == null ? 0 : article.getPrixVente());
            ps.setInt(7, article.getUtilisateur().getNoUtilisateur());
            ps.setInt(8, article.getCategorie().getNoCategorie());
            return ps;
                }, keyHolder);
        if (keyHolder.getKey() != null) {
            article.setNoArticle(keyHolder.getKey().intValue());
            }
        return article;
    }


    @Override
    public ArticleVendu editArticle(ArticleVendu articleVendu) {

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_article", articleVendu.getNoArticle());
        namedParameters.addValue("nom_article", articleVendu.getNomArticle());
        namedParameters.addValue("description", articleVendu.getDescription());
        namedParameters.addValue("date_debut_encheres", articleVendu.getDateDebutEncheres());
        namedParameters.addValue("date_fin_encheres", articleVendu.getDateFinEncheres());
        namedParameters.addValue("prix_initial", articleVendu.getPrixInitial());
        namedParameters.addValue("prix_vente", articleVendu.getPrixVente());
        namedParameters.addValue("no_utilisateur",37);
        namedParameters.addValue("no_categorie", 3);
        namedParameters.addValue("image", null);

        jdbcTemplateNamedParameter.update(MODIFIER_ARTICLE, namedParameters);

        return articleVendu;
    }
    @Override
    public ArticleVendu read(long id) {
        String FIND_BY_ID = "SELECT * FROM ARTICLES_VENDUS " +
                " INNER JOIN UTILISATEURS ON UTILISATEURS.no_utilisateur = ARTICLES_VENDUS.no_utilisateur" +
                " INNER JOIN CATEGORIES ON ARTICLES_VENDUS.no_categorie = CATEGORIES.no_categorie"+
                " WHERE ARTICLES_VENDUS.no_article = ? ";
        return jdbcTemplate.query(FIND_BY_ID, new Object[]{id}, new ArticleRowMapper()).get(0);
    }

    @Override
    public List<ArticleVendu> getArticles(String rqt) {
        return jdbcTemplate.query(rqt, new ArticleRowMapper());
    }

    @Override
    public List<ArticleVendu> consulterEncheresParNomArticle(String nomArticle) {
        return jdbcTemplate.query(FIND_ENCHERES_BY_ARTICLE, new ArticleRowMapper());
    }

    @Override
    public List<ArticleVendu> consulterEncheresParCategorie(String categorie) {
        String requete = "SELECT * FROM CATEGORIES " +
                "INNER JOIN ARTICLES_VENDUS ON CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie " +
                "INNER JOIN UTILISATEURS ON UTILISATEURS.no_utilisateur = ARTICLES_VENDUS.no_utilisateur " +
                "WHERE CATEGORIES.no_categorie = ? " +
                "ORDER BY CATEGORIES.libelle";
        return jdbcTemplate.query(requete, new Object[]{categorie}, new ArticleRowMapper());
    }


    public static class ArticleRowMapper implements RowMapper<ArticleVendu> {
        @Override
        public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {
            Utilisateur utilisateur = new Utilisateur();
            Categorie categorie = new Categorie();
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

            categorie.setNoCategorie(rs.getInt("no_categorie"));
            categorie.setLibelle(rs.getString("libelle"));

            articleVendu.setUtilisateur(utilisateur);
            articleVendu.setCategorie(categorie);
            return articleVendu;
        }
    }
}
