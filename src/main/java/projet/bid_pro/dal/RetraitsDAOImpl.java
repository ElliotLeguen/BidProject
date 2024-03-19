package projet.bid_pro.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import projet.bid_pro.bo.Retrait;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RetraitsDAOImpl implements RetraitsDAO{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Retrait readRetrait(long id) {
        String sql = "SELECT * FROM Tmembres WHERE iKey_membre=?";
        Retrait membreConnecte = jdbcTemplate.queryForObject(sql, new Object[]{id}, new MembreRowMapper());
        return membreConnecte;
    }
    public class MembreRowMapper implements RowMapper<Retrait> {

        @Override
        public Retrait mapRow(ResultSet rs, int rowNum) throws SQLException {
            Retrait retrait = new Retrait();
            retrait.setNoArticle(rs.getInt("no_article"));
            retrait.setRue(rs.getString("rue"));
            retrait.setCodePostal(rs.getString("code_postal"));
            retrait.setVille(rs.getString("ville"));
            return retrait;
        }
    }
}
