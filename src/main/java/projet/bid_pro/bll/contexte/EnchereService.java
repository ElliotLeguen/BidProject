package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;

import java.util.List;
import java.util.Map;

public interface EnchereService {
    List<Enchere> consulterEncheres();
    Enchere consulterEnchereParId(long id, long idUtil);
    void creerEnchere(Enchere enchere);
    List<Enchere> consulterEncheresParNomArticle(String nomArticle);
    void creerArticle(ArticleVendu articleVendu);
    void updateEnchere(Enchere enchere);
    Long consulterEnchereId(long id);
    List<Enchere> consulterAncienEnchere(long id, long idUtil);
    Boolean readTopEnchere(int id, int idUser);
}
