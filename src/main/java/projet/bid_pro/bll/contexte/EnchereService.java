package projet.bid_pro.bll.contexte;

import projet.bid_pro.bo.Categorie;
import projet.bid_pro.bo.Enchere;

import java.util.List;

public interface EnchereService {
    List<Enchere> consulterEncheres();
    Enchere consulterEnchereParId(long id);
    void creerEnchere(Enchere enchere);
    List<Enchere> consulterEncheresParNomArticle(String nomArticle);
    List<Categorie> consulterCategories();
    List<Enchere> getToutesVentes();
    List<Enchere> getVentesEnCoursEtNonDebutees();
    List<Enchere> getVentesEnCoursEtTerminees();
    List<Enchere> getVentesNonDebuteesEtTerminees();
    List<Enchere> getVentesEnCours();
    List<Enchere> getVentesNonDebutees();
    List<Enchere> getVentesTerminees();
}
