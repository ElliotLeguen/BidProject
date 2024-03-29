package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Retrait;

import java.util.List;

public interface ArticleService {
    List<ArticleVendu> getVentes(String rqt);
    List<ArticleVendu> consulterEncheresParNomArticle(String nomArticle);
    List<ArticleVendu> consulterEncheresParCategorie(String categorie);
    void creerArticle(ArticleVendu articleVendu);
    ArticleVendu consulterArticleParId(long id);
    ArticleVendu EditArticle(ArticleVendu articleVendu);
    void SupprArticle(ArticleVendu idArticle);

    List<ArticleVendu>  getAll();

    void finEnchereArticle();


}
