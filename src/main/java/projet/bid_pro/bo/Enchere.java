package projet.bid_pro.bo;

import projet.bid_pro.dal.UtilisateurDAO;

import java.util.Date;

public class Enchere {
    private Utilisateur Utilisateur;
    private ArticleVendu Article;
    private Date dateEnchere;
    private int montantEnchere;

    // Constructeur
    public Enchere(int Utilisateur, int Article, Date dateEnchere, int montantEnchere) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    public Enchere(Utilisateur noUtilisateur, ArticleVendu noArticle, Date dateEnchere, int montantEnchere) {
        this.Utilisateur = noUtilisateur;
        this.Article = noArticle;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    public Enchere() {

    }

    // Getters et Setters

    public Utilisateur getNoUtilisateur() {
        return Utilisateur;
    }

    public void setNoUtilisateur(Utilisateur noUtilisateur) {
        this.Utilisateur = noUtilisateur;
    }

    public ArticleVendu getNoArticle() {
        return Article;
    }

    public void setNoArticle(ArticleVendu noArticle) {
        this.Article = noArticle;
    }

    public Date getDateEnchere() {
        return dateEnchere;
    }

    public void setDateEnchere(Date dateEnchere) {
        this.dateEnchere = dateEnchere;
    }

    public int getMontantEnchere() {
        return montantEnchere;
    }

    public void setMontantEnchere(int montantEnchere) {
        this.montantEnchere = montantEnchere;
    }

    @Override
    public String toString() {
        return "Enchere{" +
                "noUtilisateur=" + Utilisateur +
                ", noArticle=" + Article +
                ", dateEnchere=" + dateEnchere +
                ", montantEnchere=" + montantEnchere +
                '}';
    }
}