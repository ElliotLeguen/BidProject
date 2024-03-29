package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ArticleDAOImpl implements ArticleDAO {
    private final String FIND_ENCHERES_BY_ARTICLE = "SELECT * FROM ARTICLES_VENDUS inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";
    private final String FIND_ALL = "SELECT * FROM ARTICLES_VENDUS INNER JOIN UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur INNER JOIN CATEGORIES on CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie";
    private final String FIND_ALL_FIN_ENCHERE = "select * from ARTICLES_VENDUS INNER JOIN UTILISATEURS on ARTICLES_VENDUS.no_utilisateur = UTILISATEURS.no_utilisateur INNER JOIN CATEGORIES on CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie WHERE CONVERT(date, date_fin_encheres) = CONVERT(date, GETDATE());";
    private final String MODIFIER_ARTICLE_PRIX_VENTE = "UPDATE ARTICLES_VENDUS SET prix_vente = :prix_vente WHERE no_article = :no_article";
    private final String MODIFIER_CREDIT_UTILISATEUR_ARTICLE = "UPDATE UTILISATEURS SET credit = :credit WHERE no_utilisateur = :no_utilisateur";
    private final String FIND_UTILISATEUR = "SELECT credit FROM UTILISATEURS WHERE no_utilisateur = ?";
    private final String MODIFIER_ARTICLE = "UPDATE ARTICLES_VENDUS SET nom_article = :nom_article, description = :description, date_debut_encheres = :date_debut_encheres, date_fin_encheres = :date_fin_encheres, prix_initial = :prix_initial, prix_vente = :prix_vente, no_categorie = :no_categorie, image= :image WHERE no_article = :no_article";
    private final String SUPPRIMER_ARTICLE = "DELETE FROM ENCHERES WHERE no_article = :no_article; " +
            "DELETE FROM ARTICLES_VENDUS WHERE no_article = :no_article";
    private final String FIND_TOP_UTI = "select TOP(1) * from ENCHERES INNER JOIN UTILISATEURS ON ENCHERES.no_utilisateur = UTILISATEURS.no_utilisateur INNER JOIN ARTICLES_VENDUS on ENCHERES.no_article = ARTICLES_VENDUS.no_article where ENCHERES.no_article = :no_article order by montant_enchere DESC";
    private final String FIND_UTILISATEURREMB = "select  * from UTILISATEURS where no_utilisateur = :no_utilisateur";
    private final String REMBOURSER_CREDIT = "UPDATE UTILISATEURS SET credit = :credit  WHERE no_utilisateur = :no_utilisateur";

//            "DELETE FROM ENCHERES WHERE no_article IN (SELECT no_article FROM ARTICLES_VENDUS WHERE no_article = ?); " +
//            "DELETE FROM ARTICLES_VENDUS WHERE no_article = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplateNamedParameter;


    @Override
    public ArticleVendu creerArticle(ArticleVendu article) {
        String sql = "INSERT INTO ARTICLES_VENDUS (nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie, image) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                    ps.setString(1, article.getNomArticle());
                    ps.setString(2, article.getDescription());
                    ps.setDate(3, new java.sql.Date(article.getDateDebutEncheres().getTime()));
                    ps.setDate(4, new java.sql.Date(article.getDateFinEncheres().getTime()));
                    ps.setInt(5, article.getPrixInitial());
                    ps.setInt(6, article.getPrixVente() == null ? 0 : article.getPrixVente());
                    ps.setInt(7, article.getUtilisateur().getNoUtilisateur());
                    ps.setInt(8, article.getCategorie().getNoCategorie());
                    ps.setString(9, article.getImage());
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
        namedParameters.addValue("no_categorie", articleVendu.getCategorie().getNoCategorie());
        namedParameters.addValue("image", articleVendu.getImage());

        jdbcTemplateNamedParameter.update(MODIFIER_ARTICLE, namedParameters);

        return articleVendu;
    }

    @Override
    public void finEnchereArticle() {
        List<ArticleVendu> listArticles = jdbcTemplate.query(FIND_ALL_FIN_ENCHERE, new ArticleRowMapper());
        for (ArticleVendu article : listArticles) {


            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("no_article", article.getNoArticle());
            namedParameters.addValue("prix_vente", article.getActuelMeilleurPrix());

            jdbcTemplateNamedParameter.update(MODIFIER_ARTICLE_PRIX_VENTE, namedParameters);

            int credit = jdbcTemplate.queryForObject(FIND_UTILISATEUR, int.class, article.getUtilisateur().getNoUtilisateur());
            MapSqlParameterSource namedParameters2 = new MapSqlParameterSource();
            namedParameters2.addValue("no_utilisateur", article.getUtilisateur().getNoUtilisateur());
            namedParameters2.addValue("credit", credit + article.getActuelMeilleurPrix());

            jdbcTemplateNamedParameter.update(MODIFIER_CREDIT_UTILISATEUR_ARTICLE, namedParameters2);


            System.out.println(credit);
        }
    }

    @Override
    public void supprArticle(ArticleVendu idArticle) {

        MapSqlParameterSource namedParameters4 = new MapSqlParameterSource();
        namedParameters4.addValue("no_article", idArticle.getNoArticle());

        try {
            Enchere enchere = jdbcTemplateNamedParameter.queryForObject(FIND_TOP_UTI, namedParameters4, new EnchereRowMapperTest());
            assert enchere != null;
            MapSqlParameterSource namedParameters3 = new MapSqlParameterSource();

            int res = enchere.getUtilisateur().getCredit() + idArticle.getActuelMeilleurPrix();
            namedParameters3.addValue("no_utilisateur", enchere.getUtilisateur().getNoUtilisateur());
            namedParameters3.addValue("credit", enchere.getUtilisateur().getCredit() + idArticle.getActuelMeilleurPrix());

            jdbcTemplateNamedParameter.update(REMBOURSER_CREDIT, namedParameters3);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Aucune enchère trouvée. Supprimer l'article.");
        }
        MapSqlParameterSource namedParameters2 = new MapSqlParameterSource();
        namedParameters2.addValue("no_article", idArticle.getNoArticle());
        jdbcTemplateNamedParameter.update(SUPPRIMER_ARTICLE, namedParameters2);
    }

    @Override
    public ArticleVendu read(long id) {
        String FIND_BY_ID = "SELECT * FROM ARTICLES_VENDUS " +
                " INNER JOIN UTILISATEURS ON UTILISATEURS.no_utilisateur = ARTICLES_VENDUS.no_utilisateur" +
                " INNER JOIN CATEGORIES ON ARTICLES_VENDUS.no_categorie = CATEGORIES.no_categorie" +
                " WHERE ARTICLES_VENDUS.no_article = ? ";
        return jdbcTemplate.query(FIND_BY_ID, new Object[]{id}, new ArticleRowMapper()).get(0);
    }

    @Override
    public List<ArticleVendu> getArticles(String rqt) {
        return jdbcTemplate.query(rqt, new ArticleRowMapper());
    }

    @Override
    public List<ArticleVendu> getAllArticles() {
        return jdbcTemplate.query(FIND_ALL, new ArticleRowMapper());
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
            articleVendu.setActuelMeilleurPrix(rs.getInt("actuelMeilleurPrix"));
            articleVendu.setIdUtilisateurGagnant(rs.getInt("idUtilisateurGagnant"));
            articleVendu.setImage(rs.getString("image"));

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

    class EnchereRowMapperTest implements RowMapper<Enchere> {
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
            articleVendu.setActuelMeilleurPrix(rs.getInt("actuelMeilleurPrix"));
            articleVendu.setIdUtilisateurGagnant(rs.getInt("idUtilisateurGagnant"));
            articleVendu.setImage(rs.getString("image"));

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

    class UtilisateurRowMapperTest implements RowMapper<Utilisateur> {
        @Override
        public Utilisateur mapRow(ResultSet rs, int rowNum) throws SQLException {

            Utilisateur utilisateur = new Utilisateur();


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


            return utilisateur;
        }
    }
}
