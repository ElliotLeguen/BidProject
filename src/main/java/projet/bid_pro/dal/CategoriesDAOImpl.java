package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Utilisateur;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoriesDAOImpl implements CategoriesDAO{

    private final String FIND_CATEGORIES = "SELECT * FROM CATEGORIES";
    private final String DELETE_CATEGORIES = "DELETE FROM CATEGORIES where no_categorie = :no_categorie";

    private final String AJOUTER_CATEGORIE = "INSERT INTO CATEGORIES (libelle) VALUES (:libelle);";
    private final String MODIFIER_CATEGORIE = "UPDATE CATEGORIES SET libelle = :libelle WHERE no_categorie = :no_categorie";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplateNamed;

    @Override
    public List<Categorie> readCategories() {
        String sql = "SELECT * FROM CATEGORIES";
        return jdbcTemplate.query(sql, new CategorieRowMapper());
    }

    @Override
    public Categorie readById(int id) {
        String sql = "SELECT * FROM CATEGORIES c WHERE c.no_categorie = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new CategorieRowMapper());
    }

    @Override
    public Categorie edit(Categorie categorie) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_categorie", categorie.getNoCategorie());
        namedParameters.addValue("libelle", categorie.getLibelle());

        jdbcTemplateNamed.update(MODIFIER_CATEGORIE, namedParameters);

        return categorie;
    }

    @Override
    public Categorie add(Categorie categorie) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("libelle", categorie.getLibelle());

        jdbcTemplateNamed.update(AJOUTER_CATEGORIE, namedParameters);

        return categorie;
    }

    @Override
    public void delete(int id) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("no_categorie", id);
        jdbcTemplateNamed.update(DELETE_CATEGORIES, namedParameters);
    }


    @Override
    public List<Categorie> consulterEncheresParCategorie(String nomCategorie) {
        return jdbcTemplate.query(FIND_CATEGORIES, new CategorieRowMapper());
    }

    public class CategorieRowMapper implements RowMapper<Categorie> {
        @Override
        public Categorie mapRow(ResultSet rs, int rowNum) throws SQLException {
            Categorie category = new Categorie();
            category.setNoCategorie(rs.getInt("no_categorie"));
            category.setLibelle(rs.getString("libelle"));
            return category;
        }
    }
}
