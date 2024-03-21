package projet.bid_pro.dal;

import projet.bid_pro.bo.ArticleVendu;
import projet.bid_pro.bo.Utilisateur;

public interface ArticleDAO {
    ArticleVendu read(int id);
}
