package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;
import projet.bid_pro.bo.Utilisateur;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class EnchereDAOImpl implements EnchereDAO{

    private final String FIND_ALL = "SELECT *  FROM ENCHERES inner join ARTICLES_VENDUS on ENCHERES.no_article = ARTICLES_VENDUS.no_article inner join UTILISATEURS on ENCHERES.no_utilisateur = UTILISATEURS.no_utilisateur";
    private final String FIND_ENCHERES_BY_ARTICLE = "SELECT * FROM ARTICLES_VENDUS inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";
    private final String FIND_ENCHERES_BY_CATEGORIE = "SELECT * FROM CATEGORIES inner join ARTICLES_VENDUS on CATEGORIES.no_categorie = ARTICLES_VENDUS.no_categorie inner join ENCHERES on ARTICLES_VENDUS.no_article = ENCHERES.no_article";
    private final String FIND_VENTES_BY_ID = "SELECT * FROM ENCHERES inner join ARTICLES_VENDUS on ENCHERES.no_article = ARTICLES_VENDUS.no_article inner join UTILISATEURS on ENCHERES.no_utilisateur = UTILISATEURS.no_utilisateur WHERE ARTICLES_VENDUS.prix_vente IS NOT NULL";
    private final String FIND_LAST_UTI = "select TOP(1) no_utilisateur,no_article, date_enchere,montant_enchere from ENCHERES where ENCHERES.no_utilisateur != ? and ENCHERES.no_article = ? order by montant_enchere DESC";

    private final String FIND_TOP_UTI = "select TOP(1) no_utilisateur,no_article, date_enchere,montant_enchere from ENCHERES where ENCHERES.no_article = ? order by montant_enchere DESC";

    String sql = "SELECT no_utilisateur FROM ENCHERES WHERE no_article = ? AND montant_enchere = (SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article = ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Boolean readTopEnchere(int id, int idUser) {
        Enchere enchere = jdbcTemplate.queryForObject(FIND_TOP_UTI, new Object[]{id}, new EnchereRowMapperTest());
        return enchere.getUtilisateur().getNoUtilisateur() == idUser;
    }
    public Long read(long id) {
        String  FIND_BY_IDD = "SELECT MAX(ENCHERES.montant_enchere) SUM_QUANTITY " +
                "FROM ENCHERES " +
                "WHERE ENCHERES.no_article = ?";
        try {
            return jdbcTemplate.queryForObject(FIND_BY_IDD, long.class, id);
        } catch (EmptyResultDataAccessException e) {
            return null; // or throw an exception depending on your requirement
        }
    }

    @Override
    public Enchere consulterEncheresParIdArticle(long id, long idUtil) {
        String FIND_BY_ID = "SELECT * FROM ENCHERES " +
                "INNER JOIN UTILISATEURS ON UTILISATEURS.no_utilisateur = ENCHERES.no_utilisateur " +
                "INNER JOIN ARTICLES_VENDUS on ARTICLES_VENDUS.no_article = ENCHERES.no_article " +
                "WHERE ENCHERES.no_article = ? and ENCHERES.no_utilisateur = ?";
        try {
            return jdbcTemplate.queryForObject(FIND_BY_ID, new Object[]{id, idUtil}, new EnchereRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Enchere> consulterAncienEnchere(long no_article, long no_utilisateur) {
        return jdbcTemplate.query(FIND_LAST_UTI,new Object[]{no_article, no_utilisateur}, new EnchereRowMapperTest());
    }
    @Override
    public List<Enchere> findAll() {
        return jdbcTemplate.query(FIND_ALL, new EnchereRowMapper());
    }

    @Override
    public List<Enchere> consulterEncheresParNomArticle(String nomArticle) {
        return jdbcTemplate.query(FIND_ENCHERES_BY_ARTICLE, new EnchereRowMapper());
    }

    @Override
    public Enchere creationEnchere(Enchere enchere) {
        String sql ="INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere)" +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update( connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
                ps.setInt(1,enchere.getUtilisateur().getNoUtilisateur());
                ps.setInt(2,enchere.getArticle().getNoArticle());
                ps.setDate(3,new Date(enchere.getDateEnchere().getTime()));
                ps.setInt(4, enchere.getMontantEnchere());
                return ps;
            });
        editActuelMeilleurPrix(enchere);
        return enchere;
    }
    public void editActuelMeilleurPrix(Enchere enchere){
        String sql = "UPDATE ARTICLES_VENDUS set actuelMeilleurPrix = ? WHERE no_article = ?";
        jdbcTemplate.update(sql, enchere.getMontantEnchere(), enchere.getArticle().getNoArticle());
    }

    @Override
    public Boolean newUtilisateurOnBid(Enchere enchere) {
        return null;
    }

    @Override
    public Enchere updateEnchere(Enchere enchere) {
        String sql = "UPDATE ENCHERES SET montant_enchere = ? WHERE no_article = ? and no_utilisateur = ?";
        jdbcTemplate.update(sql, enchere.getMontantEnchere(), enchere.getArticle().getNoArticle(), enchere.getUtilisateur().getNoUtilisateur());
        editActuelMeilleurPrix(enchere);
        return enchere;
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

    class EnchereRowMapperTest implements RowMapper<Enchere> {
        @Override
        public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
            Enchere enchere = new Enchere();
            Utilisateur utilisateur = new Utilisateur();
            ArticleVendu articleVendu = new ArticleVendu();

            articleVendu.setNoArticle(rs.getInt("no_article"));
            utilisateur.setNoUtilisateur(rs.getInt("no_utilisateur"));

            enchere.setUtilisateur(utilisateur);
            enchere.setArticle(articleVendu);
            enchere.setDateEnchere(rs.getTimestamp("date_enchere"));
            enchere.setMontantEnchere(rs.getInt("montant_enchere"));

            return enchere;
        }
    }


}
