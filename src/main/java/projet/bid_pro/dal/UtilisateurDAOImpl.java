package projet.bid_pro.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

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
    private final String FIND_BY_EMAIL = "SELECT * from UTILISATEURS "
            + " WHERE email = :email";
    private final String AJOUTER_UTILISATEUR = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe, credit, administrateur) VALUES (:pseudo, :nom, :prenom, :email, :telephone, :rue, :codePostal, :ville, :motDePasse, :credit, :administrateur);";

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
    public void ajouterUtilisateur(Utilisateur utilisateur) {
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
            return utilisateur;
        }
    }
}
