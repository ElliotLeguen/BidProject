package projet.bid_pro.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO {
    private final String FIND_BY_ID = "SELECT * from UTILISATEURS WHERE no_utilisateur = :no_utilisateur";
    private final String FIND_ALL = "SELECT * from UTILISATEURS";
    private final String FIND_BY_EMAIL = "SELECT * from UTILISATEURS " + " WHERE email = :email";

    private final String FIND_BY_PSEUDO = "SELECT * from UTILISATEURS " + " WHERE pseudo = :pseudo";
    private final String FIND_BY_TOKEN = "SELECT * from UTILISATEURS " + " WHERE reset_password_token = :reset_password_token";

    private final String AJOUTER_UTILISATEUR = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePasse, :credit, :administrateur);";

    private final String MODIFIER_UTILISATEUR = "UPDATE UTILISATEURS SET pseudo = :pseudo, nom = :nom, prenom = :prenom, email = :email, telephone = :telephone, rue = :rue, code_postal = :codePostal, ville = :ville, mot_de_passe = :mot_de_passe WHERE no_utilisateur = :no_utilisateur";
    private final String MODIFIER_PASSWORD = "UPDATE UTILISATEURS SET mot_de_passe = :mot_de_passe WHERE no_utilisateur = :no_utilisateur";
    private final String MODIFIER_UTILISATEUR_TOKEN = "UPDATE UTILISATEURS SET reset_password_token = :reset_password_token WHERE email = :email";

    private final String SUPPRIMER_ENCHERES = "DELETE FROM ENCHERES WHERE no_article IN (SELECT no_article FROM ARTICLES_VENDUS WHERE no_utilisateur = :no_utilisateur)";

    private final String SUPPRIMER_RETRAITS = "DELETE FROM RETRAITS WHERE no_article IN (SELECT no_article FROM ARTICLES_VENDUS WHERE no_utilisateur = :no_utilisateur)";

    private final String SUPPRIMER_ARTICLES_VENDUS = "DELETE FROM ARTICLES_VENDUS WHERE no_utilisateur = :no_utilisateur";

    private final String SUPPRIMER_UTILISATEUR = "DELETE FROM UTILISATEURS WHERE no_utilisateur = :no_utilisateur";
    private final String CHANGER_ETAT_UTILISATEUR = "UPDATE UTILISATEURS set etat = :etat WHERE no_utilisateur = :no_utilisateur";
    private final String AJOUTER_CREDIT = "UPDATE UTILISATEURS set credit = :credit WHERE no_utilisateur = :no_utilisateur";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Utilisateur read(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_utilisateur", id);
        return jdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new UtilisateurRowMapper());
    }

    @Override
    public Utilisateur read(String email) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("email", email);
        return jdbcTemplate.queryForObject(FIND_BY_EMAIL, namedParameters, new UtilisateurRowMapper());
    }

    @Override
    public Utilisateur getByPseudo(String pseudo) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", pseudo);
        return jdbcTemplate.queryForObject(FIND_BY_PSEUDO, namedParameters, new UtilisateurRowMapper());
    }

    @Override
    public Utilisateur ajouterUtilisateur(Utilisateur utilisateur) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String motDePasseEncode = encoder.encode(utilisateur.getMotDePasse());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("pseudo", utilisateur.getPseudo());
        namedParameters.addValue("nom", utilisateur.getNom());
        namedParameters.addValue("prenom", utilisateur.getPrenom());
        namedParameters.addValue("email", utilisateur.getEmail());
        namedParameters.addValue("telephone", utilisateur.getTelephone());
        namedParameters.addValue("rue", utilisateur.getRue());
        namedParameters.addValue("codePostal", utilisateur.getCodePostal());
        namedParameters.addValue("ville", utilisateur.getVille());
        namedParameters.addValue("motDePasse", motDePasseEncode);
        namedParameters.addValue("credit", utilisateur.getCredit());
        if(utilisateur.isAdministrateur() == null){
            namedParameters.addValue("administrateur", "ROLE_USER");
        }else namedParameters.addValue("administrateur", "ROLE_ADMIN");


        jdbcTemplate.update(AJOUTER_UTILISATEUR, namedParameters,keyHolder);
        if (keyHolder != null && keyHolder.getKey() != null) {
            // Mise à jour de l'identifiant du film auto-généré par la base
            utilisateur.setNoUtilisateur(keyHolder.getKey().intValue());
        }
        return utilisateur;
    }

    @Override
    public Utilisateur edit(Utilisateur utilisateur) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String motDePasseEncode = encoder.encode(utilisateur.getMotDePasse());
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_utilisateur", utilisateur.getNoUtilisateur());
        namedParameters.addValue("pseudo", utilisateur.getPseudo());
        namedParameters.addValue("nom", utilisateur.getNom());
        namedParameters.addValue("prenom", utilisateur.getPrenom());
        namedParameters.addValue("email", utilisateur.getEmail());
        namedParameters.addValue("telephone", utilisateur.getTelephone());
        namedParameters.addValue("rue", utilisateur.getRue());
        namedParameters.addValue("codePostal", utilisateur.getCodePostal());
        namedParameters.addValue("ville", utilisateur.getVille());
        namedParameters.addValue("mot_de_passe", motDePasseEncode);

        jdbcTemplate.update(MODIFIER_UTILISATEUR, namedParameters);

        return utilisateur;
    }

    @Override
    public void delete(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_utilisateur", id);
        jdbcTemplate.update(SUPPRIMER_ENCHERES, namedParameters);
        jdbcTemplate.update(SUPPRIMER_RETRAITS, namedParameters);
        jdbcTemplate.update(SUPPRIMER_ARTICLES_VENDUS, namedParameters);
        jdbcTemplate.update(SUPPRIMER_UTILISATEUR, namedParameters);
    }

    @Override
    public List<Utilisateur> listeUtilisateurs() {
        return jdbcTemplate.query(FIND_ALL, new UtilisateurRowMapper());
    }

    @Override
    public void changeEtat(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_utilisateur", id);
        Utilisateur utilisateur = jdbcTemplate.queryForObject(FIND_BY_ID, namedParameters, new UtilisateurRowMapper());
        byte isActive = utilisateur.getEtat();
        System.out.println(isActive);
        if(isActive == 1){
            namedParameters.addValue("etat", "0");
        }
        if(isActive == 0) {
            namedParameters.addValue("etat", "1");
        }
        jdbcTemplate.update(CHANGER_ETAT_UTILISATEUR, namedParameters);
    }

    @Override
    public void ajouterCredit(Utilisateur utilisateur) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_utilisateur", utilisateur.getNoUtilisateur());
        namedParameters.addValue("credit", utilisateur.getCredit());


        jdbcTemplate.update(AJOUTER_CREDIT, namedParameters);
    }

    @Override
    public Utilisateur findByResetPasswordToken(String token) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("reset_password_token", token);
        return jdbcTemplate.queryForObject(FIND_BY_TOKEN, namedParameters, new UtilisateurRowMapper());
    }

    @Override
    public void updatePassword(Utilisateur utilisateur, String newPassword) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String motDePasseEncode = encoder.encode(newPassword);
        namedParameters.addValue("no_utilisateur", utilisateur.getNoUtilisateur());
        namedParameters.addValue("mot_de_passe", motDePasseEncode);
        jdbcTemplate.update(MODIFIER_PASSWORD, namedParameters);

    }
    @Override
    public void updateResetPasswordToken(String token, String email) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("email", email);
        Utilisateur utilisateur = jdbcTemplate.queryForObject(FIND_BY_EMAIL, namedParameters, new UtilisateurRowMapper());
        if (utilisateur != null) {
            namedParameters.addValue("reset_password_token", token);
            jdbcTemplate.update(MODIFIER_UTILISATEUR_TOKEN, namedParameters);
        }
    }



    /**
     * Classe de mapping pour gérer les noms des colonnes
     */
    class UtilisateurRowMapper implements RowMapper<Utilisateur> {
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
            utilisateur.setEtat(rs.getByte("etat"));
            return utilisateur;
        }
    }
}
