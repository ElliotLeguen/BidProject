package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;

import java.util.List;
public interface CategorieService {
    Categorie consulterCategorieParId(int id);
    List<Categorie> consulterCategories();
}
