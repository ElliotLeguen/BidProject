package projet.bid_pro.bll.contexte;

import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;

import java.util.List;

public interface EnchereService {
    List<Enchere> consulterEncheres();
    Enchere consulterEnchereParId(long id);
    void creerEnchere(ArticleVendu articleVendu);
    List<Enchere> consulterEncheresParNomArticle(String nomArticle);
    void creerArticle(ArticleVendu articleVendu);

}
