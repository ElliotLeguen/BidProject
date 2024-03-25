package projet.bid_pro.bo;

import projet.bid_pro.dal.UtilisateurDAO;

import java.util.Date;

public class Enchere {
    private Utilisateur Utilisateur;
    private ArticleVendu Article;
    private Date dateEnchere;
    private int montantEnchere;

    // Constructeur
    public Enchere(){
    }

    public Enchere(Utilisateur utilisateur, ArticleVendu article, Date dateEnchere, int montantEnchere) {
        this.Utilisateur = utilisateur;
        this.Article = article;
        this.dateEnchere = dateEnchere;
        this.montantEnchere = montantEnchere;
    }

    // Getters et Setters

    public Utilisateur getUtilisateur() {
        return Utilisateur;
    }

    public void setUtilisateur(Utilisateur Utilisateur) {
        this.Utilisateur = Utilisateur;
    }

    public ArticleVendu getArticle() {
        return Article;
    }

    public void setArticle(ArticleVendu noArticle) {
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
                "Utilisateur=" + Utilisateur +
                ", Article=" + Article +
                ", dateEnchere=" + dateEnchere +
                ", montantEnchere=" + montantEnchere +
                '}';
    }
}