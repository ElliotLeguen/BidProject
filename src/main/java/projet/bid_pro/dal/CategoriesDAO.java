package projet.bid_pro.dal;

import projet.bid_pro.bo.Categorie;

import java.util.List;

public interface CategoriesDAO {

    List<Categorie> readCategories();

    List<Categorie> consulterEncheresParCategorie(String nomCategorie);

    Categorie readById(int id);
}
