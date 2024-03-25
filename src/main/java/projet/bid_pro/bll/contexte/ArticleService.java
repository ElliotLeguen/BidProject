package projet.bid_pro.bll.contexte;

import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;

import java.util.List;

public interface ArticleService {
    List<ArticleVendu> getVentes(String rqt);
    List<Categorie> consulterCategories();
    List<ArticleVendu> consulterEncheresParNomArticle(String nomArticle);
    List<ArticleVendu> consulterEncheresParCategorie(String categorie);
    void creerArticle(ArticleVendu articleVendu);
    Categorie consulterCategorieParId(int id);
    ArticleVendu consulterArticleParId(long id);
}
