package projet.bid_pro.dal;

import projet.bid_pro.bo.Enchere;

import java.util.List;

public interface EnchereDAO {
    Enchere read(long id);
    List<Enchere> findAll();
    List<Enchere> consulterEncheresParNomArticle(String nomArticle);
    List<Enchere> getToutesVentes();
    List<Enchere> getVentesEnCoursEtNonDebutees();
    List<Enchere> getVentesEnCoursEtTerminees();
    List<Enchere> getVentesNonDebuteesEtTerminees();
    List<Enchere> getVentesEnCours();
    List<Enchere> getVentesNonDebutees();
    List<Enchere> getVentesTerminees();

}
