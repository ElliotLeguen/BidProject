package projet.bid_pro.dal;

import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Enchere;

import java.util.List;

public interface EnchereDAO {
    Long read(long id);
    Enchere consulterEncheresParIdArticle(long id, long idUtil);
    List<Enchere> findAll();
    Enchere creationEnchere(Enchere enchere);

    Boolean newUtilisateurOnBid(Enchere enchere);

    Enchere updateEnchere(Enchere enchere);
    List<Enchere> consulterEncheresParNomArticle(String nomArticle);
}
