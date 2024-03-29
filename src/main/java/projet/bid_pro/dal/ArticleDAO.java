package projet.bid_pro.dal;

import projet.bid_pro.bo.ArticleVendu;

import java.util.List;

public interface ArticleDAO {
    ArticleVendu read(long id);
    List<ArticleVendu> getArticles(String rqt);
    List<ArticleVendu> getAllArticles();
    List<ArticleVendu> consulterEncheresParNomArticle(String nomArticle);
    List<ArticleVendu> consulterEncheresParCategorie(String categorie);
    ArticleVendu creerArticle(ArticleVendu articleVendu);
    ArticleVendu editArticle(ArticleVendu articleVendu);
    void supprArticle(ArticleVendu idArticle);
    void finEnchereArticle();
}
