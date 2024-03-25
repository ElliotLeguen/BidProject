package projet.bid_pro.dal;

import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Utilisateur;

import java.util.List;

public interface ArticleDAO {
    ArticleVendu read(int id);
    List<ArticleVendu> getArticles(String rqt);
    List<ArticleVendu> consulterEncheresParNomArticle(String nomArticle);
    List<ArticleVendu> consulterEncheresParCategorie(String categorie);

    ArticleVendu creerArticle(ArticleVendu articleVendu) ;
}
