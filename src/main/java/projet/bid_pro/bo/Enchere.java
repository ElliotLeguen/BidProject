package projet.bid_pro.bo;

import projet.bid_pro.dal.UtilisateurDAO;

import java.util.Date;

public class Enchere {
    private Utilisateur noUtilisateur;
    private ArticleVendu noArticle;
    private Date dateEnchere;
    private int montantEnchere;

    // Constructeur
    public Enchere(int noUtilisateur, int noArticle, Date dateEnchere, int montantEnchere) {
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    public Enchere(Utilisateur noUtilisateur, ArticleVendu noArticle, Date dateEnchere, int montantEnchere) {
        this.noUtilisateur = noUtilisateur;
        this.noArticle = noArticle;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    public Enchere() {

    }

    // Getters et Setters

    public Utilisateur getNoUtilisateur() {
        return noUtilisateur;
    }

    public void setNoUtilisateur(Utilisateur noUtilisateur) {
        this.noUtilisateur = noUtilisateur;
    }

    public ArticleVendu getNoArticle() {
        return noArticle;
    }

    public void setNoArticle(ArticleVendu noArticle) {
        this.noArticle = noArticle;
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
                "noUtilisateur=" + noUtilisateur +
                ", noArticle=" + noArticle +
                ", dateEnchere=" + dateEnchere +
                ", montantEnchere=" + montantEnchere +
                '}';
    }
}