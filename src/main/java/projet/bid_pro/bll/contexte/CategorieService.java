package projet.bid_pro.bll.contexte;

import org.springframework.stereotype.Service;
import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Utilisateur;

import java.util.List;
@Service
public interface CategorieService {
    Categorie consulterCategorieParId(int id);
    List<Categorie> consulterCategories();

    Categorie edit(Categorie categorie);
    void delete(int id);
}
