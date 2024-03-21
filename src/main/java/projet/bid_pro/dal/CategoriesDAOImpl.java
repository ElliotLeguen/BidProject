package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.Categorie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CategoriesDAOImpl implements CategoriesDAO{

    private final String FIND_CATEGORIES = "SELECT * FROM CATEGORIES";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Categorie> readCategories() {
        String sql = "SELECT * FROM CATEGORIES";
        List<Categorie> test= jdbcTemplate.query(sql, new CategorieRowMapper());
        System.out.println(test);
        return test;
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
