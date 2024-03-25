package projet.bid_pro.dal;

import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;

import java.util.List;

public interface EnchereDAO {
    Enchere read(long id);
    List<Enchere> findAll();
    List<Enchere> consulterEncheresParNomArticle(String nomArticle);

}
